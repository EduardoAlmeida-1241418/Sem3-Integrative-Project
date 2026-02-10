DECLARE
v_result NUMBER;
BEGIN
    v_result := add_facility(
        'Nova Estação',
        '1',
        1
    );
    dbms_output.put_line('New facility added with ID: ' || v_result);
    dbms_output.put_line('All facilities in database:');

FOR r IN (
        SELECT ID_Facility, Name, Intermodal
        FROM Facility
        ORDER BY ID_Facility
    )
    LOOP
        dbms_output.put_line('  ' || r.ID_Facility || ' - ' || r.Name ||
                           ' (Intermodal: ' || r.Intermodal || ')');
END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('Error: ' || SQLERRM);
END;