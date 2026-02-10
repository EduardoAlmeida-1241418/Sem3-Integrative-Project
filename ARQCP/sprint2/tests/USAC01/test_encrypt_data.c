#include <stdio.h>
#include <string.h>
#include "../../code/USAC01/encrypt_data.h"
#include "../../tests/test_base.h"

int main(void) {
    TestStats stats = init_stats();
    char out[256];
    int result;

    printf("=== Running tests for encrypt_data ===\n\n");

    result = encrypt_data("ABC", 1, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "BCD", "Shift by 1");

    result = encrypt_data("XYZ", 3, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "ABC", "Wrap-around");

    result = encrypt_data("HELLO", 5, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "MJQQT", "Shift by 5");

    result = encrypt_data("CAESAR", 26, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "CAESAR", "Key 26 no change");

    result = encrypt_data("", 5, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "", "Empty string");

    result = encrypt_data("HELLO", 0, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid key");
    stats = assert_equal_str(stats, out, "", "Output empty invalid key");

    result = encrypt_data("HELLO", 27, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid key");
    stats = assert_equal_str(stats, out, "", "Output empty invalid key");

    result = encrypt_data("HELLO", -2, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid key");
    stats = assert_equal_str(stats, out, "", "Output empty invalid key");

    result = encrypt_data("HELLO WORLD", 3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 space middle");
    stats = assert_equal_str(stats, out, "", "Output empty space");

    result = encrypt_data("HELLO ", 3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 space end");
    stats = assert_equal_str(stats, out, "", "Output empty space");

    result = encrypt_data(" HELLO", 3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 space start");
    stats = assert_equal_str(stats, out, "", "Output empty space");

    result = encrypt_data("HEL LO", 3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 space inside");
    stats = assert_equal_str(stats, out, "", "Output empty space");

    result = encrypt_data("TEST123", 4, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 number");
    stats = assert_equal_str(stats, out, "", "Output empty invalid char");

    result = encrypt_data("Hello", 2, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 lowercase");
    stats = assert_equal_str(stats, out, "", "Output empty lowercase");

    result = encrypt_data("AAAAA", 3, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "DDDDD", "Repeated A");

    result = encrypt_data("ZZZZZ", 1, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "AAAAA", "Wrap Z→A");

    result = encrypt_data("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 13, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "NOPQRSTUVWXYZABCDEFGHIJKLM", "Shift full alphabet");

    result = encrypt_data("A", 25, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "Z", "Shift 25");

    result = encrypt_data("Z", 25, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "Y", "Shift 25 Z→Y");

    result = encrypt_data("A B C", 1, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 spaces");
    stats = assert_equal_str(stats, out, "", "Output empty spaces");

    print_summary(stats, "encrypt_data");
    return 0;
}