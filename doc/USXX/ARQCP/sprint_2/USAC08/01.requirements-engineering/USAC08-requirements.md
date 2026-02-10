# USAC08 - Sort Array

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a System Administrator**,  
> **I want** to implement a function  
> `int sort_array(int* vec, int length, char order)`  
> that sorts an integer array in ascending or descending order,  
> **So that** I can organise numeric data for analysis within the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

The sorting operation must consider:
- **vec** → pointer to an integer array
- **length** → number of elements in the array
- **order** → sorting direction
  - `1` → ascending
  - `0` → descending

Sorting must only occur when:
- `length > 0`, and
- `order` is either `0` or `1`.

Invalid inputs must produce no changes and return `0`.

### 1.3 Acceptance Criteria

#### Scenario 1 – Successful ascending sort

- **Given** an integer array `vec` with `length > 0`,  
  and `order = 1`,

- **When** the function `sort_array` is executed,

- **Then** it must:
  - Sort the elements in **ascending order**,
  - **Return `1`** to indicate success.

#### Scenario 2 – Successful descending sort

- **Given** a valid array `vec` with `length > 0`,  
  and `order = 0`,

- **When** the function is called,

- **Then** it must:
  - Sort the elements in **descending order**,
  - **Return `1`**.

#### Scenario 3 – Invalid array length

- **Given** `length ≤ 0`,

- **When** the function is executed,

- **Then** it must:
  - Not modify the array,
  - **Return `0`** to indicate failure.

#### Scenario 4 – Undefined order parameter

- **Given** a valid array and `length > 0`,  
  but an invalid `order` (not `0` or `1`),

- **When** the function is executed,

- **Then** the function must:
  - Not perform sorting,
  - **Return `0`**.

### 1.4 Found Dependencies

This User Story:
- Has **no dependencies** on circular buffer operations.
- Requires implementation of a sorting mechanism (e.g., bubble sort, selection sort).
- Must adhere to **RISC-V Assembly calling conventions**, as sorting will be done in Assembly.

### 1.5 Input and Output Data

#### Input
- `int* vec` → pointer to integer array
- `int length` → number of elements
- `char order` → sorting order (`1` ascending, `0` descending)

#### Output
- Sorted array in memory
- Return value:
  - `1` → successful sort
  - `0` → failure (invalid parameters)

### 1.6 Other Relevant Remarks

- Sorting algorithm choice is flexible (bubble, insertion, selection, etc.).
- The function must avoid unnecessary memory accesses and follow safe pointer operations.
- Sorting direction must strictly follow the `order` parameter.
- No sorting is performed when inputs are invalid.
