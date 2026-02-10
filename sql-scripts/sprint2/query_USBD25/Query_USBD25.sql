CREATE OR REPLACE FUNCTION usbd25(p_train_id IN Train.ID_Train%TYPE)
RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
OPEN result_cursor FOR
SELECT DISTINCT
    pf.FacilityID_Facility AS Facility_ID,
    f.Name AS Facility_Name,
    pf.Position_Facility_Path AS Position_on_Route
FROM Train t
         JOIN Route r ON t.RouteID_Route = r.ID_Route
         JOIN Path p ON p.ID_Path = r.PathID_Path
         JOIN Path_Facility pf ON pf.PathID_Path = p.ID_Path
         JOIN Facility f ON f.ID_Facility = pf.FacilityID_Facility
WHERE t.ID_Train = p_train_id
  AND pf.FacilityID_Facility NOT IN (
    SELECT OriginFacilityID_Facility
    FROM Freight
    WHERE TrainID_Train = p_train_id
    UNION
    SELECT DestinationFacilityID_Facility
    FROM Freight
    WHERE TrainID_Train = p_train_id
)
ORDER BY pf.Position_Facility_Path;
RETURN result_cursor;
END;
/