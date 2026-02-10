# USLP09 - Assemble and Assign a Train to a Route

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a Traffic Manager**  
> **I want to assemble a train and assign it to a specific route**  
> **So that scheduled operations can be carried out using the appropriate rolling stock.**

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:**  
> – 
>
> **Answer:**  
> – 
>
> [Customer Clarification – Forum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Displaying available rolling stock for assembly
- **Given** the Traffic Manager is assembling a train for a selected route,  
  and all available rolling stock is loaded,
- **When** the list is presented,
- **Then** the system must:
  - clearly separate the categories:
    1. Locomotives and carriages in transit with final destinations.
    2. Locomotives and carriages parked with their station location.

---

#### Scenario 2 – Ordering parked rolling stock by distance
- **Given** the parked rolling stock is displayed,  
  and the starting station of the route is known,
- **When** the system organises the list,
- **Then** parked items must be shown in descending order of distance from the route’s starting point.

---

#### Scenario 3 – Selecting locomotives and carriages
- **Given** the available rolling stock is displayed,
- **When** the Traffic Manager selects items for assembly,
- **Then** the system must:
  - allow selection of available items,
  - prevent selection of items already assigned or unavailable,
  - temporarily reserve selected items for the assembly process.

---

#### Scenario 4 – Assembling and assigning the train
- **Given** a valid selection of locomotives and carriages,
- **When** the Traffic Manager confirms the assembly,
- **Then** the system must:
  - create the train entity,
  - associate the selected rolling stock with the train,
  - assign the train to the chosen route,
  - update all relevant statuses (e.g., “in transit”, “assigned”, “scheduled”).

---

#### Scenario 5 – Accessing data through PL/SQL functions
- **Given** rolling stock and route data must be accessed reliably,
- **When** the system retrieves or updates information,
- **Then** all database interactions must be executed exclusively via PL/SQL functions, ensuring correct validation, controlled access, and transactional integrity.

---

### 1.4. Found Dependencies

- Rules for selecting locomotives and carriages.
- Method for calculating distances between stations.
- Valid rolling stock statuses (parked, in transit, unavailable, etc.).
- PL/SQL function definitions and database schema.
- Train assembly workflow and validation by the Product Owner.

---

### 1.5. Input and Output Data

**Input**
- Selected route details.
- Available locomotives and carriages with current status and location.
- User selections for train assembly.

**Output**
- Created train entity with assigned rolling stock.
- Updated statuses of all selected locomotives and carriages.
- Assignment of train to the route.
- Database updates executed via PL/SQL functions.

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Rolling stock lists must be clearly separated and ordered correctly.
  - Selection rules must prevent conflicts with existing assignments.
  - All database operations must go through PL/SQL functions to ensure integrity.

- **Execution frequency**
  - Each time a Traffic Manager assembles a train and assigns it to a route.
