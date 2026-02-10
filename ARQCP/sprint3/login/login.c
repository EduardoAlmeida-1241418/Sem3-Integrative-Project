#include <string.h>
#include <stdio.h>
#include "../ui/ui.h"
#include "../asm/decrypt_data.h"
#include "login.h"

int manager_login(Manager *manager) {

    char username[20];
    int isAdminUI = 0;
    char password[50];

    ui_login(username, &isAdminUI);

    printf("Password: ");
    scanf("%49s", password);

    int c;
    while ((c = getchar()) != '\n' && c != EOF);


    for (int i = 0; i < (*manager).num_users; i++) {

        char decrypted[50];
        decrypt_data((*manager).users[i].password_enc,
                     (*manager).users[i].caesar_key,
                     decrypted);

        if (strcmp((*manager).users[i].username, username) == 0 &&
            strcmp(decrypted, password) == 0) {

            (*manager).current_user = &((*manager).users[i]);
            return 1;
        }
    }

    printf("Invalid credentials\n");
    return 0;
}
