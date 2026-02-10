# USAC07 - Move N Elements to an Array

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a System Administrator**,  
> **I want** to implement a function  
> `int move_n_to_array(int* buffer, int length, int* nelem, int* tail, int* head, int n, int* array)`  
> that removes the oldest `n` elements from a circular buffer and stores them into another array,  
> **So that** I can efficiently transfer data for further processing in the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

A circular buffer (ring buffer) stores data in a continuous loop using:
- **buffer** → array of fixed length
- **head** → position of the oldest element
- **tail** → position where the next element will be inserted
- **nelem** → current number of stored elements

Removing elements always starts from the position indicated by `head`.  
If `head` reaches the end of the buffer, it wraps to index `0`.

### 1.3 Acceptance Criteria

#### Scenario 1 – Successful transfer of n elements

- **Given** a valid circular buffer containing **at least `n` elements**,  
  and valid pointers for `nelem`, `head`, `tail`, and the destination `array`,

- **When** the function `move_n_to_array` is called,

- **Then** the function must:
  - Copy the **oldest `n` elements** from the buffer into `array`,
  - Update `head = (head + n) % length`,
  - Decrease `nelem` by `n`,
  - **Return `1`** to indicate success.

#### Scenario 2 – Insufficient elements to move

- **Given** a buffer that contains **fewer than `n` elements**,

- **When** the function is called,

- **Then** the function must:
  - Perform **no modifications** to the buffer,
  - Not write into `array`,
  - **Return `0`** to indicate failure.

#### Scenario 3 – Circular wrap-around behaviour

- **Given** a buffer where `head` is near the end,  
  and there are enough elements for `n` movements,

- **When** the function is executed,

- **Then** the function must:
  - Correctly wrap around to index `0` when reaching the buffer’s end,
  - Maintain the original order of the removed elements in `array`,
  - **Return `1`** on success.

### 1.4 Found Dependencies

This User Story:
- Has **no mandatory dependencies** on other stories.
- Requires understanding of circular buffer pointer arithmetic.
- Must follow **RISC-V Assembly calling conventions** for its implementation.

### 1.5 Input and Output Data

#### Input
- `int* buffer` → pointer to circular buffer
- `int length` → buffer capacity
- `int* nelem` → number of stored elements
- `int* tail` → index where next value would be inserted
- `int* head` → index of the oldest element
- `int n` → number of elements to remove
- `int* array` → destination array

#### Output
- Updated:
  - `head` pointer
  - `nelem` value
- The first `n` removed values placed into `array`
- Return value:
  - `1` → success (elements moved)
  - `0` → failure (not enough elements)

### 1.6 Other Relevant Remarks

- Wrap-around uses modulo arithmetic on pointer updates.
- No elements are removed if `n > *nelem`.
- The order of extracted elements must always match FIFO behaviour.
- The buffer must remain consistent after the operation, even when wrap-around occurs.
