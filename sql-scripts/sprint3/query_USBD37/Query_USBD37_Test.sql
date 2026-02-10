DECLARE
    v_wagon_id       Wagon.ID_Wagon%TYPE;
    v_wagon_model_id Wagon.Wagon_ModelID_Wagon_Model%TYPE;
BEGIN
    -- Criar novo Wagon (modelo escolhido tem gauges registados)
    v_wagon_id := add_wagon(
            DATE '2025-01-01',
            'PT509017800',
            6,   -- Wagon_Model 6 (tem gauges 1 e 2)
            50
                  );

    dbms_output.put_line('New wagon added with ID: ' || v_wagon_id);

    -- Obter o modelo associado ao Wagon criado
    SELECT Wagon_ModelID_Wagon_Model
    INTO v_wagon_model_id
    FROM Wagon
    WHERE ID_Wagon = v_wagon_id;

    dbms_output.put_line('Wagon uses Wagon_Model ID: ' || v_wagon_model_id);
    dbms_output.put_line('Supported gauges (inherited from wagon model):');

    -- Listar gauges suportados
    FOR r IN (
        SELECT tg.Gauge_Size
        FROM Wagon_Model_Track_Gauge wmtg
                 JOIN Track_Gauge tg
                      ON wmtg.Track_GaugeID_Track_Gauge = tg.ID_Track_Gauge
        WHERE wmtg.Wagon_ModelID_Wagon_Model = v_wagon_model_id
        ORDER BY tg.Gauge_Size
        )
        LOOP
            dbms_output.put_line('  ' || r.Gauge_Size || ' mm');
        END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('Error: ' || SQLERRM);
END;