#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "../manager/manager.h"

int test_users(void) {
    int ok = 1;

    Manager m;
    memset(&m, 0, sizeof(Manager));

    m.users = malloc(2 * sizeof(User));
    if (!m.users)
        return 0;

    m.num_users = 2;

    strcpy(m.users[0].username, "admin");
    m.users[0].role = ROLE_ADMIN;

    strcpy(m.users[1].username, "op");
    m.users[1].role = ROLE_OPERATOR;

    ok &= m.users[0].role == ROLE_ADMIN;
    ok &= m.users[1].role == ROLE_OPERATOR;

    m.current_user = &m.users[0];
    ok &= m.current_user->role == ROLE_ADMIN;

    m.current_user = &m.users[1];
    ok &= m.current_user->role == ROLE_OPERATOR;

    free(m.users);

    printf("User management tests: %s\n", ok ? "OK" : "FAIL");
    return ok;
}
