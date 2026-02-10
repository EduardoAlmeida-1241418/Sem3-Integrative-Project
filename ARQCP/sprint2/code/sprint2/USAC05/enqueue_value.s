# int enqueue_value(int* buffer, int length, int *nelem, int* tail, int* head, int value)
#   a0 = buffer
#   a1 = length
#   a2 = nelem
#   a3 = tail
#   a4 = head
#   a5 = value

    .EQU MEM_SIZE_INT, 4
    .EQU RETURN_FULL_BUFFER, 1
    .EQU RETURN_NOT_FULL_BUFFER, 0

    .section .text
    .globl enqueue_value

enqueue_value:

    # t6 = sizeof(int)
    li t6, MEM_SIZE_INT                 

#
# ADD VALUE AT HEAD POSITION
#
ADD_VAL_TO_HEAD:
    lw t0, 0(a4)                         # t0 = *head
    mul t1, t0, t6                       # t1 = *head * 4
    add t1, t1, a0                       # t1 = buffer + offset
    sw a5, 0(t1)                         # *(buffer + offset) = value

#
# ADVANCE HEAD
#
ADVANCE_HEAD:
    addi t0, t0, 1                       # t0 = *head + 1
    rem t0, t0, a1                       # t0 %= length
    sw t0, 0(a4)                         # *head = new head

#
# CHECK BUFFER SIZE
#
VERIFY_BUFFER_SIZE:
    lw t0, 0(a2)                         # t0 = *nelem
    beq t0, a1, ADVANCE_TAIL             # if(*nelem == length) -> full -> advance tail
    j INCREASE_NELEM                     # else increase nelem

#
# ADVANCE TAIL IF FULL (overwrite mode)
#
ADVANCE_TAIL:
    lw t1, 0(a3)                         # t1 = *tail
    addi t1, t1, 1                       # t1 = *tail + 1
    rem t1, t1, a1                       # t1 %= length
    sw t1, 0(a3)                         # *tail = new tail
    j VERIFY_SPACE

#
# INCREASE NELEM IF NOT FULL
#
INCREASE_NELEM:
    lw t0, 0(a2)                         # t0 = *nelem
    addi t0, t0, 1                       # *nelem + 1
    sw t0, 0(a2)                         # store *nelem
    j VERIFY_SPACE

#
# RETURN VALUES
#
VERIFY_SPACE:
    lw t0, 0(a2)                         # reload nelem
    beq t0, a1, BUFFER_IS_FULL
    j BUFFER_IS_NOT_FULL

BUFFER_IS_FULL:
    li a0, RETURN_FULL_BUFFER
    j END

BUFFER_IS_NOT_FULL:
    li a0, RETURN_NOT_FULL_BUFFER
    j END

END:
    ret
