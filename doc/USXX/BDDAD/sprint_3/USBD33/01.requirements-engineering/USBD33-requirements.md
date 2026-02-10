# USBD33 - Prevent Trains Exceeding Their Predefined Maximum Length

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Freight Manager**  
> **I want the database to prevent a train from exceeding its predefined maximum length**  
> **So that the system enforces operational limits (safety / infrastructure constraints) and avoids planning or recording trains that are too long for available infrastructure.**

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:**  
> –
>
> **Answer:**  
> –
>
> [Customer Clarification – Forum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Reject insertion of a train composition that exceeds max length
- **Given** a train with a predefined `max_length` (metres) stored in the database,  
  and a set of wagons (each with a `length` attribute) intended to be associated with the train,
- **When** an operation attempts to **insert** the composition (associate wagons with the train) whose **sum of wagon lengths + locomotives length > train.max_length**,
- **Then** the database **must reject** the insertion,  
  return a meaningful error (SQL error or raised exception) indicating `MAX_LENGTH_VIOLATION`,  
  and **no partial changes** must be left applied (the operation must be atomic / rolled back).

#### Scenario 2 – Prevent updates that cause overflow
- **Given** an existing train composition currently within limits,
- **When** a user attempts to **add** a wagon, **change a wagon's length**, or **associate a freight** whose inclusion would make the total length exceed `max_length`,
- **Then** the database **must prevent** the update (raise `MAX_LENGTH_VIOLATION`),  
  leaving the previous consistent state unchanged.

#### Scenario 3 – Allow valid operations within limits
- **Given** operations (insert/update) whose resulting total train length is **≤** `max_length`,
- **When** the operations are executed,
- **Then** they must succeed normally and be persisted.

#### Scenario 4 – Concurrent operations and race conditions
- **Given** multiple concurrent attempts to modify the same train (e.g., two users adding wagons simultaneously),
- **When** concurrent transactions are executed,
- **Then** the database must preserve correctness (no interleaving that allows the final persisted state to exceed `max_length`) using appropriate locking/isolation (e.g., `SELECT ... FOR UPDATE`, or a serialising mechanism in PL/SQL),  
  and conflicting transactions must fail with the `MAX_LENGTH_VIOLATION` when appropriate.

#### Scenario 5 – Bulk population and migrations
- **Given** population scripts or bulk imports (USBD32 data loading) that may assign wagons to trains,
- **When** these scripts run,
- **Then** any assignment that would violate `max_length` must be detected and either:
  - rejected with clear logging; or
  - handled according to a defined policy (e.g., skip the offending record and continue),  
    ensuring database integrity is never compromised.

---

### 1.4. Found Dependencies

- `train`, `wagon` (or `carriage`), and relevant association tables must exist.
- Attributes `wagon.length` and `train.max_length` must be numeric and in metres.
- Business rules defining total train length (including locomotives, buffers, tolerances) must be agreed.
- Error-handling and reporting standards for violations defined (`MAX_LENGTH_VIOLATION`).
- Enforcement mechanism chosen: database trigger, PL/SQL function, materialized/derived column + CHECK, or combination.
- Transaction isolation and concurrency mechanisms must be defined and implemented.

---

### 1.5. Input and Output Data

**Input**

- Train table: `train(max_length, id, ...)`
- Wagon table: `wagon(length, id, ...)`
- Association table: `train_wagon(train_id, wagon_id, ...)`
- Operations: insert, update, associate wagons, associate freights

**Output**

- Success/Failure of operations
- Meaningful error: `MAX_LENGTH_VIOLATION`
- Atomic operations: database state remains consistent

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Enforcement must be at **database level** (PL/SQL trigger, constraint, or protected procedure).
  - Must handle inserts, updates, association/dissociation, and concurrent modifications.
  - Bulk loads and migrations must comply with constraints or follow documented policy.
- **Execution frequency**
  - Any modification that changes total train length triggers the enforcement logic.

