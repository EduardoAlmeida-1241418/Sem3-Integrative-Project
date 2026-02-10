# USAC03 - Extract Data

## 1. Requirements Engineering

### 1.1. User Story Description

>As a System Administrator,
>I want to implement a function int extract_data(char* str, char* token, char* unit, int* value) that extracts value and unit data from a formatted string,
>So that I can efficiently parse and retrieve specific data (such as temperature or humidity) for further processing in the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
> 
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Successful extraction

  - **Given** a valid input string `str` in the format `"TOKEN &unit:xxxxxxx &value:xx #TOKEN &unit:xxxxxxxx&value:xx"`,  
    and a valid `token` (either `"TEMP"` or `"HUM"`),  
    and valid pointers for `unit` (a string) and `value` (an integer),

  - **When** the function `extract_data(str, token, unit, &value)` is called,

  - **Then** the function should extract the unit and value corresponding to the provided `token`,  
    and store the extracted `unit` in `unit`,  
    and store the extracted `value` in `value`,  
    and **return `1`** to indicate success.

- Scenario 2 – Invalid token

  - **Given** a valid input string and output pointers for `unit` and `value`,  
    and an **invalid token** (not `"TEMP"` or `"HUM"`),

  - **When** the function `extract_data(str, token, unit, &value)` is called,

  - **Then** the function should not perform any extraction,  
    and **set `unit` to an empty string** (`""`),  
    and **set `value` to `0`**,  
    and **return `0`** to indicate failure.


- Scenario 3 – Empty or malformed input string

  - **Given** an **empty or malformed input string** (not matching the required format),

  - **When** the function `extract_data(str, token, unit, &value)` is executed,

  - **Then** the function should not perform any extraction,  
    and **set `unit` to an empty string** (`""`),  
    and **set `value` to `0`**,  
    and **return `0`** to indicate failure.


- Scenario 4 – Multiple tokens in input string

  - **Given** an input string with multiple tokens (e.g., `"TEMP &unit:celsius &value:20 #HUM &unit:percentage &value:80"`),  
    and a valid `token` (e.g., `"TEMP"`),

  - **When** the function `extract_data(str, token, unit, &value)` is called,

  - **Then** the function should extract the first occurrence of the token's associated unit and value,  
    and **return `1`** to indicate success.


### 1.4 Found Dependencies
> This User Story depends on no other User Stories for its implementation, but it assumes that the development environment supports **RISC-V Assembly** and the project coding standards are defined.

### 1.5 Input and Output Data

**Input**
- `char* str` → formatted string containing tokens, units, and values
- `char* token` → token to extract (`"TEMP"` or `"HUM"`)
- `char* unit` → buffer to store extracted unit
- `int* value` → pointer to store extracted integer value

**Output**
- `unit` → extracted unit string if successful, otherwise empty string
- `value` → extracted integer value if successful, otherwise `0`
- Return value:
  - `1` → extraction successful
  - `0` → extraction failed (invalid token or malformed string)

### 1.6. Other Relevant Remarks

**(i) Special requirements:**
- Implemented in **RISC-V Assembly**, following calling conventions.
- Must parse and extract data reliably even if multiple tokens exist.
- Must handle null pointers or empty/malformed strings gracefully.

**(ii) How often this US is executed:**
- Implemented once for parsing environmental data and may be updated if new token types or string formats are added.
