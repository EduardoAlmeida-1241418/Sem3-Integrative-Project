SELECT
    w.ID_Wagon,
    w.Starting_Year_Service,
    w.OperatorID_Operator,
    wm.ID_Wagon_Model,
    wm.Name AS wagon_model,
    wt.Name AS wagon_type,
    wm.Payload,
    wm.Volume_Capacity,
    wm.Track_GaugeID_Track_Gauge
FROM Wagon w
         JOIN Wagon_Model wm ON wm.ID_Wagon_Model = w.Wagon_ModelID_Wagon_Model
         JOIN Wagon_Type  wt ON wt.ID_Wagon_Type  = wm.Wagon_TypeID_Wagon_Type
WHERE wt.ID_Wagon_Type = 4
ORDER BY w.ID_Wagon;