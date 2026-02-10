CREATE OR REPLACE FUNCTION remove_freight_from_train (
    p_freight_id IN Freight.ID_Freight%TYPE,
    p_train_id   IN Train.ID_Train%TYPE
)
RETURN NUMBER
IS
    v_count       NUMBER;
    v_route_id    Train.RouteID_Route%TYPE;
BEGIN

-- 1. Verificar se o train existe e NÃO foi dispatched
SELECT COUNT(*)
INTO v_count
FROM Train
WHERE ID_Train = p_train_id AND Dispatched = '0';

IF v_count = 0 THEN
        RETURN 0;
END IF;

-- 2. Obter a rota do train
SELECT RouteID_Route
INTO v_route_id
FROM Train
WHERE ID_Train = p_train_id;

-- 3. Verificar se o freight pertence à mesma rota
SELECT COUNT(*)
INTO v_count
FROM Freight
WHERE ID_Freight = p_freight_id AND RouteID_Route = v_route_id;

IF v_count = 0 THEN RETURN 0;
END IF;

-- 4. Remover wagons associados ao freight
DELETE FROM Wagon_Freight
WHERE FreightID_Freight = p_freight_id;

-- 5. Remover freight da rota (logo do train)
UPDATE Freight
SET RouteID_Route = NULL
WHERE ID_Freight = p_freight_id;

COMMIT;
RETURN 1;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END remove_freight_from_train;
