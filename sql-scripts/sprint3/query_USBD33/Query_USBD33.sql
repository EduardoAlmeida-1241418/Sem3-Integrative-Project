CREATE OR REPLACE TRIGGER trg_usbd33_max_train_length
BEFORE INSERT ON Wagon_Freight
FOR EACH ROW
DECLARE
    v_max_length     Train.Maximum_Length%TYPE;
    v_current_length NUMBER := 0;
    v_wagon_length   NUMBER := 0;
BEGIN
    /* Comprimento máximo do train */
    SELECT t.Maximum_Length
    INTO v_max_length
    FROM Train t
    JOIN Freight f ON f.RouteID_Route = t.RouteID_Route
    WHERE f.ID_Freight = :NEW.FreightID_Freight;

    /* Comprimento atual */
    SELECT NVL(SUM(d.Length), 0)
    INTO v_current_length
    FROM Wagon_Freight wf
    JOIN Freight f ON f.ID_Freight = wf.FreightID_Freight
    JOIN Wagon w ON w.ID_Wagon = wf.WagonID_Wagon
    JOIN Wagon_Model wm ON wm.ID_Wagon_Model = w.Wagon_ModelID_Wagon_Model
    JOIN Dimensions d ON d.ID_Dimensions = wm.DimensionsID_Dimensions
    WHERE f.RouteID_Route = (
        SELECT RouteID_Route
        FROM Freight
        WHERE ID_Freight = :NEW.FreightID_Freight
    );

    /* Comprimento do wagon */
    SELECT d.Length
    INTO v_wagon_length
    FROM Wagon w
    JOIN Wagon_Model wm ON wm.ID_Wagon_Model = w.Wagon_ModelID_Wagon_Model
    JOIN Dimensions d ON d.ID_Dimensions = wm.DimensionsID_Dimensions
    WHERE w.ID_Wagon = :NEW.WagonID_Wagon;

    IF (v_current_length + v_wagon_length) > v_max_length THEN
        raise_application_error(
            -20033,
            'USBD33 violation: train maximum length exceeded'
        );
    END IF;
END;