CREATE OR REPLACE FUNCTION add_rail_line_with_segments (
    p_id_line            IN Rail_Line.ID_Rail_Line%TYPE,
    p_name               IN Rail_Line.Name%TYPE,
    p_start_facility_id  IN Rail_Line.Start_FacilityID_Facility%TYPE,
    p_end_facility_id    IN Rail_Line.End_FacilityID_Facility%TYPE,
    p_owner_id           IN Rail_Line.OwnerID_Owner%TYPE,
    p_segments           IN SYS_REFCURSOR
) RETURN NUMBER
IS
    v_segment_id Rail_Line_Segment.ID_Rail_Line_Segment%TYPE;
    v_order      NUMBER := 1;
BEGIN
    -- Validate: at least one segment
FETCH p_segments INTO v_segment_id;
IF p_segments%NOTFOUND THEN
        RAISE_APPLICATION_ERROR(
            -20007,
            'Line must have at least one segment'
        );
END IF;

    -- Validate segment exists
    DECLARE
v_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO v_count
FROM Rail_Line_Segment
WHERE ID_Rail_Line_Segment = v_segment_id;

IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(
                -20004,
                'Rail line segment does not exist'
            );
END IF;
END;

    -- Insert Rail Line
INSERT INTO Rail_Line (
    ID_Rail_Line,
    Name,
    Start_FacilityID_Facility,
    End_FacilityID_Facility,
    OwnerID_Owner
) VALUES (
             p_id_line,
             p_name,
             p_start_facility_id,
             p_end_facility_id,
             p_owner_id
         );

-- Insert first segment
INSERT INTO Rail_Line_Rail_Line_Segment (
    Rail_LineID_Rail_Line,
    Rail_Line_SegmentID_Rail_Line_Segment,
    Order_Line
) VALUES (
             p_id_line,
             v_segment_id,
             v_order
         );

-- Remaining segments
LOOP
FETCH p_segments INTO v_segment_id;
        EXIT WHEN p_segments%NOTFOUND;

        v_order := v_order + 1;

INSERT INTO Rail_Line_Rail_Line_Segment (
    Rail_LineID_Rail_Line,
    Rail_Line_SegmentID_Rail_Line_Segment,
    Order_Line
) VALUES (
             p_id_line,
             v_segment_id,
             v_order
         );
END LOOP;

CLOSE p_segments;

RETURN p_id_line;

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE BETWEEN -20999 AND -20000 THEN
            RAISE;
ELSE
            RAISE_APPLICATION_ERROR(
                -20099,
                'Unexpected error while adding rail line'
            );
END IF;
END;
/
