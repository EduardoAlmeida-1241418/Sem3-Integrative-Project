# USBD41 - Remove a Freight from a Planned Train

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Freight Manager**  
> **I want to remove a freight from a planned train in the database**  
> **So that the system reflects the updated composition of the train.**

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

#### Scenario 1 – Successful removal of a freight
- **Given** that a freight is associated with a planned train in the database,
- **When** the Freight Manager removes the freight from the train,
- **Then** the freight must no longer be associated with that train.

#### Scenario 2 – Freight not associated with train
- **Given** that a freight is not associated with the specified train,
- **When** an attempt is made to remove it,
- **Then** the operation must be rejected or ignored without affecting other data.

---

### 1.4. Found Dependencies

- Relational model must contain tables for `train` and `freight`.
- Association between freights and planned trains must be defined.
- Database or application logic must enforce correct removal and integrity constraints.

---

### 1.5. Input and Output Data

**Input**

- Train identifier (`train_id`)
- Freight identifier (`freight_id`)
- Operation: remove freight

**Output**

- Success: freight no longer associated with the train
- Failure: operation rejected or ignored if freight not linked to train

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Database-level constraints or triggers to maintain referential integrity.
  - Proper handling of cases where freight is not associated with the train.
- **Execution frequency**
  - Triggered whenever a freight removal request is issued.
