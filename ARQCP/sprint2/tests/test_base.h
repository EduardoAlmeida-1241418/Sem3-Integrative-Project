#ifndef TEST_BASE_H
#define TEST_BASE_H

#include <stdio.h>
#include <string.h>

typedef struct {
    int total;
    int passed;
} TestStats;

TestStats init_stats() {
    TestStats stats;
    stats.total = 0;
    stats.passed = 0;
    return stats;
}

TestStats assert_equal_str(TestStats stats, const char *got, const char *expected, const char *message) {
    stats.total++;
    if (strcmp(got, expected) == 0) {
        stats.passed++;
        printf("[PASS] %s\n", message);
    } else {
        printf("[FAIL] %s\n", message);
        printf("       Expected: \"%s\"\n", expected);
        printf("       Got:      \"%s\"\n", got);
    }
    return stats;
}

TestStats assert_equal_int(TestStats stats, int got, int expected, const char *message) {
    stats.total++;
    if (got == expected) {
        stats.passed++;
        printf("[PASS] %s\n", message);
    } else {
        printf("[FAIL] %s\n", message);
        printf("       Expected: %d\n", expected);
        printf("       Got:      %d\n", got);
    }
    return stats;
}

void print_summary(TestStats stats, const char *test_name) {
    printf("\n=== Test Summary: %s ===\n", test_name);
    printf("Passed: %d / %d\n", stats.passed, stats.total);

    if (stats.passed == stats.total)
        printf("All tests passed successfully.\n\n");
    else
        printf("Some tests failed.\n\n");
}

#endif
