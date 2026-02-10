# USEI04 - Pick Path Sequencing

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Picker, **I want** to plan the sequence for collecting boxes using a 2D layout (aisles and bays), **So that** I minimise walking distance starting from the main corridor entrance, without a return leg.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:**
>
>**Answer:**
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=34936)

### 1.3 Acceptance Criteria

- **Scenario 1 – Preparing the picking plan input**

  > **Given** that the picking plan generated in USEI03 is available,
  >
  > **And** it contains the list of bays to be visited by the Picker,
  >
  > **When** the system processes this input,
  >
  > **Then** any duplicate bays must be merged into a single stop by summing their quantities,
  >
  > **So that** the resulting plan reflects the true number of unique stops before sequencing.


- **Scenario 2 – Sequencing bays using the Deterministic Sweep strategy**

  > **Given** that the cleaned list of unique bays has been prepared,
  >
  > **And** the distance function **D** and warehouse geometry rules have been reviewed,
  >
  > **When** the Picker selects the *Deterministic Sweep* strategy (Strategy A),
  >
  > **Then** the system must sort all bays in ascending order by aisle,
  >
  > **And** calculate the total distance travelled using the defined distance function **D**,
  >
  > **So that** a predictable and structured route is produced for the Picker.


- **Scenario 3 – Sequencing bays using the Nearest-Neighbour strategy**

  > **Given** the same cleaned list of unique bays and the distance function **D**,
  >
  > **When** the Picker selects the *Nearest-Neighbour* strategy (Strategy B),
  >
  > **Then** the system must order the bays by nearest distance from the current location according to **D**,
  >
  > **Then** continue this process iteratively until all bays have been sequenced,
  >
  > **Then** calculate the total distance for the resulting path,
  >
  > **So that** the Picker follows a more efficient, proximity-based route that minimises walking distance.


- **Scenario 4 – Generating and displaying the picking path results**

  > **Given** that both sequencing strategies (Deterministic Sweep and Nearest-Neighbour) have been executed,
  >
  > **When** the results are generated,
  >
  > **Then** the system must output for each strategy:
  >  1. The complete picking path (ordered list of bays).
  >  2. The total distance travelled.
  >
  > **Then** the results must clearly show the differences in route order and total distance between strategies,
  >
  > **So that** the Picker can choose the optimal route to minimise travel effort and improve operational efficiency.

### 1.4 Found out Dependencies
>This User Story depends on USEI03 (Picking Plans).

### 1.5 Input and Output Data
>**Input**
>
>- Picking plans with bay coordinates.
>
>**Output**
>
>- Optimal picking sequence and total distance.

### 1.6 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Must avoid repeated visits to the same bay.
>
>**(ii) How often this US is held:**
>
>- Executed for each new picking plan generation. 