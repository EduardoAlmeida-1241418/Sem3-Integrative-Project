.section .text
.globl extract_data

# int extract_data(char* str, char* token, char* unit, int* value)
# a0: str, a1: token, a2: unit, a3: value

extract_data:
    sb zero, 0(a2)                      # initialise unit string to empty
    sw zero, 0(a3)                      # initialise value to 0
    lb t0, 0(a0)                        # load first char of input string
    beqz t0, fail                       # if the string is empty, fail
    lb t0, 0(a1)                        # load first char of token
    beqz t0, fail                       # if the token is empty, fail
    mv t0, a0                           # t0 points to start of string

search_token:
    lb t1, 0(t0)                        # load current char from string
    beqz t1, fail                       # if is the end of the string, fail
    beq t0, a0, check_token_match       # if at start, check token
    addi t2, t0, -1                     # calculate previous char address
    lb t2, 0(t2)                        # load previous char
    li t3, '#'                          # load '#' character
    bne t2, t3, token_next_char         # if the previous char is not '#', continue

check_token_match:
    mv t2, t0                           # t2 = string pointer for comparison
    mv t3, a1                           # t3 = token pointer

compare_token:
    lb t4, 0(t3)                        # load char from token
    beqz t4, check_token_end            # if end of token, check delimiter
    lb t5, 0(t2)                        # load char from string
    bne t4, t5, token_next_char         # If the characters are different, move on to the next character
    addi t2, t2, 1                      # advance string pointer
    addi t3, t3, 1                      # advance token pointer
    j compare_token                     # repeat comparison

check_token_end:
    lb t4, 0(t2)                        # load char after token
    li t5, '&'                          # load '&' character
    beq t4, t5, token_found             # if '&', token found
    j token_next_char                   # otherwise continue search

token_next_char:
    addi t0, t0, 1                      # advance string pointer
    j search_token                      # continue search

token_found:
    mv t0, t2                           # t0 points after token

search_unit_marker:
    lb t1, 0(t0)                        # load current char
    beqz t1, fail                       # if end of string, fail
    li t2, '#'                          # load '#' character
    beq t1, t2, fail                    # if '#' encountered, fail
    la t3, unit_marker                  # load address of unit marker
    mv t4, t0                           # t4 = string pointer
    mv t5, t3                           # t5 = unit marker pointer

compare_unit_marker:
    lb t6, 0(t5)                        # load char from unit marker
    beqz t6, unit_marker_found          # if end, unit marker found
    lb a7, 0(t4)                        # load char from string
    bne t6, a7, unit_marker_next        # if chars differ, continue search
    addi t4, t4, 1                      # advance string pointer
    addi t5, t5, 1                      # advance marker pointer
    j compare_unit_marker               # repeat comparison

unit_marker_next:
    addi t0, t0, 1                      # advance string pointer
    j search_unit_marker                # continue search

unit_marker_found:
    mv t0, t4                           # t0 points to start of unit
    mv t1, a2                           # t1 = pointer to output unit
    li t3, 0                            # count of copied chars

copy_unit:
    lb t2, 0(t0)                        # load char from string
    beqz t2, fail                       # if end, fail
    li t4, '&'                          # load '&' character
    beq t2, t4, unit_copied             # if '&', stop copying
    li t4, '#'                          # load '#' character
    beq t2, t4, unit_copied             # if '#', stop copying
    sb t2, 0(t1)                        # store char in unit
    addi t0, t0, 1                      # advance string pointer
    addi t1, t1, 1                      # advance unit pointer
    addi t3, t3, 1                      # increment copied count
    j copy_unit                         # repeat copy

unit_copied:
    sb zero, 0(t1)                      # terminate unit string
    beqz t3, fail                       # if no char copied, fail

search_value_marker:
    lb t1, 0(t0)                        # load current char
    beqz t1, fail                       # if end, fail
    li t2, '#'                          # load '#' character
    beq t1, t2, fail                    # if '#', fail
    la t3, value_marker                 # load address of value marker
    mv t4, t0                           # t4 = string pointer
    mv t5, t3                           # t5 = value marker pointer

compare_value_marker:
    lb t6, 0(t5)                        # load char from value marker
    beqz t6, value_marker_found         # if end, marker found
    lb a7, 0(t4)                        # load char from string
    bne t6, a7, value_marker_next       # if chars differ, continue search
    addi t4, t4, 1                      # advance string pointer
    addi t5, t5, 1                      # advance marker pointer
    j compare_value_marker              # repeat comparison

value_marker_next:
    addi t0, t0, 1                      # advance string pointer
    j search_value_marker               # continue search

value_marker_found:
    mv t0, t4                           # t0 points to start of value
    li t1, 0                            # value accumulator
    li t2, 0                            # digit counter

parse_value:
    lb t3, 0(t0)                        # load char from string
    beqz t3, value_parsed               # if end, value parsed
    li t4, '#'                          # load '#' character
    beq t3, t4, value_parsed
    li t4, '&'                          # load '&' character
    beq t3, t4, value_parsed
    li t4, '0'                          # load '0' character
    blt t3, t4, value_parsed            # if char < '0', end
    li t4, '9'                          # load '9' character
    bgt t3, t4, value_parsed            # if char > '9', end
    addi t3, t3, -'0'                   # convert char to digit
    li t4, 10                           # load base 10
    mul t1, t1, t4                      # multiply accumulator by 10
    add t1, t1, t3                      # add new digit
    addi t2, t2, 1                      # increment digit count
    addi t0, t0, 1                      # advance string pointer
    j parse_value                       # repeat parsing

value_parsed:
    beqz t2, fail                       # if no digit, fail
    sw t1, 0(a3)                        # store value
    li a0, 1                            # return success
    ret

fail:
    sb zero, 0(a2)                      # reset unit
    sw zero, 0(a3)                      # reset value
    li a0, 0                            # return failure
    ret

unit_marker: .asciz "unit:"             # unit marker string
value_marker: .asciz "value:"           # value marker string
