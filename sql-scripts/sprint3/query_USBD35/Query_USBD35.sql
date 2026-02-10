CREATE OR REPLACE FUNCTION add_facility(
    p_name IN Facility.Name%TYPE,
    p_intermodal IN Facility.Intermodal%TYPE,
    p_facility_type_id IN Facility.Facility_TypeID_Facility_Type%TYPE
)
RETURN Facility.ID_Facility%TYPE
IS
    v_facility_id Facility.ID_Facility%TYPE;
BEGIN
    -- Calcular novo ID
SELECT NVL(MAX(ID_Facility), 0) + 1
INTO v_facility_id
FROM Facility;

-- Inserir nova facility
INSERT INTO Facility (
    ID_Facility,
    Name,
    Intermodal,
    Facility_TypeID_Facility_Type
) VALUES (
             v_facility_id,
             p_name,
             p_intermodal,
             p_facility_type_id
         );

COMMIT;

RETURN v_facility_id;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END add_facility;