#include <stdio.h>
#include <string.h>
#include "../../code/USAC03/extract_data.h"
#include "../../tests/test_base.h"

int main(void) {
    TestStats stats = init_stats();
    char str[256];
    char token[64];
    char unit[64];
    int value;
    int result;

    printf("=== Running tests for extract_data ===\n\n");

    // TESTS THAT PASS

    // Success: TEMP exists in string str with unit celsius and value 20
    strcpy(str, "TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 - TEMP token found");
    stats = assert_equal_str(stats, unit, "celsius", "Correct unit TEMP");
    stats = assert_equal_int(stats, value, 20, "Correct value TEMP");

    // Success: HUM exists in the string str with percentage unit and value 80
    strcpy(token, "HUM");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 - HUM token found");
    stats = assert_equal_str(stats, unit, "percentage", "Correct unit HUM");
    stats = assert_equal_int(stats, value, 80, "Correct value HUM");

    // Success: change the order: HUM...#TEMP - TEMP exists in the string str with the unit in uppercase (C) and value 15
    strcpy(str, "HUM&unit:percent&value:60#TEMP&unit:C&value:15");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 - TEMP at end");
    stats = assert_equal_str(stats, unit, "C", "Unit TEMP found at end");
    stats = assert_equal_int(stats, value, 15, "Value TEMP found at end");

    // Success: value with left zeros
    strcpy(str, "TEMP&unit:celsius&value:0020");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 - value with leading zeros");
    stats = assert_equal_str(stats, unit, "celsius", "Unit ok");
    stats = assert_equal_int(stats, value, 20, "Value 0020 parsed");

    // Success: #JUNK in the middle of the string
    strcpy(str, "TEMP&unit:celsius&value:20#JUNK#HUM&unit:percent&value:55");
    strcpy(token, "HUM");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 1, "Return should be 1 - Token HUM after junk");
    stats = assert_equal_str(stats, unit, "percent", "Unit ok");
    stats = assert_equal_int(stats, value, 55, "Value ok");

    // FAILED TESTS

    // Fail: Token ‘AAA’ does not exist in string str
    strcpy(str, "TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80");
    strcpy(token, "AAA");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Token not found");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: &unit: does not exist in string str
    strcpy(str, "TEMP&value:20");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Missing &unit:");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: &value: does not exist in string str
    strcpy(str, "TEMP&unit:celsius");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Missing &value:");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: &unit: present but no content
    strcpy(str, "TEMP&unit:&value:20");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Empty unit");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: &value: present but no number
    strcpy(str, "TEMP&unit:celsius&value:");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Empty value");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: &value: with non-numeric value
    strcpy(str, "TEMP&unit:celsius&value:abc");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Non-numeric value");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: empty string str
    strcpy(str, "");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Empty str");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: empty token
    strcpy(str, "TEMP&unit:celsius&value:20");
    strcpy(token, "");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Empty token");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: token with only spaces
    strcpy(token, "   ");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Token only spaces");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    // Fail: unit ends with a #
    strcpy(str, "TEMP&unit:celsius#&value:20");
    strcpy(token, "TEMP");
    result = extract_data(str, token, unit, &value);
    stats = assert_equal_int(stats, result, 0, "Return should be 0 - Unit truncated by #");
    stats = assert_equal_str(stats, unit, "", "Unit empty");
    stats = assert_equal_int(stats, value, 0, "Value zero");

    print_summary(stats, "extract_data");
    return 0;
}
