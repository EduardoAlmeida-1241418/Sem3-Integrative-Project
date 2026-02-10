#include <stdio.h>
#include <string.h>
#include "../struct/struct.h"

/* ---- Pico ---- */
int pico_init(void) {
    return 0;
}

const char* pico_send(const char* msg) {
    (void)msg;
    return "TEMP=20;HUM=50";
}

/* ---- UI ---- */
void ui_menu_admin(void) {}
void ui_menu_user(void) {}

/* ---- Login ---- */
int manager_login(Manager* m) {
    (void)m;
    return 1;
}

/* ---- Instructions ---- */
void instruction_to_string(Instruction inst, char* out) {
    if (!out) return;

    switch (inst.type) {
        case INST_ASSIGN_TRACK:
            snprintf(out, 200, "ASSIGN_TRACK track=%d train=%d",
                     inst.trackId, inst.trainId);
            break;

        case INST_SET_FREE:
            snprintf(out, 200, "SET_FREE track=%d", inst.trackId);
            break;

        default:
            snprintf(out, 200, "UNKNOWN");
            break;
    }
}

/* ---- Setup ---- */
void init_manager(Manager* m) {
    (void)m;
}

int load_initial_config(Manager* manager, const char* path) {
    (void)path;
    if (!manager) return 0;

    /* USERS */
    manager->num_users = 2;

    strcpy(manager->users[0].username, "admin");
    manager->users[0].role = ROLE_ADMIN;
    strcpy(manager->users[0].password_enc, "ADMIN");
    manager->users[0].caesar_key = 0;

    strcpy(manager->users[1].username, "user");
    manager->users[1].role = ROLE_OPERATOR;
    strcpy(manager->users[1].password_enc, "USER");
    manager->users[1].caesar_key = 0;

    /* SENSORS */
    manager->tempBuffer.length = 10;
    manager->tempBuffer.windowSize = 5;
    manager->humBuffer.length = 8;
    manager->humBuffer.windowSize = 3;

    /* TRACKS */
    manager->num_tracks = 2;
    manager->tracks[0].trackId = 1;
    manager->tracks[0].state = TRACK_BUSY;
    manager->tracks[1].trackId = 2;
    manager->tracks[1].state = TRACK_FREE;

    /* TRAINS */
    manager->num_trains = 2;
    manager->trains[0].trainId = 100;
    manager->trains[1].trainId = 101;

    /* LOGS */
    manager->num_logs = 0;

    return 1;
}

/* ---- Crypto (stub) ---- */
int decrypt_data(char* in, int key, char* out) {
    (void)key;
    if (!in || !out) return 0;
    strcpy(out, in);
    return 1;
}

/* ---- Assembly helper ---- */
int cb_is_full(CircularBuffer* cb) {
    (void)cb;
    return 0;
}
