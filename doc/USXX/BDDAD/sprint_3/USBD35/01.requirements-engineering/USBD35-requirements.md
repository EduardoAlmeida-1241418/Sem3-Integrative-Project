# USBD35 - Register a New Facility in the Database

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Planner**  
> **I want to register a new facility in the database**  
> **So that the system can store and manage facilities used by the railway network.**

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

#### Scenario 1 – Successful facility registration
- **Given** that the Planner provides the necessary information to create a facility,
- **When** the facility is registered in the database,
- **Then** the new facility must be correctly inserted.

#### Scenario 2 – Minimum required data provided
- **Given** that the Planner provides incomplete or insufficient information for the facility,
- **When** attempting to register the facility,
- **Then** the operation must be rejected.

---

### 1.4. Found Dependencies

- Relational model must contain a table for facilities.
- Required attributes for a facility must be clearly defined.
- Oracle DB schema must support insertion, validation, and integrity constraints.
- Application logic or database constraints must enforce minimum required data.

---

### 1.5. Input and Output Data

**Input**

- Facility information (e.g., `facility_id`, `name`, `type`, `location`, etc.)
- Operation: insert/register facility

**Output**

- Success: facility record inserted
- Failure: insertion rejected due to missing or invalid data

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Database-level constraints to ensure data completeness.
  - Validation of minimum required attributes before insertion.
- **Execution frequency**
  - Triggered whenever a new facility registration is requested.
