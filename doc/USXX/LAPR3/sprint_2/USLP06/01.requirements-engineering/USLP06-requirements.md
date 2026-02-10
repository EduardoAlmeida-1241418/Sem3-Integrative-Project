# USLP06 - Update data dictionary/glossary

## 1. Requirements Engineering

### 1.1. User Story Description

>As a Product Owner,
>I want the data dictionary / glossary to be updated according to the changed requirements,
> So that all project stakeholders share a clear, consistent, and up-to-date understanding of the system’s data elements and terminology within the Railway Station Management System.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Data dictionary updated successfully

  - **Given** the existence of an existing data dictionary or glossary,  
    and newly approved **changes to requirements** that introduce or modify data elements, attributes, or terminology,

  - **When** the data dictionary/glossary is **revised** to incorporate these changes,

  - **Then** all affected entries must be **updated**, **added**, or **removed** as appropriate,  
    and the dictionary must remain **consistent** with the conceptual, logical, and relational models.

- Scenario 2 – Alignment with system models

  - **Given** the **updated conceptual and logical models**,

  - **When** the data dictionary is updated,

  - **Then** every entity, attribute, and relationship name must **match exactly** the names used in the models,  
    ensuring **terminological consistency** across the documentation and database schema.


- Scenario 3 – Clarity and completeness

  - **Given** the updated data dictionary/glossary,

  - **When** it is **reviewed by the Product Owner and development team**,

  - **Then** all definitions must be **clear, unambiguous, and complete**,  
    including **term name**, **description**, **data type**, **domain/range**, and **relationships**,  
    and the glossary must be **approved** as an official reference document.

### 1.4 Found out Dependencies
| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USLP05** | Updated conceptual domain model | Glossary must be synchronized with the final conceptual model |
| **USBD21** | Updated relational/logical model | Naming and terminology must match database and conceptual definitions |
| **USLP04** | Initial glossary | This US updates and expands the glossary based on new requirements |

### 1.5 Input and Output Data
>**Input**
>
>- Updated conceptual domain model (from USLP05)
>- Updated logical relational model (from USBD21)
>- Revised project domain requirements.
>
>**Output**
>- **Updated data dictionary / glossary document**, alphabetically organized
>- Stored in the repository under `/doc/global-artifacts/01.requirements/svg`.

### 1.6 Other Relevant Remarks
>- The glossary must remain a **living document**, progressively updated if future sprints introduce new business concepts.
>- Definitions should avoid redundancy and maintain consistent terminology across:
>  - UI wording
>  - Documentation
>  - Database table names
>  - Code naming conventions