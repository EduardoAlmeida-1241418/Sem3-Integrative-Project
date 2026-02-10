CREATE OR REPLACE FUNCTION usbd23(p_train_id IN Train.ID_Train%TYPE)
    RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT
            rls.Rail_Line_SegmentID_Rail_Line_Segment AS Segment_ID,
            rlseg.Length,
            rlseg.Speed_Limit,
            rlseg.Number_Tracks,
            rlseg.Is_Electrified_Line
        FROM Train t
                 JOIN Route r
                      ON t.RouteID_Route = r.ID_Route
                 JOIN Rail_Line_Rail_Line_Segment rls
                      ON r.ID_Route = rls.Rail_LineID_Rail_Line
                 JOIN Rail_Line_Segment rlseg
                      ON rlseg.ID_Rail_Line_Segment = rls.Rail_Line_SegmentID_Rail_Line_Segment
        WHERE t.ID_Train = p_train_id
        ORDER BY rls.Order_Line;
    RETURN result_cursor;
END;
/