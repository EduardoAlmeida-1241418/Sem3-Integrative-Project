.section .text
.global cb_is_full

cb_is_full:
    beqz a0, fail
    lw t0, 8(a0)
    lw t1, 4(a0)
    beq t0, t1, full
    li a0, 0
    ret

full:
    li a0, 1
    ret

fail:
    li a0, 0
    ret
