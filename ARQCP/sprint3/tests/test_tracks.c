#include <stdio.h>
#include <string.h>
#include "../manager/manager.h"
#include "../struct/struct.h"

/* Prototype necessário para os testes */
int process_instruction(Instruction inst,
                        Track* tracks, int numTracks,
                        Train* trains, int numTrains,
                        char* dataOut, char* cmdOut);

static int passed = 0;
static int failed = 0;

// Verifica se um valor inteiro é o esperado
static void assert_int(const char* name, int expected, int got) {
    if (expected == got) {
        passed++;
    } else {
        failed++;
        printf("FAIL: %s (expected %d, got %d)\n", name, expected, got);
    }
}

// Repõe o estado inicial das tracks e comboios
static void reset_env(Track* tracks, Train* trains) {
    tracks[0] = (Track){1, TRACK_FREE, -1};     // Track livre
    tracks[1] = (Track){2, TRACK_BUSY, 10};     // Track ocupada
    tracks[2] = (Track){3, TRACK_FREE, -1};     // Track livre

    trains[0] = (Train){10, 2};                 // Comboio na track 2
    trains[1] = (Train){11, -1};                // Comboio sem track
}

int test_tracks(void) {

    printf("Track management tests — USAC16\n");

    Track tracks[3];
    Train trains[2];
    Instruction inst;
    char dataOut[100];
    char cmdOut[50];
    int res;

    // — Colocar tracks como NON-OPERATIONAL

    reset_env(tracks, trains);

    // Track livre passa para NON-OP
    inst = (Instruction){
        .type = INST_SET_NONOP,
        .trackId = 1
    };
    process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("FREE -> NON-OP", TRACK_INOPERATIVE, tracks[0].state);

    // Track ocupada passa para NON-OP e liberta comboio
    inst = (Instruction){
        .type = INST_SET_NONOP,
        .trackId = 2
    };
    process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("BUSY -> NON-OP", TRACK_INOPERATIVE, tracks[1].state);
    assert_int("Comboio libertado", -1, trains[0].trackId);

    // Tentar NON-OP numa track já NON-OP
    inst = (Instruction){
        .type = INST_SET_NONOP,
        .trackId = 2
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("NON-OP repetido bloqueado", 0, res);

    // Tentar NON-OP numa track que não existe
    inst = (Instruction){
        .type = INST_SET_NONOP,
        .trackId = 99
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("Track inexistente", 0, res);

    // — Atribuir comboios com tracks NON-OP

    reset_env(tracks, trains);
    tracks[0].state = TRACK_INOPERATIVE;

    // Não é possível atribuir comboio a track NON-OP
    inst = (Instruction){
        .type = INST_ASSIGN_TRACK,
        .trackId = 1,
        .trainId = 11
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("Assign em NON-OP bloqueado", 0, res);

    // Atribuir comboio a track livre funciona
    inst = (Instruction){
        .type = INST_ASSIGN_TRACK,
        .trackId = 3,
        .trainId = 11
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("Assign em FREE permitido", 1, res);
    assert_int("Track fica BUSY", TRACK_BUSY, tracks[2].state);
    assert_int("Comboio associado", 3, trains[1].trackId);

    // Comboio já atribuído não pode ser reatribuído
    inst = (Instruction){
        .type = INST_ASSIGN_TRACK,
        .trackId = 1,
        .trainId = 11
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("Comboio já atribuído", -1, res);

    // — Libertar tracks

    reset_env(tracks, trains);
    tracks[0].state = TRACK_INOPERATIVE;

    // Não é possível libertar uma track NON-OP
    inst = (Instruction){
        .type = INST_SET_FREE,
        .trackId = 1
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("SET_FREE em NON-OP permitido", 1, res);
    assert_int("Track fica FREE", TRACK_FREE, tracks[0].state);

    // Libertar track ocupada funciona
    inst = (Instruction){
        .type = INST_SET_FREE,
        .trackId = 2
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("BUSY -> FREE", 1, res);
    assert_int("Track fica FREE", TRACK_FREE, tracks[1].state);
    assert_int("Comboio libertado", -1, trains[0].trackId);

    // Libertar track já livre não é permitido
    inst = (Instruction){
        .type = INST_SET_FREE,
        .trackId = 3
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("FREE repetido bloqueado", 0, res);

    // — Garantir que NON-OP não afeta outras tracks

    reset_env(tracks, trains);
    tracks[0].state = TRACK_INOPERATIVE;

    // Outras tracks continuam funcionais
    inst = (Instruction){
        .type = INST_ASSIGN_TRACK,
        .trackId = 2,
        .trainId = 11
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("Outras tracks funcionam", 0, res);
    assert_int("Comboio não atribuído após emergency", -1, trains[1].trackId);

    // NON-OP continua bloqueada
    inst = (Instruction){
        .type = INST_ASSIGN_TRACK,
        .trackId = 1,
        .trainId = 10
    };
    res = process_instruction(inst, tracks, 3, trains, 2, dataOut, cmdOut);
    assert_int("NON-OP continua isolada", 0, res);

    printf("  Passed: %d\n", passed);
    printf("  Failed: %d\n\n", failed);

    return failed == 0;
}
