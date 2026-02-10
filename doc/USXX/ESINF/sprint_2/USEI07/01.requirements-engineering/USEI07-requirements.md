# USEI07 - Create a 2D Tree

## 1. Requirements Engineering

### 1.1. User Story Description

>As a Data Engineer,
>I want to build a balanced 2D-tree (k-d tree, k=2) on (latitude, longitude),
>So that range and nearest-neighbour searches run efficiently and deterministically over ~64k European train stations.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Balanced bulk build using existing indexes
  - Given prebuilt **AVL/BST indexes** on **latitude** and **longitude** (from USEI01),
  - When the system performs a **bulk build** of the 2D-tree,
  - Then it **must construct a balanced k-d tree** by recursively selecting medians,  
    reusing the presorted order to avoid re-sorting at each level.

- Scenario 2 – Deterministic ordering and tie-breaking
  - Given stations with **identical coordinates** (e.g., `38.71387, -9.122271` for Lisbon Santa Apolónia and Lisbon Oriente),
  - When inserting into the 2D-tree,
  - Then **all stations at that point must be preserved** in a leaf “bucket”, **sorted by station name (ascending)** and **stable across rebuilds**.

- Scenario 3 – Correct splitting dimensions and metadata
  - Given the k-d tree build process,
  - When building each level,
  - Then the splitter **alternates dimensions** (lat at depth 0, lon at depth 1, repeat),  
    and the node **stores**: split dimension, split value, bounding rectangle, and subtree sizes.

- Scenario 4 – Range (window) query correctness
  - Given an axis-aligned query window `[latMin, latMax] × [lonMin, lonMax]`,
  - When performing a **range search** on the 2D-tree,
  - Then the system **must return all stations within the window**,  
    including **all duplicates** at identical coordinates, with results sorted by **latitude asc, longitude asc, stationName asc**.

- Scenario 5 – Nearest-neighbour (k-NN) correctness
  - Given a query point `(lat*, lon*)` and **k ≥ 1**,
  - When performing **k-nearest neighbours** with pruning (backtracking using bounding rectangles),
  - Then the system **must return the exact k closest stations** under the **Haversine** or **Euclidean-on-projected** metric (metric must be configured and documented),  
    breaking ties by **stationName asc**.

- Scenario 6 – Validation and rejection
  - Given station records,
  - When any record violates **name/country/timeZoneGroup non-empty**, `lat ∉ [-90,90]`, or `lon ∉ [-180,180]`,
  - Then the record **must not** be included in the 2D-tree and the **reason is logged**.

- Scenario 7 – Build outputs & diagnostics
  - Given a successful build,
  - When the process completes,
  - Then the system **must report**: **tree size (node count)**, **tree height**, and the set of **distinct bucket sizes** (e.g., `{1, 2, 5}`) observed in leaves.

- Scenario 8 – Reproducibility
  - Given the **same input** and configuration,
  - When building and querying the 2D-tree multiple times,
  - Then **results (order and content)** and **diagnostics** (size, height, bucket sizes) **must be identical**.

### 1.4 Found out Dependencies

| Dependency | Description | Reason |
|-----------|-------------|--------|
| **USEI06** | Indexed data validated and loaded | KD-tree must reuse clean dataset |
| BST/AVL implementations | Used for ordering and median selection | Required to efficiently pick medians |

### 1.5 Input and Output Data
>**Input**
>
**Input**
- In-memory dataset of stations, containing at least:
  - `name`
  - `latitude`
  - `longitude`
  - `country`
  - `timeZoneGroup`

**Output**
- A **balanced KD-tree** stored in memory
- Diagnostic/summary output including:
  - Total number of nodes
  - Tree height
  - List of bucket sizes (e.g., how many stations share coordinates at each node)

### 1.6 Other Relevant Remarks
- This KD-tree will be used directly in:
  - **USEI08** (Area / Range Search)
  - **USEI09** (Nearest-N Search)
  - **USEI10** (Radius and Density Analysis)
- If needed, a re-balancing operation may be performed during updates (future sprints).