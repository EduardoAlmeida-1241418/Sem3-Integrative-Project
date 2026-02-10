# USBD02 - Design Logical Relational Model (ERD)

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Product Owner, **I want** the relational model (logical level) to be created, **So that** the database structure accurately represents the railway system.

### 1.2. Customer Specifications and Clarifications

### Customer enquiries
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

>**Question:** What's the measure unit used for the locomotive and wagon dimensions? Only their length in ft, or do we need more info?
>
>**Answer:** Wagon measures vary significantly by type, with typical railway freight wagons having lengths around 13.6 meters and widths of 2.6 meters, while specific models for different loads can range in length.
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

>**Question:** Instead of using the string "SKU0001" as primary key (as VARCHAR2) for the items, can we use the integer 0001 or just 1 (as NUMBER) and add the missing characters (either "SKU" or "SKU" + number of missing digits of zeros) through the application/query?
>
>**Answer:** Dear student,
"SKU" doesn't seem to be part of the code, but to be the identifier that tells you that "SKU0001" it is a SKU code.
> 
>One problem is that "0001" may be different from "1". Those leading zeros may have a meaning.
> 
>There is no problem in using a string as an ID, especially when it is one with a well defined format.
> 
>Best regards,
> 
>Angelo Martins
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1087)

>**Question:** "Please read the document and stop making up requirements for USBD02. Please notice that the scope of USLP01 it is not constrained to sections 2.2.1 and 2.2.2."
>The description says: "The data model should cover the requirements presented in sections 2.2.1 and 2.2.2 of the document, as well as any additional requirements resulting from the provided user stories."
>Since it's said that USLP01 is not to be considered I would like to ask if ESINF user stories is to be considered, because there is a lot of informations about the boxes, warehouses and wagons in the section.
>
>**Answer:** Dear student,
>You received information about the assessment in BDDAD. The provided user stories are UBD01 to USBD20.
> 
>Of course you have other requirements regarding EISNF and LAPR3, which may be used in the assessment of those two courses. For example, in section 3.2.1 you have information about data input/output for US USEI01 to USEI05. None of them uses a SGBD. So I don't understand how you can connect it to the relational data model.
> 
>Best regards,
> 
>Angelo Martins
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1166)

>**Question:** Good evening! I have a few questions regarding USBD02.
> 
>1- Since the rail line's segments have information about the maximum weight allowed, shouldn't they have information about the locomotive's weight?
>
>2- Rolling stock includes trains, locomotives and wagons. Since rolling stock belongs to a train operator, then should the locomotives and wagons also belong to the train operator, having a relation with it?
>
>3- Should rail lines have information about stations, since they are responsible for connecting them?
>
>**Answer:** Dear student,
>
>1. Sorry, that's not possible. Multiple locomotives can use the same line.
>
>2. Wrong. Rolling stock includes locomotives and wagons. And of course they belong to an operator.
>
>3. A line knows the start and end stations.
>
>Best regards,
>
>Angelo Martins
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1231)

>**Question:** In the excel file that includes data for USBD04, there are attributes that are not included/specified on the vision document (Example: for the locomotives, it is referenced its acceleration. However, in the excel file it isn't referenced, but attributes like max speed, operational speed and traction are included). Should we include those attributes on the relations model for USBD02, or should we only include the information given to us on the vision document?
>
>**Answer:** Dear student,
> 
>The actual acceleration of a train will be given by F = m*a, where F is the force of the locomotive and m is the mass of the train.
>As such, it doesn't make much sense to store the acceleration.
>
>Best regards,
>
>Angelo Martins
>
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1273)

>**Question:** Boa tarde cliente,
>1. Quais atributos são relevantes para guardar no sistema sobre um Owner de uma linha?
>
>2. Quais atributos são relevantes para guardar no sistema sobre um Train Operator?
>
>**Answer:** Dear student,
> 
>Both are companies and have a legal VAT number. If you want to store the address, country, etc., be my guest. But make sure that your model meets 3NF requirements.
>
>Best regards,
>Angelo Martins
> 
> [Customer Clarification - Fórum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=1278)

### 1.3 Acceptance Criteria
- Scenario 1 – Creation of the relational model (logical level)

  > **Given** that the data dictionary from USBD01 is completed and approved,
  >
  > **And** the functional and structural requirements from sections 2.2.1 and 2.2.2 have been analysed,
  >
  > **When** the team designs the relational model in Visual Paradigm,
  >
  > **Then** the model must include all entities, attributes, relationships, and constraints required to represent the railway system accurately,
  >
  > **Then** it must fully comply with the requirements described in sections 2.2.1 (Railway Infrastructure) and 2.2.2 (Rolling Stock), as well as any additional ones derived from the user stories,
  >
  > **Then** it must be capable of automatically generating the physical SQL schema through Visual Paradigm,

- Scenario 2 – Enhancement beyond the expected level

  > **Given** that the logical relational model is complete and functional,
  >
  > **When** the team aims to exceed the minimum expected requirement,
  >
  > **Then** they should also present a conceptual data model in Visual Paradigm,
  >
  > **So that** the overall structure and relationships of the system can be more easily understood and validated by all stakeholders.

### 1.4 Found out Dependencies
>This User Story depends on USBD01 (Data Dictionary).

### 1.5 Input and Output Data
>**Input**
>
>- Data dictionary/glossary (USBD01).
>- Functional and structural requirements from 2.2.1–2.2.2.
>
>**Output**
>
>- Logical ERD created in Visual Paradigm.
>- Exported `.vpp` and `.svg` diagrams stored in `/doc/USXX/BDDAD/relational-model/`.

### 1.6 Other Relevant Remarks
>**(i) Special requirements:**
>
>- The model must support automatic schema generation.
>
>**(ii) How often this US is held:**
>
>- Executed once per semester, updated if requirements change. 
