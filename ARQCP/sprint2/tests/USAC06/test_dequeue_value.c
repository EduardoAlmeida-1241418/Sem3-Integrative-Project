#include <stdio.h>
#include <string.h>
#include "../../code/USAC06/dequeue_value.h"
#include "../../code/USAC05/enqueue_value.h"
#include "../../tests/test_base.h"

int main(void) {
    TestStats stats = init_stats();
    printf("=== Running tests for dequeue_value ===\n\n");

    int result;
    int value;

    /* ============================
       DEQUEUE FROM EMPTY BUFFER
       ============================ */

    int buffer[5];
    int length = 5;
    int nelem = 0;
    int head = 0;
    int tail = 0;

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, result, 0, "Return 0 when buffer is empty");
    stats = assert_equal_int(stats, nelem, 0, "nelem stays 0");
    stats = assert_equal_int(stats, tail, 0, "tail unchanged");
    stats = assert_equal_int(stats, head, 0, "head unchanged");

    /* ============================
       SIMPLE DEQUEUE OPERATIONS
       ============================ */

    enqueue_value(buffer, length, &nelem, &tail, &head, 10);
    enqueue_value(buffer, length, &nelem, &tail, &head, 20);
    enqueue_value(buffer, length, &nelem, &tail, &head, 30);

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, result, 1, "Dequeue succeeds");
    stats = assert_equal_int(stats, value, 10, "Removed oldest value = 10");
    stats = assert_equal_int(stats, nelem, 2, "nelem = 2");

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 20, "Next removed = 20");
    stats = assert_equal_int(stats, nelem, 1, "nelem = 1");

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 30, "Next removed = 30");
    stats = assert_equal_int(stats, nelem, 0, "nelem = 0 (empty)");

    /* ============================
       DEQUEUE AFTER WRAP-AROUND
       ============================ */

    nelem = 0;
    head = 0;
    tail = 0;

    enqueue_value(buffer, length, &nelem, &tail, &head, 1);
    enqueue_value(buffer, length, &nelem, &tail, &head, 2);
    enqueue_value(buffer, length, &nelem, &tail, &head, 3);
    enqueue_value(buffer, length, &nelem, &tail, &head, 4);
    enqueue_value(buffer, length, &nelem, &tail, &head, 5);

    // Force wrap-around (overwrite oldest)
    enqueue_value(buffer, length, &nelem, &tail, &head, 6);
    enqueue_value(buffer, length, &nelem, &tail, &head, 7);

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 3, "After wrap-around: first removed = 3");

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 4, "Next removed = 4");

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 5, "Next removed = 5");

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 6, "Next removed = 6");

    result = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 7, "Next removed = 7");
    stats = assert_equal_int(stats, nelem, 0, "Buffer empty at end");

    /* ============================
       BUFFER OF SIZE 1
       ============================ */

    int small[1];
    length = 1;
    nelem = 0;
    head = 0;
    tail = 0;

    enqueue_value(small, length, &nelem, &tail, &head, 9);

    result = dequeue_value(small, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, result, 1, "Dequeue works for size 1");
    stats = assert_equal_int(stats, value, 9, "Returned value = 9");
    stats = assert_equal_int(stats, nelem, 0, "Buffer becomes empty");

    result = dequeue_value(small, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, result, 0, "Second dequeue fails on empty buffer");

    /* ============================
       NEGATIVE AND ZERO VALUES
       ============================ */

    int buf2[3];
    length = 3;
    nelem = 0;
    head = 0;
    tail = 0;

    enqueue_value(buf2, length, &nelem, &tail, &head, -5);
    enqueue_value(buf2, length, &nelem, &tail, &head, 0);
    enqueue_value(buf2, length, &nelem, &tail, &head, 8);

    result = dequeue_value(buf2, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, -5, "Negative stored and returned correctly");

    result = dequeue_value(buf2, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 0, "Zero returned correctly");

    result = dequeue_value(buf2, length, &nelem, &tail, &head, &value);
    stats = assert_equal_int(stats, value, 8, "Positive returned correctly");

    print_summary(stats, "dequeue_value");
    return 0;
}
