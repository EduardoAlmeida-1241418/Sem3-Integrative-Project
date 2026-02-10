CC = riscv32-linux-gcc
CFLAGS = -Wall -Wextra -fanalyzer -g
ARCHFLAGS = -march=rv32imd -mabi=ilp32d
LIBRV32 = ${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

OBJ = main.o ui.o manager.o log.o send_to_pico.o login.o \
      format_command.o median.o sort_array.o extract_data.o \
      enqueue_value.o move_n_to_array.o encrypt_data.o decrypt_data.o \
      cb_is_full.o setup.o board.o

all: program.elf

program.elf: $(OBJ)
	$(CC) $(CFLAGS) $(ARCHFLAGS) $(OBJ) -o program.elf

main.o: ui/main.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ui/main.c -o main.o

ui.o: ui/ui.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ui/ui.c -o ui.o

manager.o: manager/manager.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c manager/manager.c -o manager.o

login.o: login/login.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c login/login.c -o login.o

log.o: log/log.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c log/log.c -o log.o

send_to_pico.o: pico/send_to_pico.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c pico/send_to_pico.c -o send_to_pico.o

format_command.o: asm/format_command.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/format_command.s -o format_command.o

median.o: asm/median.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/median.s -o median.o

sort_array.o: asm/sort_array.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/sort_array.s -o sort_array.o

extract_data.o: asm/extract_data.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/extract_data.s -o extract_data.o

enqueue_value.o: asm/enqueue_value.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/enqueue_value.s -o enqueue_value.o

move_n_to_array.o: asm/move_n_to_array.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/move_n_to_array.s -o move_n_to_array.o

decrypt_data.o: asm/decrypt_data.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/decrypt_data.s -o decrypt_data.o

encrypt_data.o: asm/encrypt_data.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/encrypt_data.s -o encrypt_data.o

setup.o: setup/setup.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c setup/setup.c -o setup.o

board.o: board/board.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c board/board.c -o board.o

cb_is_full.o: asm/cb_is_full.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c asm/cb_is_full.s -o cb_is_full.o

clean:
	rm -f *.o program.elf

run: program.elf
	qemu-riscv32 -L $(LIBRV32) ./program.elf
