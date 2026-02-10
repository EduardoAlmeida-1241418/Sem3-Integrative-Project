
# USBD04 - Insert Provided Data Into The New Database

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Product Owner, **I want** the provided data to be inserted into the new database generated in USBD03, **So that** the system contains all necessary initial data for managing railway operations.

### 1.2 Acceptance Criteria

 - Scenario 1 – Insertion of provided data into the database

     > **Given** that the database structure created in USBD03 is deployed and accessible,  
     >
     > **And** the input dataset is available and has been validated,  
     >
     > **When** the team executes the data insertion process,  
     >
     > **Then** all provided data must be successfully imported into the database without errors,  
     >
     > **Then** referential integrity between all entities must be maintained and verified,  
     >
     > **Then** the SQL insertion script along with the validation results must be stored in the repository,  
     >
     > **So that** the system contains all the necessary initial data to support future queries and functionalities, including those defined in USBD05, USBD10, and USBD16.

### 1.3 Found out Dependencies
>This User Story depends on USBD03 (Database Instantiation).

### 1.4 Input and Output Data
>**Input**
>
>- Provided dataset in CSV or SQL insert format.
>
>**Output**
>
>- Populated database instance validated through sample queries.

### 1.5 Other Relevant Remarks
>**(i) How often this US is held:**
>
>- Performed once after database creation, updated if new data sources appear.  