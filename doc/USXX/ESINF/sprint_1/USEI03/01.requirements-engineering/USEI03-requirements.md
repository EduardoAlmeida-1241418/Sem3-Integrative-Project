# USEI03 - Picking Plans

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Planner, **I want** to pack the allocation rows produced by USEI02 into capacity-bounded trolleys using one of the available packing heuristics (FF, FFD, BFD), **So that** pickers can complete their runs without exceeding trolley capacity.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** While I suppose we're to implement the Three Heuristics, just to make sure, I want to ask - do we simply choose one heuristic to develop? OR is the intention to develop the three of them and have the user decide on which to use?
>
>**Answer:** The US (03) answers the question
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1026)

>**Question:** Regarding this US, it's mentioned in its context, that there are 3 packing heuristics, and in the acceptance criteria, that "The planner must dispatch the orders,
executing any one of the three packing heuristics on the same set of order lines.". However, it doesn't clarify who chooses the option to split or defer the boxes if the trolley runs out of capacity. Is it the goal to just develop one of them at choice (being the developers' choice), or should the "Planner" actor choose it together with the packing heuristics?
> >**Answer:** The three heuristics must be implemented. The choice of each one to use can be parameterised.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1210 )

### 1.3 Acceptance Criteria

- **Scenario 1 – Packing allocations into trolleys according to defined capacity**

  > **Given** that the allocation results from USEI02 have been validated and confirmed,
  >
  > **And** that the Planner has defined the capacity of each trolley,
  >
  > **When** the system begins packing allocation rows into trolleys,
  >
  > **Then** there must be no limit on the number of trolleys created,
  >
  > **Then** each trolley must not exceed its defined capacity,
  >
  > **So that** pickers can complete their runs efficiently without overloading.


- **Scenario 2 – Applying the selected packing heuristic (FF, FFD, or BFD)**

  > **Given** that the Planner has selected a packing heuristic (First Fit, First Fit Decreasing, or Best Fit Decreasing),
  >
  > **When** the system organises and assigns allocations to trolleys,
  >
  > **Then** it must apply the chosen heuristic as follows:
  >  1. **First Fit (FF):** Place each item in the first trolley where it fits, following the order of the allocation rows.
  >  2. **First Fit Decreasing (FFD):** Sort allocations by weight from heaviest to lightest, then place each item in the first trolley where it fits.
  >  3. **Best Fit Decreasing (BFD):** Sort allocations by weight from heaviest to lightest, then place each item in the trolley that results in the smallest unused capacity.
  >
  > **So that** the packing process reflects the selected heuristic and maintains efficient trolley utilisation.


- **Scenario 3 – Handling oversized allocations**

  > **Given** that some order lines may not fully fit into the remaining capacity of a trolley,
  >
  > **When** such an allocation is encountered during packing,
  >
  > **Then** the system must apply one of the following strategies:
  >  1. **Split:** Allocate part of the order line to the current trolley and carry the remainder to the next.
  >  2. **Defer:** Skip the order line for the current trolley and allocate it fully to the next.
  >
  > **So that** all allocations are accommodated without violating trolley capacity constraints.


- **Scenario 4 – Generating the packing output report**

  > **Given** that all allocations have been packed using the selected heuristic,
  >
  > **When** the packing process is complete,
  >
  > **Then** the system must display for each heuristic applied:
  >  1. The total number of trolleys used.
  >  2. The utilisation of each trolley (used weight vs capacity, e.g. *85% full*).
  >  3. A detailed picking plan for each trolley, listing: `orderId`, `lineNo`, `aisle`, `bay`, `boxId`, `SKU`, and `quantity`.
  >
  > **Then** the generated output must accurately represent the applied heuristic,
  >
  > **So that** the Planner can use the report to optimise picker routes and operational efficiency.

### 1.4 Found out Dependencies
>This User Story depends on USEI02 (Order Allocation).

### 1.5 Input and Output Data
>**Input**
>
>- Allocation records from USEI02.
>
>**Output**
>
>- Picking plans grouped by trolley, with utilisation and item list.

### 1.6 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Handle oversized allocations gracefully.
>
>**(ii) How often this US is held:**
>
>- Executed at the start of each dispatch cycle.  