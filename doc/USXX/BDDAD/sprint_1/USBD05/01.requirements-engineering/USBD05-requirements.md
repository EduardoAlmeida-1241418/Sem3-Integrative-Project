
# USBD05 - List All Line Segments Connecting Two Endpoints

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Planner, **I want** a list of all line segments directly connecting two endpoints, **So that** I can identify direct connections in the railway network and estimate travel times more accurately.

### 1.2 Acceptance Criteria

- Scenario 1 – Listing all direct line segments between two endpoints

  > **Given** that the database has been populated with all required tables and sample data from USBD04,
  >
  > **And** the query logic for identifying direct connections has been defined,
  >
  > **When** the Planner requests a list of line segments connecting two specific stations,
  >
  > **Then** the system must return all relevant line segments that directly connect the two given endpoints,
  >
  > **Then** the SQL query script must be stored in the `sql-scripts` directory of the repository,
  >
  > **So that** the functionality can be reused and verified consistently.

### 1.3 Found out Dependencies
>This User Story depends on USBD04.

### 1.4 Input and Output Data
>**Input**
>
>- Station identifiers (origin and destination).
>
>**Output**
>
>- SQL result set listing all segments with IDs and attributes.

### 1.5 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Must handle both single-track and double-track lines.
>
>**(ii) How often this US is held:**
>
>- Frequently used for testing and analysis.  
