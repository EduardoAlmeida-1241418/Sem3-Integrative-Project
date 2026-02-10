# USLP03 - Estimated Travel Time Calculation

## 1. Requirements Engineering

### 1.1. User Story Description

> **As a** Traffic Dispatcher, **I want** to calculate the estimated travel time between two directly connected stations using a selected locomotive, **So that** I can plan train operations efficiently and ensure accurate scheduling.

### 1.2 Acceptance Criteria

- **Scenario 1 – Calculating the estimated travel time between two directly connected stations**

  > **Given** that the railway network data includes two stations with a direct connection between them,
  >
  > **And** that a specific locomotive has been selected for the trip,
  >
  > **When** the Traffic Dispatcher requests the estimated travel time between those two stations,
  >
  > **Then** the system must calculate the total duration considering the locomotive’s speed,
  >
  > **Then** acceleration must be assumed to be instantaneous, meaning the locomotive travels at a constant speed throughout the journey,
  >
  > **So that** the estimated travel time reflects the realistic duration of travel under constant speed conditions.

- **Scenario 2 – Presenting the ordered list of covered sections**

  > **Given** that the estimated travel time has been calculated for the selected locomotive and connection,
  >
  > **When** the results are displayed,
  >
  > **Then** the system must list all railway sections in the order in which they will be covered,
  >
  > **And** include the calculated travel time for each section as well as the total time for the trip,
  >
  > **So that** the Traffic Dispatcher can easily interpret the trip sequence and duration for scheduling and operational planning.
  
### 1.3 Found out Dependencies
>This User Story doesn’t depend on any US.

### 1.4 Input and Output Data
>**Input**
>
>- Start station, end station, and locomotive speed.
>
>**Output**
>
>- Travel time and section details displayed or logged.

### 1.5 Other Relevant Remarks
>**(i) Special requirements:**
>
>- Assume constant speed (instant acceleration).
>
>**(ii) How often this US is held:**
>
>- Executed whenever travel time estimation is needed.  