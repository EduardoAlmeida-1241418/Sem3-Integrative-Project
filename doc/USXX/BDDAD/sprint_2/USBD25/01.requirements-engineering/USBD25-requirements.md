# USBD25 - List of all endpoints of a planned train route.

## 1. Requirements Engineering

### 1.1. User Story Description

> As a Planner,
>I want to retrieve a list of all endpoints (stations) along a planned train route where the train does not stop,
>So that I can identify pass-through stations, validate service patterns, and adjust the timetable or stops as needed.

### 1.2 Acceptance Criteria

- Scenario 1 – Successful retrieval of non-stop endpoints

  - Given a **valid planned route** with a defined **ordered sequence of stations** and a **timetable** indicating which stations are stops vs. pass-through,
  - When the planner requests the list of **non-stop endpoints** for that route,
  - Then the system **must return an ordered list** (origin → destination) containing only the stations where the train **does not stop**,  
    each item including:
    - **stationId** and **stationName**,
    - **sequenceIndex** in the route,
    - **planned pass-through time** (or time window),
    - **reason/non-stop flag** (e.g., “express pass-through”),  
      and **exclude** all stations where the train is scheduled to stop.

- Scenario 2 – Route not found

  - Given an **invalid route ID** or a route that **does not exist**,
  - When the planner requests the list of non-stop endpoints,
  - Then the system **must return an empty list**  
    and **display an error message** such as *“No planned route found with the provided identifier.”*

- Scenario 3 – No non-stop endpoints (train stops everywhere)

  - Given a **valid planned route** where the train **stops at all stations**,
  - When the planner requests the list of non-stop endpoints,
  - Then the system **must return an empty list**  
    and **display a message** such as *“This route has no pass-through stations.”*

- Scenario 4 – Filtering by direction or service variant

  - Given a **valid planned route** that has multiple **directions** (e.g., outbound/inbound) or **service variants** (e.g., weekday/holiday),
  - When the planner requests non-stop endpoints with a specified **direction** and/or **service variant**,
  - Then the system **must return only the non-stop endpoints** that match the **selected direction/variant**, in correct order.

- Scenario 5 – Time-window filtering

  - Given a **valid planned route** with a timetable,
  - When the planner requests non-stop endpoints **within a specified time window** (e.g., 08:00–10:00),
  - Then the system **must return only the pass-through stations** whose pass-through times fall within the **requested window**.

- Scenario 6 – Data inconsistency

  - Given a route with **incomplete or inconsistent data** (e.g., missing stop flags, out-of-order sequence indices, or missing timestamps),
  - When the planner requests non-stop endpoints,
  - Then the system **must detect the inconsistency**, **log the issue**,  
    and **notify the user** with a message such as *“Route data is inconsistent and needs verification.”*

### 1.3 Found out Dependencies

| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USBD21** | Logical / relational model | Physical implementation follows logical design |
| **USLP05** | Conceptual domain model | Ensures entities and relationships are correctly modeled |
| **USLP06** | Glossary | Maintains naming consistency |


### 1.4 Input and Output Data

**Input**
- Logical model of Freight and Routes (USBD21)
- Glossary of domain terms (USLP06)

**Output**
- SQL scripts to create `Freight`, `Route`, `RouteStation`, and `CrossingEvent` tables with constraints
- Populated test data (optional) to validate the schema
- Stored in `/sql-scripts`

### 1.5. Other Relevant Remarks

- Ensure **route station order** is strictly enforced by `order_index`.
- Support scenarios where multiple trains share segments at overlapping times, enabling accurate scheduling.
- Document assumptions about default values, nullability, and data types for operational tables.