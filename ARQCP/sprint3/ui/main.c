#include <stdio.h>
#include "../log/log.h"
#include "../manager/manager.h"
#include "../ui/ui.h"
#include "../board/board.h"
#include "../pico/send_to_pico.h"

int main(void) {

    log_initialize();
    board_init();

    while (1) {
        int option = initialize_application();

        if (option == 1) {
            manager_main_loop();
        }
        else if (option == 0) {
            pico_close();

            printf("\n");
            printf("╔══════════════════════════════╗\n");
            printf("║     EXITING APPLICATION      ║\n");
            printf("╚══════════════════════════════╝\n");
            break;
        }
    }

    return 0;
}