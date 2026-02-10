-- Gauges existentes nos inserts:
-- ID: 1 - 1668 mm
-- ID: 2 - 1435 mm

-- No final, a tabela Track_Gauge terá:
-- ID: 1 - 1668 mm
-- ID: 2 - 1435 mm
-- ID: 3 - 1000 mm
-- ID: 4 - 1520 mm
-- (a lista final é apresentada ordenada por Gauge_Size)

DECLARE
v_result NUMBER;
    v_gauge_size Track_Gauge.Gauge_Size%TYPE;
    v_gauge_id Track_Gauge.ID_Track_Gauge%TYPE;

BEGIN
    -- Teste 1: Adicionar uma nova bitola
    v_gauge_size := 1000;
    v_result := add_gauge(v_gauge_size);
    dbms_output.put_line('Test 1 - Added gauge ' || v_gauge_size || ' mm, ID: ' || v_result);

    -- Teste 2: Adicionar uma bitola já existente
    v_gauge_size := 1668;
    v_result := add_gauge(v_gauge_size);
    dbms_output.put_line('Test 2 - Existing gauge ' || v_gauge_size || ' mm, ID: ' || v_result);

    -- Teste 3: Adicionar outra nova bitola
    v_gauge_size := 1520;
    v_result := add_gauge(v_gauge_size);
    dbms_output.put_line('Test 3 - Added gauge ' || v_gauge_size || ' mm, ID: ' || v_result);

    -- Verificar o estado final
    dbms_output.put_line('All gauges in database:');
FOR r IN (SELECT ID_Track_Gauge, Gauge_Size FROM Track_Gauge ORDER BY Gauge_Size)
    LOOP
        dbms_output.put_line('  ' || r.ID_Track_Gauge || ' - ' || r.Gauge_Size || ' mm');
END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('Error');
END;