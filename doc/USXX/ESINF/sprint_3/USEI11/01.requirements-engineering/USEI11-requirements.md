# USEI11 - Directed Line Upgrade Plan

## 1. Requirements Engineering

### 1.1. User Story Description

> **As an Infrastructure Planner**  
> **I want to compute an ordering of Belgian stations based on directed track dependencies**  
> **So that I can schedule station upgrades in an order that respects all directional dependencies and identify cycles when such an order is not possible.**

> **Context:** Each directed edge A → B means:  
> “Before station **B** can be fully upgraded, all its incoming neighbours (like A) must be upgraded first.”

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:**  
> – 
>
> **Answer:**  
> – 

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Producing a valid upgrade order for an acyclic graph
- **Given** a directed graph of stations with edges A → B representing upgrade dependencies,
- **When** the system detects no cycles,
- **Then** it must:
  - produce a valid upgrade order (list of stations),
  - ensure for each edge A → B that A appears before B,
  - return this list as the station upgrade schedule.

---

#### Scenario 2 – Detecting cycles and reporting involved stations
- **Given** a directed graph of stations,
- **When** the system detects one or more cycles,
- **Then** it must:
  - identify at least one cycle,
  - return the set of stations involved in cycles,
  - include the directed edges forming those cycles,
  - allow the planner to review and resolve circular dependencies.

---

#### Scenario 3 – Handling multiple components and partial orderings
- **Given** a directed graph with potentially disconnected components,
- **When** the system computes the upgrade order,
- **Then** it must:
  - handle each component correctly,
  - return a single combined ordering respecting all dependencies,
  - allow stations without incoming dependencies to appear anywhere in the order as long as constraints are satisfied.

---

#### Scenario 4 – Reporting temporal analysis complexity
- **Given** the system has completed graph analysis,
- **When** results are returned,
- **Then** the system must:
  - report the time-complexity class of the algorithm (e.g., O(|V| + |E|)),
  - indicate that the analysis is linear with respect to the graph size.

---

#### Scenario 5 – Validating input data
- **Given** a set of stations and directed edges,
- **When** the system validates the input,
- **Then** it must:
  - ensure all edges reference existing stations,
  - flag invalid or duplicate edges,
  - provide meaningful error messages if data is incomplete or inconsistent.

---

### 1.4. Found Dependencies

- Representation format for directed graph (stations + edges).
- Source of Belgian rail network data (or test subset).
- Definition of a cycle in station dependencies.
- Non-functional constraints (max stations/edges).
- Test cases for acyclic and cyclic graphs.

---

### 1.5. Input and Output Data

**Input**
- Set of stations.
- Directed edges A → B representing upgrade dependencies.

**Output**
- Ordered list of stations for upgrades (if acyclic).
- Cycle report (stations + edges) if cycles exist.
- Time-complexity information.

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Detect cycles and report stations/edges involved.
  - Handle multiple disconnected components.
  - Output must include algorithmic complexity.
  - Input validation with clear error messages.

- **Execution frequency**
  - Each time the Infrastructure Planner computes a station upgrade plan.
