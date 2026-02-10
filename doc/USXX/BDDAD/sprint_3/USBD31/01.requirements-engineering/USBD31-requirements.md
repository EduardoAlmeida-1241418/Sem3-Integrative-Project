# USBD31 - Update Relational Model and Instantiate in Oracle

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Product Owner**  
> **I want the relational model to be updated according to the changed requirements at the logical level and instantiated in the Oracle DBMS**  
> **So that the database structure remains aligned with the system's evolving design and supports all new functionalities.**

> **Context:** The system’s data model must reflect the latest requirements and remain consistent with the domain model and architecture. The Oracle DBMS serves as the production environment for all relational data structures.

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:** 
> **Answer:** 

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Relational model updated according to new requirements
- **Given:** New or changed requirements affecting the logical model
- **When:** The development team updates the relational schema
- **Then:** Updated model must:
  - reflect all required entities, attributes, and relationships
  - comply with normalization rules (at least 3NF unless justified)
  - remain fully consistent with the domain model and system architecture

---

#### Scenario 2 – Model instantiated in Oracle DBMS
- **Given:** Updated relational model
- **When:** The team instantiates the schema in Oracle
- **Then:** Oracle DB must include:
  - all updated tables, constraints, keys, indexes, and relationships
  - appropriate Oracle-standard data types
  - successful execution of all CREATE TABLE scripts

---

#### Scenario 3 – Consistency and validation
- **Given:** Schema implemented in Oracle
- **When:** Validation is performed
- **Then:** Must ensure:
  - referential integrity enforced
  - primary and foreign keys correctly defined
  - no contradictory or orphan records
  - test datasets inserted without violating constraints

---

### 1.4. Dependencies
- Approved and documented changes to the domain model
- Existing relational model for comparison and incremental update
- Oracle DBMS environment prepared and accessible
- Version control repository for scripts

---

### 1.5. Input and Output Data

**Input**
- Updated logical model (entities, attributes, relationships)
- Existing relational model for reference
- Oracle DBMS connection credentials and environment

**Output**
- Updated ER and relational diagrams
- SQL DDL scripts for Oracle
- Instantiated Oracle schema with all tables, constraints, keys, and indexes
- Validation reports showing referential integrity and successful data insertion

---

### 1.6. Other Relevant Remarks
- Automated or manual testing of schema consistency is required
- All scripts must be stored in version control with proper documentation
- Changes must be traceable to impacted user stories and requirements
- Documentation (logical and physical) must reflect the final Oracle implementation
