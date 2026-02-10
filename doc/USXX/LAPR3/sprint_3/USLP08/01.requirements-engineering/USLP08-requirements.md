# USLP08 - Define and Assign Routes for Pending Freights

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Freight Manager**  
> **I want to use a route planner that allows me to define and assign simple or complex routes**  
> **So that I can efficiently fulfil and dispatch all pending freights.**

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

#### Scenario 1 – Presenting the sequence of stations and cargo operations
- **Given** the Freight Manager is creating or reviewing a route for a pending freight,
- **When** the route details are displayed,
- **Then** the system must:
  - present a list containing the ordered sequence of stations,
  - and for each station, show the cargo operations to be performed (load and/or unload).

---

#### Scenario 2 – Creating a simple route
- **Given** a freight with a known origin and destination,
- **When** the Freight Manager defines a direct connection between these stations,
- **Then** the system must:
  - save the route as a simple route,
  - associate it with the pending freight.

---

#### Scenario 3 – Creating a complex route
- **Given** that intermediate stops are necessary for cargo handling or network constraints,
- **When** the Freight Manager adds multiple stations forming a multi-step sequence,
- **Then** the system must:
  - save the route as a complex route,
  - display the full ordered list of stations with all associated load/unload operations.

---

### 1.4. Found Dependencies

- List of pending freights must be accessible.
- Cargo operations for each freight must be defined.
- Route structure and classification rules (simple vs complex) must be documented.
- Rules for station sequencing and cargo handling must be available.
- User interface for the Route Planner must be defined and approved.

---

### 1.5. Input and Output Data

**Input**
- Freight details:
  - Origin station
  - Destination station
  - Intermediate stations (if required)
  - Required cargo operations (load/unload)

**Output**
- Saved route assigned to the freight:
  - Ordered sequence of stations
  - Associated cargo operations at each station
- Visual representation in the Route Planner interface

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - The system must distinguish between simple and complex routes correctly.
  - The Route Planner must allow adding, modifying, or reviewing all routes for pending freights.
  - Display of routes must clearly indicate all stations and cargo actions.

- **Execution frequency**
  - Each time a Freight Manager defines or updates a route for any pending freight.
