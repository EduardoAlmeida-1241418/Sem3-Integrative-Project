-- USBD36 - Add a building to an existing facility
-- Tests: valid insertions of different building types

DECLARE
v_result NUMBER;
BEGIN
    v_result := add_building_to_facility(930, 'USBD36 Warehouse', 2, 1);
    DBMS_OUTPUT.PUT_LINE('Created Warehouse, ID: ' || v_result);
END;
/

DECLARE
v_result NUMBER;
BEGIN
    v_result := add_building_to_facility(931, 'USBD36 Refrigerated Area', 2, 2);
    DBMS_OUTPUT.PUT_LINE('Created Refrigerated Area, ID: ' || v_result);
END;
/

DECLARE
v_result NUMBER;
BEGIN
    v_result := add_building_to_facility(932, 'USBD36 Grain Silo', 2, 3);
    DBMS_OUTPUT.PUT_LINE('Created Grain Silo, ID: ' || v_result);
END;
/


-- USBD36 - Validation: duplicate building name
DECLARE
v_result NUMBER;
BEGIN
    v_result := add_building_to_facility(950, 'Main Warehouse', 1, 1);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;
/


-- USBD36 - Validation: facility does not exist

DECLARE
v_result NUMBER;
BEGIN
    v_result := add_building_to_facility(
        960,
        'USBD36 Invalid Facility',
        9999,  -- facility inexistente
        1
    );
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;
/



-- USBD36 - Validation: building type does not exist

DECLARE
v_result NUMBER;
BEGIN
    v_result := add_building_to_facility(
        961,
        'USBD36 Invalid Type',
        1,
        9999  -- tipo inexistente
    );
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;
/


-- Verification of inserted buildings.
SELECT
    b.ID_Building,
    b.Name  AS Building_Name,
    bt.Name AS Building_Type,
    f.Name  AS Facility_Name
FROM Building b
         JOIN Building_Type bt
              ON bt.ID_Building_Type = b.Building_TypeID_Building_Type
         JOIN Facility f
              ON f.ID_Facility = b.FacilityID_Facility
WHERE b.FacilityID_Facility = 2
ORDER BY b.ID_Building;

