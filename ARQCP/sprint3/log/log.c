#include <stdio.h>
#include <time.h>
#include <string.h>
#include <stdlib.h>
#include "log.h"
// log.c precisa enxergar a struct Instruction
#include "../manager/manager.h"


#define LOG_FILE "logs.txt"                  // Define a constante com o Nome do ficheiro de logs.

int found = 0;                              // Para verificar se o user existe no ficheiro

int nextLogId = 0;                          // Variavel globlal para o proximo ID dos logs.

/* Para obter o tempo atual em C usamos 3 funções para isso:
     time() -> tempo atual em segudnos desde 1970.
     localtime() -> Converte esse número para (ano, mês, dia, hora, minuto, segundo).
     strftime() -> formata como string no formato "YYYY-MM-DD HH:MM:SS"
*/

void log_get_timestamp(char* timeStamp, int size) {
    time_t now = time(NULL);

    // Converte o total de segundos em valores detalhados acima e devolve um ponteiro para essa struct preenchida.”
    struct tm* t = localtime(&now);

    //strftime(destino, tamanho_maximo, formato, struct_tm)
    strftime(timeStamp, size, "%Y-%m-%d %H:%M:%S", t);
}

/*
   Converte uma Instruction numa descrição textual para ser registada no log.
   Usa inst->type para identificar o tipo de instrução (enum definido no Manager).
   Usa sprintf para imprimir formatado dentro de uma string.
   Forma geral: sprintf(buffer, "texto %d %s", numero, string);
   Obs.: O uso da "const" serve para proteger a Instruction de alterações acidentais.
        ex.:  Se um programador tentar mexer nela:  inst->trackId = 10; o compilador não deixa.
*/
void instruction_to_string(const Instruction* inst, char* actionString) {

    switch (inst->type) {

        case INST_ASSIGN_TRACK:
            sprintf(actionString,
                    "ASSIGN_TRACK track=%d train=%d",
                    inst->trackId, inst->trainId);
            break;

        case INST_SET_FREE:
            sprintf(actionString,
                    "SET_FREE track=%d",
                    inst->trackId);
            break;

        case INST_SET_NONOP:
            sprintf(actionString,
                    "SET_NONOP track=%d",
                    inst->trackId);
            break;

        case INST_DEPARTURE:
            sprintf(actionString,
                    "DEPARTURE train=%d",
                    inst->trainId);
            break;

        case INST_EXIT:
            sprintf(actionString, "EXIT");
            break;

        default:
            sprintf(actionString, "UNKNOWN_INSTRUCTION");
            break;
    }
}

/*
   Função chamada apenas uma vez no arranque do sistema para buscar o próximo ID disponível.
   Lê o ficheiro logs.txt, percorre todas as linhas para encontrar o maior ID já usado.
   Define nextLogId (variável global) como (maior ID + 1) para ser usado no próximo log.
*/
void log_initialize() {

    FILE* f = fopen(LOG_FILE, "r");                    // Abre o ficheiro de logs em modo leitura (read).
    if (f == NULL) {
        nextLogId = 1;
        return;
    }

    char line[300];                                     // buffer onde cada linha lida do ficheiro será armazenada.
    int maxId = 0;

    while (fgets(line, sizeof(line), f) != NULL) {      // fgets pega uma linha por vez ate o fim do ficheiro (NULL).
        char* token = strtok(line, ";");                // token sao pedaços da linha separados por ";", o primeiro token já é o ID.
        if (token != NULL) {
            int id = atoi(token);                       // converte o token (string) para um inteiro. Obs.: atoi(ACII to Integer)
            if (id > maxId) maxId = id;
        }
    }

    fclose(f);

    nextLogId = maxId + 1;
}



/*
  Registrar uma action do user ativo no ficheiro logs.txt, contendo:
  logId, username, descrição da ação (action) e timestamp
  Exemplo de linha gerada: 1;marcelo;ASSIGN_TRACK track=2 train=5510;2025-01-18 19:10:22
*/

void log_record(const char* userName, const char* action) {

    // 1. Criar timestamp
    char timestamp[30];
    log_get_timestamp(timestamp, 30);

    // 2. Abrir arquivo em modo append "a" (adicionar no final sem apagar o que já existe).
    FILE* f = fopen(LOG_FILE, "a");
    if (f == NULL) {
        printf("Erro ao abrir o ficheiro de logs.\n");
        return;
    }

    // 3. Escrever a linha no formato desejado
    fprintf(f, "%d;%s;%s;%s\n", nextLogId, userName, action, timestamp);

    // 4. Fechar ficheiro
    fclose(f);

    // 5. Incrementar o ID para o próximo log
    nextLogId++;
}

/*
  1) Abrir o ficheiro logs.txt (onde todos os logs foram gravados).
  2) Ler todas as linhas existentes.
  3) Selecionar apenas as linhas cujo username é igual ao informado pelo usuário na UI.
  4) Escrever essas linhas num novo ficheiro, por ex.: report_marcelo.txt
  5) Fechar ambos os ficheiros.
*/
void log_generate_report(const char* userName) {
    FILE* logs = fopen(LOG_FILE, "r");                                              // Abrir o ficheiro logs.txt para leitura.
    if (logs == NULL) {
        printf("Erro ao abrir logs.txt para leitura.\n");
        return;
    }

    char reportName[100];
    sprintf(reportName, "report_%s.txt", userName);                                  // Criar nome do ficheiro de relatório: report_username.txt

    FILE* reportFile = fopen(reportName, "w");                                       // Cria e abre o ficheiro do relatório a partir do zero "w".
    if (reportFile == NULL) {
        printf("Erro ao criar o relatório.\n");
        fclose(logs);
        return;
    }

    int found = 0;                                                                  // indica se o utilizador existe no ficheiro.

    char line[300];
    while (fgets(line, sizeof(line), logs) != NULL) {                                // Ler linha a linha do logs.txt

        char* id        = strtok(line, ";");                                         // O strtok extrai cada campo da linha (ID; username; action; timestamp).
        char* username  = strtok(NULL, ";");
        char* action    = strtok(NULL, ";");
        char* timestamp = strtok(NULL, ";");

        if (username != NULL && strcasecmp(username, userName) == 0) {               // strcmp: case-insensitive (strcasecmp)
            found = 1;                                                               // marca que o utilizador existe.
            fprintf(reportFile, "%s;%s;%s;%s", id, username, action, timestamp);     // fprintf(ponteiro_para_ficheiro, "formato", valores);
        }
    }

    fclose(logs);                                                                   // Fechar todos os ficheiros.

    if (!found) {                                                                   // evitar criar ficheiro vazio caso user não exista
        printf("User '%s' não encontrado. Relatório não gerado.\n", userName);
        fclose(reportFile);                                                         // fechar ficheiro antes de apagar
        remove(reportName);                                                         // apaga o ficheiro vazio criado.
        return;
    }

    fclose(reportFile);

    printf("Relatório gerado com sucesso: %s\n", reportName);
}

