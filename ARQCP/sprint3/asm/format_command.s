.section .text
.globl format_command

# int format_command(char *op, int n, char *cmd)
# a0 = op, a1 = n, a2 = cmd

format_command:
    sb zero, 0(a2)                      # initialise output string to empty

trim_left:
    lb t0, 0(a0)                        # load first char of input
    beqz t0, fail                       # if input is empty, fail
    li t1, ' '                          # load space character
    bne t0, t1, process_command         # if not space, start processing
    addi a0, a0, 1                      # advance input pointer
    j trim_left                         # repeat trimming

process_command:
    mv t0, a0                           # t0 = input pointer
    mv t1, a2                           # t1 = output pointer

to_upper_loop:
    lb t3, 0(t0)                        # load current char
    beqz t3, check_commands             # if end of string, check command type
    li t4, ' '                          # load space character
    beq t3, t4, skip_space              # if space, skip it
    li t4, 'a'                          # load 'a' for lowercase check
    blt t3, t4, store_char              # if char < 'a', store directly
    li t4, 'z'                          # load 'z' for lowercase check
    bgt t3, t4, store_char              # if char > 'z', store directly
    addi t3, t3, -32                    # convert lowercase to uppercase

store_char:
    sb t3, 0(t1)                        # store char in output string
    addi t1, t1, 1                      # advance output pointer
    addi t0, t0, 1                      # advance input pointer
    j to_upper_loop                     # repeat loop

skip_space:
    addi t0, t0, 1                      # skip space in input
    j to_upper_loop                     # continue loop

check_commands:
    sb zero, 0(t1)                      # null terminate output string
    lb t0, 0(a2)                        # load first char of command
    li t1, 'G'                          # check if first char is 'G'
    bne t0, t1, check_two_char          # if not 'G', check two-char commands
    lb t1, 1(a2)                        # load second char
    li t2, 'T'                          # expected 'T'
    bne t1, t2, check_two_char          # if not 'T', check two-char commands
    lb t1, 2(a2)                        # load third char
    li t2, 'H'                          # expected 'H'
    bne t1, t2, check_two_char          # if not 'H', check two-char commands
    lb t1, 3(a2)                        # load fourth char
    bnez t1, fail                       # must have exactly 3 chars
    j success                           # GTH is valid without number

check_two_char:
    lb t1, 0(a2)                        # load first char
    li t2, 'R'                          # check for 'R'
    bne t1, t2, check_ye_ge             # if not 'R', check YE/GE
    lb t1, 1(a2)                        # load second char
    li t2, 'E'                          # expected 'E'
    beq t1, t2, valid_two_char          # RE is valid
    li t2, 'B'                          # check for 'B'
    beq t1, t2, valid_two_char          # RB is valid
    j fail                              # otherwise fail

check_ye_ge:
    lb t1, 0(a2)                        # load first char
    li t2, 'Y'                          # check for 'Y'
    bne t1, t2, check_ge                # if not 'Y', check GE
    lb t1, 1(a2)                        # load second char
    li t2, 'E'                          # expected 'E'
    beq t1, t2, valid_two_char          # YE is valid
    j fail                              # otherwise fail

check_ge:
    lb t1, 0(a2)                        # load first char
    li t2, 'G'                          # check for 'G'
    bne t1, t2, fail                    # if not 'G', fail
    lb t1, 1(a2)                        # load second char
    li t2, 'E'                          # expected 'E'
    beq t1, t2, valid_two_char          # GE is valid
    j fail                              # otherwise fail

valid_two_char:
    lb t0, 2(a2)                        # load third char
    bnez t0, fail                       # must have exactly 2 chars
    bltz a1, fail                       # number must be >= 0
    li t0, 99                           # maximum allowed number
    bgt a1, t0, fail                    # fail if number > 99
    mv t0, a2                           # pointer to end of string

find_end:
    lb t1, 0(t0)                        # load current char
    beqz t1, add_comma                  # if end, add comma
    addi t0, t0, 1                      # advance pointer
    j find_end                          # repeat

add_comma:
    li t1, ','                          # load comma character
    sb t1, 0(t0)                        # store comma
    addi t0, t0, 1                      # advance pointer
    li t2, 10                           # load 10 for decimal
    div t1, a1, t2                      # calculate tens
    rem t3, a1, t2                      # calculate units
    addi t1, t1, '0'                    # convert tens to ASCII
    addi t3, t3, '0'                    # convert units to ASCII
    sb t1, 0(t0)                        # store tens
    addi t0, t0, 1                      # advance pointer
    sb t3, 0(t0)                        # store units
    addi t0, t0, 1                      # advance pointer
    sb zero, 0(t0)                      # null terminate string

success:
    li a0, 1                            # return success
    ret

fail:
    sb zero, 0(a2)                      # ensure output string empty
    li a0, 0                            # return failure
    ret
