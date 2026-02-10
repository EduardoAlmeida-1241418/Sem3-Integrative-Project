# USAC12 - Create a Text File with a Sequence of Actions

## 1. Requirements Engineering

### 1.1. User Story Description

> **As an Administrator**  
> **I want to create a text file (.txt) containing a sequence of actions performed by a specific user**  
> **So that the Station Management System can record and store user activities for auditing and analysis purposes.**

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

#### Scenario 1 – Successful creation of a valid log file
- **Given** a specific user exists in the system,  
  and the system has recorded a sequence of actions performed by this user,
- **When** the Administrator requests the creation of the action log file,
- **Then** the system must generate a text file containing:
  - **User information**: name, username, password (encrypted using the Caesar Cipher), and encryption key.
  - **Sensor configurations**: temperature and humidity circular buffer lengths, moving median window lengths.
  - **Tracks**: identifiers, states, and train assignments (if applicable).
  - **Trains**: identifiers.
  - **Logs**: identifiers, user identification, action description, and timestamp.
  - **Actions in correct chronological order**, properly formatted.

---

#### Scenario 2 – Handling an invalid or non-existent user
- **Given** the Administrator provides a username that does not correspond to any existing user,
- **When** the system processes the request to create a log file,
- **Then** the system must:
  - reject the operation,
  - not create any file,
  - and return a meaningful error message.

---

#### Scenario 3 – File creation errors
- **Given** file system issues such as lack of permissions or insufficient disk space,
- **When** the system tries to create the text file,
- **Then** it must:
  - fail gracefully,
  - log the error,
  - and inform the Administrator of the issue.

---

#### Scenario 4 – Data integrity
- **Given** that sensitive information must be securely stored,
- **When** the system writes the file,
- **Then** it must ensure:
  - passwords are encrypted using the Caesar Cipher,
  - all data exactly matches the current state of the system,
  - no corruption occurs during file writing.

---

### 1.4. Found Dependencies

- Full definition of user, track, train, sensor, and log data structures.
- Complete specification of the log file format.
- Working implementation of the Caesar Cipher encryption utility.
- System access to file writing operations with error handling.
- Existing Manager workflow for accessing user actions and system state.

---

### 1.5. Input and Output Data

**Input**
- Username of the user whose actions should be exported
- All associated system data:
  - User details
  - Sensor configuration values
  - Tracks and train assignments
  - Logs of actions and timestamps

**Output**
- A `.txt` file containing:
  - User details (with encrypted password)
  - Sensor configuration values
  - Tracks and trains
  - Logs with timestamps
  - All actions written in chronological sequence
- Error messages if:
  - the user does not exist,
  - file system errors occur,
  - or data cannot be retrieved.

---

### 1.6. Other Relevant Remarks

- **Special requirements**
  - The Caesar Cipher must be applied correctly using the provided key.
  - The text file must follow the agreed formatting rules for readability and consistency.
  - Logs must reflect the exact system state at generation time.
  - The system must avoid partial file creation if any error occurs.

- **Execution frequency**
  - This operation is performed whenever the Administrator requests an export of a user’s activity history.  
