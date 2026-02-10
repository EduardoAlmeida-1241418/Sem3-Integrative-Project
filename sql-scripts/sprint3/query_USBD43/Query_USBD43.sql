CREATE OR REPLACE FUNCTION add_electric_locomotive_model(
    p_name           IN Locomotive_Model.Name%TYPE,
    p_power          IN Locomotive_Model.Power%TYPE,
    p_maximum_weight IN Locomotive_Model.Maximum_Weight%TYPE,
    p_acceleration   IN Locomotive_Model.Acceleration%TYPE,
    p_number_wheels  IN Locomotive_Model.Number_Wheels%TYPE,
    p_max_speed      IN Locomotive_Model.Max_Speed%TYPE,
    p_operational_speed IN Locomotive_Model.Operational_Speed%TYPE,
    p_traction       IN Locomotive_Model.Traction%TYPE,
    p_maker_id       IN Locomotive_Model.MakerID_Maker%TYPE,
    p_dimensions_id  IN Locomotive_Model.DimensionsID_Dimensions%TYPE
) RETURN Locomotive_Model.ID_Locomotive_Model%TYPE
IS
    v_model_id Locomotive_Model.ID_Locomotive_Model%TYPE;
BEGIN
    -- Calcular o novo ID
    SELECT NVL(MAX(ID_Locomotive_Model), 0) + 1
    INTO v_model_id
    FROM Locomotive_Model;

    -- Inserir (Fuel_TypeID_Fuel_Type = 2 para Electric)
    INSERT INTO Locomotive_Model VALUES (
        v_model_id,
        p_name,
        p_power,
        p_maximum_weight,
        p_acceleration,
        p_number_wheels,
        p_max_speed,
        p_operational_speed,
        p_traction,
        p_maker_id,
        p_dimensions_id,
        2  -- Electric
    );

    COMMIT;

    RETURN v_model_id;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END add_electric_locomotive_model;