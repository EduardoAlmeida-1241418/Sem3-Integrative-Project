#include <stdio.h>
#include <string.h>
#include "../../code/USAC09/median.h"
#include "../../tests/test_base.h"

int main(void) {
    TestStats stats = init_stats();
    int result, me;

    printf("=== Running tests for median ===\n\n");

    // Teste 1 — número ímpar de elementos
    int v1[] = {9, 5, 1, 7, 3};
    result = median(v1, 5, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (odd length)");
    stats = assert_equal_int(stats, me, 5, "Median of {9,5,1,7,3} should be 5");

    // Teste 2 — número par de elementos
    int v2[] = {10, 20, 30, 40};
    result = median(v2, 4, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (even length)");
    stats = assert_equal_int(stats, me, 25, "Median of {10,20,30,40} should be 25");

    // Teste 3 — vetor já ordenado ascendente
    int v3[] = {1, 2, 3, 4, 5};
    result = median(v3, 5, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (sorted ascending)");
    stats = assert_equal_int(stats, me, 3, "Median of {1,2,3,4,5} should be 3");

    // Teste 4 — vetor descendente
    int v4[] = {5, 4, 3, 2, 1};
    result = median(v4, 5, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (sorted descending)");
    stats = assert_equal_int(stats, me, 3, "Median of {5,4,3,2,1} should be 3");

    // Teste 5 — length inválido (0)
    int v5[] = {1, 2, 3};
    result = median(v5, 0, &me);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 (invalid length)");

    // Teste 6 — length inválido (negativo)
    result = median(v5, -3, &me);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 (invalid length)");

    // Teste 7 — vetor com valores repetidos
    int v6[] = {4, 4, 4, 4, 4};
    result = median(v6, 5, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (all equal)");
    stats = assert_equal_int(stats, me, 4, "Median of {4,4,4,4,4} should be 4");

    // Teste 8 — vetor com 2 elementos
    int v7[] = {100, 200};
    result = median(v7, 2, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (even length small)");
    stats = assert_equal_int(stats, me, 150, "Median of {100,200} should be 150");

    // Teste 9 — vetor com 1 elemento
    int v8[] = {42};
    result = median(v8, 1, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (single element)");
    stats = assert_equal_int(stats, me, 42, "Median of {42} should be 42");

    // 10 elements (even)
    int v9[] = {9,8,7,6,5,4,3,2,1,0};
    result = median(v9, 10, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (10 elems)");
    stats = assert_equal_int(stats, me, 4, "Median of 10 descending should be 4");

    // 11 elements (odd)
    int v10[] = {11,10,9,8,7,6,5,4,3,2,1};
    result = median(v10, 11, &me);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 (11 elems)");
    stats = assert_equal_int(stats, me, 6, "Median of 11 descending should be 6");

    // 15 elements random
    int v11[] = {5,17,3,22,14,7,9,13,6,8,15,1,2,4,10};
    result = median(v11, 15, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (15 elems random)");
    stats = assert_equal_int(stats, me, 8, "Median of 15 random should be 8");

    // 20 elements (even)
    int v12[] = {20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
    result = median(v12, 20, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (20 elems)");
    stats = assert_equal_int(stats, me, 10, "Median of 20 descending should be 10");

    // negatives
    int v13[] = {-1,-2,-3,-4,-5,-6,-7,-8,-9,-10};
    result = median(v13, 10, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (negatives even)");
    stats = assert_equal_int(stats, me, -6, "Median of negatives should be -6");

    // mixed signs
    int v14[] = {-10, -5, -1, 0, 5, 10};
    result = median(v14, 6, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (mixed signs)");
    stats = assert_equal_int(stats, me, -1, "Median of mixed signs should be -1");


    // large random 20 positive numbers
    int v15[] = {100,12,43,21,9,55,3,5,78,45,90,33,76,81,56,7,8,4,2,1};
    result = median(v15, 20, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (20 random positives)");
    stats = assert_equal_int(stats, me, 27, "Median of random positives should be 27");

    // repeated sequences
    int v16[] = {10,10,10,20,20,20,30,30,30,40,40,40};
    result = median(v16, 12, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (repeated pattern)");
    stats = assert_equal_int(stats, me, 25, "Median repeated pattern should be 25");

    // alternating signs
    int v17[] = {-10,10,-20,20,-30,30,-40,40};
    result = median(v17, 8, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (alternating signs)");
    stats = assert_equal_int(stats, me, 0, "Median alternating signs should be 0");

    // ascending long
    int v18[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
    result = median(v18, 20, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (long ascending)");
    stats = assert_equal_int(stats, me, 10, "Median long ascending should be 10");

    // INT limits
    int v19[] = {2147483647, 0, -2147483648};
    result = median(v19, 3, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (INT limits)");
    stats = assert_equal_int(stats, me, 0, "Median with INT limits should be 0");

    // almost sorted large
    int v20[] = {1,3,2,5,4,7,6,9,8,10,12,11,13,15,14,17,16,19,18,20};
    result = median(v20, 20, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (almost sorted)");
    stats = assert_equal_int(stats, me, 10, "Median almost sorted should be 10");

    // repeated negatives
    int v21[] = {-5,-5,-5,-4,-3,-2,-1};
    result = median(v21, 7, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (repeated negatives)");
    stats = assert_equal_int(stats, me, -4, "Median repeated negatives should be -4");

    // zeros only
    int v22[] = {0,0,0,0,0,0,0,0,0,0};
    result = median(v22, 10, &me);
    stats = assert_equal_int(stats, result, 1, "Return 1 (all zeros)");
    stats = assert_equal_int(stats, me, 0, "Median all zeros should be 0");

    // vetor NULL
    me = 99; // valor inicial conhecido
    result = median(NULL, 5, &me);
    stats = assert_equal_int(stats, result, 0, "Return 0 (vec == NULL)");
    stats = assert_equal_int(stats, me, 99, "Output unchanged when vec == NULL");

    // ponteiro 'me' NULL
    int vec1[5] = {1, 2, 3, 4, 5};
    result = median(vec1, 5, NULL);
    stats = assert_equal_int(stats, result, 0, "Return 0 (me == NULL)");

    // ambos NULL
    result = median(NULL, 5, NULL);
    stats = assert_equal_int(stats, result, 0, "Return 0 (vec & me == NULL)");

    // ponteiros NULL + length inválido (robustez)
    result = median(NULL, -2, NULL);
    stats = assert_equal_int(stats, result, 0, "Return 0 (NULL + length inválido)");


    print_summary(stats, "median");
    return 0;
}
