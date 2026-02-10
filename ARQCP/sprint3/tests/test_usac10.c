#include <stdio.h>
#include <string.h>
#include "../struct/struct.h"

/* Assembly functions */
int extract_data(char* str, const char* token, char* unit, int* value);
int enqueue_value(int* buffer, int length, int* nelem, int* tail, int* head, int value);
int move_n_to_array(int* buffer, int length, int* nelem, int* tail, int* head, int n, int* array);
int median(int* vec, int length, int* me);

static int passed = 0;
static int failed = 0;

// Verifica se um valor inteiro é o esperado
static void assert_int(const char* name, int expected, int got) {
    if (expected == got) passed++;
    else {
        failed++;
        printf("FAIL: %s (expected %d, got %d)\n", name, expected, got);
    }
}

// Verifica strings
static void assert_str(const char* name, const char* exp, const char* got) {
    if (strcmp(exp, got) == 0) passed++;
    else {
        failed++;
        printf("FAIL: %s (expected '%s', got '%s')\n", name, exp, got);
    }
}

int test_usac10(void) {

    printf("Sensors and LightSigns tests — USAC10\n");

    // — Sensors


    char sensorStr[] = "TEMP&unit:C&value:20#HUM&unit:%&value:60";
    char unit[10];
    int value;

    // Extrair temperatura
    int ok = extract_data(sensorStr, "TEMP", unit, &value);
    assert_int("Extract TEMP OK", 1, ok);
    assert_int("TEMP value", 20, value);

    // Extrair humidade
    ok = extract_data(sensorStr, "HUM", unit, &value);
    assert_int("Extract HUM OK", 1, ok);
    assert_int("HUM value", 60, value);

    // Buffer circular para median
    int buffer[5];
    int nelem = 0, tail = 0, head = 0;

    enqueue_value(buffer, 5, &nelem, &tail, &head, 10);
    enqueue_value(buffer, 5, &nelem, &tail, &head, 30);
    enqueue_value(buffer, 5, &nelem, &tail, &head, 20);

    int vals[3];
    move_n_to_array(buffer, 5, &nelem, &tail, &head, 3, vals);

    int med;
    median(vals, 3, &med);
    assert_int("Median correct", 20, med);

    // — LightSigns

    char cmd[10];

    // Red LED
    snprintf(cmd, sizeof(cmd), "RE,%d", 1);
    assert_str("RE command", "RE,1", cmd);

    // Yellow LED
    snprintf(cmd, sizeof(cmd), "YE,%d", 2);
    assert_str("YE command", "YE,2", cmd);

    // Green LED
    snprintf(cmd, sizeof(cmd), "GE,%d", 3);
    assert_str("GE command", "GE,3", cmd);

    // Red Blink
    snprintf(cmd, sizeof(cmd), "RB,%d", 1);
    assert_str("RB command", "RB,1", cmd);

    printf("  Passed: %d\n", passed);
    printf("  Failed: %d\n\n", failed);

    return failed == 0;
}
