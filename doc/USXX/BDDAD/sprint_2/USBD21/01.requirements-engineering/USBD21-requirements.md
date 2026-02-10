# USBD21 - Update Relational Model

## 1. Requirements Engineering

### 1.1. User Story Description

> As a Product Owner,
>I want the relational model to be updated according to the changed requirements (logical level) and instantiated in the Oracle DBMS,
>So that the system’s data structure remains aligned with the current business logic and ensures data consistency across the Railway Station Management System.

### 1.2 Acceptance Criteria

- Scenario 1 – Relational model updated successfully

  - **Given** the existence of an existing relational model (logical level),  
    and new or modified **functional requirements** affecting data entities or relationships,

  - **When** the model is **updated** to reflect these changes,

  - **Then** the updated model must comply with **normalisation rules**,  
    maintain **referential integrity**,  
    and be **fully consistent** with the revised logical model of the system.


- Scenario 2 – Successful instantiation in Oracle DBMS

  - **Given** an updated logical relational model,  
    and access to an **Oracle DBMS environment**,

  - **When** the model is instantiated through **DDL** scripts,

  - **Then** all tables, constraints, relationships, and data types must be **created successfully**,  
    and the schema must be **operational** within Oracle without errors.


- Scenario 3 – Validation of model consistency

  - **Given** the instantiated schema in Oracle,

  - **When** validation queries are executed,

  - **Then** all entities and relationships must match the logical model,  
    foreign keys must reference valid primary keys,  
    and all **constraints and relationships** must function as expected.

### 1.3 Found out Dependencies

| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USLP05** | Conceptual domain model updated | Logical model must match conceptual entities |
| **USLP06** | Updated glossary | Terminology consistency in table/column names |


### 1.4 Input and Output Data

>**Input**
- Updated conceptual model (USLP05)
- Glossary (USLP06)
- Previous relational model (if any)

**Output**
- Updated **logical / relational model diagram**
- SQL DDL statements or scripts representing tables, columns, primary keys, and foreign keys
- Stored in `/doc/global-artifacts/02.database-design/`

### 1.6. Other Relevant Remarks

- Ensure naming conventions are consistent with previous Sprints.
- Include notes on multiplicity, mandatory/optional relationships, and referential integrity.
- The model will serve as the **baseline for physical database creation** in later sprints.
