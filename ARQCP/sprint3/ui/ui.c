#include <stdio.h>
#include <string.h>
#include "ui.h"
#include "../asm/encrypt_data.h"

int initialize_application(void) {

    printf("╔══════════════════════════════════════════════════════════════╗\n");
    printf("║                                                              ║\n");
    printf("║     ██████╗  █████╗ ██╗██╗      ██████╗ ██████╗ ███████╗     ║\n");
    printf("║     ██╔══██╗██╔══██╗██║██║     ██╔═══██╗██╔══██╗██╔════╝     ║\n");
    printf("║     ██████╔╝███████║██║██║     ██║   ██║██████╔╝███████╗     ║\n");
    printf("║     ██╔══██╗██╔══██║██║██║     ██║   ██║██╔═══╝ ╚════██║     ║\n");
    printf("║     ██║  ██║██║  ██║██║███████╗╚██████╔╝██║     ███████║     ║\n");
    printf("║     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚══════╝ ╚═════╝ ╚═╝     ╚══════╝     ║\n");
    printf("║                                                              ║\n");
    printf("║         R A I L O P S   S T A T I O N   M A N A G E R        ║\n");
    printf("║                                                              ║\n");
    printf("║        Railway Station Control & Monitoring System           ║\n");
    printf("║           Tracks • Trains • Sensors • Operations             ║\n");
    printf("║                                                              ║\n");
    printf("╚══════════════════════════════════════════════════════════════╝\n");

    printf("\n");
    printf("╔══════════════════════════════╗\n");
    printf("║           MAIN MENU          ║\n");
    printf("╠══════════════════════════════╣\n");
    printf("║  1 - Login                   ║\n");
    printf("║  0 - Exit                    ║\n");
    printf("╚══════════════════════════════╝\n");
    printf("Option: ");

    return ui_get_int();
}

int ui_get_int() {
    char buffer[32];
    int value;

    if (fgets(buffer, sizeof(buffer), stdin) == NULL)
        return -1;

    if (sscanf(buffer, "%d", &value) != 1)
        return -1;

    return value;
}

void ui_login(char* username, int* isAdmin) {
    printf("\n");
    printf("╔══════════════════════════════╗\n");
    printf("║            LOGIN             ║\n");
    printf("╚══════════════════════════════╝\n");
    printf("Username: ");
    scanf("%49s", username);
    getchar();

    *isAdmin = (strcmp(username, "admin") == 0) ? 1 : 0;
}

Instruction ui_menu_user() {
    Instruction inst;
    memset(&inst, 0, sizeof(inst));

    int op;
    printf("\n");
    printf("╔══════════════════════════════════════╗\n");
    printf("║              USER MENU               ║\n");
    printf("╠══════════════════════════════════════╣\n");
    printf("║  1 - Assign Track to Train           ║\n");
    printf("║  2 - Set Track as FREE               ║\n");
    printf("║  3 - Set Track as NON-OPERATIONAL    ║\n");
    printf("║  4 - Give DEPARTURE Order            ║\n");
    printf("║  5 - Get data from Sensors           ║\n");
    printf("║  0 - Exit                            ║\n");
    printf("╚══════════════════════════════════════╝\n");
    printf("Option: ");

    op = ui_get_int();

    switch (op) {
        case 1:
            inst.type = INST_ASSIGN_TRACK;
            printf("Track ID: ");
            inst.trackId = ui_get_int();
            printf("Train ID: ");
            inst.trainId = ui_get_int();
            break;
        case 2:
            inst.type = INST_SET_FREE;
            printf("Track ID: ");
            inst.trackId = ui_get_int();
            break;
        case 3:
            inst.type = INST_SET_NONOP;
            printf("Track ID: ");
            inst.trackId = ui_get_int();
            break;
        case 4:
            inst.type = INST_DEPARTURE;
            printf("Train ID: ");
            inst.trainId = ui_get_int();
            break;
        case 5:
            inst.type = INST_GET_SENSOR_DATA;
            break;
        case 0:
            inst.type = INST_EXIT;
            break;
        default:
            inst.type = INST_INVALID;
            break;
    }

    return inst;
}

Instruction ui_menu_admin() {
    Instruction inst;
    memset(&inst, 0, sizeof(inst));

    int op;
    printf("\n");
    printf("╔══════════════════════════════════════╗\n");
    printf("║              ADMIN MENU              ║\n");
    printf("╠══════════════════════════════════════╣\n");
    printf("║  1 - Create Operator                 ║\n");
    printf("║  2 - Generate User Report            ║\n");
    printf("║  0 - Exit                            ║\n");
    printf("╚══════════════════════════════════════╝\n");
    printf("Option: ");

    op = ui_get_int();

    switch (op) {

        case 1:
            inst.type = INST_ADMIN_CREATE_OPERATOR;

            printf("\n");
            printf("╔══════════════════════════════╗\n");
            printf("║        CREATE OPERATOR       ║\n");
            printf("╚══════════════════════════════╝\n");

            printf("Operator name: ");
            scanf("%49[^\n]", inst.new_user.name);
            getchar();

            printf("Username: ");
            scanf("%49s", inst.new_user.username);
            getchar();

            int valid;
            int attempts = 0;

            do {
                attempts++;
                printf("Password (CAPS): ");
                scanf("%49s", inst.new_user.password_enc);
                getchar();

                valid = 1;
                for (int j = 0; inst.new_user.password_enc[j]; j++) {
                    if (inst.new_user.password_enc[j] < 'A' || inst.new_user.password_enc[j] > 'Z') {
                        valid = 0;
                        break;
                    }
                }

                if (!valid) {
                    printf("\n");
                    printf("╔════════════════════════════╗\n");
                    printf("║      INVALID PASSWORD      ║\n");
                    printf("╚════════════════════════════╝\n");
                }

            } while (!valid && attempts < 3);

            if (!valid)
                break;

            attempts = 0;
            do {
                attempts++;
                printf("Caesar key (1-26): ");
                inst.new_user.caesar_key = ui_get_int();

                if (inst.new_user.caesar_key > 0 && inst.new_user.caesar_key < 27)
                    break;

                printf("\n");
                printf("╔══════════════════════════════╗\n");
                printf("║      INVALID CAESAR KEY      ║\n");
                printf("╚══════════════════════════════╝\n");

            } while (attempts < 3);

            if (inst.new_user.caesar_key <= 0)
                inst.type = INST_INVALID;
            else
                encrypt_data(inst.new_user.password_enc,
                             inst.new_user.caesar_key,
                             inst.new_user.password_enc);

            break;

        case 2:
            inst.type = INST_ADMIN_REPORT;
            printf("Username for report: ");
            scanf("%49s", inst.reportUser);
            getchar();
            break;

        case 0:
            inst.type = INST_EXIT;
            break;

        default:
            inst.type = INST_INVALID;
            break;
    }

    return inst;
}
