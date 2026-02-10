# USEI09 - Proximity Search

## 1. Requirements Engineering

### 1.1. User Story Description

>As an Analyst,
>I want to find the nearest N stations to a given (lat, lon) using the 2D-tree, with optional filters,
>So that I can obtain accurate nearby results efficiently for operational and planning tasks.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Successful nearest-N search (no filters)
  - Given a valid **query point** `(lat*, lon*)`, a **positive integer** `N ≥ 1`, and a populated **2D-tree**,
  - When I request the **nearest N stations**,
  - Then the system **must use Haversine distance (km)** with a fixed Earth radius (e.g., `R = 6371.0 km`) to rank results,  
    and **return exactly N stations** (or fewer if fewer exist), ordered by **distance asc, stationName asc** for ties.

- Scenario 2 – Time zone filter
  - Given optional **time zone criteria** (e.g., `timeZoneGroup ∈ { "CET", "WET/GMT" }` or a single group),
  - When I run the nearest-N search,
  - Then the system **must restrict candidates** to stations whose **timeZoneGroup** matches the filter set,  
    and **return the N nearest** among those matches, ordered as specified.

- Scenario 3 – KD-tree pruning and correctness
  - Given the 2D-tree supports **nearest-neighbour backtracking with pruning**,
  - When the search executes,
  - Then the algorithm **must prune subtrees** whose bounding rectangles cannot contain a closer point than the **current worst** of the best-N heap,  
    while still **guaranteeing exact results**.

- Scenario 4 – Identical coordinates / duplicates
  - Given multiple stations sharing the **exact same coordinates**,
  - When these are among the nearest results,
  - Then **all such stations** must be eligible and **ordered by stationName asc** after distance tie.

- Scenario 5 – Input validation
  - Given invalid inputs (e.g., `N ≤ 0`, `lat* ∉ [-90,90]`, `lon* ∉ [-180,180]`, malformed time zone values),
  - When the search is requested,
  - Then the system **must reject the request**, **return an empty list**, and provide a **validation error message**.

- Scenario 6 – Determinism & reproducibility
  - Given the **same dataset**, query point, `N`, and filter set,
  - When the search is repeated,
  - Then the **results (content and order)** **must be identical** across runs.

- Scenario 7 – Partial results
  - Given a filter set that yields **fewer than N** matching stations,
  - When the search runs,
  - Then the system **must return all available matches** (size `< N`) and **no error**.

- Scenario 8 – Pagination (optional)
  - Given large `N` and pagination parameters,
  - When I request results by page,
  - Then the system **must return a stable page** with deterministic ordering and expose **total count** or a continuation token.

### 1.4 Found out Dependencies

| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USEI07** | Balanced KD-tree constructed | This feature must reuse that tree |
| Haversine distance utility | Function to compute geodesic distance | Required to rank nearest stations |

### 1.5 Input and Output Data

**Input**
- Balanced KD-tree of stations
- `targetLatitude`, `targetLongitude`
- `N` (number of nearest stations to return)
- Optional filter: `timeZoneGroup` (`string` or `"all"`)

**Output**
- Ordered list of the N closest stations, each containing:
  - `stationName`
  - `country`
  - `latitude`, `longitude`
  - `distanceKm`

### 1.6 Other Relevant Remarks
- Multiple stations may share the same coordinates; they must be treated individually and ordered alphabetically.
- Floating-point rounding errors must not significantly affect ranking.