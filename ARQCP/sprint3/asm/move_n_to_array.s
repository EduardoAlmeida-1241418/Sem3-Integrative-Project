# --------------------------
# int move_n_to_array(int* buffer, int length, int *nelem, int *tail, int *head, int n, int* array)
# parameters:
#   a0 = buffer
#   a1 = length    (inteiro)
#   a2 = nelem
#   a3 = tail
#   a4 = head
#   a5 = n        (inteiro)
#   a6 = array
# returns:
#   a0 = 1 (sucesso), 0 (falha)
# --------------------------

.section .text
.global move_n_to_array

move_n_to_array:

    # verificar ponteiros nulos
    beqz a0, fail                 # if (buffer == NULL) return 0
    blez a1, fail                 # if (length <= 0) return 0.
    beqz a2, fail                 # if (nelem == NULL) return 0
    beqz a3, fail                 # if (tail == NULL) return 0
    beqz a4, fail                 # if (head == NULL) return
    blez a5, fail                 # if (n < 0) return 0
    beqz a6, fail                 # if (array == NULL) return 0

    lw t0, 0(a2)                 # t0 = *nelem, carrega o valor de nelem para t0.
    blt  t0, a5, fail            # if (nelem < n), salta para fail

    li  t1, 0                    # t1 = i = 0 (contador do loop)


# loop para percorrer n vezes o buffer e copiar para o array
loop:
    beq  t1, a5, done            # se i == n, sai do loop (termina).

    lw t2, 0(a3)                 # t2 = *tail (carrega o valor (indice) tail para t2).
    slli  t3, t2, 2              # t3 = tail * 4 (cada indice possui 4 bytes)
    add  t4, a0, t3              # t4 = endereço base + offset (aponta onde está o valor a ser copiado)
    lw  t5, 0(t4)                # t5 = carrega o valor a ser copiado do buffer.
    sw  t5, 0(a6)                # salvar o valor de t5 na primeira posicao do array.

    addi  a6, a6, 4              # array++
    addi  t2, t2, 1              # tail++
    rem  t2, t2, a1              # tail = tail % length (circular buffer)
    sw t2, 0(a3)                 # grava *tail atualizado

    addi t1, t1, 1               # i++
    j    loop                    # repetir o loop

# atualizar *nelem
done:
   sub  t0, t0, a5              # *nelem -= n
   sw  t0, 0(a2)                # grava o novo valor em *nelem

   li  a0, 1                     # return 1 (sucesso)
   ret

fail:
    li  a0, 0                 # return 0 (falha)
    ret
    