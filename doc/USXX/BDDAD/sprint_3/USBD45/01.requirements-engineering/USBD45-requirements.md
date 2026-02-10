# USBD45 - Add a New Line

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Planner**  
> **I want to add a new line in the database**  
> **So that the system can represent railway lines, each of which must have at least one segment.**

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

#### Scenario 1 – Successful addition of a new line
- **Given** that the Planner provides the necessary information for the line, including at least one segment,
- **When** the line is added to the database,
- **Then** the line and its segment(s) must be correctly inserted.

#### Scenario 2 – Line without segments
- **Given** that the Planner attempts to add a line without any segment,
- **When** the operation is executed,
- **Then** it must be rejected.

#### Scenario 3 – Minimum required data provided
- **Given** that the Planner provides incomplete or insufficient information for the line or its segment(s),
- **When** attempting to add the line,
- **Then** the operation must be rejected.

---

### 1.4. Found Dependencies

- Relational model must contain tables for `line` and `segment`.
- Relationships between lines and segments must be defined.
- Each line must be associated with at least one segment.
- Required attributes for lines and segments must be clearly identified.
- Database or application logic must enforce data completeness and integrity.

---

### 1.5. Input and Output Data

**Input**

- Line information (`line_id`, `name`, `type`, etc.)
- Segment information (`segment_id`, `start_point`, `end_point`, `length`, etc.)
- Operation: insert/add line

**Output**

- Success: line and its segments inserted
- Failure: insertion rejected due to missing segments or incomplete information

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Database-level constraints or triggers to ensure lines have at least one segment.
  - Validation of minimum required attributes before insertion.
- **Execution frequency**
  - Triggered whenever a new line registration is requested.
