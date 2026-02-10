#include <stdio.h>
#include <string.h>
#include "../asm/encrypt_data.h"
#include "../asm/decrypt_data.h"

int test_auth(void) {
    char enc[50];
    char dec[50];
    int ok = 1;

    encrypt_data("A", 1, enc);
    decrypt_data(enc, 1, dec);
    ok &= strcmp(dec, "A") == 0;

    encrypt_data("Z", 1, enc);
    decrypt_data(enc, 1, dec);
    ok &= strcmp(dec, "Z") == 0;

    encrypt_data("ABC", 3, enc);
    decrypt_data(enc, 3, dec);
    ok &= strcmp(dec, "ABC") == 0;

    encrypt_data("ANDRELINDO", 10, enc);
    decrypt_data(enc, 10, dec);
    ok &= strcmp(dec, "ANDRELINDO") == 0;

    encrypt_data("TEST", 26, enc);
    decrypt_data(enc, 26, dec);
    ok &= strcmp(dec, "TEST") == 0;

    printf("Authentication tests: %s\n", ok ? "OK" : "FAIL");
    return ok;
}
