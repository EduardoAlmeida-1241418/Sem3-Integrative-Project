# USAC14 - Control the Track Sign Light

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a User**  
> **I want to control the track sign light**  
> **So that I can change the visual state of a track by activating the appropriate LED signal.**

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

#### Scenario 1 – Successful control of a track light
- **Given** the LightSigns component is connected, active, and waiting for commands,  
  and the User selects a valid track and valid LED state (red, yellow, green, blinking red),
- **When** the Manager sends the command `CMD,x` to LightSigns,
- **Then** LightSigns must:
  - turn **off all LEDs** of track *x*,
  - activate the LED specified by `CMD ∈ {RE, YE, GE, RB}`,
  - apply **blinking behaviour only if `CMD = RB`**.

---

#### Scenario 2 – Invalid track number
- **Given** the User selects a track number that does not exist,
- **When** the Manager attempts to send the command,
- **Then** the system must:
  - reject the command,
  - not change any LED states,
  - notify the User that the selected track number is invalid.

---

#### Scenario 3 – Invalid light command
- **Given** a command where `CMD` is not one of {RE, YE, GE, RB},
- **When** LightSigns receives the invalid command,
- **Then** it must:
  - ignore the command entirely,
  - keep the previous LED state unchanged.

---

#### Scenario 4 – Communication failure
- **Given** a temporary issue in communication between the Manager and LightSigns,
- **When** the Manager sends a `CMD,x` command,
- **Then** the system must:
  - detect the communication error,
  - handle the failure cleanly without crashing,
  - and remain ready to process future valid commands.

---

#### Scenario 5 – Integration with UI and Manager
- **Given** the User interacts with the UI to control a track’s signal,
- **When** the User issues a command such as “Change track state”,
- **Then** the Manager must:
  - validate the request,
  - construct the corresponding `CMD,x` command,
  - send it to LightSigns,
  - ensure the LED state matches the User’s selection.

---

### 1.4. Found Dependencies

- Definition of LightSigns commands (`RE`, `YE`, `GE`, `RB`).
- Range and identifiers of valid tracks.
- Communication protocol between Manager ↔ LightSigns.
- UI workflow for selecting track and signal state.
- LED hardware behaviour (on/off/blinking).
- Manager logic for validating and building `CMD,x` commands.

---

### 1.5. Input and Output Data

**Input**
- User selection from the UI:
  - Track number
  - Desired LED state (red, yellow, green, blinking red)
- Manager-generated command string:  
  `CMD,x` where:
  - `CMD ∈ {RE, YE, GE, RB}`
  - `x` = track number

**Output**
- LightSigns LED states for the specified track:
  - Red (steady)
  - Yellow
  - Green
  - Blinking red (RB)
- Error messages for:
  - Invalid track numbers
  - Invalid CMD values
  - Communication failures

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - `RB` must trigger a blinking red LED, not a steady one.
  - Invalid commands must never modify the track state.
  - LightSigns must remain responsive even after a communication failure.
  - The Manager is responsible for validating all UI instructions before sending commands.

- **Execution frequency**
  - This operation occurs every time the User requests a visual state change for any track.
