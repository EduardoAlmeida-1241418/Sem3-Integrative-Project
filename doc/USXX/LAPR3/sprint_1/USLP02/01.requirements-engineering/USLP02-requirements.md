# USLP02 - Implement Text-Based Interface for Cargo Handling

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Product Owner, **I want** the Cargo Handling functionality at the railway terminal to be presented through a user-friendly, text-based interface, **So that** the results of the system’s operations can be clearly understood and easily interpreted.

### 1.2 Acceptance Criteria
- **Scenario 1 – Accessing the Cargo Handling functionality through the text-based interface**

  > **Given** that all Cargo Handling features from USEI01 to USEI05 are fully implemented and available for integration,
  >
  > **And** that the interface design and functional scope have been defined in Figma,
  >
  > **When** the user launches the Cargo Handling module,
  >
  > **Then** the system must provide access through a user-friendly interface,
  >
  > **Then** this interface must be directly integrated with the Cargo Handling module,
  >
  > **So that** the user can clearly view and interpret the results of system operations.


- **Scenario 2 – Displaying and interacting with operation results**

  > **Given** that the interface is operational and connected to the Cargo Handling module,
  >
  > **When** the user performs actions such as unloading, dispatching, relocating, or inspecting cargo,
  >
  > **Then** the system must display the corresponding results in a clear and readable format,
  >
  > **Then** ensure that input and output operations are functional, intuitive, and easily interpreted by non-technical users,
  >
  > **So that** the interface facilitates understanding and effective decision-making during cargo management.


- **Scenario 3 – Validating and delivering the integrated interface**

  > **Given** that the JavaFX-based interface has been implemented and integrated with the Cargo Handling module,
  >
  > **When** the feature is presented during the Sprint Review,
  >
  > **Then** it must demonstrate full functionality, including accurate input/output operations and seamless interaction with the underlying system logic,
  >
  > **Then** the final source code must be committed under the directory `/src/main/`,
  >
  > **So that** the interface meets usability expectations and supports effective communication of Cargo Handling results to end users.
  
### 1.3 Found out Dependencies
>This User Story depends on USEI01–USEI05 (Cargo Handling Modules).

### 1.4 Input and Output Data
>**Input**
>
>- User commands for cargo handling operations.
>
>**Output**
>
>- Display of system responses (e.g., stock movement confirmations).

### 1.5 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Interface must handle input validation and clear error messages.
>
>**(ii) How often this US is held:**
>
>- Used throughout system operation for demonstration and testing.