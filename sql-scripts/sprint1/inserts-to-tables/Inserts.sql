-- Operator
INSERT INTO Operator (ID_Operator, Name)
VALUES ('PT509017800', 'Medway - Operador Ferroviário de Mercadorias, S.A');

-- Owner
INSERT INTO Owner (ID_Owner, Name)
VALUES ('PT503933813', 'Infraestruturas de Portugal, SA');

-- Maker
INSERT INTO Maker (ID_Maker, Name) VALUES (1, 'Siemens');
INSERT INTO Maker (ID_Maker, Name) VALUES (2, 'Sorefame - Alsthom');
INSERT INTO Maker (ID_Maker, Name) VALUES (3, 'MetalSines');
INSERT INTO Maker (ID_Maker, Name) VALUES (4, 'Equimetal');
INSERT INTO Maker (ID_Maker, Name) VALUES (5, 'Sepsa Cometna');
INSERT INTO Maker (ID_Maker, Name) VALUES (6, 'Emef');

-- Fuel_Type
INSERT INTO Fuel_Type (ID_Fuel_Type, Name) VALUES (1, 'Diesel');
INSERT INTO Fuel_Type (ID_Fuel_Type, Name) VALUES (2, 'Electric');

-- Track_Gauge
INSERT INTO Track_Gauge (ID_Track_Gauge, Gauge_Size) VALUES (1, 1668);
INSERT INTO Track_Gauge (ID_Track_Gauge, Gauge_Size) VALUES (2, 1435);

-- Bogie_Type
INSERT INTO Bogie_Type (ID_Bogie_Type, Name) VALUES (1, 'Bo');
INSERT INTO Bogie_Type (ID_Bogie_Type, Name) VALUES (2, 'Co');
INSERT INTO Bogie_Type (ID_Bogie_Type, Name) VALUES (3, 'Simple');
INSERT INTO Bogie_Type (ID_Bogie_Type, Name) VALUES (4, 'Double');

-- Facility_Type (All Invented)
INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (1, 'Station');
INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (2, 'Freight Yard');
INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (3, 'Terminal');

-- Dimensions
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (1, 19.2, 3, 4.375, 87);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (2, 19.084, 3.062, 4.31, 117);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (3, 17.24, 3.072, 4.27, 24);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (4, 9.64, 3.12, 4.1655, 13.8);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (5, 21.7, 3.18, 4.17, 29.8);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (6, 14.04, 3.104, 2.535, 21.2);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (7, 13.86, 2.85, 1.06, 11.9);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (8, 18.116, 2.95, 1.03, 21.6);

-- Facility (Invented: Intermodal, Facility_TypeID_Facility_Type)
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (1, 'São Romão', 1, 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (2, 'Tamel', 0, 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (3, 'Senhora das Dores', 1, 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (4, 'Lousado', 0, 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (5, 'Porto Campanhã', 1, 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (6, 'Leandro', 0, 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (7, 'Porto São Bento', 1, 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (8, 'Barcelos', 0, 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (9, 'Vila Nova da Cerveira', 1, 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (10, 'Midões', 0, 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (11, 'Valença', 1, 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (12, 'Darque', 0, 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (13, 'Contumil', 1, 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (14, 'Ermesinde', 0, 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (15, 'São Frutuoso', 1, 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (16, 'São Pedro da Torre', 0, 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (17, 'Viana do Castelo', 1, 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (18, 'Famalicão', 0, 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (19, 'Barroselas', 1, 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (20, 'Nine', 0, 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (21, 'Caminha', 1, 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (22, 'Carvalha', 0, 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (23, 'Carreço', 1, 2);

--Building_Type (All Invented)
INSERT INTO Building_Type (ID_Building_Type, Name) VALUES (1, 'Warehouse');
INSERT INTO Building_Type (ID_Building_Type, Name) VALUES (2, 'Refrigerated Area');
INSERT INTO Building_Type (ID_Building_Type, Name) VALUES (3, 'Grain Silo');

-- Building (All Invented)
INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (1, 'Main Warehouse', 1, 1);
INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (2, 'Cold Storage', 2, 2);
INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (3, 'Grain Silo A', 3, 3);
INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (4, 'Grain Silo B', 4, 3);
INSERT INTO Building (ID_Building, Name, FacilityID_Facility, Building_TypeID_Building_Type) VALUES (5, 'Secondary Warehouse', 4, 1);

-- Fuel_Diesel
INSERT INTO Fuel_Diesel (Fuel_TypeID_Fuel_Type, Fuel_Capacity) VALUES (1, 4882);

-- Fuel_Electric
INSERT INTO Fuel_Electric (Fuel_TypeID_Fuel_Type) VALUES (2);

-- Locomotive_Model (Invented: Maximum_Weight)
INSERT INTO Locomotive_Model (ID_Locomotive_Model, Name, Power, Maximum_Weight, MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type, Track_GaugeID_Track_Gauge)
VALUES (1, 'Eurosprinter', 5600, 92, 1, 1, 2, 1);
INSERT INTO Locomotive_Model (ID_Locomotive_Model, Name, Power, Maximum_Weight, MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type, Track_GaugeID_Track_Gauge)
VALUES (2, 'CP 1900', 1623, 125, 2, 2, 1, 1);

-- Wagon_Type
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (1, 'Boxcar');
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (2, 'Flatcar');
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (3, 'Tank car');
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (4, 'Hopper car');
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (5, 'Refrigerated car');

-- Wagon_Model
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type, Track_GaugeID_Track_Gauge)
VALUES (1245, 'Tadgs 32 94 082 3', 56, 75, 3, 3, 4, 1);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type, Track_GaugeID_Track_Gauge)
VALUES (1278, 'Tdgs 41 94 074 1', 26.2, 38, 4, 4, 4, 1);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type, Track_GaugeID_Track_Gauge)
VALUES (1325, 'Gabs 81 94 181 1', 50.2, 110, 5, 5, 1, 1);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type, Track_GaugeID_Track_Gauge)
VALUES (1104, 'Regmms 32 94 356 3', 60.6, 76.3, 3, 6, 2, 1);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type, Track_GaugeID_Track_Gauge)
VALUES (985, 'Lgs 22 94 441 6', 28.1, 76.3, 3, 7, 2, 1);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type, Track_GaugeID_Track_Gauge)
VALUES (987, 'Sgnss 12 94 455 2', 68.4, 76.3, 6, 8, 2, 1);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type, Track_GaugeID_Track_Gauge)
VALUES (988, 'Sgnss 12 94 455 2', 68.4, 76.3, 6, 8, 2, 2);

-- Rail_Line_Segment (Invented: Has_Siding, Speed_Limit)
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (1, 1, 8000, 2618, 4, 1, 120, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (10, 1, 8000, 29003, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (11, 1, 8000, 10000, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (15, 1, 8000, 5286, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (16, 1, 8000, 6000, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (14, 1, 8000, 10387, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (12, 1, 8000, 12000, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (13, 1, 6400, 8000, 2, 0, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (20, 1, 8000, 6000, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (21, 1, 8000, 3000, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (22, 1, 8000, 15000, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (25, 1, 8000, 20829, 2, 0, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Has_Siding, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (26, 1, 8000, 4264, 2, 0, 100, 1);

-- Rail_Line
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (1, 'Ramal São Bento - Campanhã', 7, 5, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (2, 'Ramal Campanhã - Nine', 5, 20, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (3, 'Ramal Nine - Barcelos', 20, 8, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (4, 'Ramal Barcelos - Viana', 8, 17, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (5, 'Ramal viana - Caminha', 17, 21, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (6, 'Ramal Caminha - Torre', 21, 16, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (7, 'Ramal Torre - Valença', 16, 11, 'PT503933813');

-- Train (All Invented)
INSERT INTO Train (ID_Train, Name) VALUES (1001, 'Mercadorias Norte 1');
INSERT INTO Train (ID_Train, Name) VALUES (1002, 'Mercadorias Norte 2');
INSERT INTO Train (ID_Train, Name) VALUES (1003, 'Mercadorias Norte 3');
INSERT INTO Train (ID_Train, Name) VALUES (1004, 'Mercadorias Norte 4');

-- Locomotive
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Year_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator)
VALUES (5621, 'Inês', 1995, 1, 'PT509017800');
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Year_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator)
VALUES (5623, 'Paz', 1995, 1, 'PT509017800');
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Year_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator)
VALUES (5630, 'Helena', 1996, 1, 'PT509017800');
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Year_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator)
VALUES (1903, 'Eva', 1981, 2, 'PT509017800');

-- Wagon
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563077, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563078, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563079, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563080, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563081, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563082, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563083, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563084, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563085, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563086, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563087, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563088, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563089, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563090, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563091, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (3563092, 1987, 'PT509017800', 1104);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (823045, 1990, 'PT509017800', 1245);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (823046, 1990, 'PT509017800', 1245);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (823047, 1990, 'PT509017800', 1245);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (823048, 1990, 'PT509017800', 1245);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (741001, 1977, 'PT509017800', 1278);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (741002, 1977, 'PT509017800', 1278);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (741003, 1977, 'PT509017800', 1278);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (741004, 1977, 'PT509017800', 1278);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (741005, 1977, 'PT509017800', 1278);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (741006, 1977, 'PT509017800', 1278);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (1811010, 1977, 'PT509017800', 1325);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (1811011, 1977, 'PT509017800', 1325);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (1811012, 1977, 'PT509017800', 1325);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (1811013, 1977, 'PT509017800', 1325);
INSERT INTO Wagon (ID_Wagon, Starting_Year_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model)
VALUES (1811014, 1977, 'PT509017800', 1325);


-- Rail_Line_Rail_Line_Segment
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (1, 1, 1);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (2, 10, 1);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (2, 11, 2);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (3, 15, 1);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (3, 16, 2);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (4, 14, 1);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (4, 12, 2);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (4, 13, 3);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (5, 20, 1);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (5, 21, 2);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (5, 22, 3);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (6, 25, 1);
INSERT INTO Rail_Line_Rail_Line_Segment VALUES (7, 26, 1);

-- Cargo_Type
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (1, 'Liquids');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (2, 'Chemicals');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (3, 'Fuel');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (4, 'Coal');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (5, 'Grains');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (6, 'Minerals');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (7, 'Perishable goods');

-- Wagon_Type_Cargo_Type (Invented relationships)
INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type) VALUES (1, 2);
INSERT INTO Wagon_Type_Cargo_Type VALUES (1, 7);
INSERT INTO Wagon_Type_Cargo_Type VALUES (2, 6);
INSERT INTO Wagon_Type_Cargo_Type VALUES (2, 5);
INSERT INTO Wagon_Type_Cargo_Type VALUES (3, 1);
INSERT INTO Wagon_Type_Cargo_Type VALUES (3, 3);
INSERT INTO Wagon_Type_Cargo_Type VALUES (4, 4);
INSERT INTO Wagon_Type_Cargo_Type VALUES (4, 5);
INSERT INTO Wagon_Type_Cargo_Type VALUES (5, 7);

-- Bogie_Type_Locomotive_Model
INSERT INTO Bogie_Type_Locomotive_Model (Bogie_TypeID_Bogie_Type, Locomotive_ModelID_Locomotive_Model, Bogie_Count)
VALUES (1, 1, 2);
INSERT INTO Bogie_Type_Locomotive_Model VALUES (2, 2, 2);

-- Bogie_Type_Wagon_Model
INSERT INTO Bogie_Type_Wagon_Model (Bogie_TypeID_Bogie_Type, Wagon_ModelID_Wagon_Model, Bogie_Count)
VALUES (4, 1245, 2);
INSERT INTO Bogie_Type_Wagon_Model VALUES (4, 1278, 2);
INSERT INTO Bogie_Type_Wagon_Model VALUES (3, 1325, 2);
INSERT INTO Bogie_Type_Wagon_Model VALUES (4, 1104, 2);
INSERT INTO Bogie_Type_Wagon_Model VALUES (3, 985, 1);
INSERT INTO Bogie_Type_Wagon_Model VALUES (4, 987, 2);
INSERT INTO Bogie_Type_Wagon_Model VALUES (4, 988, 2);
