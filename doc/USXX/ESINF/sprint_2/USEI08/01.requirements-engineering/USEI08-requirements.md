# USEI08 - Search by Geographical Area

## 1. Requirements Engineering

### 1.1. User Story Description

>As a Planner,
>I want to query the 2D-tree for all stations within a latitude/longitude range with optional filters,
>So that I can quickly extract relevant stations for operational decisions without scanning the whole dataset.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Successful region query (inclusive bounds)
  - Given a valid rectangular region with **lat ∈ [latMin, latMax]** and **lon ∈ [lonMin, lonMax]** (both inclusive),
  - When I query the 2D-tree without filters,
  - Then the system **must return all stations** whose coordinates fall **inside or on the boundary** of the rectangle,  
    ordered by **latitude asc, longitude asc, stationName asc**.

- Scenario 2 – Apply optional filters
  - Given a valid region and any combination of filters:
    - **isCity** ∈ {true, false} (optional)
    - **isMainStation** ∈ {true, false} (optional)
    - **country** ∈ {PT, ES, all} (optional; default = all)
  - When I run the query,
  - Then the system **must return only the stations** that satisfy **all provided filters**, preserving the specified ordering.

- Scenario 3 – Handling duplicates at identical coordinates
  - Given multiple stations with **identical (lat, lon)** within the region,
  - When I query the 2D-tree,
  - Then **all such stations** must be returned and **sorted by stationName asc** for that coordinate.

- Scenario 4 – Empty results
  - Given a valid region and filters that match no stations,
  - When I query the 2D-tree,
  - Then the system **must return an empty list** and **no error**.

- Scenario 5 – Invalid inputs
  - Given **latMin > latMax** or **lonMin > lonMax**, or unrecognized filter values,
  - When I query the 2D-tree,
  - Then the system **must reject the request**, **return an empty result**, and **report an input validation error**.

- Scenario 6 – KD-tree pruning efficiency
  - Given any valid region (small or large),
  - When the query is executed,
  - Then the system **must use KD-tree pruning** (via bounding rectangles) to avoid scanning subtrees that cannot intersect the region.

- Scenario 7 – Reproducibility and deterministic order
  - Given the same inputs and dataset,
  - When I repeat the query,
  - Then the **results (content and order)** **must be identical** across runs.

- Scenario 8 – Pagination 
  - Given a large result set and pagination parameters (offset/limit or page/size),
  - When I query the region,
  - Then the system **must return a stable, deterministically ordered page** and expose the **total count** or a continuation token.

### 1.4 Found out Dependencies

| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USEI07** | Balanced KD-tree must be already created | This query uses the KD-tree structure |
| Data validation from import | Ensures all coordinates are valid | Required for spatial correctness |

### 1.5 Input and Output Data

**Input**
- KD-tree containing the stations
- Query parameters:
  - `latMin`, `latMax`
  - `lonMin`, `lonMax`
  - Optional filters: `isCity`, `isMainStation`, `country`

**Output**
- A list of matching stations, each containing:
  - `stationName`
  - `country`
  - `latitude`
  - `longitude`
  - Flags (`isCity`, `isMainStation`)

- Results displayed or returned as a sorted list.

### 1.6 Other Relevant Remarks
- No backtracking to parent nodes is required after pruning decisions.
- Sorting **must not** depend on tree traversal order; final ordering happens after filtering.
- This functionality will support map visualization, logistics planning, and routing modules in later sprints.