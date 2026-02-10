#include <stdio.h>
#include <string.h>
#include "../../code/USAC07/move_n_to_array.h"
#include "../../tests/test_base.h"

// Função auxiliar para comparar arrays
int arrays_equal(int *a, int *b, int len) {
    for (int i = 0; i < len; i++)
        if (a[i] != b[i]) return 0;
    return 1;
}

int main(void) {
    TestStats stats = init_stats();
    int result;
    int array[10];

    printf("=== Running tests for move_n_to_array ===\n\n");

    // ----------------------------
    // TEST 1 — Basic success case
    // ----------------------------
    int buf1[5] = {7,8,9,10,11};
    int len1 = 5, ne1 = 4, ta1 = 2, he1 = 3, n1 = 3;
    int exp_arr1[] = {10, 11, 7};
    memset(array, 0, sizeof(array));

    result = move_n_to_array(buf1, len1, &ne1, &ta1, &he1, n1, array);
    stats = assert_equal_int(stats, result, 1, "Return 1 for successful move");
    stats = assert_equal_int(stats, ne1, 1, "nelem reduced correctly");
    stats = assert_equal_int(stats, he1, 1, "head updated correctly (wrap-around)");
    stats = assert_equal_int(stats, arrays_equal(array, exp_arr1, 3), 1, "Elements moved correctly");

    // ----------------------------
    // TEST 2 — Failure (n > nelem)
    // ----------------------------
    int buf2[5] = {1,2,3,4,5};
    int len2 = 5, ne2 = 2, ta2 = 2, he2 = 0, n2 = 4;
    memset(array, 0, sizeof(array));

    result = move_n_to_array(buf2, len2, &ne2, &ta2, &he2, n2, array);
    stats = assert_equal_int(stats, result, 0, "Return 0 when n > nelem");
    stats = assert_equal_int(stats, ne2, 2, "nelem unchanged on fail");
    stats = assert_equal_int(stats, he2, 0, "head unchanged on fail");

    // ----------------------------
    // TEST 3 — Move all elements
    // ----------------------------
    int buf3[5] = {10,20,30,40,50};
    int len3 = 5, ne3 = 5, ta3 = 0, he3 = 0, n3 = 5;
    int exp_arr3[] = {10,20,30,40,50};
    memset(array, 0, sizeof(array));

    result = move_n_to_array(buf3, len3, &ne3, &ta3, &he3, n3, array);
    stats = assert_equal_int(stats, result, 1, "Return 1 when moving all elements");
    stats = assert_equal_int(stats, ne3, 0, "nelem becomes zero after full move");
    stats = assert_equal_int(stats, he3, 0, "head wraps correctly");
    stats = assert_equal_int(stats, arrays_equal(array, exp_arr3, 5), 1, "All elements moved correctly");

    // ----------------------------
    // TEST 4 — Head at end (wrap)
    // ----------------------------
    int buf4[5] = {10,20,30,40,50};
    int len4 = 5, ne4 = 3, ta4 = 2, he4 = 4, n4 = 2;
    int exp_arr4[] = {50, 10};
    memset(array, 0, sizeof(array));

    result = move_n_to_array(buf4, len4, &ne4, &ta4, &he4, n4, array);
    stats = assert_equal_int(stats, result, 1, "Return 1 on wrap-around move");
    stats = assert_equal_int(stats, ne4, 1, "nelem updated correctly after wrap");
    stats = assert_equal_int(stats, he4, 1, "head wraps to 1");
    stats = assert_equal_int(stats, arrays_equal(array, exp_arr4, 2), 1, "Correct wrap-around sequence");

    // ----------------------------
    // TEST 5 — Single element move
    // ----------------------------
    int buf5[5] = {99, 0, 0, 0, 0};
    int len5 = 5, ne5 = 1, ta5 = 1, he5 = 0, n5 = 1;
    int exp_arr5[] = {99};
    memset(array, 0, sizeof(array));

    result = move_n_to_array(buf5, len5, &ne5, &ta5, &he5, n5, array);
    stats = assert_equal_int(stats, result, 1, "Return 1 (single element)");
    stats = assert_equal_int(stats, ne5, 0, "nelem becomes 0");
    stats = assert_equal_int(stats, he5, 1, "head incremented correctly");
    stats = assert_equal_int(stats, arrays_equal(array, exp_arr5, 1), 1, "Single element copied correctly");

    // ----------------------------
    // TEST 6 — n = 0 (optional edge)
    // ----------------------------
    int buf6[5] = {10,20,30,40,50};
    int len6 = 5, ne6 = 3, ta6 = 3, he6 = 1, n6 = 0;
    memset(array, 0, sizeof(array));

    result = move_n_to_array(buf6, len6, &ne6, &ta6, &he6, n6, array);
    stats = assert_equal_int(stats, result, 1, "Return 1 when n=0 (nothing moved)");
    stats = assert_equal_int(stats, ne6, 3, "nelem unchanged (n=0)");
    stats = assert_equal_int(stats, he6, 1, "head unchanged (n=0)");

    // ----------------------------
    // TEST 7 — NULL pointer validations
    // ----------------------------
    int buf7[5] = {1,2,3,4,5};
    int len7 = 5, ne7 = 3, ta7 = 1, he7 = 0, n7 = 2;

    result = move_n_to_array(NULL, len7, &ne7, &ta7, &he7, n7, array);
    stats = assert_equal_int(stats, result, 0, "Return 0 if buffer == NULL");

    result = move_n_to_array(buf7, len7, NULL, &ta7, &he7, n7, array);
    stats = assert_equal_int(stats, result, 0, "Return 0 if nelem == NULL");

    result = move_n_to_array(buf7, len7, &ne7, NULL, &he7, n7, array);
    stats = assert_equal_int(stats, result, 0, "Return 0 if tail == NULL");

    result = move_n_to_array(buf7, len7, &ne7, &ta7, NULL, n7, array);
    stats = assert_equal_int(stats, result, 0, "Return 0 if head == NULL");

    result = move_n_to_array(buf7, len7, &ne7, &ta7, &he7, n7, NULL);
    stats = assert_equal_int(stats, result, 0, "Return 0 if array == NULL");

    // combinações de ponteiros nulos
    result = move_n_to_array(NULL, len7, NULL, &ta7, &he7, n7, array);
    stats = assert_equal_int(stats, result, 0, "Return 0 if buffer and nelem NULL");

    result = move_n_to_array(NULL, len7, NULL, NULL, NULL, n7, NULL);
    stats = assert_equal_int(stats, result, 0, "Return 0 if all pointers NULL");

    // ----------------------------
    // SUMMARY
    // ----------------------------
    print_summary(stats, "move_n_to_array");
    return 0;
}
