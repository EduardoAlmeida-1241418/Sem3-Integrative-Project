#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../struct/struct.h"
#include "../ui/ui.h"

static UserRole parse_role(const char *s) {
    if (s && strcmp(s, "ADMIN") == 0)
        return ROLE_ADMIN;
    return ROLE_OPERATOR;
}

int load_users(const char *filename, User **users, int *numUsers) {

    FILE *f = fopen(filename, "r");
    if (!f) return 0;

    char line[256];
    int count = 0;
    int inUsers = 0;

    while (fgets(line, sizeof(line), f)) {
        if (strncmp(line, "[USERS]", 7) == 0) {
            inUsers = 1;
            continue;
        }
        if (line[0] == '[' && inUsers)
            break;
        if (inUsers && strchr(line, ';'))
            count++;
    }

    rewind(f);

    *users = malloc(sizeof(User) * count);
    *numUsers = 0;

    inUsers = 0;

    while (fgets(line, sizeof(line), f)) {

        if (strncmp(line, "[USERS]", 7) == 0) {
            inUsers = 1;
            continue;
        }

        if (line[0] == '[' && inUsers)
            break;

        if (inUsers && strchr(line, ';')) {

            char *role = strtok(line, ";");
            char *name = strtok(NULL, ";");
            char *username = strtok(NULL, ";");
            char *pass = strtok(NULL, ";");
            char *key = strtok(NULL, "\n");

            if (!role || !name || !username || !pass || !key)
                continue;

            User *u = &(*users)[(*numUsers)++];

            u->role = parse_role(role);
            strcpy(u->name, name);
            strcpy(u->username, username);
            strcpy(u->password_enc, pass);
            u->caesar_key = atoi(key);
        }
    }

    fclose(f);
    return 1;
}

int load_sensors_config(const char *filename,
                        CircularBuffer *temp,
                        CircularBuffer *hum)
{
    FILE *f = fopen(filename, "r");
    if (!f) return 0;

    char line[256];
    int inSensors = 0;

    while (fgets(line, sizeof(line), f)) {

        if (strncmp(line, "[SENSORS]", 9) == 0) {
            inSensors = 1;
            continue;
        }

        if (line[0] == '[' && inSensors)
            break;

        if (!inSensors)
            continue;

        char type[10];
        int buffer = 0, window = 0;

        if (sscanf(line, "%9[^;];BUFFER=%d;WINDOW=%d",
                   type, &buffer, &window) == 3) {

            if (buffer <= 0 || window <= 0 || window > buffer)
                continue;

            if (strcmp(type, "TEMP") == 0) {
                temp->length = buffer;
                temp->windowSize = window;
            } else if (strcmp(type, "HUM") == 0) {
                hum->length = buffer;
                hum->windowSize = window;
            }
        }
    }

    fclose(f);
    return 1;
}

int load_setup(const char* filename,
               Track** tracks, int* numTracks,
               Train** trains, int* numTrains)
{
    FILE* f = fopen(filename, "r");
    if (!f)
        return 0;

    char line[200];
    int trackCount = 0;
    int trainCount = 0;

    while (fgets(line, sizeof(line), f)) {
        if (strncmp(line, "TRACK", 5) == 0) trackCount++;
        else if (strncmp(line, "TRAIN", 5) == 0) trainCount++;
    }

    rewind(f);

    *tracks = malloc(sizeof(Track) * trackCount);
    *trains = malloc(sizeof(Train) * trainCount);

    if (!*tracks || !*trains) {
        fclose(f);
        return 0;
    }

    *numTracks = trackCount;
    *numTrains = trainCount;

    for (int i = 0; i < trackCount; i++) {
        (*tracks)[i].trackId = 0;
        (*tracks)[i].state = TRACK_FREE;
        (*tracks)[i].trainId = -1;
    }

    for (int i = 0; i < trainCount; i++)
        (*trains)[i].trackId = -1;

    int tIndex = 0;
    int trIndex = 0;

    while (fgets(line, sizeof(line), f)) {

        if (strncmp(line, "TRACK", 5) == 0) {
            int id, state, trainId;
            sscanf(line, "TRACK;%d;%d;%d", &id, &state, &trainId);

            (*tracks)[tIndex].trackId = id;

            if (state == TRACK_FREE)
                (*tracks)[tIndex].state = TRACK_FREE;
            else if (state == TRACK_BUSY)
                (*tracks)[tIndex].state = TRACK_BUSY;
            else if (state == TRACK_ARRIVING)
                (*tracks)[tIndex].state = TRACK_ARRIVING;
            else if (state == TRACK_INOPERATIVE)
                (*tracks)[tIndex].state = TRACK_INOPERATIVE;
            else
                (*tracks)[tIndex].state = TRACK_FREE;

            (*tracks)[tIndex].trainId = (trainId < 0 ? -1 : trainId);
            tIndex++;
        }

        else if (strncmp(line, "TRAIN", 5) == 0) {
            int id;
            sscanf(line, "TRAIN;%d", &id);
            (*trains)[trIndex].trainId = id;
            trIndex++;
        }
    }

    fclose(f);

    for (int i = 0; i < *numTracks; i++) {
        int tid = (*tracks)[i].trainId;
        if (tid != -1) {
            for (int j = 0; j < *numTrains; j++) {
                if ((*trains)[j].trainId == tid)
                    (*trains)[j].trackId = (*tracks)[i].trackId;
            }
        }
    }

    return 1;
}

void init_manager(Manager *manager) {

    manager->users = NULL;
    manager->num_users = 0;
    manager->current_user = NULL;

    manager->tracks = NULL;
    manager->num_tracks = 0;

    manager->trains = NULL;
    manager->num_trains = 0;

    manager->tempBuffer.buffer = NULL;
    manager->tempBuffer.nelem = 0;
    manager->tempBuffer.head = 0;
    manager->tempBuffer.tail = 0;

    manager->humBuffer.buffer = NULL;
    manager->humBuffer.nelem = 0;
    manager->humBuffer.head = 0;
    manager->humBuffer.tail = 0;

    manager->logs = NULL;
    manager->num_logs = 0;
}

int load_initial_config(Manager *manager, const char *filename) {

    if (!load_users(filename,
                     &manager->users,
                     &manager->num_users))
        return 0;

    if (!load_setup(filename,
                     &manager->tracks,
                     &manager->num_tracks,
                     &manager->trains,
                     &manager->num_trains))
        return 0;

    if (!load_sensors_config(filename,
                             &manager->tempBuffer,
                             &manager->humBuffer))
        return 0;

    manager->tempBuffer.buffer =
        malloc(sizeof(int) * manager->tempBuffer.length);

    manager->humBuffer.buffer =
        malloc(sizeof(int) * manager->humBuffer.length);

    manager->tempBuffer.nelem = 0;
    manager->tempBuffer.head = 0;
    manager->tempBuffer.tail = 0;

    manager->humBuffer.nelem = 0;
    manager->humBuffer.head = 0;
    manager->humBuffer.tail = 0;

    return 1;
}
