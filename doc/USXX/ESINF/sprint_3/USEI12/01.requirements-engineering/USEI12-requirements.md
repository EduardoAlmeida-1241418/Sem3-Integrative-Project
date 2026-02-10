# USEI12 - Minimal Backbone Network

## 1. Requirements Engineering

### 1.1. User Story Description

> **As an Infrastructure Planner**  
> **I want to compute a minimum backbone network over the rail network (treated as undirected)**  
> **So that I can identify a minimal set of tracks that connects all reachable stations with the minimum possible total track length.**

> **Context:** To estimate a cost baseline for maintenance or expansion, a minimal track subset is needed that still connects all reachable stations.

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

#### Scenario 1 – Computing the Minimal Backbone Network
- **Given** an undirected graph of stations and tracks with lengths,
- **When** the system computes the Minimal Backbone Network,
- **Then** it must:
  - generate a connected subgraph including all reachable stations,
  - minimise the total sum of edge lengths,
  - ensure no redundant edges exist (i.e., no removable cycles).

---

#### Scenario 2 – Representing the Minimal Backbone Network as a graph structure
- **Given** the Minimal Backbone Network is computed,
- **When** the result is returned programmatically,
- **Then** it must provide:
  - the set of vertices (stations),
  - the set of edges (tracks) in the backbone only,
  - lengths/weights for each selected edge.

---

#### Scenario 3 – Generating a DOT file for visualisation
- **Given** the Minimal Backbone Network is available,
- **When** a DOT file is generated,
- **Then** it must:
  - include all stations as vertices,
  - include selected tracks as edges,
  - position vertices using XY coordinates,
  - produce a layout visually similar to the Belgium train global map.

---

#### Scenario 4 – Producing an SVG via Graphviz `neato`
- **Given** a valid DOT file,
- **When** `neato` is used,
- **Then** the SVG must:
  - display the Minimal Backbone Network with correct XY station positions,
  - be suitable for visual inspection.

---

#### Scenario 5 – Reporting temporal analysis complexity
- **Given** the algorithm has executed,
- **When** results are returned,
- **Then** the system must:
  - report the time-complexity class (e.g., O(|E| log |V|)),
  - indicate scalability for large graphs.

---

#### Scenario 6 – Handling disconnected or partially connected networks
- **Given** the input network may have disconnected components,
- **When** computing the Minimal Backbone Network,
- **Then** the system must:
  - compute a minimal backbone per connected component,
  - indicate unreachable stations,
  - ensure each component’s backbone is minimal.

---

### 1.4. Found Dependencies

- Input format for undirected rail network (stations, edges, lengths).
- Source and format for XY coordinates.
- DOT file format specification.
- Performance constraints (max |V| and |E|).
- Example datasets for testing.
- Visual appearance expectations for SVG output.

---

### 1.5. Input and Output Data

**Input**
- Stations and undirected tracks with lengths.
- XY coordinates for stations.

**Output**
- Minimal Backbone Network as a graph structure (vertices, selected edges, lengths).
- DOT file for visualization.
- SVG file via Graphviz `neato`.
- Time-complexity report.

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Handle disconnected networks.
  - Produce DOT and SVG files with correct XY coordinates.
  - Report algorithm complexity.

- **Execution frequency**
  - Each time the Infrastructure Planner computes a minimal backbone network.
