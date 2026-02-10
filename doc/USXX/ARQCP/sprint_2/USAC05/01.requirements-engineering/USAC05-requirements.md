# USAC05 – Enqueue Value

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a System Administrator**,  
> **I want** to implement a function  
> `int enqueue_value(int* buffer, int length, int* nelem, int* tail, int* head, int value)`  
> that inserts a value into a circular buffer,  
> **So that** I can ensure values are inserted correctly and the buffer behaves as a FIFO circular queue, removing the oldest element when full.


### 1.2. Customer Specifications and Clarifications

A circular buffer (ring buffer) stores data in a continuous loop using:
- **buffer** → array of fixed length
- **head** → position of the oldest element
- **tail** → position where the next element will be inserted
- **nelem** → current number of elements in the buffer

When the buffer becomes full, inserting a new value requires removing the oldest element (advancing `head`).


### 1.3 Acceptance Criteria

#### Scenario 1 – Successful insertion (buffer not full)

- **Given** a valid integer array `buffer`,  
  a valid length `length`,  
  a value `value` to insert,  
  and valid pointers `nelem`, `tail`, `head`,

- **When** the buffer is **not full**,

- **Then** the function must:
  - Insert `value` at position `tail`,
  - Update `tail = (tail + 1) % length`,
  - Increment `nelem`,
  - **Return `0`** (buffer is not full after insertion).

#### Scenario 2 – Buffer full (oldest element removed)

- **Given** a full buffer (`*nelem == length`),  
  and a new value to insert,

- **When** the function is called,

- **Then** the function must:
  - Remove the oldest element by incrementing `head = (head + 1) % length`,
  - Overwrite the element at `tail` with the new `value`,
  - Update `tail = (tail + 1) % length`,
  - Keep `nelem = length`,
  - **Return `1`** (buffer is full after insertion).

#### Scenario 3 – Buffer initially empty (0 elements)

- **Given** `nelem = 0`, `tail = 0`, `head = 0`,  
  and a value to insert,

- **When** the function is called,

- **Then** it must:
  - Insert the value at index `0`,
  - Update `tail`,
  - Set `nelem = 1`,
  - **Return `0`** since the buffer is not full.

### 1.4 Found Dependencies

This User Story:
- Has **no direct dependencies** on other stories.
- Requires understanding of **circular buffer mechanics**, pointer updates, and memory-safe operations.
- Must follow **RISC-V Assembly calling conventions**, as this function will be implemented in Assembly.

### 1.5 Input and Output Data

#### Input
- `int* buffer` → pointer to circular buffer array
- `int length` → maximum capacity of the buffer
- `int* nelem` → number of elements currently stored
- `int* tail` → index where next value will be inserted
- `int* head` → index of the oldest value
- `int value` → value to insert

#### Output
- Updates buffer state in memory, including `head`, `tail`, and `nelem`
- Return value:
  - `1` → buffer is full after insertion
  - `0` → buffer is not full

### 1.6 Other Relevant Remarks

- The circular buffer wraps using modulo arithmetic.
- The function must correctly manage:
  - Overflow (when full)
  - FIFO behaviour
  - Updating pointers atomically
- Removing the oldest element occurs **only** when inserting into a full buffer.
- No errors should occur when values wrap around the end of the array.
