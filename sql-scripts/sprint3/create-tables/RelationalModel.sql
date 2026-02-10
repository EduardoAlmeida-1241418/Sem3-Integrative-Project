CREATE TABLE Operator (
  ID_Operator varchar2(20) NOT NULL, 
  Name        varchar2(255) NOT NULL UNIQUE, 
  Short_Name  varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Operator));
CREATE TABLE Maker (
  ID_Maker number(10) NOT NULL, 
  Name     varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Maker));
CREATE TABLE Fuel_Type (
  ID_Fuel_Type number(10) NOT NULL, 
  Name         varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Fuel_Type));
CREATE TABLE Locomotive_Model (
  ID_Locomotive_Model     number(10) NOT NULL, 
  Name                    varchar2(255) NOT NULL UNIQUE, 
  Power                   number(10) NOT NULL, 
  Maximum_Weight          number(10) NOT NULL, 
  Acceleration            number(10) NOT NULL, 
  Number_Wheels           number(10) NOT NULL, 
  Max_Speed               number(10) NOT NULL, 
  Operational_Speed       number(10) NOT NULL, 
  Traction                number(10) NOT NULL, 
  MakerID_Maker           number(10) NOT NULL, 
  DimensionsID_Dimensions number(10) NOT NULL, 
  Fuel_TypeID_Fuel_Type   number(10) NOT NULL, 
  PRIMARY KEY (ID_Locomotive_Model), 
  CONSTRAINT CK_LOCOMOTIVE_MODEL_MAXIMUM_WEIGHT_POSITIVE 
    CHECK (Maximum_Weight > 0), 
  CONSTRAINT CK_POWER_POSITIVE 
    CHECK (Power > 0));
CREATE TABLE Dimensions (
  ID_Dimensions number(10) NOT NULL, 
  Length        number(10) NOT NULL, 
  Width         number(10) NOT NULL, 
  Height        number(10) NOT NULL, 
  Weight_Tare   number(10) NOT NULL, 
  PRIMARY KEY (ID_Dimensions), 
  CONSTRAINT CK_DIMENTIONS_LENGTH_POSITIVE 
    CHECK (Length > 0), 
  CONSTRAINT CK_WIDTH_POSITIVE 
    CHECK (Width > 0), 
  CONSTRAINT CK_HEIGHT_POSITIVE 
    CHECK (Height > 0), 
  CONSTRAINT CK_WEIGHT_TARE_POSITIVE 
    CHECK (Weight_Tare > 0));
CREATE TABLE Track_Gauge (
  ID_Track_Gauge number(10) NOT NULL, 
  Gauge_Size     number(10) NOT NULL, 
  PRIMARY KEY (ID_Track_Gauge), 
  CONSTRAINT CK_GAUGE_SIZE_POSITIVE 
    CHECK (Gauge_Size > 0));
CREATE TABLE Building (
  ID_Building                   number(10) NOT NULL, 
  Name                          varchar2(255) NOT NULL UNIQUE, 
  FacilityID_Facility           number(10) NOT NULL, 
  Building_TypeID_Building_Type number(10) NOT NULL, 
  PRIMARY KEY (ID_Building));
CREATE TABLE Facility (
  ID_Facility                   number(10) NOT NULL, 
  Name                          varchar2(255) NOT NULL UNIQUE, 
  Intermodal                    char(1) NOT NULL, 
  Facility_TypeID_Facility_Type number(10) NOT NULL, 
  PRIMARY KEY (ID_Facility), 
  CONSTRAINT CK_INTERMODEL_RANGE 
    CHECK (Intermodal BETWEEN 0 AND 1));
CREATE TABLE Facility_Type (
  ID_Facility_Type number(10) NOT NULL, 
  Name             varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Facility_Type));
CREATE TABLE Train (
                       ID_Train                    number(10) NOT NULL,
                       Scheduled_Construction_Date date NOT NULL,
                       Maximum_Length              number(10) NOT NULL,
                       Dispatched                  char(1) NOT NULL,
                       OperatorID_Operator         varchar2(20) NOT NULL,
                       RouteID_Route               number(10) NOT NULL,
                       PRIMARY KEY (ID_Train));
CREATE TABLE Owner (
  ID_Owner   varchar2(255) NOT NULL, 
  Name       varchar2(255) NOT NULL UNIQUE, 
  Short_Name varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Owner));
CREATE TABLE Rail_Line_Segment (
  ID_Rail_Line_Segment      number(10) NOT NULL, 
  Is_Electrified_Line       char(1) NOT NULL, 
  Maximum_Weight            number(10) NOT NULL, 
  Length                    number(10) NOT NULL, 
  Number_Tracks             number(10) NOT NULL, 
  Speed_Limit               number(10) NOT NULL, 
  Track_GaugeID_Track_Gauge number(10) NOT NULL, 
  ID_Siding                 number(10), 
  PRIMARY KEY (ID_Rail_Line_Segment), 
  CONSTRAINT CK_NUMBER_TRACKS_POSITIVE 
    CHECK (Number_Tracks > 0), 
  CONSTRAINT CK_RAIL_LINE_SEGMENT_MAXIMUM_WEIGHT_POSITIVE 
    CHECK (Maximum_Weight > 0), 
  CONSTRAINT CK_IS_ELECTRIFIED_LINE_RANGE 
    CHECK (Is_Electrified_Line BETWEEN 0 AND 1), 
  CONSTRAINT CK_SPEED_LIMIT_POSITIVE 
    CHECK (Speed_Limit > 0), 
  CONSTRAINT CK_RAIL_LINE_SEGMENT_LENGTH_POSITIVE 
    CHECK (Length > 0));
CREATE TABLE Rail_Line (
  ID_Rail_Line              number(10) NOT NULL, 
  Name                      varchar2(255) NOT NULL UNIQUE, 
  Start_FacilityID_Facility number(10) NOT NULL, 
  End_FacilityID_Facility   number(10) NOT NULL, 
  OwnerID_Owner             varchar2(255) NOT NULL, 
  PRIMARY KEY (ID_Rail_Line), 
  CONSTRAINT CK_START_FACILITY_END_FACILITY_DIFFERENT 
    CHECK (Start_FacilityID_Facility <> End_FacilityID_Facility));
CREATE TABLE Locomotive (
  ID_Locomotive                       number(10) NOT NULL, 
  Name                                varchar2(255) UNIQUE, 
  Starting_Date_Service               date NOT NULL, 
  Locomotive_ModelID_Locomotive_Model number(10) NOT NULL, 
  OperatorID_Operator                 varchar2(20) NOT NULL, 
  Start_Facility_ID                   number(10) NOT NULL, 
  PRIMARY KEY (ID_Locomotive));
CREATE TABLE Cargo_Type (
  ID_Cargo_Type number(10) NOT NULL, 
  Name          varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Cargo_Type));
CREATE TABLE Wagon_Model (
  ID_Wagon_Model          number(10) NOT NULL, 
  Name                    varchar2(255) NOT NULL, 
  Payload                 number(10) NOT NULL, 
  Volume_Capacity         number(10) NOT NULL, 
  Number_Wheels           number(10) NOT NULL, 
  Max_Speed               number(10) NOT NULL, 
  MakerID_Maker           number(10) NOT NULL, 
  DimensionsID_Dimensions number(10) NOT NULL, 
  Wagon_TypeID_Wagon_Type number(10) NOT NULL, 
  PRIMARY KEY (ID_Wagon_Model), 
  CONSTRAINT CK_PAYLOAD_POSITIVE 
    CHECK (Payload > 0), 
  CONSTRAINT CK_VOLUME_CAPACITY_POSITIVE 
    CHECK (Volume_Capacity > 0));
CREATE TABLE Wagon_Type (
  ID_Wagon_Type number(10) NOT NULL, 
  Name          varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Wagon_Type));
CREATE TABLE Wagon (
  ID_Wagon                  number(10) NOT NULL, 
  Starting_Date_Service     date NOT NULL, 
  OperatorID_Operator       varchar2(20) NOT NULL, 
  Wagon_ModelID_Wagon_Model number(10) NOT NULL, 
  Start_Facility_ID         number(10) NOT NULL, 
  PRIMARY KEY (ID_Wagon));
CREATE TABLE Rail_Line_Rail_Line_Segment (
  Rail_LineID_Rail_Line                 number(10) NOT NULL, 
  Rail_Line_SegmentID_Rail_Line_Segment number(10) NOT NULL, 
  Order_Line                            number(10) NOT NULL, 
  PRIMARY KEY (Rail_LineID_Rail_Line, 
  Rail_Line_SegmentID_Rail_Line_Segment), 
  CONSTRAINT CK_ORDER_LINE_POSITIVE 
    CHECK (Order_Line > 0));
CREATE TABLE Wagon_Type_Cargo_Type (
  Wagon_TypeID_Wagon_Type number(10) NOT NULL, 
  Cargo_TypeId_Cargo_Type number(10) NOT NULL, 
  PRIMARY KEY (Wagon_TypeID_Wagon_Type, 
  Cargo_TypeId_Cargo_Type));
CREATE TABLE Fuel_Diesel (
  ID_Fuel_Diesel        number(10) NOT NULL, 
  Fuel_TypeID_Fuel_Type number(10) NOT NULL, 
  Fuel_Capacity         number(10) NOT NULL, 
  PRIMARY KEY (ID_Fuel_Diesel, 
  Fuel_TypeID_Fuel_Type), 
  CONSTRAINT CK_FUEL_CAPACITY_POSITIVE 
    CHECK (Fuel_Capacity > 0));
CREATE TABLE Fuel_Electric (
  ID_Fuel_Eletric       number(10) NOT NULL, 
  Fuel_TypeID_Fuel_Type number(10) NOT NULL, 
  Voltage               number(10) NOT NULL, 
  Frequency             number(10) NOT NULL, 
  PRIMARY KEY (ID_Fuel_Eletric, 
  Fuel_TypeID_Fuel_Type));
CREATE TABLE Building_Type (
  ID_Building_Type number(10) NOT NULL, 
  Name             varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Building_Type));
CREATE TABLE Locomotive_Model_Track_Gauge (
  Locomotive_ModelID_Locomotive_Model number(10) NOT NULL, 
  Track_GaugeID_Track_Gauge           number(10) NOT NULL, 
  PRIMARY KEY (Locomotive_ModelID_Locomotive_Model, 
  Track_GaugeID_Track_Gauge));
CREATE TABLE Wagon_Model_Track_Gauge (
  Wagon_ModelID_Wagon_Model number(10) NOT NULL, 
  Track_GaugeID_Track_Gauge number(10) NOT NULL, 
  PRIMARY KEY (Wagon_ModelID_Wagon_Model, 
  Track_GaugeID_Track_Gauge));
CREATE TABLE Path (
  ID_Path number(10) NOT NULL, 
  PRIMARY KEY (ID_Path));
CREATE TABLE Freight (
  ID_Freight                     number(10) NOT NULL, 
  "Date"                         date NOT NULL, 
  OriginFacilityID_Facility      number(10) NOT NULL, 
  DestinationFacilityID_Facility number(10) NOT NULL, 
  RouteID_Route                  number(10), 
  PRIMARY KEY (ID_Freight));
CREATE TABLE Path_Facility (
  PathID_Path            number(10) NOT NULL, 
  FacilityID_Facility    number(10) NOT NULL, 
  Position_Facility_Path number(10) NOT NULL, 
  PRIMARY KEY (PathID_Path, 
  FacilityID_Facility));
CREATE TABLE Siding (
  ID_Siding number(10) NOT NULL, 
  Position  number(10) NOT NULL, 
  Length    number(10) NOT NULL, 
  PRIMARY KEY (ID_Siding));
CREATE TABLE Wagon_Freight (
  WagonID_Wagon     number(10) NOT NULL, 
  FreightID_Freight number(10) NOT NULL, 
  PRIMARY KEY (WagonID_Wagon, 
  FreightID_Freight));
CREATE TABLE Locomotive_Train (
  LocomotiveID_Locomotive number(10) NOT NULL, 
  TrainID_Train           number(10) NOT NULL, 
  PRIMARY KEY (LocomotiveID_Locomotive, 
  TrainID_Train));
CREATE TABLE Route (
  ID_Route    number(10) NOT NULL, 
  Is_Simple   char(1) NOT NULL, 
  PathID_Path number(10), 
  PRIMARY KEY (ID_Route));
CREATE TABLE Individual_Schedule (
  ID            number(10) GENERATED AS IDENTITY, 
  TrainID_Train number(10) NOT NULL, 
  Start_Time    timestamp(0) NOT NULL, 
  End_Time      timestamp(0) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Movement_Schedule (
  Individual_ScheduleID number(10) NOT NULL, 
  Start_Position        number(10) NOT NULL, 
  End_Position          number(10) NOT NULL, 
  PRIMARY KEY (Individual_ScheduleID));
CREATE TABLE Waiting_Schedule (
  Individual_ScheduleID number(10) NOT NULL, 
  Position              number(10) NOT NULL, 
  PRIMARY KEY (Individual_ScheduleID));
ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive933689 FOREIGN KEY (MakerID_Maker) REFERENCES Maker (ID_Maker);
ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive725959 FOREIGN KEY (DimensionsID_Dimensions) REFERENCES Dimensions (ID_Dimensions);
ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive978926 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);
ALTER TABLE Building ADD CONSTRAINT FKBuilding759564 FOREIGN KEY (FacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Facility ADD CONSTRAINT FKFacility216067 FOREIGN KEY (Facility_TypeID_Facility_Type) REFERENCES Facility_Type (ID_Facility_Type);
ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line335759 FOREIGN KEY (OwnerID_Owner) REFERENCES Owner (ID_Owner);
ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive143017 FOREIGN KEY (Locomotive_ModelID_Locomotive_Model) REFERENCES Locomotive_Model (ID_Locomotive_Model);
ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode561591 FOREIGN KEY (MakerID_Maker) REFERENCES Maker (ID_Maker);
ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode646138 FOREIGN KEY (DimensionsID_Dimensions) REFERENCES Dimensions (ID_Dimensions);
ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode838013 FOREIGN KEY (Wagon_TypeID_Wagon_Type) REFERENCES Wagon_Type (ID_Wagon_Type);
ALTER TABLE Wagon ADD CONSTRAINT FKWagon623925 FOREIGN KEY (Wagon_ModelID_Wagon_Model) REFERENCES Wagon_Model (ID_Wagon_Model);
ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive309427 FOREIGN KEY (OperatorID_Operator) REFERENCES Operator (ID_Operator);
ALTER TABLE Wagon ADD CONSTRAINT FKWagon882963 FOREIGN KEY (OperatorID_Operator) REFERENCES Operator (ID_Operator);
ALTER TABLE Rail_Line_Segment ADD CONSTRAINT FKRail_Line_877919 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);
ALTER TABLE Rail_Line_Rail_Line_Segment ADD CONSTRAINT FKRail_Line_388952 FOREIGN KEY (Rail_LineID_Rail_Line) REFERENCES Rail_Line (ID_Rail_Line);
ALTER TABLE Rail_Line_Rail_Line_Segment ADD CONSTRAINT FKRail_Line_298292 FOREIGN KEY (Rail_Line_SegmentID_Rail_Line_Segment) REFERENCES Rail_Line_Segment (ID_Rail_Line_Segment);
ALTER TABLE Wagon_Type_Cargo_Type ADD CONSTRAINT FKWagon_Type113622 FOREIGN KEY (Wagon_TypeID_Wagon_Type) REFERENCES Wagon_Type (ID_Wagon_Type);
ALTER TABLE Wagon_Type_Cargo_Type ADD CONSTRAINT FKWagon_Type807140 FOREIGN KEY (Cargo_TypeId_Cargo_Type) REFERENCES Cargo_Type (ID_Cargo_Type);
ALTER TABLE Fuel_Diesel ADD CONSTRAINT FKFuel_Diese130462 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);
ALTER TABLE Fuel_Electric ADD CONSTRAINT FKFuel_Elect719883 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);
ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line937323 FOREIGN KEY (Start_FacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line429012 FOREIGN KEY (End_FacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Building ADD CONSTRAINT FKBuilding562384 FOREIGN KEY (Building_TypeID_Building_Type) REFERENCES Building_Type (ID_Building_Type);
ALTER TABLE Locomotive_Model_Track_Gauge ADD CONSTRAINT FKLocomotive453955 FOREIGN KEY (Locomotive_ModelID_Locomotive_Model) REFERENCES Locomotive_Model (ID_Locomotive_Model);
ALTER TABLE Locomotive_Model_Track_Gauge ADD CONSTRAINT FKLocomotive245074 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);
ALTER TABLE Wagon_Model_Track_Gauge ADD CONSTRAINT FKWagon_Mode116598 FOREIGN KEY (Wagon_ModelID_Wagon_Model) REFERENCES Wagon_Model (ID_Wagon_Model);
ALTER TABLE Wagon_Model_Track_Gauge ADD CONSTRAINT FKWagon_Mode204442 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);
ALTER TABLE Path_Facility ADD CONSTRAINT FKPath_Facil307429 FOREIGN KEY (PathID_Path) REFERENCES Path (ID_Path);
ALTER TABLE Path_Facility ADD CONSTRAINT FKPath_Facil713261 FOREIGN KEY (FacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Freight ADD CONSTRAINT FKFreight476268 FOREIGN KEY (OriginFacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Freight ADD CONSTRAINT FKFreight311169 FOREIGN KEY (DestinationFacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Train ADD CONSTRAINT FKTrain612893 FOREIGN KEY (OperatorID_Operator) REFERENCES Operator (ID_Operator);
ALTER TABLE Wagon_Freight ADD CONSTRAINT FKWagon_Frei65798 FOREIGN KEY (WagonID_Wagon) REFERENCES Wagon (ID_Wagon);
ALTER TABLE Wagon_Freight ADD CONSTRAINT FKWagon_Frei497015 FOREIGN KEY (FreightID_Freight) REFERENCES Freight (ID_Freight);
ALTER TABLE Locomotive_Train ADD CONSTRAINT FKLocomotive976504 FOREIGN KEY (LocomotiveID_Locomotive) REFERENCES Locomotive (ID_Locomotive);
ALTER TABLE Locomotive_Train ADD CONSTRAINT FKLocomotive427396 FOREIGN KEY (TrainID_Train) REFERENCES Train (ID_Train);
ALTER TABLE Rail_Line_Segment ADD CONSTRAINT FKRail_Line_804365 FOREIGN KEY (ID_Siding) REFERENCES Siding (ID_Siding);
ALTER TABLE Wagon ADD CONSTRAINT FKWagon815520 FOREIGN KEY (Start_Facility_ID) REFERENCES Facility (ID_Facility);
ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive582533 FOREIGN KEY (Start_Facility_ID) REFERENCES Facility (ID_Facility);
ALTER TABLE Train ADD CONSTRAINT FKTrain97908 FOREIGN KEY (RouteID_Route) REFERENCES Route (ID_Route);
ALTER TABLE Freight ADD CONSTRAINT FKFreight188005 FOREIGN KEY (RouteID_Route) REFERENCES Route (ID_Route);
ALTER TABLE Route ADD CONSTRAINT FKRoute625189 FOREIGN KEY (PathID_Path) REFERENCES Path (ID_Path);
ALTER TABLE Individual_Schedule ADD CONSTRAINT FKIndividual752685 FOREIGN KEY (TrainID_Train) REFERENCES Train (ID_Train);
ALTER TABLE Movement_Schedule ADD CONSTRAINT FKMovement_S425614 FOREIGN KEY (Individual_ScheduleID) REFERENCES Individual_Schedule (ID);
ALTER TABLE Waiting_Schedule ADD CONSTRAINT FKWaiting_Sc813618 FOREIGN KEY (Individual_ScheduleID) REFERENCES Individual_Schedule (ID);
ALTER TABLE Waiting_Schedule ADD CONSTRAINT FKWaiting_Sc365642 FOREIGN KEY (Position) REFERENCES Facility (ID_Facility);
ALTER TABLE Movement_Schedule ADD CONSTRAINT FKMovement_S571088 FOREIGN KEY (Start_Position) REFERENCES Facility (ID_Facility);
ALTER TABLE Movement_Schedule ADD CONSTRAINT FKMovement_S871467 FOREIGN KEY (End_Position) REFERENCES Facility (ID_Facility);
