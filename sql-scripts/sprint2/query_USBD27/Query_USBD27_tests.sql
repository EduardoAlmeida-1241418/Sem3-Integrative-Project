-- Test A
DECLARE
    c SYS_REFCURSOR;
    w NUMBER;
BEGIN
    dbms_output.put_line('--- Test A ---');
    dbms_output.put_line('Date: 2025-10-03 to 2025-10-03');
    dbms_output.put_line('Trains with grains: 5421');
    dbms_output.put_line('Expected wagons: 1811011, 1811012, 1811013');

    c := usbd27(DATE '2025-10-03', DATE '2025-10-03');
    LOOP
        FETCH c INTO w;
        EXIT WHEN c%NOTFOUND;
        dbms_output.put_line('Actual: ' || w);
    END LOOP;
    CLOSE c;
END;
/

-- Test B
DECLARE
    c SYS_REFCURSOR;
    w NUMBER;
BEGIN
    dbms_output.put_line('--- Test B ---');
    dbms_output.put_line('Date: 2025-10-06 to 2025-10-06');
    dbms_output.put_line('Train with grains: 5437');
    dbms_output.put_line('Expected wagons: 1811011, 1811012');

    c := usbd27(DATE '2025-10-06', DATE '2025-10-06');
    LOOP
        FETCH c INTO w;
        EXIT WHEN c%NOTFOUND;
        dbms_output.put_line('Actual: ' || w);
    END LOOP;
    CLOSE c;
END;
/

-- Test C
DECLARE
    c SYS_REFCURSOR;
    w NUMBER;
BEGIN
    dbms_output.put_line('--- Test C ---');
    dbms_output.put_line('Period: November 2025');
    dbms_output.put_line('Expected: (empty)');

    c := usbd27(DATE '2025-11-01', DATE '2025-11-30');

    LOOP
        FETCH c INTO w;
        dbms_output.put_line('Actual: ');
        EXIT WHEN c%NOTFOUND;
        dbms_output.put_line(w);
    END LOOP;
    CLOSE c;
END;
/

-- Test D
DECLARE
    c SYS_REFCURSOR;
    w NUMBER;
BEGIN
    dbms_output.put_line('--- Test D ---');
    dbms_output.put_line('Date: 2025-10-03 to 2025-10-06');
    dbms_output.put_line('Trains with grains: 5421, 5437');
    dbms_output.put_line('Expected wagons: 1811011, 1811012');

    c := usbd27(DATE '2025-10-03', DATE '2025-10-06');
    LOOP
        FETCH c INTO w;
        EXIT WHEN c%NOTFOUND;
        dbms_output.put_line('Actual: ' || w);
    END LOOP;
    CLOSE c;
END;
/