# USAC04 - Format Command

## 1. Requirements Engineering

### 1.1. User Story Description

>As a System Administrator,
>I want to implement a function int format_command(char* op, int n, char* cmd) that formats a given command string according to specific rules,
>So that I can ensure commands are properly formatted for the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
> 
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Successful command formatting

  - **Given** a valid input string `op` (either `"RE"`, `"YE"`, `"GE"`, or `"RB"`)  
    and a valid integer `n` (ranging from 0 to 99),  
    and an empty `cmd` string,

  - **When** the function `format_command(op, n, cmd)` is called,

  - **Then** the function should format the command as follows:
    - If `op` is `"RE"`, `"YE"`, `"GE"`, or `"RB"`, format the output as `CMD,x`, where `CMD` is the uppercase version of `op` and `x` is the two-digit representation of `n` (e.g., `RE,05`).
    - Store the result in `cmd`,
    - **Return `1`** to indicate success.


- Scenario 2 – Valid command with incorrect integer value

  - **Given** a valid command string `op` (e.g., `"RE"`)  
    and an **integer `n` greater than 99**,

  - **When** the function `format_command(op, n, cmd)` is called,

  - **Then** the function should **return `0`**  
    and **set `cmd` to an empty string** (`""`).


- Scenario 3 – Invalid command string

  - **Given** an **invalid input string** `op` (e.g., `"aaa"`),

  - **When** the function `format_command(op, n, cmd)` is called,

  - **Then** the function should **return `0`**  
    and **set `cmd` to an empty string** (`""`).


- Scenario 4 – Valid command string with spacing issues

  - **Given** an input string `op` with **leading or trailing spaces** (e.g., `" rB "`),  
    and a valid integer `n`,

  - **When** the function `format_command(op, n, cmd)` is called,

  - **Then** the function should **trim and capitalise** `op`,  
    format it as `CMD,x` (e.g., `"RB,05"`) and store the result in `cmd`,  
    and **return `1`** to indicate success.


### 1.4 Found Dependencies
> This User Story depends on no other User Stories for its implementation, but it assumes that the development environment supports **RISC-V Assembly** and the project coding standards are defined.

### 1.5 Input and Output Data

**Input**
- `char* op` → command string (may have spaces, case-insensitive)
- `int n` → integer in range 0–99
- `char* cmd` → buffer to store formatted command

**Output**
- `cmd` → formatted command string if successful, otherwise empty string
- Return value:
  - `1` → formatting successful
  - `0` → failure (invalid `op` or `n` > 99)

### 1.6. Other Relevant Remarks

**(i) Special requirements:**
- Implemented in **C**.
- Function must trim spaces and capitalise `op`.
- Must ensure two-digit representation for `n`.
- Handle invalid inputs gracefully.

**(ii) How often this US is executed:**
- Implemented once for command formatting and may be updated if new command types are added.