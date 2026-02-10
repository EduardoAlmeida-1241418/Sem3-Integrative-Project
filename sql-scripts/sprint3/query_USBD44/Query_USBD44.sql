CREATE OR REPLACE FUNCTION add_segment_to_line_with_siding (
    p_segment_id      IN Rail_Line_Segment.ID_Rail_Line_Segment%TYPE,
    p_rail_line_id    IN Rail_Line.ID_Rail_Line%TYPE,
    p_order_in_line   IN Rail_Line_Rail_Line_Segment.Order_Line%TYPE,
    p_siding_length   IN Siding.Length%TYPE,
    p_siding_position IN Siding.Position%TYPE
)
    RETURN Rail_Line_Segment.ID_Rail_Line_Segment%TYPE
AS
    v_siding_id Siding.ID_Siding%TYPE;
BEGIN
    -- Associar segmento à linha
    INSERT INTO Rail_Line_Rail_Line_Segment (
        Rail_LineID_Rail_Line,
        Rail_Line_SegmentID_Rail_Line_Segment,
        Order_Line
    ) VALUES (
                 p_rail_line_id,
                 p_segment_id,
                 p_order_in_line
             );

    -- Criar siding se aplicável
    IF p_siding_length > 0 AND p_siding_position > 0 THEN
        SELECT NVL(MAX(ID_Siding), 0) + 1
        INTO v_siding_id
        FROM Siding;

        INSERT INTO Siding (ID_Siding, Position, Length)
        VALUES (v_siding_id, p_siding_position, p_siding_length);

        UPDATE Rail_Line_Segment
        SET ID_Siding = v_siding_id
        WHERE ID_Rail_Line_Segment = p_segment_id;
    END IF;

    COMMIT;
    RETURN p_segment_id;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;