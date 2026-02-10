# USEI05 - Returns & Quarantine

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Quality Operator, **I want** returned goods placed in quarantine and processed latest-first, **So that** I can inspect them in reverse order of arrival (latest first, descending by timestamp, ties by returnId ASC), decide whether to restock or discard each item, and generate an audit log recording all inspection actions.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:**
>
>**Answer:**
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=34936)

### 1.3 Acceptance Criteria

- **Scenario 1 – Placing returned goods in quarantine**

  > **Given** that the validated returns dataset (`returns.csv`) is available,
  >
  > **And** the FEFO/FIFO insertion logic from USEI01 is accessible,
  >
  > **When** returned goods are received into the system,
  >
  > **Then** all returned items must first be placed in a quarantine queue,
  >
  > **And** the queue must be ordered in descending order of `timestamp`, with ties resolved by `returnId` in ascending order,
  >
  > **So that** the most recent returns are inspected first, ensuring a consistent latest-first inspection process.


- **Scenario 2 – Inspecting and processing quarantined returns**

  > **Given** that returned goods are stored in the quarantine queue in latest-first order,
  >
  > **When** the Quality Operator begins the inspection process,
  >
  > **Then** each return must be inspected sequentially according to the defined order,
  >
  > **And** each return must result in one of two actions:
  >  1. **Restocked:** A new box is created with:
        >     - `boxId = "RET-" + returnId`
  >     - `receivedAt = now()`
  >     - `expiryDate = as provided`, or `null` if unknown
  >     - Placement following the FEFO/FIFO insertion rules defined in USEI01.
  >  2. **Discarded:** The return is marked as unusable and excluded from inventory.
  >
  > **So that** only valid returns are reintroduced into stock and invalid ones are properly excluded.


- **Scenario 3 – Generating the audit log for all inspection actions**

  > **Given** that each return has been inspected and classified as either Restocked or Discarded,
  >
  > **When** the inspection process is executed,
  >
  > **Then** every inspection action must be written to an external audit log file,
  >
  > **And** each entry must follow the format:  
  > `timestamp | returnId=... | sku=... | action=Restocked|Discarded | qty=...`,
  >
  > **And** if a return is partially restocked, the log must also include `qtyRestocked` and `qtyDiscarded`,
  >
  > **So that** the audit log maintains a clear and chronological record of all inspection actions for traceability and future review.


- **Scenario 4 – Maintaining audit log integrity and compliance**

  > **Given** that all inspection actions are logged,
  >
  > **When** the audit log is reviewed or exported,
  >
  > **Then** all entries must appear in chronological order of execution,
  >
  > **And** contain accurate data for `timestamp`, `returnId`, `sku`, `action`, and quantity fields,
  >
  > **So that** the audit log provides a reliable and transparent record supporting compliance, audits, and troubleshooting.
### 1.4 Found out Dependencies
>This User Story depends on USEI01 (FEFO/FIFO Logic).

### 1.5 Input and Output Data
>**Input**
>
>- Returns file (`returns.csv`).
>
>**Output**
>
>- Updated stock and audit log file with inspection actions.

### 1.6 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Audit log format must follow timestamp | returnId | sku | action | qty.
>
>**(ii) How often this US is held:**
>
>- Executed whenever returns are received.  