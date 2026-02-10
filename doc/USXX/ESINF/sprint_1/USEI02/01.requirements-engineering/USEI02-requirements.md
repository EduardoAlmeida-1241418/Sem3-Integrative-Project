# USEI02 - Order Eligibility & Allocation

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Warehouse Planner, **I want** the system to verify which order lines can be dispatched and allocate quantities using FEFO, **So that** I obtain per-line statuses (ELIGIBLE, PARTIAL, UNDISPATCHABLE) together with a list of allocation rows containing box and bay information for dispatch planning.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** Can the user select which open orders to process? Or does it run through all of them?
>
>**Answer:** It must run all of them.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1252)

>**Question:** The output of the USEI02 is supposed to be used as input for the following US? Or is it just an informative description of the process?
>
>**Answer:**If you want you can use.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1253)

### 1.3 Acceptance Criteria

- **Scenario 1 – Order processing according to defined priority rules**

  > **Given** that the system has access to the validated input files (`orders.csv`, `order_lines.csv`) and the data from USEI01,
  >
  > **And** that the allocation rules and modes (strict and partial) are properly configured,
  >
  > **When** the system begins processing customer orders,
  >
  > **Then** orders must be processed in the following order of priority:
  >  1. `priority` (lowest value first),
  >  2. `dueDate` (earliest first),
  >  3. `orderId` (ascending order),
  >
  > **So that** dispatch planning is consistent and respects business priorities.


- **Scenario 2 – Allocation of quantities according to FEFO/FIFO logic**

  > **Given** that orders and order lines have been loaded and boxes are stored according to FEFO/FIFO logic from USEI01,
  >
  > **When** the system allocates stock for each order line,
  >
  > **Then** it must iterate through the SKU’s boxes in FEFO/FIFO order,
  >
  > **Then** for each visited bay:
  >  1. Allocate `min(remainingQty, box.qtyAvailable)`,
  >  2. Reduce `remainingQty` accordingly,
  >  3. Continue until the requested quantity is satisfied or no more boxes are available,
  >
  > **Then** box quantities must never fall below zero during the planning process,
  >
  > **So that** allocations are accurate and inventory integrity is maintained.


- **Scenario 3 – Eligibility determination in strict and partial allocation modes**

  > **Given** that the system supports both *Strict* and *Partial* allocation modes,
  >
  > **When** an order line is processed for dispatch,
  >
  > **Then** under **Strict Mode (default)**:
  >  - A line is marked **ELIGIBLE** only if the full requested quantity is allocated.
  >  - Otherwise, it is marked **UNDISPATCHABLE**, and no allocations are retained.
  >
  > **Then** under **Partial Mode**:
  >  - If `0 < allocated < requested`, the line is marked **PARTIAL**, and the allocated portion is kept.
  >  - If `allocated = 0`, the line is marked **UNDISPATCHABLE**.
  >
  > **So that** the system correctly classifies each order line according to the chosen allocation mode.


- **Scenario 4 – Generation of eligibility and allocation reports**

  > **Given** that all orders and allocations have been processed,
  >
  > **When** the system completes the allocation phase,
  >
  > **Then** it must generate two outputs:
  >  1. **Order eligibility list**, containing `orderId`, `lineNo`, `sku`, `requestedQty`, `allocatedQty`, and `status`.
  >  2. **Allocation records**, containing `orderId`, `lineNo`, `sku`, `qty`, `boxId`, `aisle`, and `bay`.
  >
  > **Then** the outputs must accurately reflect the allocation and eligibility logic for both strict and partial modes,
  >
  > **So that** the Warehouse Planner can plan and execute dispatch operations effectively.

### 1.4 Found out Dependencies
>This User Story depends on USEI01 (Inventory Replenishment).

### 1.5 Input and Output Data
>**Input**
>
>- Orders (`orders.csv`) and order lines (`order_lines.csv`).
>
>**Output**
>
>- Allocation results: eligible, partial, or und dispatchable per line.

### 1.6 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Prevent negative stock quantities.
>
>**(ii) How often this US is held:**
>
>- Executed for every new order batch. 