#include <stdio.h>
#include "../asm/enqueue_value.h"
#include "../asm/move_n_to_array.h"
#include "../asm/median.h"

int test_sensors(void) {
    int ok = 1;

    int buffer[5] = {0};
    int nelem = 0;
    int head = 0;
    int tail = 0;

    // Teste 1: Inserção sequencial de valores no buffer circular
    enqueue_value(buffer, 5, &nelem, &tail, &head, 10);
    enqueue_value(buffer, 5, &nelem, &tail, &head, 20);
    enqueue_value(buffer, 5, &nelem, &tail, &head, 30);
    enqueue_value(buffer, 5, &nelem, &tail, &head, 40);
    enqueue_value(buffer, 5, &nelem, &tail, &head, 50);

    ok &= nelem == 5;

    // Teste 2: Inserção com buffer cheio (overwrite do valor mais antigo)
    enqueue_value(buffer, 5, &nelem, &tail, &head, 60);
    ok &= nelem == 5;

    int values[5];

    // Teste 3: Extração da janela completa de valores do buffer
    move_n_to_array(buffer, 5, &nelem, &tail, &head, 5, values);

    // Teste 4: Cálculo da mediana com janela ímpar
    int med = 0;
    median(values, 5, &med);
    ok &= med == 40;

    // Teste 5: Extração de uma janela parcial (menor que o tamanho do buffer)
    move_n_to_array(buffer, 5, &nelem, &tail, &head, 3, values);

    // Teste 6: Cálculo da mediana numa janela parcial
    median(values, 3, &med);
    ok &= med == values[1];

    // Teste 7: Tentativa de extração com N maior do que os elementos disponíveis
    ok &= move_n_to_array(buffer, 5, &nelem, &tail, &head, 10, values) == 0;

    // Teste 8: Cálculo da mediana com tamanho inválido (zero)
    ok &= median(values, 0, &med) == 0;

    printf("Sensor processing tests (USAC13): %s\n", ok ? "OK" : "FAIL");
    return ok;
}
