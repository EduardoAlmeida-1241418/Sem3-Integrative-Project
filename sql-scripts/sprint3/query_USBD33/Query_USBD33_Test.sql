DECLARE
BEGIN
    /* Teste 1 – válido */
    BEGIN
        INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
        VALUES (3330010, 2006);

        DBMS_OUTPUT.PUT_LINE(
            'Test 1 PASSED: wagon 3330010 added (within maximum length)'
        );
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'Test 1 FAILED: unexpected error - ' || SQLERRM
            );
    END;

    /* Teste 2 – inválido */
    BEGIN
        INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
        VALUES (3330009, 2006);

        DBMS_OUTPUT.PUT_LINE(
            'Test 2 FAILED: wagon should not have been added'
        );
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(
                'Test 2 PASSED: expected USBD33 violation - ' || SQLERRM
            );
    END;

    -- Estado final
    DBMS_OUTPUT.PUT_LINE('Final wagons for Freight 2006:');

    FOR r IN (
        SELECT wf.WagonID_Wagon
        FROM Wagon_Freight wf
        WHERE wf.FreightID_Freight = 2006
        ORDER BY wf.WagonID_Wagon
    )
    LOOP
        DBMS_OUTPUT.PUT_LINE('Wagon ID: ' || r.WagonID_Wagon);
    END LOOP;
END;
