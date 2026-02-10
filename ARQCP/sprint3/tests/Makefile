CC = riscv32-linux-gcc
CFLAGS = -Wall -Wextra -fanalyzer -g
ARCHFLAGS = -march=rv32imd -mabi=ilp32d
LIBRV32 = ${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

ASM_OBJ = encrypt_data.o median.o sort_array.o \
          enqueue_value.o move_n_to_array.o extract_data.o format_command.o

C_OBJ = board.o manager.o manager_stubs.o log_stubs.o

TEST_OBJ = test_main.o \
           test_auth.o \
           test_users.o \
           test_tracks.o \
           test_sensors.o \
           test_board.o \
           test_persistence.o \
           test_report.o \
           test_usac10.o

all: tests.elf

tests.elf: $(TEST_OBJ) $(ASM_OBJ) $(C_OBJ)
	$(CC) $(CFLAGS) $(ARCHFLAGS) $(TEST_OBJ) $(ASM_OBJ) $(C_OBJ) -o tests.elf

board.o: ../board/board.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../board/board.c -o board.o

manager.o: ../manager/manager.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../manager/manager.c -o manager.o

manager_stubs.o: manager_stubs.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c manager_stubs.c -o manager_stubs.o

test_main.o: test_main.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_main.c -o test_main.o

test_auth.o: test_auth.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_auth.c -o test_auth.o

test_users.o: test_users.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_users.c -o test_users.o

test_tracks.o: test_tracks.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_tracks.c -o test_tracks.o

test_sensors.o: test_sensors.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_sensors.c -o test_sensors.o

test_board.o: test_board.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_board.c -o test_board.o

test_persistence.o: test_persistence.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_persistence.c -o test_persistence.o

test_report.o: test_report.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_report.c -o test_report.o

encrypt_data.o: ../asm/encrypt_data.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../asm/encrypt_data.s -o encrypt_data.o

decrypt_data.o: ../asm/decrypt_data.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../asm/decrypt_data.s -o decrypt_data.o

median.o: ../asm/median.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../asm/median.s -o median.o

sort_array.o: ../asm/sort_array.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../asm/sort_array.s -o sort_array.o

test_usac10.o: test_usac10.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_usac10.c -o test_usac10.o

test_setup.o: test_setup.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c test_setup.c -o test_setup.o

enqueue_value.o: ../asm/enqueue_value.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../asm/enqueue_value.s -o enqueue_value.o

move_n_to_array.o: ../asm/move_n_to_array.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../asm/move_n_to_array.s -o move_n_to_array.o

extract_data.o: ../asm/extract_data.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../asm/extract_data.s -o extract_data.o

format_command.o: ../asm/format_command.s
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c ../asm/format_command.s -o format_command.o

log_stubs.o: log_stubs.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c log_stubs.c -o log_stubs.o

clean:
	rm -f *.o tests.elf

run: tests.elf
	qemu-riscv32 -L $(LIBRV32) ./tests.elf