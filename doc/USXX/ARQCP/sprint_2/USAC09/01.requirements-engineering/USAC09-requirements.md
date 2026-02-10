# USAC09 - Find Median

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a System Administrator**,  
> **I want** to implement a function  
> `int median(int* vec, int length, int* me)`  
> that sorts an integer array and outputs its median value in the `me` parameter,  
> **So that** I can analyse and retrieve the central tendency of numeric datasets within the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

The median calculation requires:
- **vec** → pointer to an integer array
- **length** → number of elements in the array
- **me** → pointer where the median value will be stored

Rules for median:
- If `length` is **odd**, median = middle element of sorted array.
- If `length` is **even**, median = integer average of the two middle elements.

Sorting must always be performed before computing the median.  
Invalid lengths (`length ≤ 0`) must cause the function to return `0`.

### 1.3 Acceptance Criteria

#### Scenario 1 – Median for odd-length array (ascending order)

- **Given** an array `vec` with `length > 0` and an **odd** number of elements,  
  sorted in ascending order,

- **When** `median(vec, length, me)` is executed,

- **Then** the middle element must be written to `*me`,  
  and the function must **return `1`**.

#### Scenario 2 – Median for even-length array (ascending order)

- **Given** a valid array `vec` with an **even** number of elements,  
  sorted in ascending order,

- **When** the function is executed,

- **Then** the integer average of the two middle values must be stored in `*me`,  
  and the function must **return `1`**.

#### Scenario 3 – Median for odd-length array (descending order)

- **Given** the same array sorted in **descending order**,

- **When** the function is executed,

- **Then** the median must be identical to the ascending-order result,  
  and **return `1`**.

#### Scenario 4 – Median for even-length array (descending order)

- **Given** an even-length array sorted in **descending order**,

- **When** the function is executed,

- **Then** the average of the two middle values must match the ascending-order result,  
  and **return `1`**.

#### Scenario 5 – Invalid array length

- **Given** `length ≤ 0`,

- **When** the function is executed,

- **Then** it must not sort,  
  must not modify `*me`,  
  and must **return `0`**.

#### Scenario 6 – Sorting correctness verification

- **Given** a valid array `vec` with `length > 0`,

- **When** the function is executed,

- **Then** the array must be sorted correctly before computing the median,  
  guaranteeing that `*me` reflects the true middle value.

### 1.4 Found Dependencies

This User Story:
- Depends on the logic of **USAC08 – sort_array**, since sorting is required.
- Requires correct management of pointer-based operations.
- Must follow **RISC-V Assembly calling conventions**.

### 1.5 Input and Output Data

#### Input
- `int* vec` → pointer to integer array
- `int length` → number of array elements
- `int* me` → pointer to store the median value

#### Output
- Sorted array (in-place)
- `*me` updated with median value
- Return value:
  - `1` → median successfully computed
  - `0` → failure due to invalid input

### 1.6 Other Relevant Remarks

- The sorting method may reuse `sort_array()` from USAC08.
- Median must be calculated strictly according to the sorted order.
- Even-length median uses **integer division**.
- No modifications must occur when `length ≤ 0`.
- Sorting must work consistently regardless of ascending or descending order.
