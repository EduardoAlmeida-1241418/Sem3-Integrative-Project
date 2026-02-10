/**
 * Module responsible for registering all user actions in the system.
 * Defines the LogRecord structure and the functions to write log entries
 * to the logs.txt file and to generate reports (USAC12).
 * Used by the Manager to record actions and by the Admin to filter logs.
 */

#ifndef LOG_H
#define LOG_H

#include "../manager/manager.h"

// Deve chamada apenas uma vez no arranque do sistema para buscar o próximo ID disponível.
void log_initialize();

// Obtém timestamp formatado
void log_get_timestamp(char* timeStamp, int size);

// Transforma a instrucão (action) do usuario dentro da UI em uma string
void instruction_to_string(const Instruction* inst, char* actionString);

// Regista uma ação no ficheiro logs.txt
void log_record(const char* username, const char* action);

// USAC12
// Recebe o nome do utilizador que queremos filtrar.
// Nao retorna nada, apenas gera um ficheiro contendo as actions de um user em especifico.
void log_generate_report(const char* userName);

#endif
