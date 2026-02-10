#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>
#include "send_to_pico.h"

static int pico_fd = -1;
static char response[256];

int pico_init() {
    struct termios tio;

    pico_fd = open("/dev/ttyACM0", O_RDWR | O_NOCTTY | O_SYNC);
    if (pico_fd < 0)
        return -1;

    tcgetattr(pico_fd, &tio);
    cfsetispeed(&tio, B115200);
    cfsetospeed(&tio, B115200);

    tio.c_cflag |= (CLOCAL | CREAD);
    tio.c_cflag &= ~CSIZE;
    tio.c_cflag |= CS8;
    tio.c_cflag &= ~PARENB;
    tio.c_cflag &= ~CSTOPB;
    tio.c_cflag &= ~CRTSCTS;

    tio.c_lflag = 0;
    tio.c_iflag = 0;
    tio.c_oflag = 0;

    tio.c_cc[VMIN]  = 0;
    tio.c_cc[VTIME] = 20;

    tcsetattr(pico_fd, TCSANOW, &tio);
    usleep(1500000);
    return 0;
}

const char* pico_send(const char *msg) {
    if (pico_fd < 0 || !msg)
        return NULL;

    write(pico_fd, msg, strlen(msg));
    write(pico_fd, "\n", 1);

    int idx = 0;
    char c;

    while (idx < (int)sizeof(response) - 1) {
        int n = read(pico_fd, &c, 1);
        if (n <= 0)
            break;
        if (c == '\n')
            break;
        response[idx++] = c;
    }

    response[idx] = '\0';

    if (idx == 0)
        return NULL;

    return response;
}

void pico_close() {
    if (pico_fd >= 0) {
        write(pico_fd, "RESET\n", 6);
        close(pico_fd);
        pico_fd = -1;
    }
}
