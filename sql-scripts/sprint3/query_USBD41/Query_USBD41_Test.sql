DECLARE
v_result NUMBER;
    v_count  NUMBER;
BEGIN
------------------------------------------------------------
-- LIMPEZA PREVENTIVA (rerunnable)
------------------------------------------------------------
DELETE FROM Wagon_Freight WHERE FreightID_Freight IN (9000, 9001);
DELETE FROM Wagon WHERE ID_Wagon = 9000;
DELETE FROM Freight WHERE ID_Freight IN (9000, 9001);
DELETE FROM Train WHERE ID_Train = 9000;
DELETE FROM Route WHERE ID_Route = 9000;
DELETE FROM Path WHERE ID_Path = 9000;
COMMIT;

DBMS_OUTPUT.PUT_LINE('==============================');
DBMS_OUTPUT.PUT_LINE(' SETUP DOS DADOS DE TESTE ');
DBMS_OUTPUT.PUT_LINE('==============================');

-- Path
INSERT INTO Path (ID_Path) VALUES (9000);

-- Route
INSERT INTO Route (ID_Route, Is_Simple, PathID_Path)
VALUES (9000, '1', 9000);

-- Train (não dispatched)
INSERT INTO Train (ID_Train, Scheduled_Construction_Date, Maximum_Length, Dispatched, OperatorID_Operator, RouteID_Route)
VALUES (9000, SYSDATE, 500, '0', 'PT509017800', 9000);

-- Freight associado à rota
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (9000, SYSDATE, 50, 12, 9000);

-- Freight fora da rota (teste negativo)
INSERT INTO Freight (ID_Freight, "Date", OriginFacilityID_Facility, DestinationFacilityID_Facility, RouteID_Route)
VALUES (9001, SYSDATE, 50, 12, NULL);

-- Wagon
INSERT INTO Wagon (ID_Wagon, Starting_Date_Service, OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID)
VALUES (9000, SYSDATE, 'PT509017800', 1, 50);

-- Associação Wagon–Freight
INSERT INTO Wagon_Freight (WagonID_Wagon, FreightID_Freight)
VALUES (9000, 9000);

COMMIT;

------------------------------------------------------------
DBMS_OUTPUT.PUT_LINE(CHR(10) || '=== ESTADO INICIAL DAS TABELAS ===');

    DBMS_OUTPUT.PUT_LINE('-- Freight');
FOR r IN (
        SELECT ID_Freight, RouteID_Route
        FROM Freight
        WHERE ID_Freight IN (9000, 9001)
        ORDER BY ID_Freight
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Freight ' || r.ID_Freight ||
            ' | Route = ' || NVL(TO_CHAR(r.RouteID_Route), 'NULL')
        );
END LOOP;

    DBMS_OUTPUT.PUT_LINE('-- Wagon_Freight');
FOR r IN (
        SELECT WagonID_Wagon, FreightID_Freight
        FROM Wagon_Freight
        WHERE FreightID_Freight = 9000
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Wagon ' || r.WagonID_Wagon ||
            ' -> Freight ' || r.FreightID_Freight
        );
END LOOP;

    DBMS_OUTPUT.PUT_LINE('-- Train');
FOR r IN (
        SELECT ID_Train, Dispatched, RouteID_Route
        FROM Train
        WHERE ID_Train = 9000
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Train ' || r.ID_Train ||
            ' | Dispatched = ' || r.Dispatched ||
            ' | Route = ' || r.RouteID_Route
        );
END LOOP;

    ------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE(CHR(10) || '=== TESTE 1: CASO VÁLIDO ===');
    v_result := remove_freight_from_train(9000, 9000);
    DBMS_OUTPUT.PUT_LINE('Esperado: 1 | Obtido: ' || v_result);

    ------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE(CHR(10) || '=== ESTADO APÓS TESTE 1 ===');

    DBMS_OUTPUT.PUT_LINE('-- Freight');
FOR r IN (
        SELECT ID_Freight, RouteID_Route
        FROM Freight
        WHERE ID_Freight = 9000
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Freight ' || r.ID_Freight ||
            ' | Route = ' || NVL(TO_CHAR(r.RouteID_Route), 'NULL')
        );
END LOOP;

SELECT COUNT(*)
INTO v_count
FROM Wagon_Freight
WHERE FreightID_Freight = 9000;

IF v_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('-- Wagon_Freight: SEM REGISTOS');
END IF;

------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE(CHR(10) || '=== TESTES NEGATIVOS (NÃO ALTERAM ESTADO) ===');

    v_result := remove_freight_from_train(9000, 9000);
    DBMS_OUTPUT.PUT_LINE('Freight já removido → ' || v_result);

UPDATE Train SET Dispatched = '1' WHERE ID_Train = 9000;
COMMIT;
v_result := remove_freight_from_train(9000, 9000);
    DBMS_OUTPUT.PUT_LINE('Train dispatched → ' || v_result);

    v_result := remove_freight_from_train(9000, 9999);
    DBMS_OUTPUT.PUT_LINE('Train inexistente → ' || v_result);

    v_result := remove_freight_from_train(9999, 9000);
    DBMS_OUTPUT.PUT_LINE('Freight inexistente → ' || v_result);

    v_result := remove_freight_from_train(9001, 9000);
    DBMS_OUTPUT.PUT_LINE('Freight fora da rota → ' || v_result);

------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE(CHR(10) || '=== ESTADO FINAL DAS TABELAS ===');

    DBMS_OUTPUT.PUT_LINE('-- Freight');
FOR r IN (
        SELECT ID_Freight, RouteID_Route
        FROM Freight
        WHERE ID_Freight IN (9000, 9001)
        ORDER BY ID_Freight
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Freight ' || r.ID_Freight ||
            ' | Route = ' || NVL(TO_CHAR(r.RouteID_Route), 'NULL')
        );
END LOOP;

    DBMS_OUTPUT.PUT_LINE('-- Wagon_Freight');
SELECT COUNT(*)
INTO v_count
FROM Wagon_Freight
WHERE FreightID_Freight IN (9000, 9001);

IF v_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('SEM REGISTOS');
END IF;

------------------------------------------------------------
-- LIMPEZA FINAL
------------------------------------------------------------
DELETE FROM Wagon_Freight WHERE FreightID_Freight IN (9000, 9001);
DELETE FROM Wagon WHERE ID_Wagon = 9000;
DELETE FROM Freight WHERE ID_Freight IN (9000, 9001);
DELETE FROM Train WHERE ID_Train = 9000;
DELETE FROM Route WHERE ID_Route = 9000;
DELETE FROM Path WHERE ID_Path = 9000;
COMMIT;

DBMS_OUTPUT.PUT_LINE(CHR(10) || '=== TESTE CONCLUÍDO COM SUCESSO ===');

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('ERRO: ' || SQLERRM);
ROLLBACK;
RAISE;
END;
