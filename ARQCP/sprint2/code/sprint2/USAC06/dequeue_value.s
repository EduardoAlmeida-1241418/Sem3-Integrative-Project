# int dequeue_value(int* buffer, int length, int *nelem, int* tail, int* head, int *value)
#   a0 = buffer
#   a1 = length
#   a2 = nelem
#   a3 = tail
#   a4 = head
#   a5 = value

    .EQU MEM_SIZE_INT, 4
    .EQU RETURN_SUCCESS, 1
    .EQU RETURN_ERROR, 0

    .section .text
    .global dequeue_value

dequeue_value:
    lw t0, 0(a2)                # t0 =  *nelem
    beqz t0, ERROR              # if(*nelem == 0) -> ERROR

    li t0, MEM_SIZE_INT
    

atribuir_valor_da_tail:
    lw t1, 0(a3)                # t1 = *tail
    mul t1, t1, t0              # incremento
    add t1, t1, a0              # (ptr + incremento)

    lw t1, 0(t1)                # t1 = *(ptr + incremento)

    sw t1, 0(a5)                # *value = *(ptr + incremento)



DECREMENTAR_NELEM:
    # Decrementar nelem
    lw t1, 0(a2)                # t1 = *(nelem)
    addi t1, t1, -1             # t1 = *(nelem) - 1
    sw t1, 0(a2)                # *(nelem) = *(nelem) - 1

ADVANCE_TAIL:
    # Avançar a tail
    lw t1, 0(a3)                # t1 = *tail
    addi t1, t1, 1              # t1 = *tail + 1
    rem t1, t1, a1              # t1 = (*tail + 1) % lenght

    sw t1, 0(a3)                # *tail = (*tail + 1) % lenght

    j SUCCESS

SUCCESS:
    li a0, RETURN_SUCCESS
    j FIM

ERROR:
    li a0, RETURN_ERROR
    j FIM

FIM:
    ret
