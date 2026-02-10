-- Test 1: Non-Stop passage points - Train 5421
DECLARE
cursor_test SYS_REFCURSOR;
    v_facility_id Facility.ID_Facility%TYPE;
    v_facility_name Facility.Name%TYPE;
    v_position Path_Facility.Position_Facility_Path%TYPE;
BEGIN
    dbms_output.put_line('Test 1: Non-Stop passage points - Train 5421');
    dbms_output.put_line('Expected: 6 non-stop passage points on Route 1.');

    cursor_test := usbd25(5421);

    dbms_output.put_line('Actual Result:');
    LOOP
FETCH cursor_test INTO v_facility_id, v_facility_name, v_position;
        EXIT WHEN cursor_test%NOTFOUND;
        dbms_output.put_line('  ID: ' || v_facility_id || ', Name: ' || v_facility_name || ', Position: ' || v_position);
END LOOP;
CLOSE cursor_test;
END;
/

-- Test 2: Non-Stop Points - Train 5437
DECLARE
cursor_test SYS_REFCURSOR;
    v_facility_id Facility.ID_Facility%TYPE;
    v_facility_name Facility.Name%TYPE;
    v_position Path_Facility.Position_Facility_Path%TYPE;
BEGIN
    dbms_output.put_line('Test 2: Non-Stop passage points - Train 5437');
    dbms_output.put_line('Expected: 9 non-stop passage points on Route 3.');

    cursor_test := usbd25(5437);

    dbms_output.put_line('Result:');
    LOOP
FETCH cursor_test INTO v_facility_id, v_facility_name, v_position;
        EXIT WHEN cursor_test%NOTFOUND;
        dbms_output.put_line('  ID: ' || v_facility_id || ', Name: ' || v_facility_name || ', Position: ' || v_position);
END LOOP;
CLOSE cursor_test;
END;
/

-- Test 3: Route Without Associated Freight (Train 5435)
DECLARE
cursor_test SYS_REFCURSOR;
    v_facility_id Facility.ID_Facility%TYPE;
    v_facility_name Facility.Name%TYPE;
    v_position Path_Facility.Position_Facility_Path%TYPE;
BEGIN
    dbms_output.put_line('Test 3: Route Without Associated Freight - Train 5435');
    dbms_output.put_line('Expected: All 10 route points are returned on Route 2.');

    cursor_test := usbd25(5435);

    dbms_output.put_line('Result:');
    LOOP
FETCH cursor_test INTO v_facility_id, v_facility_name, v_position;
        EXIT WHEN cursor_test%NOTFOUND;
        dbms_output.put_line('  ID: ' || v_facility_id || ', Name: ' || v_facility_name || ', Position: ' || v_position);
END LOOP;
CLOSE cursor_test;
END;
/

-- Test 4: Non-Existent Train ID (Train 0)
DECLARE
cursor_test SYS_REFCURSOR;
    v_facility_id Facility.ID_Facility%TYPE;
    v_facility_name Facility.Name%TYPE;
    v_position Path_Facility.Position_Facility_Path%TYPE;
BEGIN
    dbms_output.put_line('Test 4: Non-Existent Train ID - Train 0');
    dbms_output.put_line('Expected: No results found.');

    cursor_test := usbd25(0);

    dbms_output.put_line('Result:');
    LOOP
FETCH cursor_test INTO v_facility_id, v_facility_name, v_position;
        EXIT WHEN cursor_test%NOTFOUND;
        dbms_output.put_line('  ID: ' || v_facility_id || ', Name: ' || v_facility_name || ', Position: ' || v_position);
END LOOP;

    IF cursor_test%ROWCOUNT = 0 THEN
        dbms_output.put_line('No results found.');
END IF;
CLOSE cursor_test;
END;
/
