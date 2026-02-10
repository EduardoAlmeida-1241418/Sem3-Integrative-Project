#include <stdio.h>
#include <string.h>

void log_initialize(void) {
    FILE *f = fopen("logs.txt", "w");
    if (f) fclose(f);
}

void log_record(const char *user, const char *action) {
    FILE *f = fopen("logs.txt", "a");
    if (!f) return;
    fprintf(f, "%s: %s\n", user, action);
    fclose(f);
}

void log_generate_report(const char *user) {
    FILE *fin = fopen("logs.txt", "r");
    if (!fin) return;

    char line[256];
    int found = 0;

    /* Primeiro passe: verificar se existe alguma entrada */
    while (fgets(line, sizeof(line), fin)) {
        if (strstr(line, user)) {
            found = 1;
            break;
        }
    }

    if (!found) {
        fclose(fin);
        return;   /* NÃO cria report */
    }

    rewind(fin);

    char filename[64];
    snprintf(filename, sizeof(filename), "report_%s.txt", user);

    FILE *fout = fopen(filename, "w");
    if (!fout) {
        fclose(fin);
        return;
    }

    while (fgets(line, sizeof(line), fin)) {
        if (strstr(line, user)) {
            fputs(line, fout);
        }
    }

    fclose(fin);
    fclose(fout);
}
