# USBD23 - List of all line segments of a planned train route.

## 1. Requirements Engineering

### 1.1. User Story Description

>As a Planner,
>I want to retrieve and visualize a list of all line segments that compose a planned train route,
>So that I can analyze the route structure, verify connectivity between stations, and ensure the feasibility of the planned journey.

### 1.2 Acceptance Criteria

- Scenario 1 – Successful retrieval of route segments

  - Given a **valid planned route** with **defined start and end stations**,  
    and all **intermediate line segments** correctly stored in the system database,

  - When the planner requests the list of line segments for that route,

  - Then the system **must return a complete ordered list of all line segments**,  
    each containing:
    - the **starting station**,
    - the **ending station**,
    - the **segment length**,
    - and the **track type** (single/double, electrified/non-electrified),  
      and **display or return them in sequential order** from origin to destination.

- Scenario 2 – Route not found

  - Given an **invalid route ID** or a **route that does not exist in the system**,

  - When the planner requests the list of line segments,

  - Then the system **must return an empty list**,  
    and **display an appropriate error message** such as *“No planned route found with the provided identifier.”*

- Scenario 3 – Route without defined segments

  - Given a **valid route record** that **does not yet have any line segments assigned**,

  - When the planner requests the segment list,

  - Then the system **must return an empty list**,  
    and **display a warning message** such as *“This route has no line segments defined yet.”*

- Scenario 4 – Data inconsistency

  - Given a **route** with **incomplete or inconsistent data** (e.g., missing segment endpoints or duplicated segments),

  - When the planner requests the list of line segments,

  - Then the system **must detect the inconsistency**,  
    **log the error**,  
    and **notify the user** that *“The route data is inconsistent and needs verification.”*

### 1.3 Found out Dependencies
| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USBD21** | Logical / relational model | Schema implementation is based on the logical design |
| **USLP05** | Conceptual domain model | Ensures entities and relationships are consistent |
| **USLP06** | Glossary | Ensures consistent naming of tables and columns |

### 1.4 Input and Output Data
**Input**
- Updated logical model (USBD21)
- Glossary of terms (USLP06)

**Output**
- SQL scripts to create tables, constraints, and indices
- Implemented database schema in RDBMS
- Stored in `/doc/global-artifacts/02.database-design/physical-schema/`

### 1.5. Other Relevant Remarks

- Ensure column naming follows consistent conventions.
- Prepare for potential **future migration scripts** for schema evolution.
- Document any assumptions made during implementation (e.g., default values, handling nulls, boolean flags).
