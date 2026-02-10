# USLP10 - Schedule and Dispatch Trains with Defined Routes

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Traffic Manager**  
> **I want to use a scheduler that allows me to dispatch a list of trains, each with an associated simple or complex route**  
> **So that train movements can be coordinated efficiently and safely across the network.**

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:**  
> – 
>
> **Answer:**  
> –
>
> [Customer Clarification – Forum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Planning departure date and time
- **Given** a list of trains, each with an associated route,
- **When** the Traffic Manager prepares the dispatch plan,
- **Then** the system must allow setting a planned departure date and time for each train/route combination and store it in the schedule.

---

#### Scenario 2 – Manual path definition during dispatch
- **Given** automatic path calculation may not be available,
- **When** the Freight Manager manually defines the path,
- **Then** the system must allow manual selection of segments and intermediate points, validate them against the network, and save the path for the train.

---

#### Scenario 3 – Automatic path calculation
- **Given** a train with a valid origin, destination, and route,
- **When** the Traffic Manager requests automatic path calculation,
- **Then** the system must compute a valid path automatically and associate it with the train.

---

#### Scenario 4 – Calculating travel speed based on train and track parameters
- **Given** train composition (locomotives + wagons) and route track segments with speed limits,
- **When** the system calculates travelling speed,
- **Then** it must consider:
  - track limitations (max speed per segment),
  - combined locomotive power,
  - total wagon weight (loaded or empty),  
    and use these speeds in scheduling calculations.

---

#### Scenario 5 – Considering crossings on single lines
- **Given** single-line segments with multiple trains scheduled,
- **When** the scheduler generates or updates the dispatch plan,
- **Then** it must ensure:
  - no two trains occupy the same single-line segment simultaneously,
  - crossings occur at side lines or the station immediately before,
  - conflicts are resolved by adjusting train timings or waiting times.

---

#### Scenario 6 – Estimating passage times and crossing operations
- **Given** departure times, paths, speeds, and crossings are calculated,
- **When** the scheduler presents the dispatch plan,
- **Then** it must indicate:
  - estimated passage times at stations, junctions, and crossings,
  - necessary crossing operations (where and when trains wait or pass).

---

#### Scenario 7 – Accessing the database through PL/SQL functions
- **Given** all schedule, route, path, and train data is stored in a database,
- **When** the system retrieves or updates this data,
- **Then** all database access must be performed exclusively via PL/SQL functions for validation and integrity.

---

### 1.4. Found Dependencies

- List of trains and associated routes.
- Business rules for simple vs complex routes.
- Parameters for speed calculation: track limits, locomotive power, wagon weights.
- Rules for crossings on single-line tracks.
- Required PL/SQL functions for schedule, path, and train data access.
- Scheduler interface workflow approved by Product Owner.

---

### 1.5. Input and Output Data

**Input**
- Train list with associated routes.
- Train composition (locomotives + wagons).
- Optional manual path selections.
- Departure dates and times.

**Output**
- Scheduled trains with planned departure times.
- Assigned paths (manual or automatic).
- Calculated travel speeds for each segment.
- Estimated passage times and required crossings.
- Updated database records via PL/SQL functions.

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Manual and automatic path definition must be validated.
  - Speed calculations must account for train and track constraints.
  - Single-line track crossings must prevent conflicts.
  - All database operations must be executed via PL/SQL functions.

- **Execution frequency**
  - Each time a Traffic Manager schedules and dispatches trains along defined routes.
