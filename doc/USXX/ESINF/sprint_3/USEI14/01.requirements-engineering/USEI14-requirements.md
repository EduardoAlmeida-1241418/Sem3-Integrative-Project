# USEI14 - Maximum Throughput Between Two Hubs

## 1. Requirements Engineering

### 1.1. User Story Description

> **As an Operations Analyst**  
> **I want to compute the maximum flow between two selected stations (source and sink), given per-edge capacities**  
> **So that I can estimate the theoretical throughput of the corridor, validate schedules, identify bottlenecks, and support upgrade decisions.**

> **Context:** Hub effectiveness depends on connecting corridors. Limited track capacity requires understanding the maximum possible throughput, providing an upper bound on trains routed per day.

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:** 
> -
> **Answer:** 
> - 

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Computing the maximum flow
- **Given** a rail network graph with stations (vertices), tracks (edges), and per-edge capacities,
- **When** a source and sink hub are selected,
- **Then** compute the maximum flow respecting all capacities.

---

#### Scenario 2 – Returning a clear max-flow summary
- **Given** a computed maximum flow,
- **When** producing results,
- **Then** return a summary with:
  - source_stid
  - target_stid
  - maxFlowValue

---

#### Scenario 3 – Handling disconnected hubs or zero-flow cases
- **Given** source and sink are disconnected or paths have zero capacity,
- **When** computing maximum flow,
- **Then** return maxFlowValue = 0 with clear indication of no feasible flow, including source and sink identifiers.

---

#### Scenario 4 – Validating input capacities and hub selection
- **Given** user-provided network and capacities,
- **When** validating input,
- **Then** ensure:
  - all capacities are non-negative and defined
  - source and sink exist in the network
  - invalid or inconsistent data is flagged with clear error messages

---

#### Scenario 5 – Reporting temporal analysis complexity
- **Given** computation is complete,
- **When** reporting results,
- **Then** include time-complexity class (e.g., in terms of |V| vertices and |E| edges) to indicate scalability.

---

### 1.4. Found Dependencies

- Graph representation of rail network (stations, edges, capacities)
- Selection of max-flow algorithm and its computational properties
- Input and output format specifications for source, sink, and maxFlowValue
- Defined units for edge capacities

---

### 1.5. Input and Output Data

**Input**
- Stations (vertices)
- Tracks/corridors (edges) with capacities
- Source and sink station identifiers (stid)

**Output**
- Summary:
  - source_stid
  - target_stid
  - maxFlowValue
- Optional: time-complexity information

---

### 1.6. Other Relevant Remarks

- Disconnected networks must return maxFlowValue = 0
- Validate all input data before computation
- Automated tests must cover:
  - simple networks
  - multiple paths and bottlenecks
  - disconnected networks
  - edge cases (single edge, single node)
- Results must be interpretable for throughput analysis and decision-making
