#include <stdio.h>
#include <string.h>
#include "../manager/manager.h"
#include "../struct/struct.h"

/* Função real do projeto */
int load_initial_config(Manager* manager, const char* path);

/* Assembly */
int decrypt_data(char* in, int key, char* out);

static int passed = 0;
static int failed = 0;

// Verifica inteiros
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

int test_setup(void) {

    printf("Initial setup tests — USAC11\n");

    Manager manager;
    memset(&manager, 0, sizeof(Manager));

    int ok = load_initial_config(&manager, "config_test.txt");
    assert_int("Config file loaded", 1, ok);

    /* ===============================
       USERS
       =============================== */

    assert_int("Users loaded", 2, manager.num_users);

    assert_str("Admin username", "admin", manager.users[0].username);
    assert_int("Admin role", ROLE_ADMIN, manager.users[0].role);

    char decrypted[50];
    decrypt_data(manager.users[0].password_enc,
                 manager.users[0].caesar_key,
                 decrypted);
    assert_str("Admin password decrypted", "ADMIN", decrypted);

    /* ===============================
       SENSORS
       =============================== */

    assert_int("Temp buffer size", 10, manager.tempBuffer.length);
    assert_int("Temp window size", 5, manager.tempBuffer.windowSize);

    assert_int("Hum buffer size", 8, manager.humBuffer.length);
    assert_int("Hum window size", 3, manager.humBuffer.windowSize);

    /* ===============================
       TRACKS
       =============================== */

    assert_int("Tracks loaded", 2, manager.num_tracks);
    assert_int("Track 1 ID", 1, manager.tracks[0].trackId);
    assert_int("Track 1 state", TRACK_BUSY, manager.tracks[0].state);

    /* ===============================
       TRAINS
       =============================== */

    assert_int("Trains loaded", 2, manager.num_trains);
    assert_int("Train ID 100", 100, manager.trains[0].trainId);

    /* ===============================
       LOGS
       =============================== */

    assert_int("Logs initialized", 0, manager.num_logs);

    printf("  Passed: %d\n", passed);
    printf("  Failed: %d\n\n", failed);

    return failed == 0;
}