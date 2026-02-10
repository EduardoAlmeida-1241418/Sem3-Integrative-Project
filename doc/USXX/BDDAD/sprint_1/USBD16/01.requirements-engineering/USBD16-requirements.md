# USBD16 - Get Minimum Length Of Double-Track Segments In A Line

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Planner, **I want** to know the minimum length of double-track segments in a given line, **So that** I can assess operational capacity, identify potential bottlenecks, and ensure efficient traffic management on the railway network.

### 1.2 Acceptance Criteria

- **Scenario 1 – Determining minimum length of double-track segments**

  > **Given** that the track segment table has been populated from USBD04,
  >
  > **And** the attribute representing track type (single or double) is defined,
  >
  > **When** the Planner requests the minimum length of double-track segments for a specific line,
  >
  > **Then** the system must return the smallest segment length of double-track type for the selected line,
  >
  > **Then** the SQL query must be validated with example data,
  >
  > **Then** the query script along with validation evidence must be stored in the repository.

### 1.3 Found out Dependencies
>This User Story depends on USBD04.

### 1.4 Input and Output Data
>**Input**
>
>- Line identifier.
>
>**Output**
>
>- Minimum double-track segment length (numeric value).

### 1.5 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Must handle NULL or missing segment lengths gracefully.
>
>**(ii) How often this US is held:**
>
>- Occasionally used for capacity analysis.  