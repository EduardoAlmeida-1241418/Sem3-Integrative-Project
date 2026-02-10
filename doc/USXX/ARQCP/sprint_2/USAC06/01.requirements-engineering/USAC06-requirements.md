# USAC06 – Dequeue Value

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a System Administrator**,  
> **I want** to implement a function  
> `int dequeue_value(int* buffer, int length, int* nelem, int* tail, int* head, int* value)`  
> that removes the oldest element from a circular buffer and outputs it,  
> **So that** I can correctly retrieve and manage the stored data within the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

A circular buffer behaves as a FIFO structure with wrap-around:
- **head** → position of the *oldest* element
- **tail** → next position where a new element would be inserted
- **nelem** → number of valid elements stored
- When the buffer is empty (`nelem == 0`), no element can be removed.

The function must output the removed element in `*value`.


### 1.3 Acceptance Criteria

#### Scenario 1 – Successful removal from non-empty buffer

- **Given** a circular buffer containing one or more elements,  
  and valid pointers for `nelem`, `head`, `tail`, and `value`,  
  and a valid buffer size `length`,

- **When** the function `dequeue_value(...)` is called,

- **Then** the function must:
  - Retrieve the oldest element from `buffer[*head]`,
  - Store it into `*value`,
  - Increment `head = (head + 1) % length`,
  - Decrement `nelem`,
  - **Return `1`** (successful removal).

#### Scenario 2 – Attempt to remove from an empty buffer

- **Given** a circular buffer with `nelem == 0`,  
  and valid pointers for `head`, `tail`, and `value`,

- **When** `dequeue_value(...)` is called,

- **Then** the function must:
  - Leave the buffer unchanged,
  - Leave `head` and `tail` unchanged,
  - Set `*value = 0`,
  - **Return `0`** (removal failed).

#### Scenario 3 – Circular wrap-around behaviour

- **Given** a circular buffer where `head` is near the end of the array  
  and `tail` is near the beginning (buffer wrapped),

- **When** `dequeue_value(...)` is executed,

- **Then** the function must:
  - Correctly increment the `head` pointer using wrap-around logic,
  - i.e., `head = (head + 1) % length`,
  - And **return `1`** to indicate successful removal.

### 1.4 Found Dependencies

This User Story:
- Has no direct dependencies on previous stories.
- Requires understanding of circular buffer semantics.
- Must respect RISC-V Assembly calling conventions.
- Depends on correct implementation of buffer structure and state variables.

### 1.5 Input and Output Data

#### Input
- `int* buffer` → pointer to circular buffer
- `int length` → maximum capacity
- `int* nelem` → number of elements currently stored
- `int* tail` → index of next insertion
- `int* head` → index of oldest element
- `int* value` → pointer where removed element will be stored

#### Output
- Writes the removed element into `*value`
- Updates `head` and `nelem` accordingly
- Return values:
  - `1` → removal succeeded
  - `0` → buffer empty / failure

### 1.6 Other Relevant Remarks

- The function must safely handle circular wrap-around.
- It must not modify the buffer or pointers when empty.
- `*value` should always be defined (0 on failure).
- This operation is FIFO: always remove from `head`.
