#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

#include "manager.h"
#include "../struct/struct.h"
#include "../ui/ui.h"
#include "../log/log.h"
#include "../asm/format_command.h"
#include "../asm/median.h"
#include "../asm/extract_data.h"
#include "../asm/enqueue_value.h"
#include "../asm/move_n_to_array.h"
#include "../setup/setup.h"
#include "../pico/send_to_pico.h"
#include "../login/login.h"
#include "../board/board.h"
#include "../asm/cb_is_full.h"

/* ============================================================
   SENSOR I/O
   ============================================================ */

char* wait_for_data_from_sensors(void) {
    static char buffer[256];
    const char* response = pico_send("GTH");

    if (response) {
        strncpy(buffer, response, 255);
        buffer[255] = '\0';
        return buffer;
    }
    return NULL;
}

void send_data_to_pico(const char* data) {
    if (data && strlen(data) > 0)
        pico_send(data);
}

/* ============================================================
   SENSOR COLLECTION (USAC14)
   ============================================================ */

void collect_sensor_data(Manager *manager) {

    if (!manager->tempBuffer.buffer || !manager->humBuffer.buffer ||
        manager->tempBuffer.length <= 0 || manager->humBuffer.length <= 0) {

        printf("ERROR: Sensors not configured\n");
        board_update_action("ERROR: Sensors not configured");
        printf("\nPress ENTER...");
        getchar();
        return;
    }

    if (cb_is_full(&manager->tempBuffer)) {
        board_update_action("Temperature buffer full");
    }

    const char *response = pico_send("GTH");

    printf("\n");

    if (!response || strcmp(response, "ERROR") == 0) {

        printf("╔═══════════════════════════════════════════════════════════════╗\n");
        printf("║         COLLECTING SENSOR DATA (Moving Median Filter)         ║\n");
        printf("╚═══════════════════════════════════════════════════════════════╝\n\n");
        printf("ERROR: No connection to Raspberry Pi\n\n");


        board_update_action("ERROR: No connection to Raspberry Pi (Sensors offline)");

        printf("\nPress ENTER...");
        getchar();
        return;
    }

    printf("╔═══════════════════════════════════════════════════════════════╗\n");
    printf("║         COLLECTING SENSOR DATA (Moving Median Filter)         ║\n");
    printf("╚═══════════════════════════════════════════════════════════════╝\n\n");

    int gotTemp = 0, gotHum = 0;
    char unit[20];
    int value;
    char tmp[256];

    strncpy(tmp, response, 255);
    tmp[255] = '\0';

    if (extract_data(tmp, "TEMP", unit, &value)) {
        gotTemp = 1;
        enqueue_value(manager->tempBuffer.buffer,
                      manager->tempBuffer.length,
                      &manager->tempBuffer.nelem,
                      &manager->tempBuffer.tail,
                      &manager->tempBuffer.head,
                      value);
    }

    strncpy(tmp, response, 255);
    tmp[255] = '\0';

    if (extract_data(tmp, "HUM", unit, &value)) {
        gotHum = 1;
        enqueue_value(manager->humBuffer.buffer,
                      manager->humBuffer.length,
                      &manager->humBuffer.nelem,
                      &manager->humBuffer.tail,
                      &manager->humBuffer.head,
                      value);
    }

    if (!gotTemp && !gotHum) {
        board_update_action("ERROR: Sensor data invalid or unavailable");
        printf("\nPress ENTER...");
        getchar();
        return;
    }

    int tLast = 0, tMed = 0, tWin = 0;
    int hLast = 0, hMed = 0, hWin = 0;

    if (manager->tempBuffer.nelem > 0) {
        tWin = manager->tempBuffer.windowSize;
        if (tWin > manager->tempBuffer.nelem)
            tWin = manager->tempBuffer.nelem;

        int tVals[tWin];
        int idx = manager->tempBuffer.tail;

        for (int i = 0; i < tWin; i++) {
            tVals[i] = manager->tempBuffer.buffer[idx];
            idx = (idx + 1) % manager->tempBuffer.length;
        }

        tLast = manager->tempBuffer.buffer[
            (manager->tempBuffer.head - 1 + manager->tempBuffer.length) %
            manager->tempBuffer.length
        ];

        median(tVals, tWin, &tMed);
    }

    if (manager->humBuffer.nelem > 0) {
        hWin = manager->humBuffer.windowSize;
        if (hWin > manager->humBuffer.nelem)
            hWin = manager->humBuffer.nelem;

        int hVals[hWin];
        int idx = manager->humBuffer.tail;

        for (int i = 0; i < hWin; i++) {
            hVals[i] = manager->humBuffer.buffer[idx];
            idx = (idx + 1) % manager->humBuffer.length;
        }

        hLast = manager->humBuffer.buffer[
            (manager->humBuffer.head - 1 + manager->humBuffer.length) %
            manager->humBuffer.length
        ];

        median(hVals, hWin, &hMed);
    }

    board_update_sensors(tLast, tMed, tWin,
                         hLast, hMed, hWin);

    printf("\nPress ENTER...");
    getchar();
}

/* ============================================================
   TRACK / TRAIN HELPERS
   ============================================================ */

static int has_free_track(Track* tracks, int numTracks) {
    for (int i = 0; i < numTracks; i++)
        if (tracks[i].state == TRACK_FREE)
            return 1;
    return 0;
}

int findTrackById(int id, Track* tracks, int numTracks) {
    for (int i = 0; i < numTracks; i++)
        if (tracks[i].trackId == id)
            return i;
    return -1;
}

int findTrainById(int id, Train* trains, int numTrains) {
    for (int i = 0; i < numTrains; i++)
        if (trains[i].trainId == id)
            return i;
    return -1;
}

void freeTrack(Track* tracks, int trackIndex, Train* trains, int numTrains) {
    if (trackIndex < 0) return;

    int trainId = tracks[trackIndex].trainId;
    tracks[trackIndex].state   = TRACK_FREE;
    tracks[trackIndex].trainId = -1;

    for (int i = 0; i < numTrains; i++)
        if (trains[i].trainId == trainId)
            trains[i].trackId = -1;
}

void occupyTrack(Track* tracks, int trackIndex, int trainId) {
    if (trackIndex < 0) return;
    tracks[trackIndex].state   = TRACK_BUSY;
    tracks[trackIndex].trainId = trainId;
}

/* ============================================================
   PROCESS INSTRUCTION
   return:  1 -> ação válida executada
            0 -> nenhuma ação
           -1 -> emergency stop
   ============================================================ */

int process_instruction(Instruction inst,
                        Track* tracks, int numTracks,
                        Train* trains, int numTrains,
                        char* dataOut, char* cmdOut)
{
    dataOut[0] = '\0';
    cmdOut[0]  = '\0';

    if (inst.type == INST_ASSIGN_TRACK) {

        if (!has_free_track(tracks, numTracks)) {
            sprintf(dataOut, "EMERGENCY STOP: no free tracks available");
            board_update_action(dataOut);
            return -1;
        }

        int t  = findTrackById(inst.trackId, tracks, numTracks);
        int tr = findTrainById(inst.trainId, trains, numTrains);

        if (t < 0) {
            printf("ERROR: Track %d does not exist\n", inst.trackId);
            return 0;
        }

        if (tr < 0) {
            printf("ERROR: Train %d does not exist\n", inst.trainId);
            return 0;
        }

        if (tracks[t].state == TRACK_INOPERATIVE) {
            printf("ERROR: Track %d is NON-OPERATIONAL\n", inst.trackId);
            return 0;
        }

        if (tracks[t].state == TRACK_BUSY) {
            printf("ERROR: Track %d is BUSY by Train %d\n",
                   inst.trackId, tracks[t].trainId);
            return 0;
        }

        if (trains[tr].trackId != -1) {
            printf("ERROR: Train %d is already assigned to Track %d\n",
                   inst.trainId, trains[tr].trackId);
            return 0;
        }

        occupyTrack(tracks, t, inst.trainId);
        trains[tr].trackId = inst.trackId;

        sprintf(dataOut, "Track %d assigned to Train %d",
                inst.trackId, inst.trainId);

        format_command("YE", inst.trackId, cmdOut);

        board_update_tracks(tracks, numTracks);
        board_update_action(dataOut);
        return 1;
    }


    if (inst.type == INST_SET_FREE) {

        int t = findTrackById(inst.trackId, tracks, numTracks);

        if (t < 0) {
            printf("ERROR: Track %d does not exist\n", inst.trackId);
            return 0;
        }

        if (tracks[t].state == TRACK_FREE) {
            printf("ERROR: Track %d is already FREE\n", inst.trackId);
            return 0;
        }

        freeTrack(tracks, t, trains, numTrains);

        sprintf(dataOut, "Track %d set FREE", inst.trackId);
        format_command("GE", inst.trackId, cmdOut);

        board_update_tracks(tracks, numTracks);
        board_update_action(dataOut);
        return 1;
    }


    if (inst.type == INST_SET_NONOP) {

        int t = findTrackById(inst.trackId, tracks, numTracks);

        if (t < 0) {
            printf("ERROR: Track %d does not exist\n", inst.trackId);
            return 0;
        }

        if (tracks[t].state == TRACK_INOPERATIVE) {
            printf("ERROR: Track %d is already NON-OPERATIONAL\n", inst.trackId);
            return 0;
        }

        if (tracks[t].state == TRACK_BUSY)
            freeTrack(tracks, t, trains, numTrains);

        tracks[t].state   = TRACK_INOPERATIVE;
        tracks[t].trainId = -1;

        sprintf(dataOut, "Track %d set to NON-OPERATIONAL", inst.trackId);
        format_command("RB", inst.trackId, cmdOut);

        board_update_tracks(tracks, numTracks);
        board_update_action(dataOut);
        return 1;
    }


    if (inst.type == INST_DEPARTURE) {

        int tr = findTrainById(inst.trainId, trains, numTrains);

        if (tr < 0) {
            printf("ERROR: Train %d does not exist\n", inst.trainId);
            return 0;
        }

        if (trains[tr].trackId == -1) {
            printf("ERROR: Train %d is not assigned to any track\n", inst.trainId);
            return 0;
        }

        int t = findTrackById(trains[tr].trackId, tracks, numTracks);

        freeTrack(tracks, t, trains, numTrains);

        sprintf(dataOut, "Departure for Train %d", inst.trainId);
        format_command("RE", tracks[t].trackId, cmdOut);

        board_update_tracks(tracks, numTracks);
        board_update_action(dataOut);
        return 1;
    }



    return 0;
}

/* ============================================================
   USER / ADMIN MANAGEMENT
   ============================================================ */

Manager create_new_operator(Manager manager,
                            const char *name,
                            const char *username,
                            const char *password_enc,
                            int caesar_key)
{
    if (!manager.current_user) return manager;
    if (manager.current_user->role != ROLE_ADMIN) return manager;

    for (int i = 0; i < manager.num_users; i++)
        if (strcmp(manager.users[i].username, username) == 0)
            return manager;

    User *tmp = realloc(manager.users,
                        (manager.num_users + 1) * sizeof(User));
    if (!tmp) return manager;

    manager.users = tmp;

    User *u = &manager.users[manager.num_users];
    memset(u, 0, sizeof(User));

    strcpy(u->name, name);
    strcpy(u->username, username);
    strcpy(u->password_enc, password_enc);
    u->caesar_key = caesar_key;
    u->role = ROLE_OPERATOR;

    manager.num_users++;
    return manager;
}

/* ============================================================
   LOG WRAPPERS
   ============================================================ */

void manager_generate_report(const char* username) {
    log_generate_report(username);
}

void manager_record_action(const char* username, Instruction inst) {
    char text[200];
    instruction_to_string(&inst, text);
    log_record(username, text);
}

/* ============================================================
   SAVE STATE
   ============================================================ */

void manager_save_state(Manager *manager, const char *filename)
{
    FILE *f = fopen(filename, "w");
    if (!f)
        return;

    fprintf(f, "[USERS]\n");
    for (int i = 0; i < manager->num_users; i++) {
        User *u = &manager->users[i];
        fprintf(f, "%s;%s;%s;%s;%d\n",
                u->role == ROLE_ADMIN ? "ADMIN" : "OPERATOR",
                u->name,
                u->username,
                u->password_enc,
                u->caesar_key);
    }

    fprintf(f, "\n[SENSORS]\n");
    fprintf(f, "TEMP;BUFFER=%d;WINDOW=%d\n",
            manager->tempBuffer.length,
            manager->tempBuffer.windowSize);
    fprintf(f, "HUM;BUFFER=%d;WINDOW=%d\n",
            manager->humBuffer.length,
            manager->humBuffer.windowSize);

    fprintf(f, "\n[TRACKS]\n");
    for (int i = 0; i < manager->num_tracks; i++) {
        Track *t = &manager->tracks[i];
        fprintf(f, "TRACK;%d;%d;%d\n",
                t->trackId,
                t->state,
                t->trainId);
    }

    fprintf(f, "\n[TRAINS]\n");
    for (int i = 0; i < manager->num_trains; i++) {
        Train *tr = &manager->trains[i];
        fprintf(f, "TRAIN;%d\n", tr->trainId);
    }

    fclose(f);
}

static void sync_tracks_with_pico(Track *tracks, int numTracks)
{
    char cmd[50];

    for (int i = 0; i < numTracks; i++) {

        cmd[0] = '\0';

        if (tracks[i].state == TRACK_BUSY) {
            format_command("RE", tracks[i].trackId, cmd);
        }
        else if (tracks[i].state == TRACK_FREE) {
            format_command("GE", tracks[i].trackId, cmd);
        }
        else if (tracks[i].state == TRACK_INOPERATIVE) {
            format_command("RB", tracks[i].trackId, cmd);
        }
        else {
            continue;
        }

        if (cmd[0] != '\0')
            send_data_to_pico(cmd);
    }
}

/* ============================================================
   MAIN LOOP
   ============================================================ */

void manager_main_loop() {
    Manager manager;
    init_manager(&manager);

    if (!load_initial_config(&manager, "setup/config.txt"))
        return;

    if (!manager_login(&manager))
        return;

    pico_init();

    board_update_tracks(manager.tracks, manager.num_tracks);
    board_update_trains(manager.trains, manager.num_trains);
    board_update_action("System started");

    sync_tracks_with_pico(manager.tracks, manager.num_tracks);

    while (1) {
        Instruction inst;

        if (manager.current_user->role == ROLE_ADMIN) {

            inst = ui_menu_admin();

            if (inst.type == INST_ADMIN_CREATE_OPERATOR) {
                int before = manager.num_users;

                manager = create_new_operator(manager,
                                              inst.new_user.name,
                                              inst.new_user.username,
                                              inst.new_user.password_enc,
                                              inst.new_user.caesar_key);

                if (manager.num_users == before)
                    printf("Error creating operator\n");
            }

            if (inst.type == INST_ADMIN_REPORT)
                manager_generate_report(inst.reportUser);

        } else {

            inst = ui_menu_user();

            if (inst.type == INST_GET_SENSOR_DATA)
                collect_sensor_data(&manager);

            char dataToBoard[100];
            char cmdOut[50];

            int result = process_instruction(inst,
                                             manager.tracks,
                                             manager.num_tracks,
                                             manager.trains,
                                             manager.num_trains,
                                             dataToBoard,
                                             cmdOut);

            send_data_to_pico(cmdOut);

            if (result == 1) {
                manager_record_action(manager.current_user->username, inst);
            } else if (result == -1) {
                log_record(manager.current_user->username,
                           "EMERGENCY STOP: no free tracks available");
            }
        }

        if (inst.type == INST_EXIT)
            break;
    }

    manager_save_state(&manager, "setup/config.txt");

    free(manager.users);
    free(manager.tracks);
    free(manager.trains);
    free(manager.tempBuffer.buffer);
    free(manager.humBuffer.buffer);
}
