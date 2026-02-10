#include <stdio.h>
#include <string.h>
#include "../../code/USAC04/format_command.h"
#include "../../tests/test_base.h"

int main(void) {
    TestStats stats = init_stats();
    char cmd[256];
    int result;

    printf("=== Running tests for format_command ===\n\n");

    // TESTS THAT PASS

    // Success: valid tests (CMD,x)
    result = format_command(" rB ", 5, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, cmd, "RB,05", "Valid RB with leading/trailing spaces and small number");

    result = format_command(" Ye ", 25, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, cmd, "YE,25", "Valid YE, trim and uppercase");

    result = format_command(" ge ", 9, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, cmd, "GE,09", "Valid GE lowercase input");

    result = format_command("RE", 99, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, cmd, "RE,99", "Uppercase input no spaces");

    // Success: valid test (GTH, ignore number)
    result = format_command(" gTh ", 123, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1");
    stats = assert_equal_str(stats, cmd, "GTH", "Valid GTH, ignores number");

    // Success: n == 0 valid
    result = format_command(" YE ", 0, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 zero is valid");
    stats = assert_equal_str(stats, cmd, "YE,00", "Zero formatted correctly");

    // Success: GTH command with negative number (ignored)
    result = format_command("GTH", -5, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 GTH ignores negative number");
    stats = assert_equal_str(stats, cmd, "GTH", "GTH output unchanged");

    // Success: lowercase letters and extra spaces
    result = format_command("  yE   ", 7, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 lowercase + spaces");
    stats = assert_equal_str(stats, cmd, "YE,07", "Proper trimming and uppercase");

    // Success: maximum valid limit (n=99)
    result = format_command(" rb ", 99, cmd);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 max valid number");
    stats = assert_equal_str(stats, cmd, "RB,99", "Handles upper limit correctly");


    // FAILED TESTS

    // Fail: Invalid n >= 100
    result = format_command(" Ye ", 125, cmd);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid n >= 100");
    stats = assert_equal_str(stats, cmd, "", "Output should be empty invalid n");

    // Fail: negative n
    result = format_command(" RB ", -5, cmd);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid n negative");
    stats = assert_equal_str(stats, cmd, "", "Output should be empty invalid n");

    // Fail: invalid op
    result = format_command(" aaa ", 25, cmd);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid command");
    stats = assert_equal_str(stats, cmd, "", "Output should be empty invalid command");

    // Fail: op only with spaces
    result = format_command("    ", 25, cmd);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 empty after trimming");
    stats = assert_equal_str(stats, cmd, "", "Output should be empty");

    // Fail: empty string
    result = format_command("", 25, cmd);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 empty string");
    stats = assert_equal_str(stats, cmd, "", "Output should be empty");

    // Fail: invalid number with valid command
    result = format_command(" GE ", 500, cmd);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid large number");
    stats = assert_equal_str(stats, cmd, "", "Output empty large number");

    // Fail: invalid characters
    result = format_command("???", 10, cmd);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 invalid chars");
    stats = assert_equal_str(stats, cmd, "", "Output empty invalid chars");

    print_summary(stats, "format_command");
    return 0;
}
