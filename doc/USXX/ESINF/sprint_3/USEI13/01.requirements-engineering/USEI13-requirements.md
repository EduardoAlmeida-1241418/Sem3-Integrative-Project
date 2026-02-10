# USEI13 - Rail Hub Centrality Analysis

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Maintenance Planner**  
> **I want to compute centrality measures for each station (betweenness, harmonic closeness, and strength/degree) and combine them into a composite HubScore**  
> **So that I can rank and export the network’s hubs for informed decision-making about capacity, robustness, and upgrade priorities.**

> **Context:** Hub stations concentrate transfers, connect regions, and act as chokepoints. Identifying hubs quantitatively supports capacity planning, robustness analysis, and upgrade prioritisation.
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

#### Scenario 1 – Computing basic connectivity metrics
- **Given** a graph of stations and tracks,
- **When** the system analyses the network,
- **Then** compute for each station:
  - degree (number of direct neighbours)
  - strength (sum of incident edge weights)

---

#### Scenario 2 – Computing betweenness centrality
- **Given** the station graph,
- **When** computing betweenness centrality,
- **Then** for each station:
  - consider all shortest paths
  - count occurrences on paths (excluding endpoints if defined)
  - produce betweenness values
  - normalise to [0,1] per connected component

---

#### Scenario 3 – Computing harmonic closeness
- **Given** the station graph,
- **When** computing harmonic closeness,
- **Then** for each station:
  - sum reciprocals of shortest-path distances to reachable stations
  - handle unreachable stations (contribute zero)
  - normalise to [0,1] per component

---

#### Scenario 4 – Normalising strength
- **Given** computed strength values,
- **When** normalising,
- **Then** compute strengthNorm in [0,1] within each component, proportional to higher strength

---

#### Scenario 5 – Computing composite HubScore
- **Given** betweenness, harmonic_closeness, strengthNorm,
- **When** combining them,
- **Then** compute:  
  hubscore = 0.35 × betweenness + 0.35 × harmonic_closeness + 0.30 × strengthNorm  
  ensuring hubscore ∈ [0,1]

---

#### Scenario 6 – Ranking and exporting hubs
- **Given** HubScore and metrics,
- **When** exporting,
- **Then** provide for each station: stid, stname, degree, strength, betweenness, harmonic_closeness, hubscore, ranked by hubscore descending

---

#### Scenario 7 – Reporting temporal analysis complexity
- **Given** computations are complete,
- **When** returning results,
- **Then** report time-complexity class in terms of |V| stations and |E| edges for scalability understanding

---

#### Scenario 8 – Handling disconnected components
- **Given** the network may have multiple disconnected components,
- **When** computing and normalising centrality,
- **Then** normalise within each component separately and provide a global export

---

### 1.4. Found Dependencies

- Representation of the rail network graph (stations, edges, weights)
- Edge weights for shortest paths and strength
- Normalisation method for [0,1] per component
- Output format specification for stid, stname, and metrics
- Test datasets with known centrality values

---

### 1.5. Input and Output Data

**Input**
- Stations, edges, edge weights

**Output**
- Per station: stid, stname, degree, strength, betweenness, harmonic_closeness, hubscore
- Exportable list ranked by hubscore
- Time-complexity information

---

### 1.6. Other Relevant Remarks

- Handle multiple connected components independently
- Apply normalisation consistently per component
- Include automated tests for simple, multi-component, and edge-case networks
