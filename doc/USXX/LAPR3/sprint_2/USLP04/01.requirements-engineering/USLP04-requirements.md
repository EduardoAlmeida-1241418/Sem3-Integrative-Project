# USLP04 - Update Data Dictionary / Glossary

## 1. Requirements Engineering

### 1.1. User Story Description

>**As a** Product Owner, **I want** a data dictionary/glossary to be created, **So that** all team members have a shared understanding of database concepts and entities.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** After analyzing some User Stories (ESINF, mainly), we found some users not defined in the System User's list - Warehouse Planner, Terminal Operator, etc. Are they only for interpretation purposes or real users? If so, is the Warehouse Planner a Scheduler/Planner, Terminal Operator a Station Master, etc?
>
>**Answer:** Thanks for noticing that; Terminal Operator will be added in the System Users list. Warehouse Planner and Storage Manager are synonyms.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria
- Scenario 1 – Creation of the data dictionary/glossary

  > **Given** that the railway database system is being designed,
  >
  > **When** the team documents all entities, attributes, and relationships defined in the data model,
  >
  > **Then** a complete data dictionary or glossary must be created,
  >
  >  **Then** it must include clear definitions for all entities, attributes, and relationships used in the system,
  >
  >  **Then** it must ensure consistent terminology and accurate descriptions aligned with the database model.

### 1.4 Found out Dependencies
>This User Story doesn’t depend on any other US.

### 1.5 Input and Output Data
>**Input**
>
>- Source materials and requirements (sections 2.2.1 and 2.2.2 of the project specification).
>- Team discussion notes about domain concepts.
>
>**Output**
>
>- A data dictionary/glossary document listing all entities, attributes, and relationships with definitions.
>- Stored in the repository under `/doc/global-artifacts/01.requirements-engineering/`.

### 1.6 Other Relevant Remarks
>**(i) Special requirements:**
>
>- The glossary must use consistent terminology with the logical model.
>- Terms must be alphabetically ordered and concise.
>
>**(ii) How often this US is held:**
>
>- This US is executed once at the start of database design and may be revised after feedback from later sprints.