#include <stdio.h>
#include <string.h>
#include "../../code/USAC08/sort_array.h"
#include "../../tests/test_base.h"

int arrays_equal(int *a, int *b, int len) {
    for (int i = 0; i < len; i++)
        if (a[i] != b[i]) return 0;
    return 1;
}

int main(void) {
    TestStats stats = init_stats();
    int result;
    int out[20];

    printf("=== Running tests for sort_array ===\n\n");

    int arr1[] = {3, 1, 2};
    int exp1[] = {1, 2, 3};
    memcpy(out, arr1, sizeof(arr1));
    result = sort_array(out, 3, 1);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (ascending)");
    stats = assert_equal_int(stats, arrays_equal(out, exp1, 3), 1, "Ascending order works");

    int arr2[] = {3, 1, 2};
    int exp2[] = {3, 2, 1};
    memcpy(out, arr2, sizeof(arr2));
    result = sort_array(out, 3, 0);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (descending)");
    stats = assert_equal_int(stats, arrays_equal(out, exp2, 3), 1, "Descending order works");

    int arr3[] = {5, 5, 5, 5};
    int exp3[] = {5, 5, 5, 5};
    memcpy(out, arr3, sizeof(arr3));
    result = sort_array(out, 4, 1);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (all equal ascending)");
    stats = assert_equal_int(stats, arrays_equal(out, exp3, 4), 1, "Array unchanged (equal elements)");

    int arr4[] = {1};
    int exp4[] = {1};
    memcpy(out, arr4, sizeof(arr4));
    result = sort_array(out, 1, 1);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (single element)");
    stats = assert_equal_int(stats, arrays_equal(out, exp4, 1), 1, "Single element unchanged");

    int arr5[] = {-3, 10, 0, -5, 8};
    int exp5[] = {-5, -3, 0, 8, 10};
    memcpy(out, arr5, sizeof(arr5));
    result = sort_array(out, 5, 1);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (ascending negatives)");
    stats = assert_equal_int(stats, arrays_equal(out, exp5, 5), 1, "Ascending order with negatives");

    int arr6[] = {-3, 10, 0, -5, 8};
    int exp6[] = {10, 8, 0, -3, -5};
    memcpy(out, arr6, sizeof(arr6));
    result = sort_array(out, 5, 0);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (descending negatives)");
    stats = assert_equal_int(stats, arrays_equal(out, exp6, 5), 1, "Descending order with negatives");

    int arr7[] = {1, 2, 3, 4, 5};
    int exp7[] = {1, 2, 3, 4, 5};
    memcpy(out, arr7, sizeof(arr7));
    result = sort_array(out, 5, 1);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (already ascending)");
    stats = assert_equal_int(stats, arrays_equal(out, exp7, 5), 1, "Array unchanged");

    int arr8[] = {5, 4, 3, 2, 1};
    int exp8[] = {5, 4, 3, 2, 1};
    memcpy(out, arr8, sizeof(arr8));
    result = sort_array(out, 5, 0);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (already descending)");
    stats = assert_equal_int(stats, arrays_equal(out, exp8, 5), 1, "Array unchanged");

    int arr9[] = {7, 1, 9, 1, 7};
    int exp9[] = {1, 1, 7, 7, 9};
    memcpy(out, arr9, sizeof(arr9));
    result = sort_array(out, 5, 1);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (duplicates ascending)");
    stats = assert_equal_int(stats, arrays_equal(out, exp9, 5), 1, "Duplicates sorted correctly ascending");

    int arr10[] = {7, 1, 9, 1, 7};
    int exp10[] = {9, 7, 7, 1, 1};
    memcpy(out, arr10, sizeof(arr10));
    result = sort_array(out, 5, 0);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (duplicates descending)");
    stats = assert_equal_int(stats, arrays_equal(out, exp10, 5), 1, "Duplicates sorted correctly descending");

    int arr11[] = {42, -1, 100, 0};
    int exp11[] = {100, 42, 0, -1};
    memcpy(out, arr11, sizeof(arr11));
    result = sort_array(out, 4, 0);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (descending mix)");
    stats = assert_equal_int(stats, arrays_equal(out, exp11, 4), 1, "Descending with mixed values");

    int arr12[] = {42, -1, 100, 0};
    int exp12[] = {-1, 0, 42, 100};
    memcpy(out, arr12, sizeof(arr12));
    result = sort_array(out, 4, 1);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (ascending mix)");
    stats = assert_equal_int(stats, arrays_equal(out, exp12, 4), 1, "Ascending with mixed values");

    int arr13[] = {5, 4, 3, 2, 1};
    memcpy(out, arr13, sizeof(arr13));
    result = sort_array(out, 0, 1);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 (length = 0)");

    int arr14[] = {5, 4, 3, 2, 1};
    memcpy(out, arr14, sizeof(arr14));
    result = sort_array(out, -5, 1);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 (negative length)");

    int arr15[] = {9, 8, 7};
    memcpy(out, arr15, sizeof(arr15));
    result = sort_array(out, 3, 2);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 (invalid order param)");

    print_summary(stats, "sort_array");
    return 0;
}
