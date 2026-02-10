-- Operator
INSERT INTO Operator (ID_Operator, Name, Short_Name)
VALUES ('PT509017800', 'Medway - Operador Ferroviário de Mercadorias, S.A', 'Medway');
INSERT INTO Operator (ID_Operator, Name, Short_Name)
VALUES ('PT507832388', 'Captrain Portugal S.A.', 'Captrain');

-- Owner
INSERT INTO Owner (ID_Owner, Name, Short_Name)
VALUES ('PT503933813', 'Infraestruturas de Portugal, SA', 'IP');

-- Maker
INSERT INTO Maker (ID_Maker, Name) VALUES (1, 'Siemens');
INSERT INTO Maker (ID_Maker, Name) VALUES (2, 'Sorefame - Alsthom');
INSERT INTO Maker (ID_Maker, Name) VALUES (3, 'MetalSines');
INSERT INTO Maker (ID_Maker, Name) VALUES (4, 'Equimetal');
INSERT INTO Maker (ID_Maker, Name) VALUES (5, 'Sepsa Cometna');
INSERT INTO Maker (ID_Maker, Name) VALUES (6, 'Emef');
INSERT INTO Maker (ID_Maker, Name) VALUES (7, 'Simmering');
INSERT INTO Maker (ID_Maker, Name) VALUES (8, 'Stadler');

-- Fuel_Type
INSERT INTO Fuel_Type (ID_Fuel_Type, Name) VALUES (1, 'Diesel');
INSERT INTO Fuel_Type (ID_Fuel_Type, Name) VALUES (2, 'Electric');

-- Track_Gauge
INSERT INTO Track_Gauge (ID_Track_Gauge, Gauge_Size) VALUES (1, 1668);
INSERT INTO Track_Gauge (ID_Track_Gauge, Gauge_Size) VALUES (2, 1435);

-- Facility_Type (All Invented)
INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (1, 'Station');
INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (2, 'Freight Yard');
INSERT INTO Facility_Type (ID_Facility_Type, Name) VALUES (3, 'Terminal');

-- Dimensions
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (1, 19.2, 3, 4.375, 87);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (2, 19.084, 3.062, 4.31, 117);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (3, 23.02, 3, 4.264, 124);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (4, 17.24, 3.072, 4.27, 24);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (5, 9.64, 3.12, 4.1655, 13.8);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (6, 21.7, 3.18, 4.17, 29.8);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (7, 14.04, 3.104, 2.535, 21.2);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (8, 13.86, 2.85, 1.06, 11.9);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (9, 18.116, 2.95, 1.03, 21.6);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (10, 13.8, 2.95, 4.226, 22.9);
INSERT INTO Dimensions (ID_Dimensions, Length, Width, Height, Weight_Tare) VALUES (11, 14.02, 2.842, 3.3, 14.2);

-- Facility (Invented: Intermodal, Facility_TypeID_Facility_Type)
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (1, 'São Romão', '1', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (2, 'Tamel', '0', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (3, 'Senhora das Dores', '1', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (4, 'Lousado', '0', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (5, 'Porto Campanhã', '1', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (6, 'Leandro', '0', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (7, 'Porto São Bento', '1', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (8, 'Barcelos', '0', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (9, 'Vila Nova da Cerveira', '1', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (10, 'Midões', '0', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (11, 'Valença', '1', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (12, 'Darque', '0', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (13, 'Contumil', '1', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (14, 'Ermesinde', '0', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (15, 'São Frutuoso', '1', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (16, 'São Pedro da Torre', '0', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (17, 'Viana do Castelo', '1', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (18, 'Famalicão', '0', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (19, 'Barroselas', '1', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (20, 'Nine', '0', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (21, 'Caminha', '1', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (22, 'Carvalha', '0', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (23, 'Carreço', '1', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (30, 'Braga', '0', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (31, ' Manzagão', '1', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (32, 'Cerqueiral', '0', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (33, 'Gemieira', '1', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (35, 'Paredes de Coura', '0', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (43, 'São Gemil', '1', 2);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (45, '"	São Mamede de Infesta"', '0', 3);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (48, 'Leça do Balio', '1', 1);
INSERT INTO Facility (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type) VALUES (50, 'Leixões', '0', 2);

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
INSERT INTO Fuel_Diesel (ID_Fuel_Diesel, Fuel_TypeID_Fuel_Type, Fuel_Capacity) VALUES (1, 1,4882);
INSERT INTO Fuel_Diesel (ID_Fuel_Diesel, Fuel_TypeID_Fuel_Type, Fuel_Capacity) VALUES (2, 1,6700);

-- Fuel_Electric
INSERT INTO Fuel_Electric (ID_Fuel_Eletric, Fuel_TypeID_Fuel_Type, Voltage, Frequency) VALUES (1, 2, 25, 50);

-- Locomotive_Model (Invented: Maximum_Weight, Acceleration)
INSERT INTO Locomotive_Model (ID_Locomotive_Model, Name, Power, Maximum_Weight, Acceleration, Number_Wheels, Max_Speed, Operational_Speed, Traction, MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type)
VALUES (1, 'Eurosprinter', 5600, 90, 0.8, 8, 220, 70, 300, 1, 1, 2);
INSERT INTO Locomotive_Model (ID_Locomotive_Model, Name, Power, Maximum_Weight, Acceleration, Number_Wheels, Max_Speed, Operational_Speed, Traction, MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type)
VALUES (2, 'CP 1900', 1623, 120, 0.6, 12, 100, 42.5, 396, 2, 2, 1);
INSERT INTO Locomotive_Model (ID_Locomotive_Model, Name, Power, Maximum_Weight, Acceleration, Number_Wheels, Max_Speed, Operational_Speed, Traction, MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type)
VALUES (3, 'E4000', 3178, 130, 0.7, 18, 120, 100, 400, 8, 3, 1);

-- Wagon_Type
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (1, 'Cereal wagon');
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (2, 'Covered wagon with sliding door');
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (3, 'Container wagon (max 40'''' HC)');
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (4, 'Biodiesel wagon');
INSERT INTO Wagon_Type (ID_Wagon_Type, Name) VALUES (5, 'Wood wagon');

-- Wagon_Model
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
VALUES (1, 'Tadgs 32 94 082 3', 56, 75, 8, 120, 4, 4, 1);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
VALUES (2, 'Tdgs 41 94 074 1', 26.2, 38, 8, 100, 4, 5, 1);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
VALUES (3, 'Gabs 81 94 181 1', 50.2, 110, 8, 100, 5, 6, 2);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
VALUES (4, 'Regmms 32 94 356 3', 60.6, 76.3, 8, 120, 3, 7, 3);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
VALUES (5, 'Lgs 22 94 441 6', 28.1, 76.3, 8, 120, 3, 8, 3);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
VALUES (6, 'Sgnss 12 94 455 2', 68.4, 76.3, 8, 120, 6, 9, 3);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
VALUES (7, 'Zaes 81 94 788', 57.1, 64.6, 8, 120, 4, 10, 4);
INSERT INTO Wagon_Model (ID_Wagon_Model, Name, Payload, Volume_Capacity, Number_Wheels, Max_Speed, MakerID_Maker, DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
VALUES (8, 'Kbs 41 94 333', 25.8, 62.7, 8, 100, 7, 11, 5);

-- Siding
INSERT INTO Siding (ID_Siding, Position, Length) VALUES (1, 2000, 864);
INSERT INTO Siding (ID_Siding, Position, Length) VALUES (2, 11000, 266);

-- Rail_Line_Segment (Invented: Speed_Limit)
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (1, 1, 8000, 2618, 4, 120, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (3, 1, 8000, 2443, 4, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (10, 1, 8000, 26560, 2, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (11, 1, 8000, 10000, 2, 90, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (15, 1, 8000, 5286, 2, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (16, 1, 8000, 6000, 2, 90, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (14, 1, 8000, 10387, 2, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (12, 1, 8000, 12000, 2, 90, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (13, 1, 8000, 3100, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (20, 1, 6400, 4890, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (18, 1, 8000, 6000, 1, 90, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge, ID_Siding)
VALUES (21, 1, 8000, 5000, 1, 80, 1, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (22, 1, 8000, 12000, 1, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge, ID_Siding)
VALUES (25, 1, 8000, 20829, 1, 80, 1, 2);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (26, 1, 8000, 4264, 1, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (30, 1, 8000, 3883, 2, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (31, 1, 8400, 1174, 2, 100, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (32, 1, 8000, 2534, 2, 90, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (33, 1, 8000, 1566, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (34, 1, 8000, 1453, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (35, 1, 8100, 3597, 2, 90, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (36, 1, 8000, 4334, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (50, 1, 8000, 3555, 2, 90, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (51, 1, 8000, 1222, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (52, 1, 8000, 1760, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (53, 1, 8000, 1720, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (54, 1, 8000, 3350, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (55, 1, 8000, 3470, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (58, 1, 8000, 8050, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (59, 1, 8000, 22320, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (60, 1, 8000, 16310, 2, 80, 1);
INSERT INTO Rail_Line_Segment (ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge)
VALUES (61, 1, 8000, 15200, 2, 80, 1);

-- Rail_Line
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (1, 'Ramal São Bento - Campanhã', 7, 5, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (2, 'Ramal Camapanhã - Contumil', 5, 13, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (3, 'Ramal Contumil - Nine', 13, 20, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (4, 'Ramal Nine - Barcelos', 20, 8, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (5, 'Ramal Barcelos - Darque', 8, 12, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (6, 'Ramal Darque - Viana', 12, 17, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (7, 'Ramal Viana - Caminha', 17, 21, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (8, 'Ramal Caminha - Torre', 21, 16, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (9, 'Ramal Torre - Valença', 16, 11, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (21, 'Ramal Contumil - São Gemil', 13, 43, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (22, 'Ramal São Gemil - São Mamede de Infesta', 43, 45, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (23, 'Ramal São Mamede de Infesta - Leça do Balio', 45, 48, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (24, 'Ramal Leça do Balio - Leixões', 48, 50, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (30, 'Ramal Braga', 31, 30, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (31, 'Ramal Nine - Manzagão', 20, 31, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (32, 'Ramal Manzagão - Cerqueiral', 31, 32, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (35, 'Ramal Cerqueiral - Gemieira', 32, 33, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (36, 'Ramal Gemieira - Paredes de Coura', 33, 35, 'PT503933813');
INSERT INTO Rail_Line (ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner)
VALUES (37, 'Ramal Paredes de Coura - Valença', 35, 11, 'PT503933813');

-- Locomotive
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
VALUES (5621, 'Inês', DATE '1995-01-01', 1, 'PT509017800', 50);
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
VALUES (5623, 'Paz', DATE '1995-04-01', 1, 'PT509017800', 50);
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
VALUES (5630, 'Helena', DATE '1996-01-02', 1, 'PT509017800', 50);
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
VALUES (1903, 'Eva', DATE '1981-04-07', 2, 'PT509017800', 50);
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
VALUES (5034, 'Adriana', DATE '2017-02-15', 3, 'PT509017800', 50);
INSERT INTO Locomotive (ID_Locomotive, Name, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
VALUES (5036, 'Marina', DATE '2017-02-01', 3, 'PT509017800', 50);
INSERT INTO Locomotive (ID_Locomotive, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
VALUES (335001, DATE '2019-05-07', 3, 'PT509017800', 5);
INSERT INTO Locomotive (ID_Locomotive, Starting_Date_Service, Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
VALUES (335003, DATE '2019-06-03', 3, 'PT509017800', 5);

-- Wagon
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563077, DATE '1987-05-02', 'PT509017800', 4, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563078, DATE '1987-05-02', 'PT509017800', 4, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563079, DATE '1987-05-02', 'PT509017800', 4, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563080, DATE '1987-05-02', 'PT509017800', 4, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563081, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563082, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563083, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563084, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563085, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563086, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563087, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563088, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563089, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563090, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563091, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3563092, DATE '1987-05-02', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (823045, DATE '1990-04-12', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (823046, DATE '1990-04-12', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (823047, DATE '1990-04-12', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (823048, DATE '1990-04-12', 'PT509017800', 1, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (741001, DATE '1977-02-03', 'PT509017800', 2, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (741002, DATE '1977-02-03', 'PT509017800', 2, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (741003, DATE '1977-02-03', 'PT509017800', 2, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (741004, DATE '1977-02-03', 'PT509017800', 2, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (741005, DATE '1977-02-03', 'PT509017800', 2, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (741006, DATE '1977-02-03', 'PT509017800', 2, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (1811010, DATE '1977-02-03', 'PT509017800', 3, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (1811011, DATE '1977-02-03', 'PT509017800', 3, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (1811012, DATE '1977-02-03', 'PT509017800', 3, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (1811013, DATE '1977-02-03', 'PT509017800', 3, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (1811014, DATE '1977-02-03', 'PT509017800', 3, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330001, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330002, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330003, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330004, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330005, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330006, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330007, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330008, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330009, DATE '2005-09-01', 'PT509017800', 5, 50);
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (3330010, DATE '2005-09-01', 'PT509017800', 5, 50);

-- Rail_Line_Rail_Line_Segment
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (1, 1, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (2, 3, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (3, 10, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (3, 11, 2);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (4, 15, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (5, 16, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (6, 14, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (7, 12, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (7, 13, 2);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (8, 20, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (9, 18, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (9, 21, 2);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (9, 22, 3);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (21, 30, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (22, 31, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (22, 32, 2);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (23, 33, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (23, 34, 2);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (24, 35, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (24, 36, 2);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (30, 50, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (31, 51, 5);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (31, 52, 4);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (31, 53, 3);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (31, 54, 2);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (31, 55, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (32, 58, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (35, 59, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (36, 60, 1);
INSERT INTO Rail_Line_Rail_Line_Segment (Rail_LineID_Rail_Line, Rail_Line_SegmentID_Rail_Line_Segment, Order_Line)
VALUES (37, 61, 1);

-- Cargo_Type (All invented)
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (1, 'Liquids');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (2, 'Chemicals');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (3, 'Fuel');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (4, 'Coal');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (5, 'Grains');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (6, 'Minerals');
INSERT INTO Cargo_Type (ID_Cargo_Type, Name) VALUES (7, 'Perishable goods');

-- Wagon_Type_Cargo_Type (Invented relationships)
INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)
VALUES (1, 4);
INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)
VALUES (2, 5);
INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)
VALUES (3, 1);
INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)
VALUES (3, 2);
INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)
VALUES (3, 3);
INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)
VALUES (4, 6);
INSERT INTO Wagon_Type_Cargo_Type (Wagon_TypeID_Wagon_Type, Cargo_TypeID_Cargo_Type)
VALUES (5, 7);

--Path
INSERT INTO Path (ID_Path)
VALUES (1);
INSERT INTO Path (ID_Path)
VALUES (2);

-- Path_Facility
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 50, 1);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 48, 2);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 45, 3);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 43, 4);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 13, 5);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 20, 6);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 8, 7);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 12, 8);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 17, 9);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 21, 10);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 16, 11);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (1, 11, 12);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 11, 1);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 16, 2);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 21, 3);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 17, 4);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 12, 5);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 8, 6);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 20, 7);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 13, 8);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 43, 9);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 45, 10);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 48, 11);
INSERT INTO Path_Facility (PathID_Path, FacilityID_Facility, Position_Facility_Path)
VALUES (2, 50, 12);

--Route
INSERT INTO Route (ID_Route, Is_Simple, PathID_Path)
VALUES (1, '0', 1);
INSERT INTO Route (ID_Route, Is_Simple, PathID_Path)
VALUES (2, '1', 2);
INSERT INTO Route (ID_Route, Is_Simple, PathID_Path)
VALUES (3, '0', 2);

-- Train (All Invented)
INSERT INTO Train (ID_Train, Scheduled_Construction_Date, Maximum_Length, Dispatched, OperatorID_Operator, RouteID_Route)
VALUES (5421, TO_DATE('2025-10-03 09:45:00', 'YYYY-MM-DD HH24:MI:SS'), 500, 0, 'PT509017800', 1);
INSERT INTO Train (ID_Train, Scheduled_Construction_Date, Maximum_Length, Dispatched, OperatorID_Operator, RouteID_Route)
VALUES (5435, TO_DATE('2025-10-03 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), 50, 0,  'PT509017800', 2);
INSERT INTO Train (ID_Train, Scheduled_Construction_Date, Maximum_Length, Dispatched, OperatorID_Operator, RouteID_Route)
VALUES (5437, TO_DATE('2025-10-06 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 500, 0, 'PT509017800', 3);

--Freight
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2001, DATE '2025-10-03', 50, 12, 1);
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2002, DATE '2025-10-03', 13, 11, 1);
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2003, DATE '2025-10-03', 50, 11, 1);
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2004, DATE '2025-10-03', 50, 21, 1);
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2005, DATE '2025-10-03', 20, 11, 1);
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2006, DATE '2025-10-03', 12, 50, 2);
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2007, DATE '2025-10-03', 50, 5, 1);
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2050, DATE '2025-10-06', 12, 50, 3);
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (2051, DATE '2025-10-06', 11, 50, 3);

--Wagon_Freight
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330001, 2001);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330002, 2001);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330004, 2001);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330005, 2001);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330006, 2001);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563089, 2002);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (1811011, 2003);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (1811012, 2003);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (1811013, 2004);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563077, 2005);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563078, 2005);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563079, 2005);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563080, 2005);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330003, 2006);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330007, 2006);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563090, 2007);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563091, 2007);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563092, 2007);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330001, 2050);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330002, 2050);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330004, 2050);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330005, 2050);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3330006, 2050);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (1811011, 2051);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (1811012, 2051);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563077, 2051);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563078, 2051);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563079, 2051);
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (3563080, 2051);

--Locomotive_Train
INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)
VALUES (5621, 5421);
INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)
VALUES (5623, 5421);
INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)
VALUES (5623, 5435);
INSERT INTO Locomotive_Train (LocomotiveID_Locomotive, TrainID_Train)
VALUES (5621, 5437);

--Locomotive_Model_Track_Gauge
INSERT INTO Locomotive_Model_Track_Gauge VALUES (1, 1);
INSERT INTO Locomotive_Model_Track_Gauge VALUES (2, 1);
INSERT INTO Locomotive_Model_Track_Gauge VALUES (3, 1);

--Wagon_Model_Track_Gauge
INSERT INTO Wagon_Model_Track_Gauge VALUES (1, 1);
INSERT INTO Wagon_Model_Track_Gauge VALUES (2, 1);
INSERT INTO Wagon_Model_Track_Gauge VALUES (3, 1);
INSERT INTO Wagon_Model_Track_Gauge VALUES (4, 1);
INSERT INTO Wagon_Model_Track_Gauge VALUES (5, 1);
INSERT INTO Wagon_Model_Track_Gauge VALUES (6, 1);
INSERT INTO Wagon_Model_Track_Gauge VALUES (6, 2);
INSERT INTO Wagon_Model_Track_Gauge VALUES (7, 1);
INSERT INTO Wagon_Model_Track_Gauge VALUES (7, 2);
INSERT INTO Wagon_Model_Track_Gauge VALUES (8, 1);
