    .section .text
    .global encrypt_data

# int encrypt_data(char *in, int key, char *out)
# a0 = input string
# a1 = key
# a2 = output string
# returns: 1 - success, 0 - failure

encrypt_data:
    mv t3, a2                  # save start out address

    li   t0, 1
    blt a1, t0,  invalid       # key < 1 -> invalid

    li   t0, 26
    bgt a1, t0, invalid        # key > 26 -> invalid

loop:
    lbu  t0, 0(a0)             # t0 = *in
    beqz t0, done              # *in == 0, end string -> done

    li   t1, 'A'
    blt t0, t1, invalid        # *in < 'A' -> invalid
    li   t2, 'Z'
    bgt t0, t2, invalid        # *in > 'Z' -> invalid

    # encrypt: ((char - 'A' + key) % 26) + 'A'

    sub  t0, t0, t1            # char - 'A'
    add  t0, t0, a1            # char - 'A' + key
    li   t2, 26
    remu t0, t0, t2            # (char - 'A' + key) % 26
    add  t0, t0, t1            # ((char - 'A' + key) % 26) + 'A'

    sb   t0, 0(a2)             # *out = encrypt character
    addi a0, a0, 1             # in++
    addi a2, a2, 1             # out++
    j    loop

done:
    sb   zero, 0(a2)           # terminate output string
    li   a0, 1                 # success = 1
    ret

invalid:
    sb   zero, 0(t3)           # set empty string
    li   a0, 0                 # failure = 0
    ret
