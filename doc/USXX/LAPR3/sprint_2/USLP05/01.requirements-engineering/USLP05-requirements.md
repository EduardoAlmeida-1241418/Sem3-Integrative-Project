# USLP05 - Update conceptual domain model

## 1. Requirements Engineering

### 1.1. User Story Description

>As a Product Owner,
>I want the conceptual domain model to be updated according to the changed requirements,
>So that the system’s high-level representation accurately reflects the new business logic, entities, and relationships within the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Conceptual model updated successfully

  - **Given** the existence of a previous conceptual domain model,  
    and newly approved **requirement changes** impacting entities, attributes, or relationships,

  - **When** the team updates the **conceptual domain model** to reflect those changes,

  - **Then** the updated model must accurately represent all **revised entities, associations, and multiplicities**,  
    and must be **consistent** with the current business requirements and logical model.


- Scenario 2 – Alignment with updated requirements

  - **Given** the updated requirements documentation (user stories, specifications, or change requests),

  - **When** the conceptual model is revised,

  - **Then** the new or modified entities and relationships must correspond to the **functional and non-functional requirements**,  
    and maintain **semantic consistency** (no ambiguity in relationships or roles).


- Scenario 3 – Model validation

  - **Given** the updated conceptual model diagram,

  - **When** it is **reviewed by the development team**,

  - **Then** the model must be **validated** for completeness, correctness, and consistency with the system’s logical architecture,  
    and **approved** as the official conceptual representation for future iterations.

### 1.4 Found out Dependencies
| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USLP01** | Initial conceptual domain model | This US updates it |
| **USLP04** | Glossary creation | Glossary must align with the updated model |
| **USBD21** | Updated relational/ logical model | Conceptual and logical must stay aligned |
### 1.5 Input and Output Data
>**Input**
>
>- Updated functional specification (sections 2.2 and 3 of the project document).
>- Previous conceptual domain model.
>- Team discussion notes about domain concepts.
>
>**Output**
>
>- Updated conceptual domain model diagram.
>- Stored in the repository under `/doc/global-artifacts/02.analysis/svg`.

### 1.6 Other Relevant Remarks
>Model must reflect both domain structure and operational workflow

>Naming conventions must remain consistent across model, glossary, and data model.

>This US is iterative; the model may be revised after feedback from Sprint 2 functionality.