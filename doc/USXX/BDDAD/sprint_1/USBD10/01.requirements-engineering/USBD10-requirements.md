# USBD10 - List All Wagons Of A Given Type

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Planner, **I want** to view a list of all wagons of a given type, **So that** I can efficiently plan train composition, allocate rolling stock appropriately, and ensure smooth freight operations across the railway network.

### 1.2 Acceptance Criteria

-  **Scenario 1 – Viewing wagons by type**

   > **Given** that the wagon types are defined in the data model (USBD02),
   >
   > **And** the relevant data has been loaded into Oracle LiveSQL,
   >
   > **And** a query plan has been drafted and validated with sample values,
   >
   > **When** the Planner requests a list of wagons of a specific type,
   >
   > **Then** the system must return all wagons filtered according to the requested type (e.g., boxcar, flatcar, tank car, refrigerated),
   >
   > **Then** the SQL query must be verified with at least two test cases,
   >
   > **Then** the query script should be stored in the `sql-scripts` directory of the repository.

### 1.3 Found out Dependencies
>This User Story depends on USBD04 (Database Populated).

### 1.4 Input and Output Data
>**Input**
>
>- Wagon type (e.g., boxcar, flatcar).
>
>**Output**
>
>- List of wagons matching type criteria.

### 1.5 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Query must be case-insensitive.
>
>**(ii) How often this US is held:**
>
>- Executed frequently for operational reporting. 