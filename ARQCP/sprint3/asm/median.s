# ----------------------------------------------------
# int median(int* vec, int length, int *me)
# Parameters:
#   a0 = *vec
#   a1 = length    (inteiro)
#   a2 = *me
# Returns:
#   a0 = 1 on success or 0 on failure
#
#  Obs: Usa a função sort_array já implementada na USEI08:
#  int sort_array(int* vec, int length, char order);
# ----------------------------------------------------

.section .text
.global median

median:
    addi sp, sp, -16              # criar espaço na pilha
    sw   ra, 12(sp)               # salvar ra na pilha
    sw   s0, 8(sp)                # salvar s0 na pilha
    sw   s1, 4(sp)                # salvar s1 na pilha

    # --- Verificar ponteiros nulos ---
    beqz a0, invalid              # se vec == NULL, retorna 0
    beqz a2, invalid              # se me == NULL, retorna 0

    # --- Salvar ponteiros ---
    mv   s0, a2                   # s0 = me
    mv   s1, a0                   # s1 = vec

    # --- Validar length ---
    blez a1, invalid               # if (length <= 0) fail

    # --- sort_array(vec, length, 1)
    mv   a0, s1                   # a0 = vec

    # Ordenar a array
    li   a2, 1                    # salva em a2 a ordem ascendente (1)
    call  sort_array              # chama sort_array, a1 já contém length
    beqz a0, invalid              # se sort falhar, retorna 0

    # --- Calcular median ---
    andi t0, a1, 1                # t0 = length % 2  (0=par, 1=ímpar)
    srai t1, a1, 1                # t1 = length / 2
    slli t2, t1, 2                # t2 = (length / 2) * 4 (distancia em bytes)
    bne  t0, zero, odd_len        # se ímpar → jump para odd_len

    # --- length par ---
    addi t3, t1, -1               # t3 = (len/2) - 1
    slli t3, t3, 2                # t3 = ((len/2)-1) * 4 (distancia em bytes)
    add  t4, s1, t3               # t4 = &vec[(len/2)-1]
    add  t5, s1, t2               # t5 = &vec[len/2]
    lw   t3, 0(t4)                # left value
    lw   t4, 0(t5)                # right value
    add  t6, t3, t4               # soma left e right
    srai t6, t6, 1                # média inteira
    sw   t6, 0(s0)                # armazenar média em s0 (*me)
    li   a0, 1                    # retorno de sucesso, a0 ==  1.
    j    done                     # salta para done.

odd_len:
    # --- length ímpar ---
    add  t3, s1, t2               # &vec[len/2], endereço base * offset
    lw   t4, 0(t3)                # valor do elemento central
    sw   t4, 0(s0)                # armazenar em *me
    li   a0, 1                    # retorno de sucesso, a0 ==  1.
    j    done                     # salta para done.

invalid:
    li   a0, 0                    # retorno de falha

done:
    lw   ra, 12(sp)               # restaurar ra
    lw   s0, 8(sp)                # restaurar s0
    lw   s1, 4(sp)                # restaurar s1
    addi sp, sp, 16               # liberar espaço na pilha
    ret
