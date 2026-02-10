#ifndef MANAGER_H
#define MANAGER_H

#include "../struct/struct.h"

void manager_main_loop(void);

void init_manager(Manager *manager);
int load_initial_config(Manager *manager, const char *filename);

void collect_sensor_data(Manager *manager);
void display_board(Track* tracks, int numTracks, Train* trains, int numTrains, Manager* manager);

void manager_record_action(const char *username, Instruction inst);
void manager_generate_report(const char *username);

#endif
