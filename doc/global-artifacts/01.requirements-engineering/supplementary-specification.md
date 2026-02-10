# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:  
&nbsp;&nbsp;(i) are common across several US/UC;  
&nbsp;&nbsp;(ii) are not related to US/UC, namely: Audit, Reporting, and Security._

> This system supports the management of logistics operations within railway networks, ensuring robust domain modelling and strong data integrity.
It defines and validates core entities such as rail lines, segments, facilities (stations, terminals, freight yards), locomotives, wagons, and warehouse operations, enforcing essential business rules, for example, distinct endpoints, proper segment composition, and infrastructure compatibility.

> All modules (LAPR3, BDDAD, ESINF, ARQCP, and FSIAP) must share a consistent domain model and semantics to ensure seamless interoperability.
The main application will be developed in Java, the database will be implemented in PL/SQL, and interaction with stations will rely on C/Assembly. Integration across modules will be achieved primarily through structured file exchange using CSV formats (bays.csv, wagons.csv, items.csv, orders.csv, order_lines.csv, returns.csv) with comprehensive validation rules.

> Data will be stored and retrieved using a remote Database Management System (DBMS), which serves as the central repository for all project information.
All operations must maintain referential integrity and data consistency, ensuring valid foreign keys, coherent records, and synchronised information across related entities.
When data is exchanged between modules, its meaning must remain equivalent so that concepts are uniformly represented across LAPR3, BDDAD, and ESINF.

> The system will support comprehensive railway operations including:
- **Infrastructure Management**: Rail lines, segments, facilities, and track characteristics
- **Rolling Stock Management**: Locomotives, wagons, and train operator ownership
- **Warehouse Operations**: Building management, aisle/bay organization, and box storage
- **Cargo Operations**: Loading/unloading operations at various locations
- **Transport Planning**: Freight routing, train scheduling, and endpoint event tracking
- **Order Management**: Order processing with FEFO/FIFO allocation logic
- **Picking Operations**: Trolley-based picking with heuristic optimization
- **Returns Management**: Quarantine processing and disposition handling

> Cargo unloading and storage processes must follow FEFO (First-Expired-First-Out) and FIFO (First-In-First-Out) principles, ensuring efficient and traceable operations.
While detailed algorithms are implemented in ESINF, LAPR3 must define the corresponding domain entities and rules to guarantee data consistency and accurate communication.

> Development must follow Test-Driven Development (TDD) practices with comprehensive test coverage. The system should maintain audit logs for critical actions, such as imports, validations, and updates, and allow the configuration of global parameters whenever required.

## Usability

_Evaluates the user interface. It has several subcategories, among them: error prevention; interface aesthetics and design; help and documentation; consistency and standards._

> The system's usability is centred on clarity, consistency, and intuitive interaction. The interface should present a clean and well-structured design, allowing users to easily identify available options and system states at any given time.

> Error messages and notifications must be deterministic, concise, and informative. They should clearly explain what went wrong and how to correct it.
All text within the interface (including labels, buttons, and tooltips) must be written in English to ensure consistency across modules and facilitate collaboration among development teams.

> Navigation patterns and visual layouts must remain consistent throughout the system. Common actions, such as Confirm and Cancel, should always appear in predictable positions, while colour schemes should help distinguish between information, warnings, and errors.
When users perform actions such as creating or editing entities, the interface should respond immediately, providing visible feedback or progress indicators.

> User documentation and in-app guidance should be accessible and written in plain, unambiguous language. This ensures that users can complete their tasks and interpret validation results with confidence.

## Reliability

_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._

> System reliability is achieved through strong data integrity, comprehensive testing, and effective error handling.
All critical operations must be validated and tested using JUnit 5, ensuring consistent behaviour under both normal and exceptional conditions.
Test coverage should be high across functional components, and reports must be generated with JaCoCo to verify completeness.

> Database integrity constraints (including primary keys, foreign keys, and check conditions) must prevent invalid data and maintain referential integrity between modules.
If unexpected errors occur, such as a database failure, the system must handle them gracefully: the user should be informed clearly, while the error is logged for audit and debugging purposes.

> The system must be capable of recovering from partial failures without data loss.
At all times, it should maintain a consistent internal state and prevent data corruption during interruptions such as abrupt termination or power loss.

## Performance

_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

> The system must provide efficient execution and maintain a responsive user interface, even under typical operational loads.
Operations such as data loading, entity validation, and database access should be optimised to avoid noticeable delays.

> Database queries must be efficient, using appropriate indexes and optimised SQL statements to minimise latency.

> Resource management must be handled carefully: database connections must be closed after use, unnecessary objects released, and memory leaks prevented.

> Performance testing should confirm that the system can handle realistic datasets — including typical numbers of rail lines, segments, facilities, rolling stock, and cargo operations without perceptible degradation of responsiveness.

## Supportability

_The supportability requirements gathers several characteristics, such as: testability, adaptability, maintainability, compatibility, configurability, installability, scalability and more._

> The system must be designed for maintainability, extensibility, and ease of testing.
Its architecture should follow object-oriented design principles (including encapsulation, low coupling, and high cohesion) as well as recognised coding standards such as CamelCase naming and separation of concerns.

> To ensure maintainability and traceability, all public classes and methods must include Javadoc documentation that clearly describes their purpose and parameters.
Source code and related resources should be stored in the team's GitHub repository, organised by package and following consistent naming conventions.
All diagrams and figures must be stored in SVG format to ensure compatibility and effective version control.

> The system should be portable across any operating system that supports Java — including Windows, macOS, and Linux — and should be scalable to accommodate future extensions, such as new types of railway infrastructure elements, rolling stock, or cargo-handling rules.

## +

### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

> The project must be developed in Java, following an agile process based on SCRUM with four-week sprints.
Version control will be managed through GitHub, and all diagrams or figures must be created and maintained in SVG format.
Development must follow Test-Driven Development (TDD) methodology throughout all sprints.

### Implementation Constraints

_Specifies or constraints the code or construction of a system such as: mandatory standards/patterns, implementation languages, database integrity, resource limits, operating system._

> The application must be implemented primarily in Java, with the database defined and managed using PL/SQL, and station interaction components developed in C/Assembly as required.
All code must be designed for easy maintenance and future extension, adhering to sound object-oriented principles.
Javadoc documentation is mandatory for all public classes and methods, and comprehensive testing must be implemented using JUnit 5.

### Interface Constraints

_Specifies or constraints the features inherent to the interaction of the system being developed with other external systems._

> Integration between ESINF and other modules will be implemented through structured CSV file exchange (bays.csv, wagons.csv, items.csv, orders.csv, order_lines.csv, returns.csv) with comprehensive validation rules including SKU verification, quantity validation, and timestamp checks.

### Physical Constraints

_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._

> No specific physical constraints are imposed for now. The system should operate efficiently on standard development and production hardware capable of running Java applications and remote database connections.