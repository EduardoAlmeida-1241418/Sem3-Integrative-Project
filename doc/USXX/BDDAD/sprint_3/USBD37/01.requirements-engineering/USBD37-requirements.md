# USBD37 - Register a New Wagon and Its Supported Gauges

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Planner**  
> **I want to register a new wagon in the database**  
> **So that the system can store wagons and their supported gauges.**

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

#### Scenario 1 – Successful wagon registration
- **Given** that the Planner provides the necessary information to create a wagon and its supported gauges,
- **When** the wagon is registered in the database,
- **Then** the wagon and all its supported gauges must be correctly inserted.

#### Scenario 2 – Minimum required data provided
- **Given** that the Planner provides incomplete or insufficient information for the wagon or its gauges,
- **When** attempting to register the wagon,
- **Then** the operation must be rejected.

---

### 1.4. Found Dependencies

- Relational model must contain tables for `wagon` and `gauge`.
- Required attributes for wagons and gauges must be clearly defined.
- Foreign-key or association tables must exist to link wagons to their supported gauges.
- Application or database logic must enforce data completeness and integrity.

---

### 1.5. Input and Output Data

**Input**

- Wagon information (`wagon_id`, `type`, `length`, etc.)
- Supported gauges (`gauge_id`, `width`, etc.)
- Operation: insert/register wagon

**Output**

- Success: wagon and associated gauges inserted
- Failure: insertion rejected due to missing or invalid information

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Database-level constraints or triggers to ensure proper registration of wagon and gauges.
  - Validation of minimum required attributes before insertion.
- **Execution frequency**
  - Triggered whenever a new wagon registration is requested.