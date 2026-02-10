# USAC02 - Decrypt Data

## 1. Requirements Engineering

### 1.1. User Story Description

>As a System Administrator,
>I want to implement a function int decrypt_data(char* in, int key, char* out) that decrypts a given string using the Caesar Cipher,
>So that encrypted operational data can be securely decoded and interpreted within the Railway Station Management System.at sensitive operational data can be securely encoded before being transmitted or stored within the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
> 
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria
- Scenario 1 – Successful decryption

  - **Given** a valid input string `in` containing **only uppercase letters from A to Z**,  
    and a **valid key value** between **1 and 26**,  
    and a **valid pointer** to the output string `out`,
  - **When** the function `decrypt_data(in, key, out)` is called,
  - **Then** the function must apply the Caesar Cipher **decryption** by shifting each letter **backwards** by the key value,  
    and **store the resulting decrypted string in `out`**,  
    and **return `1`** to indicate success.


- Scenario 2 – Invalid key value

  - **Given** a valid input string and output string,  
    and an **invalid key value** (less than 1 or greater than 26),

  - **When** the function `decrypt_data(in, key, out)` is called,

  - **Then** the function **must not perform any decryption**,  
    and **set `out` as an empty string**,  
    and **return `0`**.


- Scenario 3 – Null input or output pointer

  - **Given** a **null pointer** for either `in` or `out`,

  - **When** the function `decrypt_data(in, key, out)` is executed,

  - **Then** the function **must return `0`**,  
    and ensure that **`out` is set as an empty string**.


- Scenario 4 – Lowercase letters in input

  - **Given** an **input string containing lowercase letters (a–z)**,

  - **When** the function `decrypt_data(in, key, out)` is called,

  - **Then** the function **must reject the input**,  
    and **set `out` as an empty string**,  
    and **return `0`**,  
    ensuring that only **valid uppercase alphabetic data** is processed.


- Scenario 5 – Edge character shifting (wrap-around)

  - **Given** an **input string containing the letter A**,  
    and a **valid key value**,

  - **When** the function `decrypt_data(in, key, out)` is called,

  - **Then** the letter **A** must wrap around to the end of the alphabet (e.g., with key=1, `A` becomes `Z`).


### 1.4 Found Dependencies
> This User Story depends on no other User Stories for its implementation, but it assumes that the development environment supports **RISC-V Assembly** and the project coding standards are defined.

### 1.5 Input and Output Data
**Input**
- `char* in` → pointer to the input string (must contain only uppercase letters A–Z)
- `int key` → integer key between 1 and 26
- `char* out` → pointer to output string buffer

**Output**
- `out` → decrypted string if successful, or empty string if any error occurs
- Return value:
  - `1` → decryption successful
  - `0` → failure (invalid input, null pointer, invalid key, or lowercase letters in input)

### 1.6. Other Relevant Remarks

**(i) Special requirements:**
- Function must be implemented in **RISC-V Assembly**, following calling conventions.
- Only uppercase letters (A–Z) are accepted; lowercase letters or other characters are rejected.
- Key must wrap around the alphabet backward (e.g., A - 1 → Z).
- Function must handle null pointers gracefully and ensure `out` is empty on failure.

**(ii) How often this US is executed:**
- This User Story is implemented once for the decryption utility and may be updated if future security requirements or character set changes occur.
