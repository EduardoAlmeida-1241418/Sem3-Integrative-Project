#ifndef SETUP_H
#define SETUP_H

#include "../struct/struct.h"

int load_setup(const char* filename,
               Track** tracks, int* numTracks,
               Train** trains, int* numTrains);

int load_sensors_config(const char *filename,
                        CircularBuffer *temp,
                        CircularBuffer *hum);

int load_initial_config(Manager *manager, const char *filename);

void init_manager(Manager *manager);

#endif
