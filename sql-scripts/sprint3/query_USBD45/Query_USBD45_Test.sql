-- USBD45 - Test 1: Create line with 1 segment
DECLARE
v_result NUMBER;
    c_segments SYS_REFCURSOR;
BEGIN
OPEN c_segments FOR
SELECT 10 FROM dual;

v_result := add_rail_line_with_segments(
        9001,
        'USBD45_LINE_1_SEG_A',
        7,
        5,
        'PT503933813',
        c_segments
    );

    DBMS_OUTPUT.PUT_LINE('Line created, ID: ' || v_result);
END;
/

--- USBD45 - Test 2: Create line with no segments.
DECLARE
v_result NUMBER;
    c_segments SYS_REFCURSOR;
BEGIN
OPEN c_segments FOR
SELECT ID_Rail_Line_Segment
FROM Rail_Line_Segment
WHERE 1 = 0; -- cursor vazio

v_result := add_rail_line_with_segments(
        5004,
        'USBD45_NO_SEGMENTS',
        7,
        5,
        'PT503933813',
        c_segments
    );
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;
/


--- USBD45 - Query to verify the inserted rail line and its segments.
SELECT
    rl.ID_Rail_Line,
    rl.Name                AS Rail_Line_Name,
    rls.Order_Line,
    rls.Rail_Line_SegmentID_Rail_Line_Segment AS Segment_ID
FROM Rail_Line rl
         JOIN Rail_Line_Rail_Line_Segment rls
              ON rls.Rail_LineID_Rail_Line = rl.ID_Rail_Line
ORDER BY rl.ID_Rail_Line, rls.Order_Line;

