CREATE OR REPLACE FUNCTION add_gauge(
    p_gauge_size IN Track_Gauge.Gauge_Size%TYPE
)
RETURN Track_Gauge.ID_Track_Gauge%TYPE
IS
    v_gauge_id Track_Gauge.ID_Track_Gauge%TYPE;
    v_count    NUMBER;

BEGIN
    -- Verificar se já existe
    SELECT COUNT(*)
    INTO v_count
    FROM Track_Gauge
    WHERE Gauge_Size = p_gauge_size;

    -- Retornar ID existente
    IF v_count > 0 THEN
        SELECT ID_Track_Gauge
        INTO v_gauge_id
        FROM Track_Gauge
        WHERE Gauge_Size = p_gauge_size;

        RETURN v_gauge_id;
    END IF;

    -- Calcular o novo ID
    SELECT NVL(MAX(ID_Track_Gauge), 0) + 1
    INTO v_gauge_id
    FROM Track_Gauge;

    -- Inserir
    INSERT INTO Track_Gauge (ID_Track_Gauge, Gauge_Size)
    VALUES (v_gauge_id, p_gauge_size);

    COMMIT;

    RETURN v_gauge_id;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END add_gauge;