CREATE TABLE Operator (
  ID_Operator varchar2(20) NOT NULL, 
  Name        varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Operator));
CREATE TABLE Bogie_Type (
  ID_Bogie_Type number(10) NOT NULL, 
  Name          varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Bogie_Type));
CREATE TABLE Maker (
  ID_Maker number(10) NOT NULL, 
  Name     varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Maker));
CREATE TABLE Fuel_Type (
  ID_Fuel_Type number(10) NOT NULL, 
  Name         varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Fuel_Type));
CREATE TABLE Locomotive_Model (
  ID_Locomotive_Model       number(10) NOT NULL, 
  Name                      varchar2(255) NOT NULL, 
  Power                     number(10) NOT NULL, 
  Maximum_Weight            number(10) NOT NULL, 
  MakerID_Maker             number(10) NOT NULL, 
  DimensionsID_Dimensions   number(10) NOT NULL, 
  Fuel_TypeID_Fuel_Type     number(10) NOT NULL, 
  Track_GaugeID_Track_Gauge number(10) NOT NULL, 
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
  ID_Train number(10) NOT NULL, 
  Name     varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Train));
CREATE TABLE Owner (
  ID_Owner varchar2(255) NOT NULL, 
  Name     varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Owner));
CREATE TABLE Rail_Line_Segment (
  ID_Rail_Line_Segment      number(10) NOT NULL, 
  Is_Electrified_Line       char(1) NOT NULL, 
  Maximum_Weight            number(10) NOT NULL, 
  Length                    number(10) NOT NULL, 
  Number_Tracks             number(10) NOT NULL, 
  Has_Siding                char(1) NOT NULL, 
  Speed_Limit               number(10) NOT NULL, 
  Track_GaugeID_Track_Gauge number(10) NOT NULL, 
  PRIMARY KEY (ID_Rail_Line_Segment), 
  CONSTRAINT CK_NUMBER_TRACKS_POSITIVE 
    CHECK (Number_Tracks > 0), 
  CONSTRAINT CK_HAS_SIDING_RANGE 
    CHECK (Has_Siding BETWEEN 0 AND 1), 
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
  OwnerID_Owner             varchar2(255) NOT NULL, 
  Start_FacilityID_Facility number(10) NOT NULL, 
  End_FacilityID_Facility   number(10) NOT NULL, 
  PRIMARY KEY (ID_Rail_Line), 
  CONSTRAINT CK_START_FACILITY_END_FACILITY_DIFFERENT 
    CHECK (Start_FacilityID_Facility <> End_FacilityID_Facility));
CREATE TABLE Locomotive (
  ID_Locomotive                       number(10) NOT NULL, 
  Name                                varchar2(255) NOT NULL UNIQUE, 
  Starting_Year_Service               number(4) NOT NULL, 
  Locomotive_ModelID_Locomotive_Model number(10) NOT NULL, 
  OperatorID_Operator                 varchar2(20) NOT NULL, 
  TrainID_Train                       number(10), 
  PRIMARY KEY (ID_Locomotive), 
  CONSTRAINT CK_LOCOMOTIVE_STARTING_YEAR_SERVICE_POSITIVE 
    CHECK (Starting_Year_Service > 0));
CREATE TABLE Cargo_Type (
  ID_Cargo_Type number(10) NOT NULL, 
  Name          varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Cargo_Type));
CREATE TABLE Wagon_Model (
  ID_Wagon_Model            number(10) NOT NULL, 
  Name                      varchar2(255) NOT NULL, 
  Payload                   number(10) NOT NULL, 
  Volume_Capacity           number(10) NOT NULL, 
  MakerID_Maker             number(10) NOT NULL, 
  DimensionsID_Dimensions   number(10) NOT NULL, 
  Wagon_TypeID_Wagon_Type   number(10) NOT NULL, 
  Track_GaugeID_Track_Gauge number(10) NOT NULL, 
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
  Starting_Year_Service     number(4) NOT NULL, 
  OperatorID_Operator       varchar2(20) NOT NULL, 
  Wagon_ModelID_Wagon_Model number(10) NOT NULL, 
  TrainID_Train             number(10), 
  PRIMARY KEY (ID_Wagon), 
  CONSTRAINT CK_WAGON_STARTING_YEAR_SERVICE_POSITIVE 
    CHECK (Starting_Year_Service > 0));
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
  Fuel_TypeID_Fuel_Type number(10) NOT NULL, 
  Fuel_Capacity         number(10) NOT NULL, 
  PRIMARY KEY (Fuel_TypeID_Fuel_Type), 
  CONSTRAINT CK_FUEL_CAPACITY_POSITIVE 
    CHECK (Fuel_Capacity > 0));
CREATE TABLE Fuel_Electric (
  Fuel_TypeID_Fuel_Type number(10) NOT NULL, 
  PRIMARY KEY (Fuel_TypeID_Fuel_Type));
CREATE TABLE Bogie_Type_Locomotive_Model (
  Bogie_TypeID_Bogie_Type             number(10) NOT NULL, 
  Locomotive_ModelID_Locomotive_Model number(10) NOT NULL, 
  Bogie_Count                         number(10) NOT NULL, 
  PRIMARY KEY (Bogie_TypeID_Bogie_Type, 
  Locomotive_ModelID_Locomotive_Model), 
  CONSTRAINT CK_BOGIE_TYPE_LOCOMOTIVE_MODEL_BOGIE_COUNT_POSITIVE 
    CHECK (Bogie_Count > 0));
CREATE TABLE Bogie_Type_Wagon_Model (
  Bogie_TypeID_Bogie_Type   number(10) NOT NULL, 
  Wagon_ModelID_Wagon_Model number(10) NOT NULL, 
  Bogie_Count               number(10) NOT NULL, 
  PRIMARY KEY (Bogie_TypeID_Bogie_Type, 
  Wagon_ModelID_Wagon_Model), 
  CONSTRAINT CK_BOGIE_TYPE_WAGON_MODEL_BOGIE_COUNT_POSITIVE 
    CHECK (Bogie_Count > 0));
CREATE TABLE Building_Type (
  ID_Building_Type number(10) NOT NULL, 
  Name             varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID_Building_Type));
ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive933689 FOREIGN KEY (MakerID_Maker) REFERENCES Maker (ID_Maker);
ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive725959 FOREIGN KEY (DimensionsID_Dimensions) REFERENCES Dimensions (ID_Dimensions);
ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive978926 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);
ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive298061 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);
ALTER TABLE Building ADD CONSTRAINT FKBuilding759564 FOREIGN KEY (FacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Facility ADD CONSTRAINT FKFacility216067 FOREIGN KEY (Facility_TypeID_Facility_Type) REFERENCES Facility_Type (ID_Facility_Type);
ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line335759 FOREIGN KEY (OwnerID_Owner) REFERENCES Owner (ID_Owner);
ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive143017 FOREIGN KEY (Locomotive_ModelID_Locomotive_Model) REFERENCES Locomotive_Model (ID_Locomotive_Model);
ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode561591 FOREIGN KEY (MakerID_Maker) REFERENCES Maker (ID_Maker);
ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode646138 FOREIGN KEY (DimensionsID_Dimensions) REFERENCES Dimensions (ID_Dimensions);
ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode838013 FOREIGN KEY (Wagon_TypeID_Wagon_Type) REFERENCES Wagon_Type (ID_Wagon_Type);
ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode925962 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);
ALTER TABLE Wagon ADD CONSTRAINT FKWagon623925 FOREIGN KEY (Wagon_ModelID_Wagon_Model) REFERENCES Wagon_Model (ID_Wagon_Model);
ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive309427 FOREIGN KEY (OperatorID_Operator) REFERENCES Operator (ID_Operator);
ALTER TABLE Wagon ADD CONSTRAINT FKWagon882963 FOREIGN KEY (OperatorID_Operator) REFERENCES Operator (ID_Operator);
ALTER TABLE Rail_Line_Segment ADD CONSTRAINT FKRail_Line_877919 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);
ALTER TABLE Rail_Line_Rail_Line_Segment ADD CONSTRAINT FKRail_Line_388952 FOREIGN KEY (Rail_LineID_Rail_Line) REFERENCES Rail_Line (ID_Rail_Line);
ALTER TABLE Rail_Line_Rail_Line_Segment ADD CONSTRAINT FKRail_Line_298292 FOREIGN KEY (Rail_Line_SegmentID_Rail_Line_Segment) REFERENCES Rail_Line_Segment (ID_Rail_Line_Segment);
ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive512201 FOREIGN KEY (TrainID_Train) REFERENCES Train (ID_Train);
ALTER TABLE Wagon ADD CONSTRAINT FKWagon885852 FOREIGN KEY (TrainID_Train) REFERENCES Train (ID_Train);
ALTER TABLE Wagon_Type_Cargo_Type ADD CONSTRAINT FKWagon_Type113622 FOREIGN KEY (Wagon_TypeID_Wagon_Type) REFERENCES Wagon_Type (ID_Wagon_Type);
ALTER TABLE Wagon_Type_Cargo_Type ADD CONSTRAINT FKWagon_Type807140 FOREIGN KEY (Cargo_TypeId_Cargo_Type) REFERENCES Cargo_Type (ID_Cargo_Type);
ALTER TABLE Fuel_Diesel ADD CONSTRAINT FKFuel_Diese130462 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);
ALTER TABLE Fuel_Electric ADD CONSTRAINT FKFuel_Elect719883 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);
ALTER TABLE Bogie_Type_Locomotive_Model ADD CONSTRAINT FKBogie_Type274342 FOREIGN KEY (Bogie_TypeID_Bogie_Type) REFERENCES Bogie_Type (ID_Bogie_Type);
ALTER TABLE Bogie_Type_Locomotive_Model ADD CONSTRAINT FKBogie_Type588825 FOREIGN KEY (Locomotive_ModelID_Locomotive_Model) REFERENCES Locomotive_Model (ID_Locomotive_Model);
ALTER TABLE Bogie_Type_Wagon_Model ADD CONSTRAINT FKBogie_Type270194 FOREIGN KEY (Bogie_TypeID_Bogie_Type) REFERENCES Bogie_Type (ID_Bogie_Type);
ALTER TABLE Bogie_Type_Wagon_Model ADD CONSTRAINT FKBogie_Type294285 FOREIGN KEY (Wagon_ModelID_Wagon_Model) REFERENCES Wagon_Model (ID_Wagon_Model);
ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line937323 FOREIGN KEY (Start_FacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line429012 FOREIGN KEY (End_FacilityID_Facility) REFERENCES Facility (ID_Facility);
ALTER TABLE Building ADD CONSTRAINT FKBuilding562384 FOREIGN KEY (Building_TypeID_Building_Type) REFERENCES Building_Type (ID_Building_Type);
