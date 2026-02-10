CREATE OR REPLACE FUNCTION add_wagon(
    p_starting_date_service IN Wagon.Starting_Date_Service%TYPE,
    p_operator_id           IN Wagon.OperatorID_Operator%TYPE,
    p_wagon_model_id        IN Wagon.Wagon_ModelID_Wagon_Model%TYPE,
    p_start_facility_id     IN Wagon.Start_Facility_ID%TYPE
)
    RETURN Wagon.ID_Wagon%TYPE
    IS
    v_wagon_id Wagon.ID_Wagon%TYPE;
BEGIN
    -- Gerar novo ID para o Wagon
    SELECT NVL(MAX(ID_Wagon), 0) + 1
    INTO v_wagon_id
    FROM Wagon;

    -- Inserir novo Wagon
    INSERT INTO Wagon (
        ID_Wagon,
        Starting_Date_Service,
        OperatorID_Operator,
        Wagon_ModelID_Wagon_Model,
        Start_Facility_ID
    ) VALUES (
                 v_wagon_id,
                 p_starting_date_service,
                 p_operator_id,
                 p_wagon_model_id,
                 p_start_facility_id
             );

    COMMIT;
    RETURN v_wagon_id;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END add_wagon;