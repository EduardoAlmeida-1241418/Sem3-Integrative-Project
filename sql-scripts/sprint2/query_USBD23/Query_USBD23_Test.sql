-- Test 1: Simple Route (Train 5421)
DECLARE
    cursor_test SYS_REFCURSOR;
    v_segment_id Rail_Line_Segment.ID_Rail_Line_Segment%TYPE;
    v_length Rail_Line_Segment.Length%TYPE;
    v_speed_limit Rail_Line_Segment.Speed_Limit%TYPE;
    v_number_tracks Rail_Line_Segment.Number_Tracks%TYPE;
    v_is_electrified_line Rail_Line_Segment.Is_Electrified_Line%TYPE;
BEGIN
    dbms_output.put_line('Test 1: Simple Route - Train 5421');
    dbms_output.put_line('Expected: 1 segment (ID: 1).');

    cursor_test := usbd23(5421);

    dbms_output.put_line('Result:');
    LOOP
        FETCH cursor_test INTO v_segment_id, v_length, v_speed_limit, v_number_tracks, v_is_electrified_line;
        EXIT WHEN cursor_test%NOTFOUND;
        dbms_output.put_line('  Segment ID: ' || v_segment_id || ', Length: ' || v_length || ', Speed Limit: ' || v_speed_limit);
    END LOOP;
    CLOSE cursor_test;
END;
/

-- Test 2: Complex Route (Train 5435)
DECLARE
    cursor_test SYS_REFCURSOR;
    v_segment_id Rail_Line_Segment.ID_Rail_Line_Segment%TYPE;
    v_length Rail_Line_Segment.Length%TYPE;
    v_speed_limit Rail_Line_Segment.Speed_Limit%TYPE;
    v_number_tracks Rail_Line_Segment.Number_Tracks%TYPE;
    v_is_electrified_line Rail_Line_Segment.Is_Electrified_Line%TYPE;
BEGIN
    dbms_output.put_line('Test 2: Complex Route - Train 5437');
    dbms_output.put_line('Expected: 2 segments, in order (ID: 10, then ID: 11).');

    cursor_test := usbd23(5437);

    dbms_output.put_line('Result:');
    LOOP
        FETCH cursor_test INTO v_segment_id, v_length, v_speed_limit, v_number_tracks, v_is_electrified_line;
        EXIT WHEN cursor_test%NOTFOUND;
        dbms_output.put_line('  Segment ID: ' || v_segment_id || ', Length: ' || v_length || ', Speed Limit: ' || v_speed_limit);
    END LOOP;
    CLOSE cursor_test;
END;
/

-- Test 3: Non-Existent ID (Train 0)
DECLARE
    cursor_test SYS_REFCURSOR;
    v_segment_id Rail_Line_Segment.ID_Rail_Line_Segment%TYPE;
    v_length Rail_Line_Segment.Length%TYPE;
    v_speed_limit Rail_Line_Segment.Speed_Limit%TYPE;
    v_number_tracks Rail_Line_Segment.Number_Tracks%TYPE;
    v_is_electrified_line Rail_Line_Segment.Is_Electrified_Line%TYPE;
BEGIN
    dbms_output.put_line('Test 3: Non-Existent ID - Train 0');
    dbms_output.put_line('Expected: No results found.');

    cursor_test := usbd23(0);

    dbms_output.put_line('Result:');
    LOOP
        FETCH cursor_test INTO v_segment_id, v_length, v_speed_limit, v_number_tracks, v_is_electrified_line;
        EXIT WHEN cursor_test%NOTFOUND;
        dbms_output.put_line('  Segment ID: ' || v_segment_id || ', Length: ' || v_length || ', Speed Limit: ' || v_speed_limit);
    END LOOP;

    IF cursor_test%ROWCOUNT = 0 THEN
        dbms_output.put_line('	No results found.');
    END IF;
    CLOSE cursor_test;
END;
/