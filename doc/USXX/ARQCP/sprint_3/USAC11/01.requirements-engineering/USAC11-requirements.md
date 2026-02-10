# USAC11 - Initial Setup of Data Structures from a Text File

## 1. Requirements Engineering

### 1.1. User Story Description

> **As an Administrator**  
> **I want to perform the initial setup of all system data structures using a text file**  
> **So that the Station Management System can be correctly initialised with users, tracks, trains, sensors, and other necessary information before normal operation begins.**

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

#### Scenario 1 – Successful loading of a valid text file
- **Given** a valid text file containing correctly formatted data for all entities  
  (Users, Tracks, Trains, Sensors, Logging, etc.),  
  and the Manager component is ready to initialise its structures,
- **When** the system reads and parses the file,
- **Then** all data structures must be correctly created, dynamically allocated,  
  and populated with the values taken from the file.

---

#### Scenario 2 – Handling malformed or incomplete data
- **Given** a file containing formatting errors, missing fields, or inconsistent values,
- **When** the system attempts to parse the file,
- **Then** it must:
  - detect invalid entries,
  - ignore or reject only those specific records,
  - continue initialising valid structures without crashing.

---

#### Scenario 3 – Memory allocation failures
- **Given** that all arrays and structures must be dynamically allocated,
- **When** a memory allocation fails during setup,
- **Then** the system must:
  - abort initialisation safely,
  - release any previously allocated memory,
  - report an error state so the Administrator is informed.

---

#### Scenario 4 – Use of Assembly functions from Sprint 2
- **Given** that certain operations were implemented in Assembly in Sprint 2,
- **When** the Manager loads and processes file data,
- **Then** it must invoke the relevant Assembly functions exactly as required,  
  following the RV32IM calling conventions.

---

#### Scenario 5 – Integration with Manager workflow
- **Given** that the Manager is the central controller of the Station Management System,
- **When** the initialisation completes successfully,
- **Then** the Manager must be able to:
  - process instructions from the UI,
  - send data to the Board,
  - issue commands to LightSigns,
  - interact correctly with all loaded entities.

---

### 1.4. Found Dependencies

- The full specification of the text file format (structure, fields, separators).
- Definition of all data structures used by the Manager (Users, Tracks, Trains, Sensors, Logging, etc.).
- Dynamic allocation rules and memory constraints.
- Assembly functions implemented in Sprint 2 that must be reused.
- Manager workflow and integration with UI, Board, Sensors, LightSigns, etc.

---

### 1.5. Input and Output Data

**Input**
- Text file including:
  - User records
  - Track definitions
  - Train information
  - Sensor configurations
  - Logging/monitoring parameters
  - Any additional required system entities

**Output**
- Dynamically allocated and populated structures representing:
  - Users
  - Tracks
  - Trains
  - Sensors
  - Logging structures
- Error reports when invalid or malformed data is detected
- A final initialisation state (success or failure)

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - All structures must be dynamically allocated.
  - Invalid entries must not block the setup of other components.
  - Memory leaks must be avoided by freeing partially allocated structures on failure.
  - Assembly functions from Sprint 2 must be correctly integrated where required.

- **Execution frequency**
  - This initialisation procedure is executed **once at system startup**,  
    or whenever the Administrator performs a full reload of system data.
