    .section .text
    .globl sort_array

# int sort_array(int* vec, int length, char order)
# a0 = vec (array address)
# a1 = length
# a2 = order (1 = ascending, 0 = descending)
# returns 1 on success, 0 on error

sort_array:
    blez a1, invalid        # invalid if length <= 0

    bltz a2, invalid        # invalid if order < 0

    li t0, 1
    bgt a2, t0, invalid      # invalid if order > 1

    addi t0, zero, 0        # i = 0 (outer counter)

outer_loop:
    mv t1, a1               # copy array length
    addi t1, t1, -1         # t1 = length - 1
    sub t1, t1, t0          # t1 = length - 1 - i
    blez t1, done           # stop if array already sorted

    addi t2, zero, 0        # j = 0 (inner counter)

inner_loop:
    addi t3, t1, -1         # t3 = last index for this pass
    bgt t2, t3, next_pass   # finished inner loop for this i

    slli t4, t2, 2          # offset = j * 4 (word size)
    add t4, a0, t4          # t4 = &arr[j]
    lw t5, 0(t4)            # load arr[j]
    lw t6, 4(t4)            # load arr[j+1]

    beqz a2, desc_check     # if order == 0 then descending mode

    ble t5, t6, no_swap     # ascending: skip if in correct order
    j do_swap

desc_check:
    bge t5, t6, no_swap     # descending: skip if in correct order

do_swap:
    sw t6, 0(t4)            # arr[j] = arr[j+1]
    sw t5, 4(t4)            # arr[j+1] = arr[j]

no_swap:
    addi t2, t2, 1          # move to next pair
    j inner_loop            # repeat inner loop

next_pass:
    addi t0, t0, 1          # next outer pass
    j outer_loop            # repeat sorting pass

done:
    li a0, 1                # return success
    ret

invalid:
    li a0, 0                # return failure
    ret
