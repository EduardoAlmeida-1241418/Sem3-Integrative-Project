#include <stdio.h>
#include <string.h>
#include "board.h"

static Track* tracks_ptr;
static int num_tracks;
static Train* trains_ptr;
static int num_trains;

static int tLast, tMed, tWin;
static int hLast, hMed, hWin;

static char last_action[200];

void render() {

    printf("\n");
    printf("                                             (@@)     (@@@@@@@)     \n");
    printf("                                       (@@@@@@@)   (@@@@@)       (@@@@\n");
    printf("                                  (@@@)     (@@@@@@@)   (@@@@@@)    \n");
    printf("                             (@@@@@@)    (@@@@@@)                (@)\n");
    printf("                         (@@@)  (@@@@)           (@@)\n");
    printf("                      (@@)              (@@@)\n");
    printf("                     .-.               \n");
    printf("                     ] [    .-.      _    .-----.\n");
    printf("                   .\"   \"\"\"\"   \"\"\"\"\"\" \"\"\"\" | .--`\n");
    printf("                  (:--:--:--:--:--:--:--:-| [___    .--------------\n");
    printf("                   |C&O  :  :  :  :  :  : [_9_] |'='|.--------------\n");
    printf("                  /|.___________________________|___|'--.___.--.___\n");
    printf("                 / ||_.--.______.--.______.--._ |---\'--\\-.-/==\\-.-\n");
    printf("                /__;^=(==)======(==)======(==)=^~^^^ ^^^^(-)^^^^(-)^\n");
    printf("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    printf("\n");

    printf("╔═══════════════════════════════════════════════════════════════════════════════╗\n");
    printf("║                          STATION BOARD UPDATE                                 ║\n");
    printf("╠═══════════════════════════════════════════════════════════════════════════════╣\n");

    printf("║ Last Operation: %-61s ║\n", last_action);
    printf("║ %-77s ║\n", "");

    printf("║ TRACKS STATUS:%-63s ║\n", "");
    if (tracks_ptr) {
        for (int i = 0; i < num_tracks; i++) {
            char status[64];
            if (tracks_ptr[i].state == TRACK_INOPERATIVE)
                strcpy(status, "NON-OPERATIONAL");
            else if (tracks_ptr[i].state == TRACK_FREE)
                strcpy(status, "FREE");
            else
                snprintf(status, sizeof(status),
                         "OCCUPIED (Train %d)", tracks_ptr[i].trainId);

            printf("║ Track %-3d %-67s ║\n",
                   tracks_ptr[i].trackId, status);
        }
    }

    printf("║ %-77s ║\n", "");
    printf("║ TRAINS STATUS:%-63s ║\n", "");
    if (trains_ptr) {
        for (int i = 0; i < num_trains; i++) {
            char loc[64];
            if (trains_ptr[i].trackId == -1)
                strcpy(loc, "Not assigned");
            else
                snprintf(loc, sizeof(loc),
                         "On Track %d", trains_ptr[i].trackId);

            printf("║ Train %-3d %-67s ║\n",
                   trains_ptr[i].trainId, loc);
        }
    }

    printf("║ %-77s ║\n", "");
    printf("║ SENSORS (Moving Median Filter):%-46s ║\n", "");

    if (tWin > 0) {
        printf("║ Temperature: %-3d°C (Last) | Median: %-3d°C (Window: %-2d) %-22s ║\n",
               tLast, tMed, tWin, "");
        printf("║ Humidity:    %-3d %% (Last) | Median: %-3d %% (Window: %-2d) %-22s ║\n",
               hLast, hMed, hWin, "");
    } else {
        printf("║ No sensor data available%-53s ║\n", "");
    }

    printf("║ %-77s ║\n", "");
    printf("╚═══════════════════════════════════════════════════════════════════════════════╝\n");
}

void board_init() {
    tracks_ptr = NULL;
    trains_ptr = NULL;
    num_tracks = 0;
    num_trains = 0;
    tWin = 0;
    hWin = 0;
    strcpy(last_action, "System started");
}

void board_update_sensors(int tl, int tm, int tw,
                          int hl, int hm, int hw) {
    tLast = tl; tMed = tm; tWin = tw;
    hLast = hl; hMed = hm; hWin = hw;
    render();
}

void board_update_tracks(Track* tracks, int n_tracks) {
    tracks_ptr = tracks;
    num_tracks = n_tracks;
}

void board_update_trains(Train* trains, int n_trains) {
    trains_ptr = trains;
    num_trains = n_trains;
}

void board_update_action(const char* action) {
    strncpy(last_action, action, sizeof(last_action) - 1);
    last_action[sizeof(last_action) - 1] = '\0';
    render();
}
