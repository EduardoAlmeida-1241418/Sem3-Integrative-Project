# USAC15 - Send Data to the Board Component

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a User**  
> **I want to send data to the Board component**  
> **So that I can display information on the console-based synoptic board in a clear and visually engaging way.**

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

#### Scenario 1 – Successful display of data
- **Given** the Board component is running and connected to the Manager,  
  and the User provides valid data to display,
- **When** the Manager sends this data to the Board component,
- **Then** the Board must render the information on the console using a structured and visually organised format  
  (symbols, icons, boxes, lines) according to the system design.

---

#### Scenario 2 – Handling invalid or incomplete data
- **Given** that some data fields are missing or invalid,
- **When** the Board component receives the data,
- **Then** it must:
  - detect invalid or missing fields,
  - display only the valid portions,
  - clearly indicate missing values without crashing.

---

#### Scenario 3 – Concurrent operation with UI
- **Given** the Board and UI share the same console,
- **When** the Board displays information while the UI is active,
- **Then** the Board output must:
  - remain readable,
  - not overwrite UI input lines,
  - and maintain clarity of presentation.

---

#### Scenario 4 – Integration with Manager
- **Given** the User provides new information to be displayed,
- **When** the Manager sends this information to the Board,
- **Then** the Board must:
  - correctly render all presented content,
  - remain synchronised with the Manager,
  - and update the console accordingly.

---

#### Scenario 5 – Error handling for console output
- **Given** temporary console output issues or rendering failures,
- **When** the Board component attempts to display the information,
- **Then** it must:
  - handle the issue gracefully,
  - preserve the current display state if possible,
  - and remain ready to display new valid data.

---

### 1.4. Found Dependencies

- Data structures and system fields intended for display.
- Console rendering and formatting rules (symbols, borders, spacing).
- Communication protocol between Manager ↔ Board.
- Shared console use with UI.
- Manager workflow for transmitting display data.

---

### 1.5. Input and Output Data

**Input**
- Data provided by the User via the UI
- Formatted data sent by the Manager to the Board component
- Content may include:
  - Track information
  - Train statuses
  - Sensor readings
  - Logging details
  - Other system state information

**Output**
- Structured console display including:
  - Symbols
  - Icons
  - Boxes
  - Lines
  - Aligned and formatted text
- Error messages if:
  - fields are invalid or missing,
  - console display fails,
  - or synchronisation cannot be maintained.

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - Board output must never interfere with UI prompts or input fields.
  - Partial data must be represented clearly without ambiguous formatting.
  - Visual structure must follow the system design exactly.
  - Board must remain responsive even after an output error.

- **Execution frequency**
  - This operation is performed every time the User requests data to be displayed,  
    or whenever system updates require the Board to refresh its view.
