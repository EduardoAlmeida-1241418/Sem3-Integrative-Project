# USAC13 - Retrieve Data from the Sensors Component

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a User**  
> **I want to get data from the Sensors component**  
> **So that I can obtain up-to-date temperature and humidity readings for the Station Management System.**

---

### 1.2. Customer Specifications and Clarifications

#### Customer enquiries
> **Question:**  
> – What format should the Sensors component use when returning temperature and humidity values?
>
> **Answer:**  
> – It must return a string formatted as:  
>   `TOKEN&unit:xxxxxxx&value:xx#TOKEN&unit:xxxxxxxx&value:xx`,  
>   where `TOKEN ∈ {TEMP, HUM}` and values correspond to Moving Median filtered readings.
>
> **Question:**  
> – How should the component behave if a GTH command is not received?
>
> **Answer:**  
> – The Sensors component must ignore invalid commands and keep waiting for a valid request.
>
> [Customer Clarification – Forum](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=919)

---

### 1.3. Acceptance Criteria

#### Scenario 1 – Successful retrieval of sensor data
- **Given** the Sensors component is powered, connected, and waiting for commands,  
  and the Manager sends a valid request to retrieve the latest sensor data,
- **When** the Sensors component processes this request,
- **Then** it must return a string formatted as: TOKEN&unit:xxxxxxx&value:xx#TOKEN&unit:xxxxxxxx&value:xx
  where `TOKEN ∈ {TEMP, HUM}`, and the values correspond to filtered readings using the Moving Median technique.

---

#### Scenario 2 – Handling sensor noise or invalid readings
- **Given** that raw sensor data may contain inconsistent or noisy values,
- **When** the Sensors component computes the Moving Median for temperature and humidity,
- **Then** it must:
- sort the last *n* readings,
- select the median value,
- and return this median as the final data to the Manager.

---

#### Scenario 3 – Communication failure with Manager
- **Given** a temporary failure in the communication link between Sensors and Manager,
- **When** a data request is sent or received,
- **Then** the Sensors component must:
- detect the communication problem,
- return an appropriate error status,
- and remain ready to process future valid requests.

---

#### Scenario 4 – Invalid or unexpected commands
- **Given** that the command received is not in the expected format (e.g., not `GTH`),
- **When** the Sensors component processes this command,
- **Then** it must:
- ignore the invalid command,
- and continue waiting for a valid request from the Manager.

---

### 1.4. Found Dependencies

- Communication protocol between Manager ↔ Sensors must be clearly defined.
- Moving Median filter implementation and buffer lengths must be available.
- Hardware configuration and sensor wiring must be functional.
- Error-handling routines for communication failures and invalid commands must exist.
- The Manager workflow for requesting sensor data must be integrated.

---

### 1.5. Input and Output Data

**Input**
- Command from Manager (expected: `GTH`)
- Raw sensor readings
- Last *n* values stored in the circular buffers for temperature and humidity

**Output**
- Filtered temperature and humidity in the format: TEMP&unit:<unit_temp>&value:<value_temp>#HUM&unit:<unit_hum>&value:<value_hum>
- Error status in case of communication failure
- No response when command is invalid (ignored)

---

### 1.6. Other Relevant Remarks

- **Special requirements**
- The Moving Median algorithm must be correctly applied before returning any data.
- Invalid commands must never cause failures or undefined behaviour.
- Communication must follow the predefined protocol at all times.

- **Execution frequency**
- This operation is performed every time the Manager requests updated sensor data.


