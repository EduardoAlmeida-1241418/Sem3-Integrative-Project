# USEI06 - Create Trees

## 1. Requirements Engineering

### 1.1. User Story Description

>As a Planner,
>I want a set of BST/AVL trees to quickly search stations by latitude, longitude, and (time zone group, country) (including windowed queries over time zone groups),
>So that I can efficiently retrieve all stations for a given time zone group ordered by country, or within a range of time zone groups (e.g., [‘CET’, ‘WET/GMT’]), without scanning the entire 64k-row CSV.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** 
>
>**Answer:** 
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

### 1.3 Acceptance Criteria

- Scenario 1 – Successful index build
  - Given a **64k-row CSV** with station fields: **name**, **country**, **timeZoneGroup**, **latitude**, **longitude**,
  - When the system **parses and validates** each row and **builds indexes**,
  - Then it **must construct**:
    - an **AVL** (or balanced BST) keyed by **latitude** (value: list of stations with that latitude),
    - an **AVL** keyed by **longitude** (value: list of stations with that longitude),
    - a **composite index** keyed by **timeZoneGroup** → **country** → **stationName** (nested trees or a single multi-key comparator),  
      and **return success** with counts of loaded vs. rejected rows.

- Scenario 2 – Input validation & rejection
  - Given any row with:
    - **empty** `name`, `country`, or `timeZoneGroup`, **or**
    - `latitude ∉ [-90, 90]`, **or** `longitude ∉ [-180, 180]`,
  - When building indexes,
  - Then the system **must reject** the row, **not insert** it into any index, and **log** the reason (row number + validation error).

- Scenario 3 – Duplicate coordinates with stable ordering
  - Given multiple stations sharing **exact same coordinates** (e.g., “Lisbon Santa Apolónia” and “Lisbon Oriente”),
  - When inserting into the latitude/longitude trees,
  - Then the index **must preserve all stations** at that key, **sorted by station name (ascending)** and **stable** across rebuilds.

- Scenario 4 – Exact time zone group listing ordered by country
  - Given a **valid timeZoneGroup** (e.g., `CET`),
  - When querying the composite index for that group,
  - Then the system **must return all stations** in that group, **ordered by country (ascending)** and then **station name (ascending)**.

- Scenario 5 – Windowed time zone group query
  - Given an **inclusive range** of time zone groups (e.g., `[ 'CET', 'WET/GMT' ]`),
  - When performing a **range query** over the composite index,
  - Then the system **must return all stations** whose timeZoneGroup falls within the **lexicographic window**,  
    **ordered** by timeZoneGroup → country → station name (all ascending).

- Scenario 6 – Latitude/Longitude window queries
  - Given a **latitude interval** `[latMin, latMax]` or **longitude interval** `[lonMin, lonMax]`,
  - When performing a **range search** over the respective AVL,
  - Then the system **must return all matching stations** in **ascending key order**; for equal keys, **station names ascending**.

- Scenario 7 – Robustness and reproducibility
  - Given repeated index builds on the **same input**,
  - When executing the same query,
  - Then results **must be identical in content and ordering** (deterministic comparator, stable storage of equals).

### 1.4 Found out Dependencies

| Dependency | Description | Reason |
|-----------|-------------|--------|
| In-memory dataset import | CSV dataset of ~64k stations | This US depends on the dataset already being loaded |
| BST / AVL tree implementation from classes | Reused data structure from ESINF | Required to build efficient search trees |

### 1.5 Input and Output Data
>**Input**
>
**Input**
- CSV file containing the European railway stations dataset
- Data structure definitions for BST / AVL trees

**Output**
- Indexed tree structures supporting efficient lookup:
  - `Tree_Latitude`
  - `Tree_Longitude`
  - `Tree_TimeZoneGroup → Country → StationList`

- Query results printed or returned as lists:
  - `StationName`, `Country`, `Latitude`, `Longitude`, `timeZoneGroup`

- Time complexity notes included in documentation or comments.

### 1.6 Other Relevant Remarks
- All indexing structures must remain synchronized; if one station is rejected during import, it must not appear in any tree.
- Query responses must not depend on insertion order.
- If multiple stations share the same coordinates, the station names must be stored in sorted order within the same node.
