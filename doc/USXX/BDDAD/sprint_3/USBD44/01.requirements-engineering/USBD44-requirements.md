# USBD44 - Add a Segment to an Existing Line

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Planner**  
> **I want to add a segment to an existing line in the database**  
> **So that the system can represent the full structure of railway lines, including any sidings.**

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

#### Scenario 1 – Successful addition of a segment
- **Given** that a line exists in the database,  
  and the Planner provides the necessary information for the segment,
- **When** the segment is added,
- **Then** it must be correctly inserted and associated with the line.

#### Scenario 2 – Segment has a siding
- **Given** that the segment includes a siding,
- **When** the segment is added,
- **Then** the siding must also be correctly inserted and associated with the segment.

#### Scenario 3 – Minimum required data provided
- **Given** that the Planner provides incomplete or insufficient information for the segment (or siding),
- **When** attempting to add it,
- **Then** the operation must be rejected.

---

### 1.4. Found Dependencies

- Relational model must contain tables for `line` and `segment`.
- Relationships between lines, segments, and optional sidings must be defined.
- Required attributes for segments and sidings must be clearly identified.
- Database or application logic must enforce data completeness and integrity.

---

### 1.5. Input and Output Data

**Input**

- Line identifier (`line_id`)
- Segment information (`segment_id`, `start_point`, `end_point`, `length`, etc.)
- Optional siding information (`siding_id`, `length`, `position`, etc.)
- Operation: insert/add segment

**Output**

- Success: segment (and siding if present) inserted and associated with the line
- Failure: insertion rejected due to missing or invalid information

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Database-level constraints or triggers to ensure segments and sidings are properly associated.
  - Validation of minimum required attributes before insertion.
- **Execution frequency**
  - Triggered whenever a new segment (or segment with siding) is added.

