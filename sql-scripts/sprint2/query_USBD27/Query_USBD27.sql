CREATE OR REPLACE FUNCTION usbd27(
    p_start IN DATE,
    p_end   IN DATE
)
RETURN SYS_REFCURSOR
AS
    c SYS_REFCURSOR;
    n_trains NUMBER;
BEGIN

SELECT COUNT(DISTINCT f.TrainID_Train)
INTO n_trains
FROM Freight f
         JOIN Wagon_Freight wf ON wf.FreightID_Freight = f.ID_Freight
         JOIN Wagon w ON w.ID_Wagon = wf.WagonID_Wagon
         JOIN Wagon_Model wm ON wm.ID_Wagon_Model = w.Wagon_ModelID_Wagon_Model
         JOIN Wagon_Type_Cargo_Type wtct ON wtct.Wagon_TypeID_Wagon_Type = wm.Wagon_TypeID_Wagon_Type
         JOIN Cargo_Type ct ON ct.ID_Cargo_Type = wtct.Cargo_TypeID_Cargo_Type
WHERE UPPER(ct.Name) = 'GRAINS'
  AND f."Date" BETWEEN p_start AND p_end
  AND f.TrainID_Train IS NOT NULL;

OPEN c FOR
SELECT wf.WagonID_Wagon
FROM Wagon_Freight wf
         JOIN Freight f ON f.ID_Freight = wf.FreightID_Freight
         JOIN Wagon w ON w.ID_Wagon = wf.WagonID_Wagon
         JOIN Wagon_Model wm ON wm.ID_Wagon_Model = w.Wagon_ModelID_Wagon_Model
         JOIN Wagon_Type_Cargo_Type wtct ON wtct.Wagon_TypeID_Wagon_Type = wm.Wagon_TypeID_Wagon_Type
         JOIN Cargo_Type ct ON ct.ID_Cargo_Type = wtct.Cargo_TypeID_Cargo_Type
WHERE UPPER(ct.Name) = 'GRAINS'
  AND f."Date" BETWEEN p_start AND p_end
GROUP BY wf.WagonID_Wagon
HAVING COUNT(DISTINCT f.TrainID_Train) = n_trains;

RETURN c;
END;
/