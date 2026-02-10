#include <stdio.h>
#include <string.h>
#include "../../code/USAC05/enqueue_value.h"
#include "../../tests/test_base.h"

int main(void) {
    TestStats stats = init_stats();
    printf("=== Running tests for enqueue_value ===\n\n");

    int result;

    /* ============================
       VALID INSERTIONS (NOT FULL)
       ============================ */

    int buffer[5];
    int length = 5;
    int nelem = 0;
    int head = 0;
    int tail = 0;

    // Insert first element
    result = enqueue_value(buffer, length, &nelem, &tail, &head, 10);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 when buffer not full");
    stats = assert_equal_int(stats, nelem, 1, "nelem should be 1");
    stats = assert_equal_int(stats, buffer[0], 10, "buffer[0] should store 10");

    // Second
    result = enqueue_value(buffer, length, &nelem, &tail, &head, 20);
    stats = assert_equal_int(stats, result, 0, "Return 0 for second insert");
    stats = assert_equal_int(stats, nelem, 2, "nelem = 2");
    stats = assert_equal_int(stats, buffer[1], 20, "buffer[1]=20");

    // Third
    result = enqueue_value(buffer, length, &nelem, &tail, &head, 30);
    stats = assert_equal_int(stats, result, 0, "Return 0 for third insert");
    stats = assert_equal_int(stats, nelem, 3, "nelem = 3");
    stats = assert_equal_int(stats, buffer[2], 30, "buffer[2]=30");

    // Fourth
    result = enqueue_value(buffer, length, &nelem, &tail, &head, 40);
    stats = assert_equal_int(stats, result, 0, "Return 0 for fourth insert");
    stats = assert_equal_int(stats, nelem, 4, "nelem = 4");
    stats = assert_equal_int(stats, buffer[3], 40, "buffer[3]=40");

    /* ============================
       FILL TO MAX
       ============================ */

    result = enqueue_value(buffer, length, &nelem, &tail, &head, 50);
    stats = assert_equal_int(stats, result, 1, "Return 1 when buffer becomes full");
    stats = assert_equal_int(stats, nelem, 5, "nelem = length (5)");
    stats = assert_equal_int(stats, buffer[4], 50, "buffer[4]=50");

    /* ============================
       OVERFLOW (OVERWRITE OLDEST)
       ============================ */

    // Oldest = buffer[0]
    result = enqueue_value(buffer, length, &nelem, &tail, &head, 60);
    stats = assert_equal_int(stats, result, 1, "Return 1 when overwriting in full state");
    stats = assert_equal_int(stats, nelem, 5, "nelem remains 5");
    stats = assert_equal_int(stats, buffer[0], 60, "Oldest element overwritten with 60");

    // Next overwrite
    result = enqueue_value(buffer, length, &nelem, &tail, &head, 70);
    stats = assert_equal_int(stats, buffer[1], 70, "Next circular overwrite stores 70");

    result = enqueue_value(buffer, length, &nelem, &tail, &head, 80);
    stats = assert_equal_int(stats, buffer[2], 80, "Circular overwrite stores 80");

    /* ============================
       BUFFER OF SIZE 1
       ============================ */

    int small[1];
    length = 1;
    nelem = 0;
    head = 0;
    tail = 0;

    result = enqueue_value(small, length, &nelem, &tail, &head, 5);
    stats = assert_equal_int(stats, result, 1, "1-element buffer becomes full");
    stats = assert_equal_int(stats, small[0], 5, "Stored 5");

    result = enqueue_value(small, length, &nelem, &tail, &head, 9);
    stats = assert_equal_int(stats, result, 1, "Overwrite still returns 1");
    stats = assert_equal_int(stats, small[0], 9, "Overwrite with 9 works");

    /* ============================
       NEGATIVE AND ZERO VALUES
       ============================ */

    int buf2[3];
    length = 3;
    nelem = 0;
    head = 0;
    tail = 0;

    result = enqueue_value(buf2, length, &nelem, &tail, &head, -10);
    stats = assert_equal_int(stats, result, 0, "Negative numbers should be allowed");
    stats = assert_equal_int(stats, buf2[0], -10, "Stored -10");

    result = enqueue_value(buf2, length, &nelem, &tail, &head, 0);
    stats = assert_equal_int(stats, result, 0, "Zero should be allowed");
    stats = assert_equal_int(stats, buf2[1], 0, "Stored 0");

    print_summary(stats, "enqueue_value");
    return 0;
}
