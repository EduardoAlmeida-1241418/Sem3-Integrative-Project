# USAC16 - Manage Railway Tracks of a Station

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a User**  
> **I want to manage the railway tracks of a station by assigning trains, releasing tracks, or setting their operational state**  
> **So that the Station Management System correctly coordinates track allocation, train movement, and signalling on the Board and LightSigns components.**

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:**  
> – What system components must react to a change in track state?
>
> **Answer:**  
> – The Board and LightSigns components must always be updated whenever a track’s state changes.
>
> **Question:**  
> – How should the system handle cases where a track cannot be assigned to a train?
>
> **Answer:**  
> – The system must issue an emergency stop order, log the action, and notify the relevant components.
>
> [Customer Clarification – Forum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Assign a track to an arriving train
- **Given** a train arrives at the station  
  and at least one track is available,
- **When** the User assigns the train to an available track,
- **Then** the system must:
  - allocate the selected track to the train,
  - update the Board to reflect the train’s new position,
  - update LightSigns to indicate the track is now occupied.

---

#### Scenario 2 – Emergency stop order when no track is available
- **Given** a train arrives and no tracks are available,
- **When** the User attempts to assign that train to a track,
- **Then** the system must:
  - issue an emergency stop order,
  - log the action with timestamp and user ID,
  - notify the Board and LightSigns components as necessary.

---

#### Scenario 3 – Set a track as nonoperational
- **Given** a track requires maintenance or must be blocked,
- **When** the User marks the track as nonoperational,
- **Then** the system must:
  - update the track’s operational state,
  - update the Board to reflect the change,
  - update LightSigns to indicate the track is unavailable or blocked.

---

#### Scenario 4 – Set a track as free
- **Given** a track previously occupied or nonoperational becomes available,
- **When** the User sets the track as free,
- **Then** the system must:
  - update the track’s state to free,
  - display the new state on the Board,
  - update LightSigns to indicate that the track is available.

---

#### Scenario 5 – Give departure order to a stopped train
- **Given** a train is currently stopped at a track,
- **When** the User issues a departure order,
- **Then** the system must:
  - update the corresponding track and train state,
  - update the Board to show the train departing,
  - adjust LightSigns accordingly based on the departure.

---

### 1.4. Found Dependencies

- Complete definitions of track, train, and station data structures.
- Integration rules with Board and LightSigns components.
- Operational state machine for tracks (free, occupied, nonoperational).
- User workflow for assigning and releasing tracks.
- Communication protocol between Manager ↔ Board ↔ LightSigns.
- Logging functionalities for user actions and system changes.

---

### 1.5. Input and Output Data

**Input**
- User-issued commands through the UI:
  - Assign train to track
  - Mark track as nonoperational
  - Mark track as free
  - Issue departure order
- Internal system data:
  - Track availability
  - Train states
  - Station operational rules

**Output**
- Updated states in:
  - Track structures
  - Train structures
  - LightSigns signals
  - Board display
- Log entries containing:
  - User ID
  - Timestamp
  - Action performed

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - All updates must propagate consistently to Board and LightSigns.
  - Emergency stop actions must always be logged and visually represented.
  - Invalid operations (e.g., setting free a track with no assigned train) must be handled safely.

- **Execution frequency**
  - This operation occurs whenever a train arrives, departs, or whenever the User changes the operational state of a track.
