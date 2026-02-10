#ifndef SEND_TO_PICO_H
#define SEND_TO_PICO_H

int pico_init();
const char* pico_send(const char *msg);
void pico_close();

#endif
