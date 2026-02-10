CREATE OR REPLACE FUNCTION add_building_to_facility (
    p_id_building        IN Building.ID_Building%TYPE,
    p_name               IN Building.Name%TYPE,
    p_facility_id        IN Facility.ID_Facility%TYPE,
    p_building_type_id   IN Building.Building_TypeID_Building_Type%TYPE
) RETURN NUMBER
IS
    v_count NUMBER;
BEGIN
    -- Check if Facility exists
SELECT COUNT(*)
INTO v_count
FROM Facility
WHERE ID_Facility = p_facility_id;

IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Facility does not exist');
END IF;

    -- Check if Building Type exists
SELECT COUNT(*)
INTO v_count
FROM Building_Type
WHERE ID_Building_Type = p_building_type_id;

IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Building type does not exist');
END IF;

    -- Insert Building
INSERT INTO Building (
    ID_Building,
    Name,
    FacilityID_Facility,
    Building_TypeID_Building_Type
) VALUES (
             p_id_building,
             p_name,
             p_facility_id,
             p_building_type_id
         );

RETURN p_id_building;

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        RAISE_APPLICATION_ERROR(-20003, 'Building name already exists');

WHEN OTHERS THEN

        IF SQLCODE BETWEEN -20999 AND -20000 THEN
            RAISE;
ELSE
            RAISE_APPLICATION_ERROR(-20099, 'Unexpected error while adding building');
END IF;
END;
/
