#ifndef BOARD_H
#define BOARD_H

#include "../struct/struct.h"

void render();
void board_init();
void board_update_sensors(int tLast, int tMed, int tWin,
                          int hLast, int hMed, int hWin);
void board_update_tracks(Track* tracks, int n_tracks);
void board_update_trains(Train* trains, int n_trains);
void board_update_action(const char* action);

#endif