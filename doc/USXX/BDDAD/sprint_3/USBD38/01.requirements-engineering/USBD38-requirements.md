# USBD38 - Add a New Gauge

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Planner**  
> **I want to add a new gauge in the database**  
> **So that the system can store and manage all railway gauges used in the network.**

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

#### Scenario 1 – Successful gauge addition
- **Given** that the Planner provides the necessary information to create a gauge,
- **When** the gauge is added to the database,
- **Then** the gauge must be correctly inserted.

#### Scenario 2 – Minimum required data provided
- **Given** that the Planner provides incomplete or insufficient information for the gauge,
- **When** attempting to add the gauge,
- **Then** the operation must be rejected.

---

### 1.4. Found Dependencies

- Relational model must contain a table for `gauge`.
- Required attributes for a gauge must be clearly defined.
- Application or database logic must enforce data completeness and integrity.

---

### 1.5. Input and Output Data

**Input**

- Gauge information (`gauge_id`, `width`, `description`, etc.)
- Operation: insert/add gauge

**Output**

- Success: gauge inserted
- Failure: insertion rejected due to missing or invalid data

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Database-level constraints or triggers to ensure proper registration of gauges.
  - Validation of minimum required attributes before insertion.
- **Execution frequency**
  - Triggered whenever a new gauge registration is requested.
