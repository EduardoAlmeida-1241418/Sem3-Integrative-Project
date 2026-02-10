# USEI15 - Risk-Aware Shortest Paths

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Route Planner**  
> **I want to compute the shortest path between two stations using an edge cost that combines distance and penalties/bonuses (which may be negative)**  
> **So that I can propose robust routes and detect inconsistent configurations such as negative cycles.**

> **Context:** Some track connections are slower or less reliable due to works or constraints.  
> Each edge has a **cost** combining distance and penalty/bonus.  
> Negative costs require an algorithm **beyond Dijkstra**.

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries

> **Question:**
> 
> **Answer:**
> 
---

### 1.3. Acceptance Criteria

#### Scenario 1 – Computing a risk-aware shortest path (no negative cycles)
- **Given:** Directed graph with stations (stid) and edges with risk-aware costs
- **When:** Source and target stations are provided, and no negative cycles are reachable
- **Then:** Compute finite-cost shortest path and return in the specified cumulative-cost format.

---

#### Scenario 2 – Detecting and reporting negative cycles
- **Given:** Same network
- **When:** A negative cycle is reachable from the source
- **Then:** Stop computing a finite path, indicate the negative cycle, and return the set of stations and edges involved.

---

#### Scenario 3 – Handling the absence of any path
- **Given:** Source and target
- **When:** No path exists
- **Then:** Indicate no available route.

---

#### Scenario 4 – Validating edge costs and input parameters
- **Given:** Edge costs may be negative
- **When:** Validating input
- **Then:** Ensure all edges have numeric costs, source and target exist, and invalid data is reported clearly.

---

#### Scenario 5 – Reporting temporal analysis complexity
- **Given:** Computation executed
- **When:** Reporting results
- **Then:** Include the time-complexity class in terms of |V| and |E| for scalability assessment.

---

### 1.4. Dependencies
- Graph representation of the rail network (vertices, edges, costs)
- Algorithm for shortest path with negative edges (e.g., Bellman–Ford)
- Validation of station identifiers and edge costs
- Output format specification for paths and negative-cycle reports

---

### 1.5. Input and Output Data

**Input**
- Stations (stid)
- Edges with risk-aware costs (distance + penalties/bonuses, possibly negative)
- Source and target stid

**Output**
- Shortest path (if finite):
  - `depart_stid, cost, interm_stid1, cost, …, target_stid, total_cost_to_target`
- Negative cycle report:
  - stations and edges involved
- No-path indication if unreachable
- Optional: time-complexity info

---

### 1.6. Other Relevant Remarks
- Validate all input before computation
- Automated tests must cover:
  - positive-only cost graphs
  - negative edges without negative cycles
  - graphs with negative cycles
  - no-path scenarios
- Results must be interpretable for robust route planning and configuration verification
