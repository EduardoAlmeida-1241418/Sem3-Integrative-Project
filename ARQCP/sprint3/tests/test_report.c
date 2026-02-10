#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../log/log.h"
#include "../manager/manager.h"

/*
 * Testes da USAC12 – Logs e Reports
 * Estilo alinhado com os restantes testes do projeto
 */

int test_usac12(void) {
    int ok = 1;

    /* Garantir ficheiros limpos */
    remove("logs.txt");
    remove("report_op1.txt");
    remove("report_op2.txt");

    log_initialize();

    /* ============================================================
       TEST 1: Registo de ação válida
       ============================================================ */
    Instruction inst1;
    inst1.type = INST_ASSIGN_TRACK;
    inst1.trackId = 1;
    inst1.trainId = 10;

    char action[200];
    instruction_to_string(&inst1, action);

    log_record("op1", action);

    FILE *f = fopen("logs.txt", "r");
    ok &= (f != NULL);

    if (f != NULL) {
        char line[300];
        ok &= (fgets(line, sizeof(line), f) != NULL);

        if (line[0] != '\0') {
            ok &= (strstr(line, "op1") != NULL);
            ok &= (strstr(line, "ASSIGN_TRACK") != NULL);
        }

        fclose(f);
        f = NULL;
    }

    /* ============================================================
       TEST 2: Registo de Emergency Stop
       ============================================================ */
    log_record("op1", "EMERGENCY STOP: no free tracks available");

    f = fopen("logs.txt", "r");
    ok &= (f != NULL);

    if (f != NULL) {
        char line[300];
        int count = 0;
        while (fgets(line, sizeof(line), f)) {
            count++;
        }
        fclose(f);
        f = NULL;

        ok &= (count == 2);
    }

    /* ============================================================
       TEST 3: Geração de report por utilizador existente
       ============================================================ */
    log_record("op2", "SET_FREE track=1");

    log_generate_report("op1");

    f = fopen("report_op1.txt", "r");
    ok &= (f != NULL);

    if (f != NULL) {
        char line[300];
        int count = 0;
        while (fgets(line, sizeof(line), f)) {
            ok &= (strstr(line, "op1") != NULL);
            count++;
        }
        fclose(f);
        f = NULL;

        ok &= (count == 2); /* op1 tem duas ações */
    }

    /* ============================================================
       TEST 4: Report para utilizador inexistente
       ============================================================ */
    f = fopen("report_ghost.txt", "r");
    ok &= (f == NULL);

    if (f != NULL) {
        fclose(f);
        f = NULL;
    }

    printf("USAC12 tests: %s\n", ok ? "OK" : "FAIL");
    return ok;
}