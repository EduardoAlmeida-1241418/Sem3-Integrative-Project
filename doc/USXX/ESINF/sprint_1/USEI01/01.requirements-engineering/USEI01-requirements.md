# USEI01 - Wagons Unloading (Inventory replenishment)

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Terminal Operator, **I want** unloading operations to automatically store inventory using FEFO/FIFO logic, **So that** I can ensure correct dispatch order, maintain traceability, and minimise product spoilage.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** How many boxes can a bay store?
>
>**Answer:** bays.csv includes capacityBoxes per bay.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=946)


>**Question:** Is there an already defined cargo limit for wagons, or is it according to the developing team opinion? Is it unitary (number of boxes) or weight?
>
>**Answer:** The dataset includes 8 wagons, but the system should not assume any fixed number. Your logic should work for any number of wagons and boxes.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=918)

>**Question:** Do the storage facilities (freight yard, station, terminal) have a specific storage capacity? And does that characteristic change depending on the type of storage facility?
>
>**Answer:** In the context of our problem, storage capacity will depend on the capacity of the buildings installed and not on the type: station, terminal or freight yard.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=966)

>**Question:** Could you please clarify how the assignment of aisles and bays to each wagon’s contents is supposed to be done? Should we follow a specific rule to decide which aisle/bay each box should go?
>
>**Answer:** You must consider that boxes are atomic; a whole box occupies 1 slot. Never split a box across bays, and never exceed capacityBoxes for that bay.
Concerning the waggon’s contents allocation, you can choose how to assign each incoming box to which aisle/bay, as long as the rule is deterministic and documented in the report.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1059)

>**Question:** Could you please clarify how the assignment of aisles and bays to each wagon’s contents is supposed to be done? Should we follow a specific rule to decide which aisle/bay each box should go?
>
>**Answer:** You must consider that boxes are atomic; a whole box occupies 1 slot. Never split a box across bays, and never exceed capacityBoxes for that bay.
Concerning the waggon’s contents allocation, you can choose how to assign each incoming box to which aisle/bay, as long as the rule is deterministic and documented in the report.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1059)


>**Question:**For each Bay, are multiple SKUs allowed, or must it store only a single SKU?
>
>**Answer:**Each bay can store multiple SKUs.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1130)

>**Question:**When the system will have more than one warehouseId. It will read/import multiple "bays.csv" files, each with the info of one warehouse or it will use a single "bays.csv" file that has the info about all warehouses. Or it could be a mix of multiple files being imported at diferent times each with multiple warehouse info.
All the aisles in the same warehouse must have the same number of bays? Finally, what guarantees do we have about the "bays.csv" file data?
>
> **Answer:**A single bays.csv lists all warehouses.
Aisles may have different bay counts.
Each row must be unique by (warehouseId, aisle, bay), and include capacityBoxes.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1139)

>**Question:** To comply with FEFO/FIFO, do we have to treat the entire warehouse as a list, or do we just have to comply with FEFO/FIFO within each bay?
Regarding AC03 I have 2 questions:
In what situation will we have to relocate a box?
At the end, it states that we will not have to reorder the bay if the expiration or receipt date does not change. In what situation can one of these dates change?
>
> **Answer:**You don’t (and shouldn’t) scan the entire warehouse. FEFO/FIFO is per-SKU across all boxes, but you can get that in with simple JCF structures—no global full scan.
Relocation may be necessary; for example, to consolidate space, you have the same SKU scattered across many bays; move a box to co-locate and free up a bay (or reduce the number of partial bays).
Those dates shouldn’t change, that’s why you don’t need to reorder a bay when you only relocate a box. Relocation just updates the location, not the sorting  keys (expiry/receivedAt).
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1201)

>**Question:** In the USEI02, since we can have more than one warehouse and the orders are not designated for a specific warehouse, can we make allocations from different warehouses for the same order if the first warehouse doesn’t have enough quantity of an SKU to complete the order?
If yes, in the USEI04, do we calculate the total distance for each warehouse and then sum them up?
> **Answer:**Yes, you can have allocations from more than one warehouse if it is necessary. The distances traversed in the different warehouses must be added.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1207)

### 1.3 Acceptance Criteria

- **Scenario 1 – Automatic unloading and storage with FEFO/FIFO logic**

  > **Given** that the input CSV files (`bays.csv`, `wagons.csv`, `items.csv`) have been validated and imported,
  >
  > **And** the warehouse structure with aisles and bays is properly defined,
  >
  > **When** wagons are unloaded and their contents from `wagons.csv` are processed,
  >
  > **Then** each product must be automatically allocated to the appropriate warehouse bay,
  >
  > **Then** products must be inserted in the bay following this priority order:
  >  1. Expiry date in ascending order (earliest first; null last),
  >  2. `receivedAt` in ascending order (oldest first),
  >  3. `boxId` in ascending order (as a tie-breaker),
  >
  > **So that** all products are stored consistently according to FEFO/FIFO logic and ready for dispatch operations.


- **Scenario 2 – Dispatch operation applying FEFO/FIFO logic**

  > **Given** that products are stored in bays according to FEFO/FIFO rules,
  >
  > **When** a dispatch operation occurs,
  >
  > **Then** stock must always be consumed from the front of each bay’s list,
  >
  > **Then** FEFO must apply to perishable goods (earlier expiry first),
  >
  > **Then** FIFO must apply to non-perishable goods (earliest `receivedAt` first),
  >
  > **Then** bays that become empty must remain in the Warehouse Management System (as empty lists, not deleted),
  >
  > **Then** partial dispatch operations must continue to the next available bay in ascending order when one becomes empty,
  >
  > **So that** product traceability and dispatch efficiency are ensured.


- **Scenario 3 – Relocation operation maintaining FEFO/FIFO order**

  > **Given** that a box needs to be relocated to another bay or aisle,
  >
  > **When** its `warehouseId`, `aisle`, or `bay` values are updated,
  >
  > **Then** the relocated box must be inserted into the new bay in the correct FEFO/FIFO position,
  >
  > **And** its `expiryDate` and `receivedAt` attributes must remain unchanged,
  >
  > **So that** warehouse integrity and correct dispatch sequencing are maintained after relocation.

### 1.4 Found out Dependencies
>This User Story doesn’t depend on any other US.

### 1.5 Input and Output Data
>**Input**
>
>- CSV files: `bays.csv`, `wagons.csv`, `items.csv`.
>
>**Output**
>
>- Warehouse data structure updated with new stock by FEFO/FIFO order.

### 1.6 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Handle perishable and non-perishable goods differently.
>
>**(ii) How often this US is held:**
>
>- Executed frequently during stock replenishment cycles.  