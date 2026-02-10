# OO Analysis

## Rationale to Identify Domain Conceptual Classes

### _Conceptual Class Category List_

---

### **Business Transactions**
- Wagon Unloading (Inventory Replenishment)
- Order Eligibility & Allocation
- Picking Plans
- Pick Path Sequencing
- Returns & Quarantine
- Train Dispatch
- Freight Planning
- Schedule Management

---

### **Transaction Line Items**
- Wagon
- Box
- Order
- Order Line
- Return
- Freight Wagon
- Trolley Allocation
- Schedule Event

---

### **Product/Service related to a Transaction or Transaction Line Item**
- **Wagon** — Arrives carrying boxes of products.
- **Box** — Represents a product lot (SKU, quantity, expiry date, received date).
- **Order** — Shipment request composed of one or more order lines.
- **Return** — Returned products subject to inspection and restocking/disposal.
- **Item** — SKU-based catalog entry (name, category, unit, volume, weight).
- **Freight** — Logical grouping of wagons and cargo for transport.
- **FreightWagon** — Wagon assigned to specific freight with planned cargo details.
- **Locomotive** — Powered vehicle for pulling trains.

---

### **Transaction Records**
- Orders Eligibility List
- Allocation Rows
- Picking Plan
- Audit Log File
- Warehouse Stock Report
- General Schedule
- Train Trip Log
- Crossing Operation Records

---

### **Roles of People or Organizations**
- **Product Owner** — Defines domain model and validates data dictionary.
- **Planner** — Manages rail line information and wagons.
- **Terminal Operator** — Performs wagon unloading.
- **Warehouse Planner** — Handles order allocation.
- **Picker** — Executes picking and trolley operations.
- **Quality Operator** — Manages returns and quarantine.
- **Station Storage Manager** — Oversees cargo handling interface.
- **Traffic Dispatcher** — Calculates travel times and manages crossings.
- **Train Operator** — Owns locomotives and wagons.
- **Rail Line Owner** — Owns and maintains railway infrastructure.
- **Inspector** — Inspects quarantined goods.
- **User** — Operates the system (Admin, Station Operator, System Administrator).
- **Freight Manager** — Manages freights and routes from a logistics perspective.
- **Data Engineer** — Builds and maintains data structures and spatial indexes.
- **Analyst** — Performs data analysis and proximity searches.
- **Operations Planner** — Plans daily operations and assesses local coverage.
- **Infrastructure Planner** — Plans station upgrades and network optimization.
- **Maintenance Planner** — Performs rail hub centrality analysis.
- **Operations Analyst** — Analyzes maximum throughput between hubs.
- **Route Planner** — Plans routes and detects configuration errors.
- **Traffic Manager** — Assembles and assigns trains to routes.

---

### **Places**
- Facility (Station, Terminal, Freight Yard, Intermodal Terminal)
- Building (Warehouse, Refrigerated, Grain Silo)
- Aisle
- Bay
- Quarantine Area
- Railway Dock
- Rail Line Segment
- Siding Location
- Track

---

### **Noteworthy Events**
- Wagon Arrival
- Box Unloaded
- Order Received
- Picking Plan Generated
- Picking Path Calculated
- Return Inspected
- Product Restocked or Discarded
- Train Dispatched
- Schedule Event (Movement, Waiting, Load, Unload)
- Crossing Operation
- Train Trip Started/Completed
- Allocation Created

---

### **Physical Objects**
- Wagon
- Box
- Trolley
- Locomotive
- Aisle
- Bay
- Building
- Rail Line Segment
- Track Gauge
- Bogie
- Track

---

### **Descriptions of Things**
- FEFO/FIFO rules for stock rotation
- Picking heuristics (First Fit, First Fit Decreasing, Best Fit Decreasing)
- Distance metrics between aisles and bays
- Return reasons (Customer Remorse, Damaged, Expired, Cycle Count)
- Facility types (Station, Terminal, Freight Yard, Intermodal Terminal)
- Wagon types (Boxcar, Flatcar, Tankcar, Hoppercar, Refrigeratedcar)
- Cargo types (General Goods, Containers, Liquids, Chemicals, etc.)
- Schedule Event types (MOVEMENT, WAITING, LOAD, UNLOAD, etc.)
- Track state (FREE, BUSY, ASSIGNED, INOPERATIVE)

---

### **Catalogs**
- bays.csv
- wagons.csv
- items.csv
- orders.csv
- order_lines.csv
- returns.csv
- locomotives.csv
- rail_lines.csv
- facilities.csv

---

### **Containers**
- Wagon
- Building
- Trolley
- Quarantine
- Bay
- Aisle
- Facility
- Rail Line

---

### **Elements of Containers**
- Box (inside Wagon, Bay, or Trolley)
- Bay (inside Aisle)
- Aisle (inside Building)
- Building (inside Facility)
- Rail Line Segment (inside Rail Line)
- Schedule Event (inside General Schedule)

---

### **Other (External/Collaborating) Systems**
- Warehouse Management System (WMS)
- Database Management System (Oracle)
- Train Traffic Management System
- Railway Infrastructure Database

---

### **Documents mentioned/used to perform some work**
- Project Assignment - Sprint 1_v1_ENG.pdf
- Railway Operation Manuals
- Safety Regulations
- Maintenance Schedules

---

## Rationale to Identify Associations Between Conceptual Classes (Completo)

| Concept (A)            | Association / Relation              | Concept (B)              | Multiplicity (A - B) |
|------------------------|:-----------------------------------:|-------------------------:|:--------------------:|
| **RailLineSegment**    | has a                               | TrackGauge               | 1 - 1                |
| **RailLineSegment**    | has type                            | TrackType                | 1 - 1                |
| **RailLineSegment**    | has                                 | Siding                   | 1 - *                |
| **RailLine**           | composed of                         | RailLineSegment          | 1 - 1..*             |
| **RailLine**           | owned by                            | RailLineOwner            | 1 - 1                |
| **RailLine**           | connects                            | Facility                 | 1 - 2..*             |
| **Facility**           | has type                            | FacilityType             | 1 - 1                |
| **Facility**           | includes                            | Building                 | 1 - 0..*             |
| **Facility**           | has                                 | Track                    | 1 - 1..99            |
| **Building**           | has                                 | Aisle                    | 1 - 0..*             |
| **Building**           | has type                            | BuildingType             | 1 - 1                |
| **Aisle**              | contains                            | Bay                      | 1 - *                |
| **Bay**                | stores                              | Box                      | 1 - 0..*             |
| **Box**                | represents                          | Item                     | 1 - 1                |
| **TrainOperator**      | owns                                | Locomotive               | 1 - *                |
| **TrainOperator**      | owns                                | Wagon                    | 1 - *                |
| **TrainOperator**      | owns                                | Train                    | 1 - *                |
| **Locomotive**         | has type                            | LocomotiveModel          | 1..* - 1             |
| **LocomotiveModel**    | has type                            | FuelType                 | 1 - 1                |
| **LocomotiveModel**    | has gauge                           | TrackGauge               | 1..* - 1..*          |
| **LocomotiveModel**    | has bogie type                      | BogieType                | 1 - 1                |
| **Wagon**              | is of model                         | WagonModel               | * - 1                |
| **WagonModel**         | has type                            | WagonType                | 1 - 1                |
| **WagonModel**         | has gauge                           | TrackGauge               | 1 - 1                |
| **WagonModel**         | has bogie type                      | BogieType                | 1 - 1                |
| **WagonModel**         | tank subtype                        | TankCarSubtype           | 0..1 - 1             |
| **WagonType**          | supports                            | CargoType                | 1 - 1..*             |
| **Wagon**              | is parked there                     | Facility                 | 1 - 1                |
| **Locomotive**         | is parked there                     | Facility                 | 1 - 1                |
| **Freight**            | origin                              | Facility                 | 1 - 1                |
| **Freight**            | destination                         | Facility                 | 1 - 1                |
| **Freight**            | consists of                         | FreightWagon             | 1 - 1..*             |
| **FreightWagon**       | uses                                | Wagon                    | * - 1                |
| **Path**               | ordered list of                     | RailLine                 | 1 - 1..*             |
| **Route**              | includes                            | Freight                  | 1 - 1..*             |
| **Route**              | has type                            | RouteType                | 1 - 1                |
| **Route**              | uses a                              | Path                     | 1 - 1                |
| **Train**              | has                                 | Route                    | 1 - 1                |
| **Train**              | has                                 | Locomotive               | 1 - 1..*             |
| **General_Schedule**   | is composed by                      | Schedule_Event           | 1 - *                |
| **Schedule_Event**     | has                                 | Train                    | 1 - 1                |
| **Schedule_Event**     | is kind of the                      | Schedule_Event_Type      | 1 - 1                |
| **Schedule_Event**     | has                                 | Position                 | * - 2                |
| **Position**           | can be of type                      | Facility                 | 1 - 0..1             |
| **Position**           | can be of type                      | Siding                   | 1 - 0..1             |
| **Position**           | can be of type                      | RailLineSegment          | 1 - 0..1             |
| **Order**              | contains                            | OrderLine                | 1 - 1..*             |
| **OrderLine**          | has state                           | OrderLineStatus          | 1 - 1                |
| **OrderLine**          | possible boxes                      | Box                      | 0..* - 0..*          |
| **OrderLine**          | has allocation                      | AllocatedInfo            | 0..* - 0..*          |
| **Trolley**            | has model                           | TrolleyModel             | 0..* - 1             |
| **Trolley**            | has allocations                     | TrolleyAllocation        | 1 - 0..*             |
| **TrolleyAllocation**  | from box                            | Box                      | 1 - 1                |
| **TrolleyAllocation**  | for order line                      | OrderLine                | 1 - 1                |
| **PickingPlan**        | fulfills                            | OrderLine                | 1 - 1..*             |
| **Return**             | has                                 | Item                     | 1 - 1..*             |
| **Return**             | due to                              | ReturnReason             | 1 - 1                |
| **Return**             | placed in                           | Quarantine               | 1 - 1                |
| **Quarantine**         | results in                          | Disposition              | 1 - 1                |
| **Track**              | has a                               | TrackState               | * - 1                |
| **Track**              | assigned to                         | Train                    | 1 - 0..1             |
| **Facility**           | has                                 | Track                    | 1 - 1..99            |
| **User**               | has a                               | UserRole                 | * - 1                |
| **Log**                | performed by                        | User                     | * - 1                |

---

## Domain Model — Concept Attributes

### **RailLine**
- id

### **RailLineSegment**
- maximumWeight
- speedLimit
- length
- isElectrified
- gauge

### **Siding**
- position
- length

### **TrackType** (enum)
- SINGLE_TRACK
- DOUBLE_TRACK

### **TrackGauge**
- size

### **RailLineOwner**
- name

### **Facility**
- name

### **FacilityType** (enum)
- STATION
- TERMINAL
- FREIGHT_YARD
- INTERMODAL_TERMINAL

### **Building**
- name

### **BuildingType** (enum)
- WAREHOUSE
- REFRIGERATED
- GRAIN_SILO

### **Aisle**
- aisleNumber

### **Bay**
- bayNumber
- capacityBoxes

### **Item**
- sku
- name
- category
- unit
- unitVolume
- unitWeight

### **Box**
- sku
- quantity
- expiryDate
- receivedAt

### **TrainOperator**
- name

### **Locomotive**
- id
- yearOfEntryInService

### **LocomotiveModel**
- id
- make
- modelName
- power
- acceleration
- maximumWeight
- numberOfBogies
- fuelCapacity
- voltage
- frequency
- gauge

### **FuelType** (enum)
- DIESEL
- ELECTRIC

### **Wagon**
- id
- yearOfEntryInService

### **WagonModel**
- id
- ModelName
- make
- payload
- volumeCapacity
- maximumWeight
- numberOfBogies
- gauge

### **WagonType** (enum)
- BOXCAR
- FLATCAR
- TANKCAR
- HOPPERCAR
- REFRIGERATEDCAR

### **Dimensions**
- Length
- Width
- Height
- WeightTare

### **TankCarSubtype** (enum)
- CHEMICAL
- MINERAL_OIL
- PRESSURE_GAS

### **CargoType** (enum)
- GENERAL_GOODS
- CONTAINERS
- VEHICLES
- MACHINERY
- LIQUIDS
- CHEMICALS
- FUEL
- COAL
- GRAINS
- MINERALS
- PERISHABLE_GOODS

### **BogieType**
- id
- ModelName
- WheelCount

### **Train**
- id
- scheduledDepartureDate
- dispatchedState

### **Freight**
- id

### **FreightWagon**
- id
- plannedCargoWeight
- plannedBoxCount

### **Path**
- id

### **Route**
- id

### **RouteType** (enum)
- SIMPLE
- COMPLEX

### **General_Schedule**
- id

### **Schedule_Event**
- Start_Date
- End_Date

### **Schedule_Event_Type** (enum)
- MOVEMENT
- WAITING_IN_SIDING
- WAITING_IN_STATION
- WAITING_FOR_ASSEMBLE
- LOAD
- UNLOAD
- WAITING
- MOVEMENT_TO_SEGMENT
- MOVEMENT_IN_SEGMENT
- MOVEMENT_IN_SIDING

### **Position**
- (interface)

### **Order**
- id
- dueDate
- priority

### **OrderLine**
- id
- orderId
- lineNumber
- sku
- quantity

### **OrderLineStatus** (enum)
- ELIGIBLE
- PARTIAL
- UNDISPATCHABLE
- ALLOCATED

### **AllocatedInfo**
- allocationId
- quantityAllocated

### **Trolley**
- trolleyId
- capacity

### **TrolleyModel**
- id
- name
- maximumWeight

### **TrolleyAllocation**
- quantity
- itemWeight

### **PickingPlan**
- heuristicUsed

### **Return**
- reason
- quantity
- timestamp

### **ReturnReason** (enum)
- CUSTOMER_REMORSE
- DAMAGED
- EXPIRED
- CYCLE_COUNT

### **Quarantine**
- entryDate
- inspectionDate
- inspector
- disposition

### **Disposition** (enum)
- RESTOCKED
- DISCARDED

### **Track**
- trackNumber
- state

### **TrackState** (enum)
- FREE
- BUSY
- ASSIGNED
- INOPERATIVE

### **User**
- username
- password

### **UserRole** (enum)
- ADMIN
- STATION_OPERATOR

### **Log**
- action_date
- action

---

## Enumerations

### **FacilityType**
- STATION
- TERMINAL
- FREIGHT_YARD
- INTERMODAL_TERMINAL

### **BuildingType**
- WAREHOUSE
- REFRIGERATED
- GRAIN_SILO

### **FuelType**
- DIESEL
- ELECTRIC

### **WagonType**
- BOXCAR
- FLATCAR
- TANKCAR
- HOPPERCAR
- REFRIGERATEDCAR

### **TankCarSubtype**
- CHEMICAL
- MINERAL_OIL
- PRESSURE_GAS

### **CargoType**
- GENERAL_GOODS
- CONTAINERS
- VEHICLES
- MACHINERY
- LIQUIDS
- CHEMICALS
- FUEL
- COAL
- GRAINS
- MINERALS
- PERISHABLE_GOODS

### **RouteType**
- SIMPLE
- COMPLEX

### **Schedule_Event_Type**
- MOVEMENT
- WAITING_IN_SIDING
- WAITING_IN_STATION
- WAITING_FOR_ASSEMBLE
- LOAD
- UNLOAD
- WAITING
- MOVEMENT_TO_SEGMENT
- MOVEMENT_IN_SEGMENT
- MOVEMENT_IN_SIDING

### **TrackType**
- SINGLE_TRACK
- DOUBLE_TRACK

### **OrderLineStatus**
- ELIGIBLE
- PARTIAL
- UNDISPATCHABLE
- ALLOCATED

### **ReturnReason**
- CUSTOMER_REMORSE
- DAMAGED
- EXPIRED
- CYCLE_COUNT

### **Disposition**
- RESTOCKED
- DISCARDED

### **TrackState**
- FREE
- BUSY
- ASSIGNED
- INOPERATIVE

### **UserRole**
- ADMIN
- STATION_OPERATOR