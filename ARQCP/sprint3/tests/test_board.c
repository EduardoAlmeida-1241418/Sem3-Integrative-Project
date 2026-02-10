#include <stdio.h>
#include <string.h>

#include "../struct/struct.h"
#include "../board/board.h"

int test_board(void) {

    int ok = 1;

    // Teste 1: Inicialização do Board
    board_init();

    Track tracks[3];

    tracks[0].trackId = 1;
    tracks[0].state   = TRACK_FREE;
    tracks[0].trainId = -1;

    tracks[1].trackId = 2;
    tracks[1].state   = TRACK_BUSY;
    tracks[1].trainId = 101;

    tracks[2].trackId = 3;
    tracks[2].state   = TRACK_INOPERATIVE;
    tracks[2].trainId = -1;

    // Teste 2: Apresentação no Board dos estados das vias
    board_update_tracks(tracks, 3);

    Train trains[2];

    trains[0].trainId = 101;
    trains[0].trackId = 2;

    trains[1].trainId = 102;
    trains[1].trackId = -1;

    // Teste 3: Apresentação no Board do estado dos comboios
    board_update_trains(trains, 2);

    // Teste 4: Apresentação no Board de uma ação (atribuição de via)
    board_update_action("Track 2 assigned to Train 101");

    // Teste 5: Apresentação no Board de uma ação (partida de comboio)
    board_update_action("Departure for Train 101");


    // Teste 6: Apresentação no Board de dados dos sensores
    board_update_sensors(
        22,  /* temperatura - último valor */
        21,  /* temperatura - mediana */
        3,   /* temperatura - janela */
        65,  /* humidade - último valor */
        63,  /* humidade - mediana */
        3    /* humidade - janela */
    );

    // Teste 7: Apresentação no Board de uma situação de emergência
    tracks[0].state = TRACK_BUSY;
    tracks[0].trainId = 102;

    trains[1].trainId = 102;
    trains[1].trackId = 1;

    board_update_tracks(tracks, 3);
    board_update_action("EMERGENCY STOP: no free tracks available");


    printf("Board component tests (USAC15): %s\n", ok ? "OK" : "FAIL");
    return ok;
}
