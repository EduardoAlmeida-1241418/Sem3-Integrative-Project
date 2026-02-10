# USBD36 - Add a Building to an Existing Facility

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Planner**  
> **I want to add a building to an existing facility**  
> **So that the database reflects the structures that belong to each facility.**

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

#### Scenario 1 – Add a building to a valid facility
- **Given** that a facility already exists in the database,  
  and the Planner provides the necessary information to create a building,
- **When** the building is added to that facility,
- **Then** the building must be correctly inserted and associated with the facility.

#### Scenario 2 – Facility does not exist
- **Given** that the Planner provides a facility identifier that is not present in the database,
- **When** trying to add a building to that facility,
- **Then** the operation must be rejected.

#### Scenario 3 – Minimum required data provided
- **Given** that the Planner provides incomplete or insufficient building information,
- **When** attempting to add the building,
- **Then** the operation must not proceed.

---

### 1.4. Found Dependencies

- The relational model must define the concept of “facility”.
- The database must support buildings as entities associated with facilities.
- Foreign-key constraints or equivalent relationships between buildings and facilities must exist.
- Required building attributes must be clearly defined.
- Application or database logic must enforce data completeness and valid facility association.

---

### 1.5. Input and Output Data

**Input**

- Facility identifier (`facility_id`)
- Building information (`building_id`, `name`, `type`, `location`, etc.)
- Operation: insert/add building

**Output**

- Success: building inserted and associated with facility
- Failure: insertion rejected due to non-existent facility or missing information

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Database-level constraints or triggers to ensure buildings are only added to valid facilities.
  - Validation of minimum required attributes before insertion.
- **Execution frequency**
  - Triggered whenever a new building registration is requested.
