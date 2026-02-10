package pt.ipp.isep.dei.data.config;

/**
 * Central holder for the database DDL and initial data used by the
 * application in development and tests.
 *
 * <p>This class exposes three public static constants: an array of
 * representative table names used for quick tests, a single large
 * string containing the complete DDL schema, and another string with
 * INSERT statements used to populate the database with sample data.
 * These constants are intended for use by test utilities and by code
 * that initialises an in‑memory or local development database.</p>
 *
 * <p>All comments added are in British English and are purely
 * descriptive; no executable code or behaviour has been altered.</p>
 */
public class DatabaseSchema {

    /**
     * A short list of table names used by lightweight tests.
     *
     * <p>This array contains a small selection of table names that may be
     * useful for quick verification or smoke tests which do not require
     * initialising the full database schema. The names are provided as
     * literal identifiers and should match the actual table names used
     * in {@link #schema}.</p>
     */
    public static final String[] tablesTest = {
            "Wagon_Freight",
            "Dimensions",
            "Rail_Line_Rail_Line_Segment",
            "Path_Facility"
    };

    /**
     * The full DDL schema used to create the database tables and
     * constraints.
     *
     * <p>This large string contains a sequence of CREATE TABLE and ALTER
     * TABLE statements that define the production schema used by the
     * system. It is intended for development and integration tests that
     * require a local database instance to be initialised from scratch.
     * The SQL dialect and execution context are the responsibility of the
     * caller; this class only provides the text.</p>
     */
    public static final String schema =
            "CREATE TABLE Operator (\n" +
                    "  ID_Operator varchar2(20) NOT NULL, \n" +
                    "  Name        varchar2(255) NOT NULL UNIQUE, \n" +
                    "  Short_Name  varchar2(255) NOT NULL UNIQUE, \n" +
                    "  PRIMARY KEY (ID_Operator));\n" +
                    "CREATE TABLE Maker (\n" +
                    "  ID_Maker number(10) NOT NULL, \n" +
                    "  Name     varchar2(255) NOT NULL UNIQUE, \n" +
                    "  PRIMARY KEY (ID_Maker));\n" +
                    "CREATE TABLE Fuel_Type (\n" +
                    "  ID_Fuel_Type number(10) NOT NULL, \n" +
                    "  Name         varchar2(255) NOT NULL UNIQUE, \n" +
                    "  PRIMARY KEY (ID_Fuel_Type));\n" +
                    "CREATE TABLE Locomotive_Model (\n" +
                    "  ID_Locomotive_Model     number(10) NOT NULL, \n" +
                    "  Name                    varchar2(255) NOT NULL UNIQUE, \n" +
                    "  Power                   number(10) NOT NULL, \n" +
                    "  Maximum_Weight          number(10) NOT NULL, \n" +
                    "  Acceleration            number(10) NOT NULL, \n" +
                    "  Number_Wheels           number(10) NOT NULL, \n" +
                    "  Max_Speed               number(10) NOT NULL, \n" +
                    "  Operational_Speed       number(10) NOT NULL, \n" +
                    "  Traction                number(10) NOT NULL, \n" +
                    "  MakerID_Maker           number(10) NOT NULL, \n" +
                    "  DimensionsID_Dimensions number(10) NOT NULL, \n" +
                    "  Fuel_TypeID_Fuel_Type   number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Locomotive_Model), \n" +
                    "  CONSTRAINT CK_LOCOMOTIVE_MODEL_MAXIMUM_WEIGHT_POSITIVE \n" +
                    "    CHECK (Maximum_Weight > 0), \n" +
                    "  CONSTRAINT CK_POWER_POSITIVE \n" +
                    "    CHECK (Power > 0));\n" +
                    "CREATE TABLE Dimensions (\n" +
                    "  ID_Dimensions number(10) NOT NULL, \n" +
                    "  Length        number(10) NOT NULL, \n" +
                    "  Width         number(10) NOT NULL, \n" +
                    "  Height        number(10) NOT NULL, \n" +
                    "  Weight_Tare   number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Dimensions), \n" +
                    "  CONSTRAINT CK_DIMENTIONS_LENGTH_POSITIVE \n" +
                    "    CHECK (Length > 0), \n" +
                    "  CONSTRAINT CK_WIDTH_POSITIVE \n" +
                    "    CHECK (Width > 0), \n" +
                    "  CONSTRAINT CK_HEIGHT_POSITIVE \n" +
                    "    CHECK (Height > 0), \n" +
                    "  CONSTRAINT CK_WEIGHT_TARE_POSITIVE \n" +
                    "    CHECK (Weight_Tare > 0));\n" +
                    "CREATE TABLE Track_Gauge (\n" +
                    "  ID_Track_Gauge number(10) NOT NULL, \n" +
                    "  Gauge_Size     number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Track_Gauge), \n" +
                    "  CONSTRAINT CK_GAUGE_SIZE_POSITIVE \n" +
                    "    CHECK (Gauge_Size > 0));\n" +
                    "CREATE TABLE Building (\n" +
                    "  ID_Building                   number(10) NOT NULL, \n" +
                    "  Name                          varchar2(255) NOT NULL UNIQUE, \n" +
                    "  FacilityID_Facility           number(10) NOT NULL, \n" +
                    "  Building_TypeID_Building_Type number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Building));\n" +
                    "CREATE TABLE Facility (\n" +
                    "  ID_Facility                   number(10) NOT NULL, \n" +
                    "  Name                          varchar2(255) NOT NULL UNIQUE, \n" +
                    "  Intermodal                    char(1) NOT NULL, \n" +
                    "  Facility_TypeID_Facility_Type number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Facility), \n" +
                    "  CONSTRAINT CK_INTERMODEL_RANGE \n" +
                    "    CHECK (Intermodal BETWEEN 0 AND 1));\n" +
                    "CREATE TABLE Facility_Type (\n" +
                    "  ID_Facility_Type number(10) NOT NULL, \n" +
                    "  Name             varchar2(255) NOT NULL UNIQUE, \n" +
                    "  PRIMARY KEY (ID_Facility_Type));\n" +
                    "CREATE TABLE Train (\n" +
                    "  ID_Train                    number(10) NOT NULL, \n" +
                    "  Scheduled_Construction_Date date NOT NULL, \n" +
                    "  Dispatched                  char(1) NOT NULL, \n" +
                    "  OperatorID_Operator         varchar2(20) NOT NULL, \n" +
                    "  RouteID_Route               number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Train));\n" +
                    "CREATE TABLE Owner (\n" +
                    "  ID_Owner   varchar2(255) NOT NULL, \n" +
                    "  Name       varchar2(255) NOT NULL UNIQUE, \n" +
                    "  Short_Name varchar2(255) NOT NULL UNIQUE, \n" +
                    "  PRIMARY KEY (ID_Owner));\n" +
                    "CREATE TABLE Rail_Line_Segment (\n" +
                    "  ID_Rail_Line_Segment      number(10) NOT NULL, \n" +
                    "  Is_Electrified_Line       char(1) NOT NULL, \n" +
                    "  Maximum_Weight            number(10) NOT NULL, \n" +
                    "  Length                    number(10) NOT NULL, \n" +
                    "  Number_Tracks             number(10) NOT NULL, \n" +
                    "  Speed_Limit               number(10) NOT NULL, \n" +
                    "  Track_GaugeID_Track_Gauge number(10) NOT NULL, \n" +
                    "  ID_Siding                 number(10), \n" +
                    "  PRIMARY KEY (ID_Rail_Line_Segment), \n" +
                    "  CONSTRAINT CK_NUMBER_TRACKS_POSITIVE \n" +
                    "    CHECK (Number_Tracks > 0), \n" +
                    "  CONSTRAINT CK_RAIL_LINE_SEGMENT_MAXIMUM_WEIGHT_POSITIVE \n" +
                    "    CHECK (Maximum_Weight > 0), \n" +
                    "  CONSTRAINT CK_IS_ELECTRIFIED_LINE_RANGE \n" +
                    "    CHECK (Is_Electrified_Line BETWEEN 0 AND 1), \n" +
                    "  CONSTRAINT CK_SPEED_LIMIT_POSITIVE \n" +
                    "    CHECK (Speed_Limit > 0), \n" +
                    "  CONSTRAINT CK_RAIL_LINE_SEGMENT_LENGTH_POSITIVE \n" +
                    "    CHECK (Length > 0));\n" +
                    "CREATE TABLE Rail_Line (\n" +
                    "  ID_Rail_Line              number(10) NOT NULL, \n" +
                    "  Name                      varchar2(255) NOT NULL UNIQUE, \n" +
                    "  Start_FacilityID_Facility number(10) NOT NULL, \n" +
                    "  End_FacilityID_Facility   number(10) NOT NULL, \n" +
                    "  OwnerID_Owner             varchar2(255) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Rail_Line), \n" +
                    "  CONSTRAINT CK_START_FACILITY_END_FACILITY_DIFFERENT \n" +
                    "    CHECK (Start_FacilityID_Facility <> End_FacilityID_Facility));\n" +
                    "CREATE TABLE Locomotive (\n" +
                    "  ID_Locomotive                       number(10) NOT NULL, \n" +
                    "  Name                                varchar2(255) UNIQUE, \n" +
                    "  Starting_Date_Service               date NOT NULL, \n" +
                    "  Locomotive_ModelID_Locomotive_Model number(10) NOT NULL, \n" +
                    "  OperatorID_Operator                 varchar2(20) NOT NULL, \n" +
                    "  Start_Facility_ID                   number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Locomotive));\n" +
                    "CREATE TABLE Cargo_Type (\n" +
                    "  ID_Cargo_Type number(10) NOT NULL, \n" +
                    "  Name          varchar2(255) NOT NULL UNIQUE, \n" +
                    "  PRIMARY KEY (ID_Cargo_Type));\n" +
                    "CREATE TABLE Wagon_Model (\n" +
                    "  ID_Wagon_Model          number(10) NOT NULL, \n" +
                    "  Name                    varchar2(255) NOT NULL, \n" +
                    "  Payload                 number(10) NOT NULL, \n" +
                    "  Volume_Capacity         number(10) NOT NULL, \n" +
                    "  Number_Wheels           number(10) NOT NULL, \n" +
                    "  Max_Speed               number(10) NOT NULL, \n" +
                    "  MakerID_Maker           number(10) NOT NULL, \n" +
                    "  DimensionsID_Dimensions number(10) NOT NULL, \n" +
                    "  Wagon_TypeID_Wagon_Type number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Wagon_Model), \n" +
                    "  CONSTRAINT CK_PAYLOAD_POSITIVE \n" +
                    "    CHECK (Payload > 0), \n" +
                    "  CONSTRAINT CK_VOLUME_CAPACITY_POSITIVE \n" +
                    "    CHECK (Volume_Capacity > 0));\n" +
                    "CREATE TABLE Wagon_Type (\n" +
                    "  ID_Wagon_Type number(10) NOT NULL, \n" +
                    "  Name          varchar2(255) NOT NULL UNIQUE, \n" +
                    "  PRIMARY KEY (ID_Wagon_Type));\n" +
                    "CREATE TABLE Wagon (\n" +
                    "  ID_Wagon                  number(10) NOT NULL, \n" +
                    "  Starting_Date_Service     date NOT NULL, \n" +
                    "  OperatorID_Operator       varchar2(20) NOT NULL, \n" +
                    "  Wagon_ModelID_Wagon_Model number(10) NOT NULL, \n" +
                    "  Start_Facility_ID         number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Wagon));\n" +
                    "CREATE TABLE Rail_Line_Rail_Line_Segment (\n" +
                    "  Rail_LineID_Rail_Line                 number(10) NOT NULL, \n" +
                    "  Rail_Line_SegmentID_Rail_Line_Segment number(10) NOT NULL, \n" +
                    "  Order_Line                            number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (Rail_LineID_Rail_Line, \n" +
                    "  Rail_Line_SegmentID_Rail_Line_Segment), \n" +
                    "  CONSTRAINT CK_ORDER_LINE_POSITIVE \n" +
                    "    CHECK (Order_Line > 0));\n" +
                    "CREATE TABLE Wagon_Type_Cargo_Type (\n" +
                    "  Wagon_TypeID_Wagon_Type number(10) NOT NULL, \n" +
                    "  Cargo_TypeId_Cargo_Type number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (Wagon_TypeID_Wagon_Type, \n" +
                    "  Cargo_TypeId_Cargo_Type));\n" +
                    "CREATE TABLE Fuel_Diesel (\n" +
                    "  ID_Fuel_Diesel        number(10) NOT NULL, \n" +
                    "  Fuel_TypeID_Fuel_Type number(10) NOT NULL, \n" +
                    "  Fuel_Capacity         number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Fuel_Diesel, \n" +
                    "  Fuel_TypeID_Fuel_Type), \n" +
                    "  CONSTRAINT CK_FUEL_CAPACITY_POSITIVE \n" +
                    "    CHECK (Fuel_Capacity > 0));\n" +
                    "CREATE TABLE Fuel_Electric (\n" +
                    "  ID_Fuel_Eletric       number(10) NOT NULL, \n" +
                    "  Fuel_TypeID_Fuel_Type number(10) NOT NULL, \n" +
                    "  Voltage               number(10) NOT NULL, \n" +
                    "  Frequency             number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Fuel_Eletric, \n" +
                    "  Fuel_TypeID_Fuel_Type));\n" +
                    "CREATE TABLE Building_Type (\n" +
                    "  ID_Building_Type number(10) NOT NULL, \n" +
                    "  Name             varchar2(255) NOT NULL UNIQUE, \n" +
                    "  PRIMARY KEY (ID_Building_Type));\n" +
                    "CREATE TABLE Locomotive_Model_Track_Gauge (\n" +
                    "  Locomotive_ModelID_Locomotive_Model number(10) NOT NULL, \n" +
                    "  Track_GaugeID_Track_Gauge           number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (Locomotive_ModelID_Locomotive_Model, \n" +
                    "  Track_GaugeID_Track_Gauge));\n" +
                    "CREATE TABLE Wagon_Model_Track_Gauge (\n" +
                    "  Wagon_ModelID_Wagon_Model number(10) NOT NULL, \n" +
                    "  Track_GaugeID_Track_Gauge number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (Wagon_ModelID_Wagon_Model, \n" +
                    "  Track_GaugeID_Track_Gauge));\n" +
                    "CREATE TABLE Path (\n" +
                    "  ID_Path number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Path));\n" +
                    "CREATE TABLE Freight (\n" +
                    "  ID_Freight                     number(10) NOT NULL, \n" +
                    "  \"Date\"                         date NOT NULL, \n" +
                    "  OriginFacilityID_Facility      number(10) NOT NULL, \n" +
                    "  DestinationFacilityID_Facility number(10) NOT NULL, \n" +
                    "  RouteID_Route                  number(10), \n" +
                    "  PRIMARY KEY (ID_Freight));\n" +
                    "CREATE TABLE Path_Facility (\n" +
                    "  PathID_Path            number(10) NOT NULL, \n" +
                    "  FacilityID_Facility    number(10) NOT NULL, \n" +
                    "  Position_Facility_Path number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (PathID_Path, \n" +
                    "  FacilityID_Facility));\n" +
                    "CREATE TABLE Siding (\n" +
                    "  ID_Siding number(10) NOT NULL, \n" +
                    "  Position  number(10) NOT NULL, \n" +
                    "  Length    number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Siding));\n" +
                    "CREATE TABLE Wagon_Freight (\n" +
                    "  WagonID_Wagon     number(10) NOT NULL, \n" +
                    "  FreightID_Freight number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (WagonID_Wagon, \n" +
                    "  FreightID_Freight));\n" +
                    "CREATE TABLE Locomotive_Train (\n" +
                    "  LocomotiveID_Locomotive number(10) NOT NULL, \n" +
                    "  TrainID_Train           number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (LocomotiveID_Locomotive, \n" +
                    "  TrainID_Train));\n" +
                    "CREATE TABLE Route (\n" +
                    "  ID_Route    number(10) NOT NULL, \n" +
                    "  Is_Simple   char(1), \n" +
                    "  PathID_Path number(10), \n" +
                    "  PRIMARY KEY (ID_Route));\n" +
                    "CREATE TABLE Position (\n" +
                    "  ID_Position         number(10) NOT NULL, \n" +
                    "  Type                number(10) NOT NULL, \n" +
                    "  FacilityID_Facility number(10), \n" +
                    "  SidingID_Siding     number(10), \n" +
                    "  DividedID           number(10), \n" +
                    "  PRIMARY KEY (ID_Position));\n" +
                    "CREATE TABLE Schedule_Event (\n" +
                    "  ID_Schedule_Event  number(10) NOT NULL, \n" +
                    "  Start_Time         date NOT NULL, \n" +
                    "  End_Time           date NOT NULL, \n" +
                    "  Type               varchar2(255) NOT NULL, \n" +
                    "  Start_Position     number(10) NOT NULL, \n" +
                    "  End_Position       number(10) NOT NULL, \n" +
                    "  TrainID_Train      number(10) NOT NULL, \n" +
                    "  General_ScheduleID number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Schedule_Event));\n" +
                    "CREATE TABLE General_Schedule (\n" +
                    "  ID_General_Schedule number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_General_Schedule));\n" +
                    "CREATE TABLE Position_Rail_Line_Segment (\n" +
                    "  PositionId                            number(10) NOT NULL, \n" +
                    "  Rail_Line_SegmentID_Rail_Line_Segment number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (PositionId, \n" +
                    "  Rail_Line_SegmentID_Rail_Line_Segment));\n" +
                    "CREATE TABLE Divided (\n" +
                    "  ID_Divided           number(10) NOT NULL, \n" +
                    "  ID_Order             number(10) NOT NULL, \n" +
                    "  Length               number(10) NOT NULL, \n" +
                    "  ID_Rail_Line_Segment number(10) NOT NULL, \n" +
                    "  PRIMARY KEY (ID_Divided));\n" +
                    "ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive933689 FOREIGN KEY (MakerID_Maker) REFERENCES Maker (ID_Maker);\n" +
                    "ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive725959 FOREIGN KEY (DimensionsID_Dimensions) REFERENCES Dimensions (ID_Dimensions);\n" +
                    "ALTER TABLE Locomotive_Model ADD CONSTRAINT FKLocomotive978926 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);\n" +
                    "ALTER TABLE Building ADD CONSTRAINT FKBuilding759564 FOREIGN KEY (FacilityID_Facility) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Facility ADD CONSTRAINT FKFacility216067 FOREIGN KEY (Facility_TypeID_Facility_Type) REFERENCES Facility_Type (ID_Facility_Type);\n" +
                    "ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line335759 FOREIGN KEY (OwnerID_Owner) REFERENCES Owner (ID_Owner);\n" +
                    "ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive143017 FOREIGN KEY (Locomotive_ModelID_Locomotive_Model) REFERENCES Locomotive_Model (ID_Locomotive_Model);\n" +
                    "ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode561591 FOREIGN KEY (MakerID_Maker) REFERENCES Maker (ID_Maker);\n" +
                    "ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode646138 FOREIGN KEY (DimensionsID_Dimensions) REFERENCES Dimensions (ID_Dimensions);\n" +
                    "ALTER TABLE Wagon_Model ADD CONSTRAINT FKWagon_Mode838013 FOREIGN KEY (Wagon_TypeID_Wagon_Type) REFERENCES Wagon_Type (ID_Wagon_Type);\n" +
                    "ALTER TABLE Wagon ADD CONSTRAINT FKWagon623925 FOREIGN KEY (Wagon_ModelID_Wagon_Model) REFERENCES Wagon_Model (ID_Wagon_Model);\n" +
                    "ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive309427 FOREIGN KEY (OperatorID_Operator) REFERENCES Operator (ID_Operator);\n" +
                    "ALTER TABLE Wagon ADD CONSTRAINT FKWagon882963 FOREIGN KEY (OperatorID_Operator) REFERENCES Operator (ID_Operator);\n" +
                    "ALTER TABLE Rail_Line_Segment ADD CONSTRAINT FKRail_Line_877919 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);\n" +
                    "ALTER TABLE Rail_Line_Rail_Line_Segment ADD CONSTRAINT FKRail_Line_388952 FOREIGN KEY (Rail_LineID_Rail_Line) REFERENCES Rail_Line (ID_Rail_Line);\n" +
                    "ALTER TABLE Rail_Line_Rail_Line_Segment ADD CONSTRAINT FKRail_Line_298292 FOREIGN KEY (Rail_Line_SegmentID_Rail_Line_Segment) REFERENCES Rail_Line_Segment (ID_Rail_Line_Segment);\n" +
                    "ALTER TABLE Wagon_Type_Cargo_Type ADD CONSTRAINT FKWagon_Type113622 FOREIGN KEY (Wagon_TypeID_Wagon_Type) REFERENCES Wagon_Type (ID_Wagon_Type);\n" +
                    "ALTER TABLE Wagon_Type_Cargo_Type ADD CONSTRAINT FKWagon_Type807140 FOREIGN KEY (Cargo_TypeId_Cargo_Type) REFERENCES Cargo_Type (ID_Cargo_Type);\n" +
                    "ALTER TABLE Fuel_Diesel ADD CONSTRAINT FKFuel_Diese130462 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);\n" +
                    "ALTER TABLE Fuel_Electric ADD CONSTRAINT FKFuel_Elect719883 FOREIGN KEY (Fuel_TypeID_Fuel_Type) REFERENCES Fuel_Type (ID_Fuel_Type);\n" +
                    "ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line937323 FOREIGN KEY (Start_FacilityID_Facility) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Rail_Line ADD CONSTRAINT FKRail_Line429012 FOREIGN KEY (End_FacilityID_Facility) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Building ADD CONSTRAINT FKBuilding562384 FOREIGN KEY (Building_TypeID_Building_Type) REFERENCES Building_Type (ID_Building_Type);\n" +
                    "ALTER TABLE Locomotive_Model_Track_Gauge ADD CONSTRAINT FKLocomotive453955 FOREIGN KEY (Locomotive_ModelID_Locomotive_Model) REFERENCES Locomotive_Model (ID_Locomotive_Model);\n" +
                    "ALTER TABLE Locomotive_Model_Track_Gauge ADD CONSTRAINT FKLocomotive245074 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);\n" +
                    "ALTER TABLE Wagon_Model_Track_Gauge ADD CONSTRAINT FKWagon_Mode116598 FOREIGN KEY (Wagon_ModelID_Wagon_Model) REFERENCES Wagon_Model (ID_Wagon_Model);\n" +
                    "ALTER TABLE Wagon_Model_Track_Gauge ADD CONSTRAINT FKWagon_Mode204442 FOREIGN KEY (Track_GaugeID_Track_Gauge) REFERENCES Track_Gauge (ID_Track_Gauge);\n" +
                    "ALTER TABLE Path_Facility ADD CONSTRAINT FKPath_Facil307429 FOREIGN KEY (PathID_Path) REFERENCES Path (ID_Path);\n" +
                    "ALTER TABLE Path_Facility ADD CONSTRAINT FKPath_Facil713261 FOREIGN KEY (FacilityID_Facility) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Freight ADD CONSTRAINT FKFreight476268 FOREIGN KEY (OriginFacilityID_Facility) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Freight ADD CONSTRAINT FKFreight311169 FOREIGN KEY (DestinationFacilityID_Facility) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Train ADD CONSTRAINT FKTrain612893 FOREIGN KEY (OperatorID_Operator) REFERENCES Operator (ID_Operator);\n" +
                    "ALTER TABLE Wagon_Freight ADD CONSTRAINT FKWagon_Frei65798 FOREIGN KEY (WagonID_Wagon) REFERENCES Wagon (ID_Wagon);\n" +
                    "ALTER TABLE Wagon_Freight ADD CONSTRAINT FKWagon_Frei497015 FOREIGN KEY (FreightID_Freight) REFERENCES Freight (ID_Freight);\n" +
                    "ALTER TABLE Locomotive_Train ADD CONSTRAINT FKLocomotive976504 FOREIGN KEY (LocomotiveID_Locomotive) REFERENCES Locomotive (ID_Locomotive);\n" +
                    "ALTER TABLE Locomotive_Train ADD CONSTRAINT FKLocomotive427396 FOREIGN KEY (TrainID_Train) REFERENCES Train (ID_Train);\n" +
                    "ALTER TABLE Rail_Line_Segment ADD CONSTRAINT FKRail_Line_804365 FOREIGN KEY (ID_Siding) REFERENCES Siding (ID_Siding);\n" +
                    "ALTER TABLE Wagon ADD CONSTRAINT FKWagon815520 FOREIGN KEY (Start_Facility_ID) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Locomotive ADD CONSTRAINT FKLocomotive582533 FOREIGN KEY (Start_Facility_ID) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Train ADD CONSTRAINT FKTrain97908 FOREIGN KEY (RouteID_Route) REFERENCES Route (ID_Route);\n" +
                    "ALTER TABLE Freight ADD CONSTRAINT FKFreight188005 FOREIGN KEY (RouteID_Route) REFERENCES Route (ID_Route);\n" +
                    "ALTER TABLE Route ADD CONSTRAINT FKRoute625189 FOREIGN KEY (PathID_Path) REFERENCES Path (ID_Path);\n" +
                    "ALTER TABLE Position ADD CONSTRAINT FKPosition693674 FOREIGN KEY (FacilityID_Facility) REFERENCES Facility (ID_Facility);\n" +
                    "ALTER TABLE Position ADD CONSTRAINT FKPosition712653 FOREIGN KEY (SidingID_Siding) REFERENCES Siding (ID_Siding);\n" +
                    "ALTER TABLE Schedule_Event ADD CONSTRAINT FKSchedule_E386556 FOREIGN KEY (Start_Position) REFERENCES Position (ID_Position);\n" +
                    "ALTER TABLE Schedule_Event ADD CONSTRAINT FKSchedule_E829112 FOREIGN KEY (End_Position) REFERENCES Position (ID_Position);\n" +
                    "ALTER TABLE Schedule_Event ADD CONSTRAINT FKSchedule_E438614 FOREIGN KEY (TrainID_Train) REFERENCES Train (ID_Train);\n" +
                    "ALTER TABLE Schedule_Event ADD CONSTRAINT FKSchedule_E400924 FOREIGN KEY (General_ScheduleID) REFERENCES General_Schedule (ID_General_Schedule);\n" +
                    "ALTER TABLE Position_Rail_Line_Segment ADD CONSTRAINT FKPosition_R856818 FOREIGN KEY (PositionId) REFERENCES Position (ID_Position);\n" +
                    "ALTER TABLE Position_Rail_Line_Segment ADD CONSTRAINT FKPosition_R646311 FOREIGN KEY (Rail_Line_SegmentID_Rail_Line_Segment) REFERENCES Rail_Line_Segment (ID_Rail_Line_Segment);\n" +
                    "ALTER TABLE Divided ADD CONSTRAINT FKDivided963554 FOREIGN KEY (ID_Rail_Line_Segment) REFERENCES Rail_Line_Segment (ID_Rail_Line_Segment);\n" +
                    "ALTER TABLE Position ADD CONSTRAINT FKPosition660609 FOREIGN KEY (DividedID) REFERENCES Divided (ID_Divided);\n";

    /**
     * Sample data inserts for the database, covering all tables and
     * relationships.
     *
     * <p>This large string contains a sequence of INSERT statements that
     * populate the database with sample data for development and testing
     * purposes. The data includes representative values for all columns
     * and is intended to exercise the full range of functionality in the
     * application. Care has been taken to ensure that the data is
     * consistent and valid according to the constraints defined in the
     * schema.</p>
     */
    public static final String inserts = "-- Operator\n" +
            "INSERT INTO Operator (ID_Operator, Name, Short_Name)\n" +
            "VALUES ('PT509017800', 'Medway - Operador Ferroviário de Mercadorias, S.A', 'Medway');\n" +
            "INSERT INTO Operator (ID_Operator, Name, Short_Name)\n" +
            "VALUES ('PT507832388', 'Captrain Portugal S.A.', 'Captrain');\n" +
            "\n" +
            "-- Owner\n" +
            "INSERT INTO Owner (ID_Owner, Name, Short_Name)\n" +
            "VALUES ('PT503933813', 'Infraestruturas de Portugal, SA', 'IP');\n" +
            "\n" +
            "-- Maker\n" +
            "INSERT INTO Maker (ID_Maker, Name) VALUES (1, 'Siemens');\n" +
            "INSERT INTO Maker (ID_Maker, Name) VALUES (2, 'Sorefame - Alsthom');\n" +
            "INSERT INTO Maker (ID_Maker, Name) VALUES (3, 'MetalSines');\n" +
            "INSERT INTO Maker (ID_Maker, Name) VALUES (4, 'Equimetal');\n" +
            "INSERT INTO Maker (ID_Maker, Name) VALUES (5, 'Sepsa Cometna');\n" +
            "INSERT INTO Maker (ID_Maker, Name) VALUES (6, 'Emef');\n" +
            "INSERT INTO Maker (ID_Maker, Name) VALUES (7, 'Simmering');\n" +
            "INSERT INTO Maker (ID_Maker, Name) VALUES (8, 'Stadler');\n" +
            "\n" +
            "-- Fuel_Type\n" +
            "INSERT INTO Fuel_Type (ID_Fuel_Type, Name) VALUES (1, 'Diesel');\n" +
            "INSERT INTO Fuel_Type (ID_Fuel_Type, Name) VALUES (2, 'Electric');\n" +
            "\n" +
            "-- Track_Gauge\n" +
            "INSERT INTO Track_Gauge (ID_Track_Gauge, Gauge_Size) VALUES (1, 1668);\n" +
            "INSERT INTO Track_Gauge (ID_Track_Gauge, Gauge_Size) VALUES (2, 1435);\n" +
            "\n" +
            "-- Facility_Type (All Invented)\n" +
            "INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (1, 'Station');\n" +
            "INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (2, 'Freight Yard');\n" +
            "INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (3, 'Terminal');\n" +
            "\n" +
            "-- Dimensions\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (1, 19.2, 3, 4.375, 87);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (2, 19.084, 3.062, 4.31, 117);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (3, 23.02, 3, 4.264, 124);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (4, 17.24, 3.072, 4.27, 24);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (5, 9.64, 3.12, 4.1655, 13.8);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (6, 21.7, 3.18, 4.17, 29.8);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (7, 14.04, 3.104, 2.535, 21.2);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (8, 13.86, 2.85, 1.06, 11.9);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (9, 18.116, 2.95, 1.03, 21.6);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (10, 13.8, 2.95, 4.226, 22.9);\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (11, 14.02, 2.842, 3.3, 14.2);\n" +
            "\n" +
            "-- Facility (Invented: Intermodal, Facility_TypeID_Facility_Type)\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (1, 'São Romão', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (2, 'Tamel', '0', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (3, 'Senhora das Dores', '1', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (4, 'Lousado', '0', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (5, 'Porto Campanhã', '1', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (6, 'Leandro', '0', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (7, 'Porto São Bento', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (8, 'Barcelos', '0', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (9, 'Vila Nova da Cerveira', '1', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (10, 'Midões', '0', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (11, 'Valença', '1', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (12, 'Darque', '0', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (13, 'Contumil', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (14, 'Ermesinde', '0', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (15, 'São Frutuoso', '1', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (16, 'São Pedro da Torre', '0', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (17, 'Viana do Castelo', '1', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (18, 'Famalicão', '0', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (19, 'Barroselas', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (20, 'Nine', '0', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (21, 'Caminha', '1', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (22, 'Carvalha', '0', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (23, 'Carreço', '1', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (30, 'Braga', '0', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (31, ' Manzagão', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (32, 'Cerqueiral', '0', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (33, 'Gemieira', '1', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (35, 'Paredes de Coura', '0', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (43, 'São Gemil', '1', 2);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (45, 'São Mamede de Infesta', '0', 3);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (48, 'Leça do Balio', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (50, 'Leixões', '0', 2);\n" +
            "\n" +
            "--Building_Type (All Invented)\n" +
            "INSERT INTO Building_Type (ID_Building_Type, Name) VALUES (1, 'Warehouse');\n" +
            "INSERT INTO Building_Type (ID_Building_Type, Name) VALUES (2, 'Refrigerated Area');\n" +
            "INSERT INTO Building_Type (ID_Building_Type, Name) VALUES (3, 'Grain Silo');\n" +
            "\n" +
            "-- Building (All Invented)\n" +
            "INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (1, 'Main Warehouse', 1, 1);\n" +
            "INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (2, 'Cold Storage', 2, 2);\n" +
            "INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (3, 'Grain Silo A', 3, 3);\n" +
            "INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (4, 'Grain Silo B', 4, 3);\n" +
            "INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (5, 'Secondary Warehouse', 4, 1);\n" +
            "\n" +
            "-- Fuel_Diesel\n" +
            "INSERT INTO Fuel_Diesel (ID_Fuel_Diesel, Fuel_TypeID_Fuel_Type, Fuel_Capacity) VALUES (1, 1,4882);\n" +
            "INSERT INTO Fuel_Diesel (ID_Fuel_Diesel, Fuel_TypeID_Fuel_Type, Fuel_Capacity) VALUES (2, 1,6700);\n" +
            "\n" +
            "-- Fuel_Electric\n" +
            "INSERT INTO Fuel_Electric (ID_Fuel_Eletric, Fuel_TypeID_Fuel_Type, Voltage, Frequency) VALUES (1, 2, 25, 50);\n" +
            "\n" +
            "-- Locomotive_Model (Invented: Maximum_Weight, Acceleration)\n" +
            "INSERT INTO Locomotive_Model (ID_Locomotive_Model, Name, Power, Maximum_Weight, Acceleration, Number_Wheels, Max_Speed, Operational_Speed, Traction, MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type)\n" +
            "VALUES (1, 'Eurosprinter', 5600, 90, 0.8, 8, 220, 70, 300, 1, 1, 2);\n" +
            "INSERT INTO Locomotive_Model (ID_Locomotive_Model, Name, Power, Maximum_Weight, Acceleration, Number_Wheels, Max_Speed, Operational_Speed, Traction, MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type)\n" +
            "VALUES (2, 'CP 1900', 1623, 120, 0.6, 12, 100, 42.5, 396, 2, 2, 1);\n" +
            "INSERT INTO Locomotive_Model (ID_Locomotive_Model, Name, Power, Maximum_Weight, Acceleration, Number_Wheels, Max_Speed, Operational_Speed, Traction, MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type)\n" +
            "VALUES (3, 'E4000', 3178, 130, 0.7, 18, 120, 100, 400, 8, 3, 1);\n" +
            "\n" +
            "-- Wagon_Type\n" +
            "INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (1, 'Cereal wagon');\n" +
            "INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (2, 'Covered wagon with sliding door');\n" +
            "INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (3, 'Container wagon (max 40'''' HC)');\n" +
            "INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (4, 'Biodiesel wagon');\n" +
            "INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (5, 'Wood wagon');\n" +
            "\n" +
            "-- Wagon_Model\n" +
            "INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (1, 'Tadgs 32 94 082 3', 56, 75, 8, 120, 4, 4, 1);\n" +
            "INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (2, 'Tdgs 41 94 074 1', 26.2, 38, 8, 100, 4, 5, 1);\n" +
            "INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (3, 'Gabs 81 94 181 1', 50.2, 110, 8, 100, 5, 6, 2);\n" +
            "INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (4, 'Regmms 32 94 356 3', 60.6, 76.3, 8, 120, 3, 7, 3);\n" +
            "INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (5, 'Lgs 22 94 441 6', 28.1, 76.3, 8, 120, 3, 8, 3);\n" +
            "INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (6, 'Sgnss 12 94 455 2', 68.4, 76.3, 8, 120, 6, 9, 3);\n" +
            "INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (7, 'Zaes 81 94 788', 57.1, 64.6, 8, 120, 4, 10, 4);\n" +
            "INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (8, 'Kbs 41 94 333', 25.8, 62.7, 8, 100, 7, 11, 5);\n" +
            "\n" +
            "-- Siding\n" +
            "INSERT INTO Siding (ID_Siding, Position, Length) VALUES (1, 2000, 864);\n" +
            "INSERT INTO Siding (ID_Siding, Position, Length) VALUES (2, 11000, 266);\n" +
            "\n" +
            "-- Rail_Line_Segment (Invented: Speed_Limit)\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (1, 1, 8000, 2618, 4, 120, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (3, 1, 8000, 2443, 4, 100, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (10, 1, 8000, 26560, 2, 100, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (11, 1, 8000, 10000, 2, 90, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (15, 1, 8000, 5286, 2, 100, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (16, 1, 8000, 6000, 2, 90, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (14, 1, 8000, 10387, 2, 100, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (12, 1, 8000, 12000, 2, 90, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (13, 1, 8000, 3100, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (20, 1, 6400, 4890, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (18, 1, 8000, 6000, 1, 90, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge, ID_Siding)\n" +
            "VALUES (21, 1, 8000, 5000, 1, 80, 1, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (22, 1, 8000, 12000, 1, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge, ID_Siding)\n" +
            "VALUES (25, 1, 8000, 20829, 1, 80, 1, 2);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (26, 1, 8000, 4264, 1, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (30, 1, 8000, 3883, 2, 100, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (31, 1, 8400, 1174, 2, 100, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (32, 1, 8000, 2534, 2, 90, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (33, 1, 8000, 1566, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (34, 1, 8000, 1453, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (35, 1, 8100, 3597, 2, 90, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (36, 1, 8000, 4334, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (50, 1, 8000, 3555, 2, 90, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (51, 1, 8000, 1222, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (52, 1, 8000, 1760, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (53, 1, 8000, 1720, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (54, 1, 8000, 3350, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (55, 1, 8000, 3470, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (58, 1, 8000, 8050, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (59, 1, 8000, 22320, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (60, 1, 8000, 16310, 2, 80, 1);\n" +
            "INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (61, 1, 8000, 15200, 2, 80, 1);\n" +
            "\n" +
            "-- Rail_Line\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (1, 'Ramal São Bento - Campanhã', 7, 5, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (2, 'Ramal Camapanhã - Contumil', 5, 13, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (3, 'Ramal Contumil - Nine', 13, 20, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (4, 'Ramal Nine - Barcelos', 20, 8, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (5, 'Ramal Barcelos - Darque', 8, 12, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (6, 'Ramal Darque - Viana', 12, 17, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (7, 'Ramal Viana - Caminha', 17, 21, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (8, 'Ramal Caminha - Torre', 21, 16, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (9, 'Ramal Torre - Valença', 16, 11, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (21, 'Ramal Contumil - São Gemil', 13, 43, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (22, 'Ramal São Gemil - São Mamede de Infesta', 43, 45, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (23, 'Ramal São Mamede de Infesta - Leça do Balio', 45, 48, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (24, 'Ramal Leça do Balio - Leixões', 48, 50, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (30, 'Ramal Braga', 31, 30, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (31, 'Ramal Nine - Manzagão', 20, 31, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (32, 'Ramal Manzagão - Cerqueiral', 31, 32, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (35, 'Ramal Cerqueiral - Gemieira', 32, 33, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (36, 'Ramal Gemieira - Paredes de Coura', 33, 35, 'PT503933813');\n" +
            "INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (37, 'Ramal Paredes de Coura - Valença', 35, 11, 'PT503933813');\n" +
            "\n" +
            "-- Locomotive\n" +
            "INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (5621, 'Inês', DATE '1995-01-01', 1, 'PT509017800', 50);\n" +
            "INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (5623, 'Paz', DATE '1995-04-01', 1, 'PT509017800', 50);\n" +
            "INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (5630, 'Helena', DATE '1996-01-02', 1, 'PT509017800', 50);\n" +
            "INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (1903, 'Eva', DATE '1981-04-07', 2, 'PT509017800', 50);\n" +
            "INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (5034, 'Adriana', DATE '2017-02-15', 3, 'PT509017800', 50);\n" +
            "INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (5036, 'Marina', DATE '2017-02-01', 3, 'PT509017800', 50);\n" +
            "INSERT INTO Locomotive (ID_Locomotive, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (335001, DATE '2019-05-07', 3, 'PT509017800', 5);\n" +
            "INSERT INTO Locomotive (ID_Locomotive, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (335003, DATE '2019-06-03', 3, 'PT509017800', 5);\n" +
            "\n" +
            "-- Wagon\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563077, DATE '1987-05-02', 'PT509017800', 4, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563078, DATE '1987-05-02', 'PT509017800', 4, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563079, DATE '1987-05-02', 'PT509017800', 4, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563080, DATE '1987-05-02', 'PT509017800', 4, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563081, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563082, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563083, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563084, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563085, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563086, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563087, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563088, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563089, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563090, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563091, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3563092, DATE '1987-05-02', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (823045, DATE '1990-04-12', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (823046, DATE '1990-04-12', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (823047, DATE '1990-04-12', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (823048, DATE '1990-04-12', 'PT509017800', 1, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (741001, DATE '1977-02-03', 'PT509017800', 2, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (741002, DATE '1977-02-03', 'PT509017800', 2, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (741003, DATE '1977-02-03', 'PT509017800', 2, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (741004, DATE '1977-02-03', 'PT509017800', 2, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (741005, DATE '1977-02-03', 'PT509017800', 2, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (741006, DATE '1977-02-03', 'PT509017800', 2, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (1811010, DATE '1977-02-03', 'PT509017800', 3, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (1811011, DATE '1977-02-03', 'PT509017800', 3, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (1811012, DATE '1977-02-03', 'PT509017800', 3, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (1811013, DATE '1977-02-03', 'PT509017800', 3, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (1811014, DATE '1977-02-03', 'PT509017800', 3, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330001, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330002, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330003, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330004, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330005, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330006, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330007, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330008, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330009, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (3330010, DATE '2005-09-01', 'PT509017800', 5, 50);\n" +
            "\n" +
            "-- Rail_Line_Rail_Line_Segment\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (1, 1, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (2, 3, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (3, 10, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (3, 11, 2);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (4, 15, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (5, 16, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (6, 14, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (7, 12, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (7, 13, 2);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (8, 20, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (9, 18, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (9, 21, 2);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (9, 22, 3);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (21, 30, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (22, 31, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (22, 32, 2);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (23, 33, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (23, 34, 2);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (24, 35, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (24, 36, 2);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (30, 50, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (31, 51, 5);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (31, 52, 4);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (31, 53, 3);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (31, 54, 2);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (31, 55, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (32, 58, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (35, 59, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (36, 60, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (37, 61, 1);\n" +
            "\n" +
            "-- Cargo_Type (All invented)\n" +
            "INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (1, 'Liquids');\n" +
            "INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (2, 'Chemicals');\n" +
            "INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (3, 'Fuel');\n" +
            "INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (4, 'Coal');\n" +
            "INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (5, 'Grains');\n" +
            "INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (6, 'Minerals');\n" +
            "INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (7, 'Perishable goods');\n" +
            "\n" +
            "-- Wagon_Type_Cargo_Type (Invented relationships)\n" +
            "INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)\n" +
            "VALUES (1, 4);\n" +
            "INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)\n" +
            "VALUES (2, 5);\n" +
            "INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)\n" +
            "VALUES (3, 1);\n" +
            "INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)\n" +
            "VALUES (3, 2);\n" +
            "INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)\n" +
            "VALUES (3, 3);\n" +
            "INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)\n" +
            "VALUES (4, 6);\n" +
            "INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)\n" +
            "VALUES (5, 7);\n" +
            "\n" +
            "--Path\n" +
            "INSERT INTO Path (ID_Path)\n" +
            "VALUES (1);\n" +
            "INSERT INTO Path (ID_Path)\n" +
            "VALUES (2);\n" +
            "\n" +
            "-- Path_Facility\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 50, 1);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 48, 2);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 45, 3);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 43, 4);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 13, 5);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 20, 6);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 8, 7);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 12, 8);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 17, 9);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 21, 10);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 16, 11);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (1, 11, 12);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 11, 1);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 16, 2);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 21, 3);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 17, 4);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 12, 5);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 8, 6);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 20, 7);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 13, 8);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 43, 9);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 45, 10);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 48, 11);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (2, 50, 12);\n" +
            "\n" +
            "--Route\n" +
            "INSERT INTO Route (ID_Route, Is_Simple, PathID_Path)\n" +
            "VALUES (1, '0', 1);\n" +
            "INSERT INTO Route (ID_Route, Is_Simple, PathID_Path)\n" +
            "VALUES (2, '1', 2);\n" +
            "INSERT INTO Route (ID_Route, Is_Simple, PathID_Path)\n" +
            "VALUES (3, '0', 2);\n" +
            "\n" +
            "-- Train (All Invented)\n" +
            "INSERT INTO Train (ID_Train, Scheduled_Construction_Date, Dispatched, OperatorID_Operator, RouteID_Route)\n" +
            "VALUES (5421, TO_DATE('2025-10-03 09:45:00', 'YYYY-MM-DD HH24:MI:SS'), 0, 'PT509017800', 1);\n" +
            "INSERT INTO Train (ID_Train, Scheduled_Construction_Date, Dispatched, OperatorID_Operator, RouteID_Route)\n" +
            "VALUES (5435, TO_DATE('2025-10-03 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0,  'PT509017800', 2);\n" +
            "INSERT INTO Train (ID_Train, Scheduled_Construction_Date, Dispatched, OperatorID_Operator, RouteID_Route)\n" +
            "VALUES (5437, TO_DATE('2025-10-06 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0, 'PT509017800', 3);\n" +
            "\n" +
            "--Freight\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2001, DATE '2025-10-03', 50, 12, 1);\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2002, DATE '2025-10-03', 13, 11, 1);\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2003, DATE '2025-10-03', 50, 11, 1);\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2004, DATE '2025-10-03', 50, 21, 1);\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2005, DATE '2025-10-03', 20, 11, 1);\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2006, DATE '2025-10-03', 12, 50, 2);\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2007, DATE '2025-10-03', 50, 5, 1);\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2050, DATE '2025-10-06', 12, 50, 3);\n" +
            "INSERT INTO Freight (ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (2051, DATE '2025-10-06', 11, 50, 3);\n" +
            "\n" +
            "--Wagon_Freight\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330001, 2001);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330002, 2001);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330004, 2001);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330005, 2001);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330006, 2001);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563089, 2002);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (1811011, 2003);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (1811012, 2003);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (1811013, 2004);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563077, 2005);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563078, 2005);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563079, 2005);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563080, 2005);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330003, 2006);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330007, 2006);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563090, 2007);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563091, 2007);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563092, 2007);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330001, 2050);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330002, 2050);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330004, 2050);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330005, 2050);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3330006, 2050);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (1811011, 2051);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (1811012, 2051);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563077, 2051);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563078, 2051);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563079, 2051);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (3563080, 2051);\n" +
            "\n" +
            "--Locomotive_Train\n" +
            "INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)\n" +
            "VALUES (5621, 5421);\n" +
            "INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)\n" +
            "VALUES (5623, 5421);\n" +
            "INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)\n" +
            "VALUES (5623, 5435);\n" +
            "INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)\n" +
            "VALUES (5621, 5437);\n" +
            "\n" +
            "--Locomotive_Model_Track_Gauge\n" +
            "INSERT INTO Locomotive_Model_Track_Gauge VALUES (1, 1);\n" +
            "INSERT INTO Locomotive_Model_Track_Gauge VALUES (2, 1);\n" +
            "INSERT INTO Locomotive_Model_Track_Gauge VALUES (3, 1);\n" +
            "\n" +
            "--Wagon_Model_Track_Gauge\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (1, 1);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (2, 1);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (3, 1);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (4, 1);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (5, 1);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (6, 1);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (6, 2);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (7, 1);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (7, 2);\n" +
            "INSERT INTO Wagon_Model_Track_Gauge VALUES (8, 1);\n" +

            "-- ================= TEST DATA =================\n" +

            "-- FACILITIES\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type)\n" +
            "VALUES (51, 'Teste1', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type)\n" +
            "VALUES (52, 'Teste2', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type)\n" +
            "VALUES (53, 'Teste3', '1', 1);\n" +
            "INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type)\n" +
            "VALUES (54, 'Teste4', '1', 1);\n" +

            "\n-- DIMENSIONS\n" +
            "INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare)\n" +
            "VALUES (100, 12, 3, 3, 20);\n" +

            "\n-- WAGON MODEL\n" +
            "INSERT INTO Wagon_Model\n" +
            "(ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed,\n" +
            " MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)\n" +
            "VALUES (100, 'Wagon-Test', 30000, 60, 8, 90, 3, 100, 3);\n" +

            "\n-- WAGONS\n" +
            "INSERT INTO Wagon\n" +
            "(ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (999, DATE '1999-01-01', 'PT509017800', 100, 51);\n" +
            "INSERT INTO Wagon\n" +
            "(ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (1000, DATE '2000-01-01', 'PT509017800', 100, 52);\n" +
            "INSERT INTO Wagon\n" +
            "(ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (1001, DATE '2001-01-01', 'PT509017800', 100, 53);\n" +
            "INSERT INTO Wagon\n" +
            "(ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)\n" +
            "VALUES (1002, DATE '2002-01-01', 'PT509017800', 100, 54);\n" +

            "\n-- RAIL LINE SEGMENTS\n" +
            "INSERT INTO Rail_Line_Segment\n" +
            "(ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (101, '1', 10000, 5000, 1, 120, 1);\n" +
            "INSERT INTO Rail_Line_Segment\n" +
            "(ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)\n" +
            "VALUES (102, '1', 10000, 7000, 1, 100, 1);\n" +

            "\n-- RAIL LINES\n" +
            "INSERT INTO Rail_Line\n" +
            "(ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (101, 'Linha Teste A', 51, 52, 'PT503933813');\n" +
            "INSERT INTO Rail_Line\n" +
            "(ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)\n" +
            "VALUES (102, 'Linha Teste B', 52, 53, 'PT503933813');\n" +

            "\n-- RAIL LINE ↔ SEGMENT\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment\n" +
            "(Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (101, 101, 1);\n" +
            "INSERT INTO Rail_Line_Rail_Line_Segment\n" +
            "(Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)\n" +
            "VALUES (102, 102, 1);\n" +

            "\n-- PATH\n" +
            "INSERT INTO Path (ID_Path) VALUES (101);\n" +

            "\n-- PATH ↔ FACILITY\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (101, 51, 1);\n" +
            "INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)\n" +
            "VALUES (101, 52, 2);\n" +

            "\n-- ROUTE\n" +
            "INSERT INTO Route (ID_Route, PathID_Path)\n" +
            "VALUES (101, 101);\n" +
            "INSERT INTO Route (ID_Route)\n" +
            "VALUES (102);\n" +

            "\n-- FREIGHTS\n" +
            "INSERT INTO Freight\n" +
            "(ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (3001, DATE '2025-12-25', 51, 52, 101);\n" +
            "INSERT INTO Freight\n" +
            "(ID_Freight, \"Date\", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)\n" +
            "VALUES (3002, DATE '2025-11-10', 52, 51, 102);\n" +

            "\n-- WAGON ↔ FREIGHT\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (999, 3001);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (1000, 3001);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (1001, 3002);\n" +
            "INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)\n" +
            "VALUES (1002, 3002);\n" +

            "\n-- LOCOMOTIVE\n" +
            "INSERT INTO Locomotive\n" +
            "(ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)\n" +
            "VALUES (5600, 'André', DATE '1995-01-01', 1, 'PT509017800', 7);\n" +

            "\n-- TRAIN\n" +
            "INSERT INTO Train\n" +
            "(ID_Train, Scheduled_Construction_Date, Dispatched, OperatorID_Operator, RouteID_Route)\n" +
            "VALUES (9001, DATE '2025-12-25', '0', 'PT509017800', 101);\n" +

            "\n-- LOCOMOTIVE ↔ TRAIN\n" +
            "INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)\n" +
            "VALUES (5600, 9001);\n";
}

