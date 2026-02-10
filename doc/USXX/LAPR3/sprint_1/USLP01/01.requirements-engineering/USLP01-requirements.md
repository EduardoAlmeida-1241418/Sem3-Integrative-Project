# USLP01 - Create Conceptual Domain Model

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Product Owner, **I want** a conceptual-level domain model to be created, **So that** that it serves as a central communication element among all stakeholders and reflects the shared understanding of the railway system’s operation.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
>**Question:** What unit of measurement will be used to measure the gauge, width of a rail (m, dm, cm, mm)?
> 
>**Answer:** Usualy track gauge have a name and a measure in mm. Please check: https://en.wikipedia.org/wiki/Track_gauge
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=901)


>**Question:** According to the given information, an owner is responsible for the maintenance on all its segments, but the maintenance cost is fixed, or depends of its characteristics as distance, width, and others?
>
>**Answer:** For this sprint no US were proposed to calculate maintanance costs, so atm detailed cost maintanance is not required.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=945)

>**Question:** Can rolling stock from one company be used in combination with another company's rolling stock? For example, could a freight train be composed of locomotives from different companies (e.g., Company1.Locomotive -> Company2.Locomotive -> Wagons), or could Company 1’s  freight train include wagons owned by Company 2?
>
> **Answer:** Atm you can consider that each company manages/uses only its rolling stock.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1008)

>**Question:** Should the system record specific attributes for each wagon subtype? For example: Refrigerated cars: Cooling capacity (kW)
>
>**Answer:** I believe that there is no need to go into detail on these aspects at this stage. However, when new requirements are defined, it may be necessary to update the MCD.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=907)

>**Question:** Can one owner own several rail lines?
>
>**Answer:** Yes but not the converse.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=911)

>**Question:** Is there an already defined cargo limit for wagons, or is it according to the developing team opinion? Is it unitary (number of boxes) or weight?
> 
> **Answer:** The dataset includes 8 wagons, but the system should not assume any fixed number. Your logic should work for any number of wagons and boxes.
> 
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=918)

>**Question:** Um line segment pode ter vários gauges?
>
> **Answer:** Embora tal aconteça na realidade, no contexto do PI vamos considerar que uma linha/segmento pode ter apenas uma bitola.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=930)

>**Question:** Does a locomotive have a number plate or serial number?
>
>**Answer:** yes
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=944)

>**Question:** In the information about the project, the locomotive has a set of characteristics as model, power, etc. but the characteristics wouldn´t be related to the model instead? For example the model X has a maximum speed of 200 km/h, and other characteristics as dimensions.
>
>**Answer:** yes
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=944)

>**Question:** The wagons can be designed transport many goods. One of the designs is the Flatcar which can transport containers. Some of the information given talks about containers, as in the terminal refers that "where containers can be transferred
between trains, trucks, and ships.", this means that ONLY containers from the flatcars can be transferred? Or the referred containers are different?
>
>**Answer:** In theory everything can be transfered by atm there is no need to model these possibilities with great detail.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=944)

>**Question:** A train may couple locomotives together in a train. The locomotives have to be the same type? If not the train can move on a electric line having locomotives moved by electricity and fuel?
>
>**Answer:** One can combine different locomotives in the same train but please keep in mind that electric engines can't provide power in no electric tracks (!!)
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=944)

>**Question:** Should we be prepared for the case where an items weight is greater than any wagons max possible weight? Or are we to assume from the get go that no item will be heavier than a wagons max capacity?
>
>**Answer:** Does not make sense to have items with weights greater than the trolley's capacity. Anyway, the system should be prepared for those situations.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1026)

>**Question:** Do the storage facilities (freight yard, station, terminal) have a specific storage capacity? And does that characteristic change depending on the type of storage facility?
>
>**Answer:** In the context of our problem, storage capacity will depend on the capacity of the buildings installed and not on the type: station, terminal or freight yard.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=966)

>**Question:** In the assignment, both stations and terminals are mentioned.
Should we assume that terminals are always associated with a station (for example, a terminal located within a station), or can there be independent terminals that do not belong to any station?
Regarding the storage areas — warehouses, refrigerated areas, and grain silos — should these be associated only with terminals (as logistical sub-facilities) in the case where terminals are associated with stations (e.g., Station A → Terminal A → 5 Warehouses),
or can they also be directly associated with stations in the case where stations are not associated with terminals (e.g., Terminal A → 1 Warehouse + 1 Refrigerated Area; Station X → 1 Warehouse)?
If stations and terminals are distinct entities, does it make sense to consider that a station can contain one or more terminals, and that each terminal may include multiple storage areas (for example, a terminal with both a warehouse and a refrigerated area)?
Finally, regarding freight yards and intermodal terminals, should we treat them as specific types of terminal (e.g., TerminalType = 'freight_yard' or TerminalType = 'intermodal'),
or only classify intermodal as a terminal type, while freight yards are considered similar to stations and terminals if those two are not directly related?
>
>**Answer:** In this context, there are no significant differences between a terminal and a station. A terminal is a LARGE station, but they can provide the same services.
A freight yard is exclusively for freight service, while terminals and stations provide an interface with the public (and transport passengers).
Although the issue at hand is only about logistics, the railway network can serve a dual purpose.
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=971)


### 1.3 Acceptance Criteria

- **Scenario 1 – Creating the conceptual-level domain model**

  > **Given** that the requirements from all course units have been consolidated,
  >
  > **And** that the main domain entities have been identified and drafted,
  >
  > **When** the team begins the creation of the conceptual-level domain model,
  >
  > **Then** the model must represent the key aspects of the railway system’s operation,
  >
  > **Then** the model must include the main concepts, relationships, and interactions relevant to the system’s functioning,
  >
  > **So that** it serves as a shared visual representation of the system’s structure and logic.


- **Scenario 2 – Sharing and documenting the conceptual model**

  > **Given** that the conceptual domain model has been created,
  >
  > **When** the deliverable is ready for submission,
  >
  > **Then** it must be documented either in Visual Paradigm or represented using PlantUML,
  >
  > **Then** the final version of the model must be exported and committed to the directory `/doc/global-artifacts/02.analysis/domain-model/` as both `.svg` and `.puml` files,
  >
  > **So that** the model is easily accessible and serves as a central communication artefact among all stakeholders.

### 1.4 Found out Dependencies
>This User Story doesn’t depend on any US.

### 1.5 Input and Output Data
>**Input**
>
>- Requirements and analysis from BDDAD and ESINF.
>
>**Output**
>
>- Domain model diagram (`.puml`, `.svg`) stored under `/doc/global-artifacts/02.analysis/domain-model/`.

### 1.6 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Model must include core classes such as Train, Wagon, Station, Line.
>
>**(ii) How often this US is held:**
>
>- Developed once and refined iteratively.  
