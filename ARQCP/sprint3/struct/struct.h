#ifndef STRUCT_H
#define STRUCT_H

#include <time.h>

typedef enum {
    INST_ASSIGN_TRACK,
    INST_SET_FREE,
    INST_SET_NONOP,
    INST_DEPARTURE,
    INST_ADMIN_CREATE_OPERATOR,
    INST_ADMIN_REPORT,
    INST_GET_SENSOR_DATA,
    INST_EXIT,
    INST_EMERGENCY_STOP,
    INST_INVALID
} InstructionType;

typedef enum {
    ROLE_ADMIN,
    ROLE_OPERATOR
} UserRole;

typedef struct {
    char name[50];
    char username[20];
    char password_enc[50];
    int caesar_key;
    UserRole role;
} User;

typedef struct {
    InstructionType type;
    int trackId;
    int trainId;
    User new_user;
    char reportUser[50];    
} Instruction;

typedef enum {
    TRACK_FREE,
    TRACK_BUSY,
    TRACK_ARRIVING,
    TRACK_INOPERATIVE
} TrackState;

typedef struct {
    int trackId;
    TrackState state;
    int trainId;
} Track;

typedef struct {
    int trainId;
    int trackId;
} Train;

typedef struct {
    int *buffer;
    int length;
    int nelem;
    int tail;
    int head;
    int windowSize;
} CircularBuffer;

typedef struct {
    int id;
    char username[20];
    char action[100];
    time_t timestamp;
} LogEntry;

typedef struct {
    User *users;
    int num_users;
    User *current_user;

    Track *tracks;
    int num_tracks;

    Train *trains;
    int num_trains;

    CircularBuffer tempBuffer;
    CircularBuffer humBuffer;

    LogEntry *logs;
    int num_logs;
} Manager;

#endif
