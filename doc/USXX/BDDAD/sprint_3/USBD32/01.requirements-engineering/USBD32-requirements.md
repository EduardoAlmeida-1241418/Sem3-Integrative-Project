# USBD32 - Populate Oracle Database with Required Data

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Product Owner**  
> **I want the database generated in USBD31 populated with the applicable data provided, and any other data required to test Sprint 3 user stories**  
> **So that the system can be properly validated with realistic and complete datasets.**

> **Context:** Populating the Oracle database ensures that all Sprint 3 functionalities can be tested with realistic and consistent data. Both mandatory and supplementary datasets must be inserted while respecting all relational constraints.

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:** 
> **Answer:** 

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Insert provided applicable data
- **Given:** Database schema from USBD31 is implemented and mandatory datasets are provided
- **When:** Development team inserts this data
- **Then:** All datasets must be inserted successfully, respecting all constraints and referential integrity

---

#### Scenario 2 – Insert additional data required to test Sprint 3
- **Given:** Extra data needed to validate Sprint 3 user stories
- **When:** Complementary datasets are prepared and inserted
- **Then:** The database must contain all records necessary for complete functional and integration testing of Sprint 3 features

---

#### Scenario 3 – Validation of inserted data
- **Given:** All data has been inserted
- **When:** Validation queries are executed
- **Then:** Must ensure:
  - foreign keys and constraints are satisfied
  - mandatory fields are not empty
  - data matches expected formats and semantics (e.g., encrypted passwords, valid identifiers, track states)
  - no duplicates or inconsistent records are present

---

#### Scenario 4 – Reproducible population scripts
- **Given:** Future deployments or CI pipelines may need to recreate the database
- **When:** Population scripts are executed from scratch
- **Then:** Scripts must consistently produce the exact same populated initial state without manual intervention

---

### 1.4. Dependencies
- Oracle database schema from USBD31 fully implemented
- All applicable and supplementary datasets collected and validated
- Mapping of datasets to relational schema verified
- CI/CD pipelines or fresh Oracle instances available for testing

---

### 1.5. Input and Output Data

**Input**
- SQL INSERT statements or bulk load files for mandatory initial datasets
- Additional test data for Sprint 3 features
- Validated data types and relational mappings

**Output**
- Fully populated Oracle database with:
  - mandatory datasets inserted
  - supplementary Sprint 3 test data inserted
  - all constraints and referential integrity satisfied
- Reproducible population scripts

---

### 1.6. Other Relevant Remarks
- All scripts must be tested on fresh Oracle instances to verify reproducibility
- Documentation must clearly describe the data content and purpose for each dataset
- Scripts must be version-controlled and stored in the repository
- Population workflow should support automated CI pipeline execution
