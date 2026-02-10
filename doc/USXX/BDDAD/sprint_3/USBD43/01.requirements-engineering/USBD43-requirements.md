# USBD43 - Add a New Electric Locomotive Model

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Planner**  
> **I want to add a new Electric locomotive model in the database**  
> **So that the system can store and manage the types of electric locomotives used in the railway network.**

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

#### Scenario 1 – Successful addition of a new Electric locomotive model
- **Given** that the Planner provides the necessary information for the new Electric locomotive model,
- **When** the model is added to the database,
- **Then** it must be correctly inserted and available for use in the system.

#### Scenario 2 – Minimum required data provided
- **Given** that the Planner provides incomplete or insufficient information for the Electric locomotive model,
- **When** attempting to add the model,
- **Then** the operation must be rejected.

---

### 1.4. Found Dependencies

- Relational model must contain a table for `locomotive_model`.
- Required attributes for Electric locomotives (e.g., `model_id`, `power_type`, `max_speed`, etc.) must be defined.
- Database or application logic must enforce data completeness and integrity.

---

### 1.5. Input and Output Data

**Input**

- Electric locomotive model information (`model_id`, `power_type`, `max_speed`, `weight`, etc.)
- Operation: insert/add locomotive model

**Output**

- Success: model inserted and available for use
- Failure: operation rejected due to missing or invalid information

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Database-level constraints or triggers to ensure proper registration of Electric locomotive models.
  - Validation of minimum required attributes before insertion.
- **Execution frequency**
  - Triggered whenever a new Electric locomotive model registration is requested.

