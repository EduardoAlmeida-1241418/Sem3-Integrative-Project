SELECT
    rl.Name AS Rail_Line_Name,
    MIN(rls.Length) AS Min_Double_Track_Length
FROM
    Rail_Line rl
        JOIN
    Rail_Line_Rail_Line_Segment rlr
    ON rl.ID_Rail_Line = rlr.Rail_LineID_Rail_Line
        JOIN
    Rail_Line_Segment rls
    ON rlr.Rail_Line_SegmentID_Rail_Line_Segment = rls.ID_Rail_Line_Segment
WHERE
    rls.Number_Tracks = 2
    AND rl.ID_Rail_Line = 3
GROUP BY
    rl.Name;