#include <stdio.h>
#include <string.h>
#include "../asm/decrypt_data.h"

int test_persistence(void) {
    int ok = 1;
    char dec[50];

    decrypt_data("KXNBOVSXNY", 10, dec);
    ok &= strcmp(dec, "ANDRELINDO") == 0;

    decrypt_data("G", 3, dec);
    ok &= strcmp(dec, "D") == 0;

    decrypt_data("Z", 26, dec);
    ok &= strcmp(dec, "Z") == 0;

    printf("Persistence consistency tests: %s\n", ok ? "OK" : "FAIL");
    return ok;
}
