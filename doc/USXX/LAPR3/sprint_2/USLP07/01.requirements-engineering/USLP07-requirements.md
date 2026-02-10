# USLP07 - Dispatch a list of trains

## 1. Requirements Engineering

### 1.1. User Story Description

>As a Freight Manager,
>I want to use a scheduler that allows me to dispatch a list of trains,
>So that I can manage train departures, define their routes, and ensure efficient and safe railway operations, even when the automatic system is not available.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

-Scenario 1 – Scheduling trains with defined routes and departure times
Scheduler must associate each train with its defined route and departure time, displaying them in the dispatch plan.

- Scenario 2 – Manual route definition
Scheduler must allow manual entry of routes, validate feasibility, and record them in the plan when automation is unavailable.

- Scenario 3 – Speed calculation based on train and track parameters
Scheduler must calculate travel speed based on locomotive power, wagon weights, and track limits, using it to estimate travel time.

- Scenario 4 – Train crossings on single lines
Scheduler must identify crossing points and ensure that no two trains occupy the same single-line segment simultaneously.

- Scenario 5 – Estimation of passage times and crossing operations
Scheduler must display estimated passage and arrival times and planned crossings for all trains.

### 1.4 Found out Dependencies

| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USLP05** | Updated conceptual domain model | Scheduler uses model entities: Train, Route, Line Segment |
| **USLP06** | Updated glossary | Terminology consistency (e.g., route vs path, freight vs train) |
| **USBD21** | Updated database model | Scheduling logic depends on accurate relational data |

### 1.5 Input and Output Data
>**Input**
>
**Input**
- Train composition and freight details
- Line segment characteristics (distance, max speed, track type)
- Manually defined route provided by Freight Manager
- Scheduling date/time

**Output**
- Train Dispatch Schedule (timetable)
  - Stored in project repository and available in UI
- Route crossing report (if applicable)

### 1.6 Other Relevant Remarks
- At this stage, the scheduler does **not** optimize the route automatically.
- Route selection is **manual**, supporting scenarios where the automatic dispatcher is unavailable.
- The generated schedule will serve as the reference for the automatic dispatcher implemented in future sprints.
