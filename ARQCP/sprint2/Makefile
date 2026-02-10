# ============================================================
# Makefile ARQCP - Sprint USACxx
# Compila e executa funções em Assembly RISC-V com testes em C
# ============================================================

CC = riscv32-linux-gcc
CFLAGS = -Wall -Wextra -fanalyzer -g
ARCHFLAGS = -march=rv32imd -mabi=ilp32d
LIBRV32 = ${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

CODE_DIR = code
TEST_DIR = tests
BUILD_DIR = build

# Define qual USAC será testada
TEST ?= USAC09


ifeq ($(TEST),USAC01)
    SRC_ASM  = $(CODE_DIR)/USAC01/encrypt_data.s
    SRC_HDR  = $(CODE_DIR)/USAC01/encrypt_data.h
    SRC_TEST = $(TEST_DIR)/USAC01/test_encrypt_data.c
endif

ifeq ($(TEST),USAC02)
    SRC_ASM  = $(CODE_DIR)/USAC02/decrypt_data.s
    SRC_HDR  = $(CODE_DIR)/USAC02/decrypt_data.h
    SRC_TEST = $(TEST_DIR)/USAC02/test_decrypt_data.c
endif

ifeq ($(TEST),USAC03)
    SRC_ASM  = $(CODE_DIR)/USAC03/extract_data.s
    SRC_HDR  = $(CODE_DIR)/USAC03/extract_data.h
    SRC_TEST = $(TEST_DIR)/USAC03/test_extract_data.c
endif

ifeq ($(TEST),USAC04)
    SRC_ASM  = $(CODE_DIR)/USAC04/format_command.s
    SRC_HDR  = $(CODE_DIR)/USAC04/format_command.h
    SRC_TEST = $(TEST_DIR)/USAC04/test_format_command.c
endif

ifeq ($(TEST),USAC05)
    SRC_ASM  = $(CODE_DIR)/USAC05/enqueue_value.s
    SRC_HDR  = $(CODE_DIR)/USAC05/enqueue_value.h
    SRC_TEST = $(TEST_DIR)/USAC05/test_enqueue_value.c
endif

ifeq ($(TEST),USAC06)
    SRC_ASM  = $(CODE_DIR)/USAC06/dequeue_value.s \
               $(CODE_DIR)/USAC05/enqueue_value.s
    SRC_HDR  = $(CODE_DIR)/USAC06/dequeue_value.h
    SRC_TEST = $(TEST_DIR)/USAC06/test_dequeue_value.c
endif

ifeq ($(TEST),USAC07)
    SRC_ASM  = $(CODE_DIR)/USAC07/move_n_to_array.s
    SRC_HDR  = $(CODE_DIR)/USAC07/move_n_to_array.h
    SRC_TEST = $(TEST_DIR)/USAC07/test_move_n_to_array.c
endif

ifeq ($(TEST),USAC08)
    SRC_ASM  = $(CODE_DIR)/USAC08/sort_array.s
    SRC_HDR  = $(CODE_DIR)/USAC08/sort_array.h
    SRC_TEST = $(TEST_DIR)/USAC08/test_sort_array.c
endif

ifeq ($(TEST),USAC09)
    # USAC09 depende de sort_array (USAC08)
    SRC_ASM  = $(CODE_DIR)/USAC09/median.s $(CODE_DIR)/USAC08/sort_array.s
    SRC_HDR  = $(CODE_DIR)/USAC09/median.h
    SRC_TEST = $(TEST_DIR)/USAC09/test_median.c
endif


TARGET = $(BUILD_DIR)/$(TEST).elf

# Gera objetos .o para cada arquivo fonte (C e ASM)
OBJS = $(patsubst $(TEST_DIR)/$(TEST)/%.c, $(BUILD_DIR)/%.o, $(SRC_TEST)) \
       $(patsubst $(CODE_DIR)/%/%.s, $(BUILD_DIR)/%.o, $(SRC_ASM))

all: $(TARGET)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

# Compila os testes em C
$(BUILD_DIR)/%.o: $(TEST_DIR)/$(TEST)/%.c $(SRC_HDR) | $(BUILD_DIR)
	$(CC) $(CFLAGS) $(ARCHFLAGS) -I$(CODE_DIR)/$(TEST) -c $< -o $@

$(BUILD_DIR)/%.o: $(CODE_DIR)/%/%.s | $(BUILD_DIR)
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c $< -o $@


$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) $(ARCHFLAGS) $(OBJS) -o $(TARGET)


clean:
	rm -rf $(BUILD_DIR) *.o *.elf .gdbinit

run: $(TARGET)
	qemu-riscv32 -L $(LIBRV32) ./$(TARGET)

debug: $(TARGET) .gdbinit
	qemu-riscv32 -L $(LIBRV32) -g 1234 $(TARGET) -S


define GDBINIT
target extended-remote localhost:1234
set sysroot ~/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot
break main
continue
endef

export GDBINIT

.gdbinit:
	@echo "$$GDBINIT" > .gdbinit
