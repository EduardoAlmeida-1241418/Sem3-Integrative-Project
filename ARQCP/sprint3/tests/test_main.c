#include <stdio.h>

int test_users(void);
int test_tracks(void);
int test_sensors(void);
int test_board(void);
int test_usac10(void);
int test_usac12(void);

int main(void) {

    int ok = 1;

    printf("SPRINT 3 UNIT TESTS\n\n");

    ok &= test_users();
    ok &= test_tracks();
    ok &= test_sensors();
    ok &= test_board();
    ok &= test_usac12();
    ok &= test_usac10();

    printf("\nOverall result: %s\n", ok ? "OK" : "FAIL");

    return 0;
}
