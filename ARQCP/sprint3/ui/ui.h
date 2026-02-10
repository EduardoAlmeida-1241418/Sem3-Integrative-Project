#ifndef UI_H
#define UI_H

#include "../struct/struct.h"

int initialize_application();

int ui_get_int();

/*
 * Recolhe username e define role temporária
 * (admin = username "admin")
 */
void ui_login(char* username, int* isAdmin);

/*
 * Menu para USER normal
 */
Instruction ui_menu_user();

/*
 * Menu para ADMIN (apenas relatório)
 */
Instruction ui_menu_admin();

#endif
