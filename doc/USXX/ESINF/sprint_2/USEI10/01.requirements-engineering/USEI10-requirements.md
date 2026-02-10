# USEI10 - Radius search and density summary

## 1. Requirements Engineering

### 1.1. User Story Description

>As an Operations Planner,
>I want to fetch all stations within a radius R (km) of a target (lat, lon) using the 2D-tree, and get a summary by country and by isCity,
>So that I can assess local coverage quickly and accurately.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Successful radius search (no filters)
  - Given a valid **query point** `(lat*, lon*)` and a **radius** `R > 0` (in km), and a populated **2D-tree**,
  - When I perform a **radius search**,
  - Then the system **must compute Haversine distance (km)** (with a fixed Earth radius, e.g., `R⊕ = 6371.0 km`)  
    and **return all stations** with `distanceKm ≤ R`,  
    and **produce a results index** (BST/AVL) **sorted by distance ASC**, then **stationName DESC** for ties.

- Scenario 2 – Summary by country and by isCity
  - Given the result set from the radius search,
  - When the search completes,
  - Then the system **must compute** two summaries:
    1) **By country** → counts per `country`,
    2) **By isCity** → counts for `true` / `false`,  
       and **return both summaries** alongside the ordered results.

- Scenario 3 – Deterministic ordering & duplicates
  - Given multiple stations with **identical coordinates** or identical **distance** to `(lat*, lon*)`,
  - When they are included in the results,
  - Then ordering **must be deterministic**: **distance ASC**, then **stationName DESC** (strict tie-break),  
    and **all stations** at identical coordinates **must be included**.

- Scenario 4 – Input validation
  - Given invalid inputs (`R ≤ 0`, `lat* ∉ [-90,90]`, `lon* ∉ [-180,180]`),
  - When the search is requested,
  - Then the system **must reject the request**, **return an empty result**, and provide a **validation error message**.

- Scenario 5 – KD-tree pruning efficiency
  - Given the 2D-tree with bounding rectangles,
  - When the radius search runs,
  - Then it **must prune** subtrees whose minimum possible distance to `(lat*, lon*)` is **greater than R**, avoiding full scan.

- Scenario 6 – Optional pagination (if applicable)
  - Given a large result set and pagination parameters,
  - When the results are paginated,
  - Then each page **must respect the global order** (distance ASC, name DESC), be **stable**, and **expose total count** or a continuation token.

- Scenario 7 – Reproducibility
  - Given the same dataset, query point, and R,
  - When the search is repeated,
  - Then the **results (content and order)** and **summaries** **must be identical**.

### 1.4 Found out Dependencies

| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USEI07** | KD-Tree constructed | Radius search relies on spatial index |
| Distance calculation | Haversine formula utility | Required for accurate km distances |
| Optional plotting module | For visualization of density | Not mandatory, but useful for analysis |

### 1.5 Input and Output Data

**Input**
- KD-Tree of validated stations
- Center point coordinates (`latitude`, `longitude`)
- Radius `R` in kilometers
- Optional filters: `timeZoneGroup`, `isMainStation`, `isCity`

**Output**
- List of all stations within radius, including:
  - `stationName`, `country`, `latitude`, `longitude`, `distanceKm`
- Density analysis summary:
  - Total stations found
  - Average distance to center
  - Distribution by country 

### 1.6 Other Relevant Remarks
- Must handle cases where **no stations** are found inside the radius gracefully.
- Sorting of the output list should be by **distance ascending**, then **station name alphabetically**.
- Supports later visualization or clustering tasks for operational planning.