#include <stdio.h>
#include <string.h>
#include "../../code/USAC02/decrypt_data.h"
#include "../../tests/test_base.h"

int main(void) {
    TestStats stats = init_stats();
    char out[256];
    int result;

    printf("=== Running tests for decrypt_data ===\n\n");

    result = decrypt_data("BCD", 1, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "ABC", "Shift by 1 backwards");

    result = decrypt_data("ABC", 3, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "XYZ", "Wrap-around Z");

    result = decrypt_data("MJQQT", 5, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "HELLO", "Shift by 5 backwards");

    result = decrypt_data("CAESAR", 26, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "CAESAR", "Key 26 no change");

    result = decrypt_data("", 10, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "", "Empty string");

    result = decrypt_data("HELLO", 0, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid key");
    stats = assert_equal_str(stats, out, "", "Output empty invalid key");

    result = decrypt_data("HELLO", 27, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid key");
    stats = assert_equal_str(stats, out, "", "Output empty invalid key");

    result = decrypt_data("HELLO", -3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid key");
    stats = assert_equal_str(stats, out, "", "Output empty invalid key");

    result = decrypt_data("HELLO WORLD", 3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 space middle");
    stats = assert_equal_str(stats, out, "", "Output empty space");

    result = decrypt_data("HELLO ", 3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 space end");
    stats = assert_equal_str(stats, out, "", "Output empty space");

    result = decrypt_data(" HELLO", 3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 space start");
    stats = assert_equal_str(stats, out, "", "Output empty space");

    result = decrypt_data("HEL LO", 3, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 space inside");
    stats = assert_equal_str(stats, out, "", "Output empty space");

    result = decrypt_data("XIWX123", 4, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 number in input");
    stats = assert_equal_str(stats, out, "", "Output empty invalid char");

    result = decrypt_data("j", 2, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 lowercase");
    stats = assert_equal_str(stats, out, "", "Output empty lowercase");

    result = decrypt_data("DDDDD", 3, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "AAAAA", "Repeated letters decrypt correctly");

    result = decrypt_data("AAAAA", 1, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "ZZZZZ", "Wrap-around A→Z backwards");

    result = decrypt_data("NOPQRSTUVWXYZABCDEFGHIJKLM", 13, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "Full alphabet shift backwards");

    result = decrypt_data("B", 1, out);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, out, "A", "Shift 1 backwards simple case");

    result = decrypt_data("A B C", 2, out);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 spaces");
    stats = assert_equal_str(stats, out, "", "Output empty spaces");

    print_summary(stats, "decrypt_data");
    return 0;
}
