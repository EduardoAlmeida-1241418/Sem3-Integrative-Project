# USBD27 - List of grain wagons

## 1. Requirements Engineering

### 1.1. User Story Description

> As a Planner,
>I want a list of grain wagons that were used in every train that used grain wagons within a given period,
>So that I can identify the common wagon set across all relevant services for compliance, maintenance planning, and utilization analysis.

### 1.2 Acceptance Criteria

- Scenario 1 – Successful retrieval of common grain wagons
  - Given a **valid date/time period** (start and end) and a system where **train consists** and **wagon types** are correctly recorded,
  - And given that within this period there are **one or more trains** that include **grain wagons**,
  - When the planner requests the list of **grain wagons used by every such train**,
  - Then the system **must return the intersection** of wagon identifiers across all trains that used grain wagons in that period,  
    each item including at least: **wagonId**, **wagonType**, **capacity**, **owner/operator**, and **status (active/maintenance/retired)**,  
    and **exclude duplicates**.

- Scenario 2 – No trains with grain wagons in the period
  - Given a **valid period** where **no trains** within that period used grain wagons,
  - When the planner requests the list,
  - Then the system **must return an empty list**  
    and **display a message** such as *“No trains with grain wagons found in the selected period.”*

- Scenario 3 – Single train with grain wagons
  - Given a **valid period** where **exactly one train** used grain wagons,
  - When the list is requested,
  - Then the system **must return all grain wagons used by that train** (since they are trivially used by every such train).

- Scenario 4 – Invalid period
  - Given a **start time after the end time**, or an **unparseable date/time**,
  - When the planner requests the list,
  - Then the system **must reject the request**,  
    **return an empty list**,  
    and **display an error** such as *“Invalid period. Please verify start and end timestamps.”*

- Scenario 5 – Wagon lifecycle within the period
  - Given wagons that **changed status** (e.g., retired/entered service) within the period,
  - When the list is generated,
  - Then the system **must include a wagon** in the result **only if it appears in every train** that used grain wagons in that period **during their respective departure times**,  
    and **must not exclude** a wagon merely because it was **inactive outside** the trains’ actual usage times.

- Scenario 6 – Direction, service, and filter options
  - Given optional filters such as **direction**, **service class/commodity code**, or **operator**,
  - When the planner applies these filters,
  - Then the system **must compute the intersection** only over **trains matching the filters**,  
    and **return the wagon list accordingly**.

- Scenario 7 – Data quality/inconsistency
  - Given **incomplete or inconsistent consist data** (e.g., missing wagon IDs, ambiguous wagon type, duplicated records for a train),
  - When the list is requested,
  - Then the system **must detect and log the inconsistency**,  
    **exclude invalid records from the intersection**,  
    and **notify the user** with *“Some records were inconsistent and were ignored. Results may be partial.”*

### 1.3 Found out Dependencies
| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USBD21** | Logical / relational model | Physical implementation follows logical design |
| **USLP05** | Conceptual domain model | Ensures entities and relationships are correctly represented |
| **USLP06** | Glossary | Ensures consistent naming of tables and columns |

### 1.4 Input and Output Data

**Input**
- Logical model of Train Composition (USBD21)
- Glossary of terms (USLP06)

**Output**
- SQL scripts to create `Train`, `TrainLocomotive`, and `TrainWagon` tables with primary and foreign keys
- Populated test data (optional) to validate integrity
- Stored in `/sql-scripts`

### 1.5. Other Relevant Remarks

- The schema must enforce at least **one locomotive per train**.
- Duplicate locomotive or wagon entries for a train should be prevented.
- Column data types should align with previous rolling stock tables (`Locomotive`, `Wagon`) for consistency.
- Ensure future updates to train composition can be handled without violating referential integrity.