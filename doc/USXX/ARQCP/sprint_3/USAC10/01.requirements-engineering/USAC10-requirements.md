# USAC10 - Assemble Sensors and LightSigns Devices

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a System Administrator**  
> **I want to implement and assemble the Sensors and LightSigns components using C/C++ for Arduino**  
> **So that the Station Management System can obtain reliable temperature/humidity data and correctly control visual signalling for each track.**

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

#### Scenario 1 – Successful acquisition of temperature and humidity values
- **Given** the Sensors component is powered, wired, and waiting for commands,  
  and the Manager sends the **GTH** command,
- **When** the component collects the temperature and humidity readings and applies the **Moving Median** filter,
- **Then** it must return the correctly formatted string: TOKEN&unit:xxxxxxx&value:xx#TOKEN&unit:xxxxxxxx&value:xx
  where `TOKEN ∈ {TEMP, HUM}`, and the values correspond to the filtered sensor data.

---

#### Scenario 2 – Successful processing of LightSigns commands
- **Given** a valid command string `CMD,x`, where `CMD ∈ {RE, YE, GE, RB}` and `x` is a valid track number,
- **When** the LightSigns component receives the command,
- **Then** it must:
- turn **off all LEDs** for the specified track,
- activate the LED corresponding to `CMD`,
- ensure that `RB` triggers a **blinking red LED**,
- and remain responsive to new incoming commands.

---

#### Scenario 3 – Handling invalid or noisy sensor readings
- **Given** that raw sensor data may contain erroneous or inconsistent values,
- **When** the Moving Median filter processes the latest *n* samples,
- **Then** the system must:
- sort the collected values,
- compute the median correctly,
- and use it as the final value sent to the Manager.

---

#### Scenario 4 – Invalid LightSigns command
- **Given** a command where `CMD` is unknown or the track number is invalid,
- **When** the LightSigns component processes the command,
- **Then** it must:
- ignore the invalid command,
- keep the previous LED state unchanged,
- and avoid crashes, freezes, or undefined behaviour.

---

#### Scenario 5 – Shared hardware between Sensors and LightSigns
- **Given** that both components share the same Raspberry Pi Pico hardware platform,
- **When** they receive sequential commands from the Manager (e.g., `GTH`, `RE`, `YE`, `GE`, `RB`),
- **Then** both components must:
- operate without blocking each other,
- maintain independent functionality,
- and comply with the communication protocol established for Sprint 3.

---

### 1.4. Found Dependencies

- Communication protocol between Manager → Sensors → LightSigns must be defined.
- Integration with median, sorting, extraction and circular buffer logic (from previous sprints if applicable).
- Hardware limitations and GPIO mapping of the Raspberry Pi Pico must be respected.
- Interaction with Manager, UI, and Board components.

---

### 1.5. Input and Output Data

**Input**

- **Sensors**
- Command: `GTH`
- Raw readings: temperature and humidity
- Circular buffer storing the last *n* samples
- **LightSigns**
- Command: `CMD,x` where `CMD ∈ {RE, YE, GE, RB}` and `x` is a two-digit track number

**Output**

- **Sensors**
- String in the format:
  ```
  TEMP&unit:<unit_temp>&value:<value_temp>#HUM&unit:<unit_hum>&value:<value_hum>
  ```
- **LightSigns**
- Updated LED states per track (on/off, blinking when applicable)
- **General**
- Return codes indicating success or failure depending on implementation details

---

### 1.6. Other Relevant Remarks

- **Special requirements**
- Implementation must be done in **C/C++ for Arduino** or an equivalent environment compatible with Raspberry Pi Pico.
- Must integrate or reimplement Moving Median filtering using a circular buffer.
- Must ensure safe usage of shared hardware resources (GPIO, timers, interrupts).
- **Execution frequency**
- Sensor readings occur whenever the Manager requests `GTH`.
- LightSigns commands occur whenever a track state needs to change.  

