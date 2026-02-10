-- Segmentos existentes nos INSERTs:
-- ID 1: Comprimento 2618 | Vel. Máx 120 | Já está na linha 1 (ordem 1)
-- ID 3: Comprimento 2443 | Vel. Máx 100 | Já está na linha 2
-- ID 10: Comprimento 26560 | Vel. Máx 100 | Já está na linha 3

-- Sidings existentes: ID 1 (segmento 21), ID 2 (segmento 25)

-- Testes: Adicionar segmento 3 à linha 1 (sem siding) e segmento 10 à linha 1 (com siding)

DECLARE
    v_result NUMBER;
BEGIN
    v_result := add_segment_to_line_with_siding(3, 1, 2, 0, 0);
    dbms_output.put_line('Segment 3 added to line 1 without siding');

    v_result := add_segment_to_line_with_siding(10, 1, 3, 500, 1200);
    dbms_output.put_line('Segment 10 added to line 1 with siding');

    dbms_output.put_line('Line 1 after the inserts:');

    FOR r IN (
        SELECT rlrs.Order_Line,
               rls.ID_Rail_Line_Segment,
               s.ID_Siding
        FROM Rail_Line_Rail_Line_Segment rlrs
                 JOIN Rail_Line_Segment rls
                      ON rlrs.Rail_Line_SegmentID_Rail_Line_Segment =
                         rls.ID_Rail_Line_Segment
                 LEFT JOIN Siding s
                           ON rls.ID_Siding = s.ID_Siding
        WHERE rlrs.Rail_LineID_Rail_Line = 1
        ORDER BY rlrs.Order_Line
        )
        LOOP
            IF r.ID_Siding IS NULL THEN
                dbms_output.put_line(
                        'Order ' || r.Order_Line ||
                        ' | Segment ' || r.ID_Rail_Line_Segment ||
                        ' | no siding'
                );
            ELSE
                dbms_output.put_line(
                        'Order ' || r.Order_Line ||
                        ' | Segment ' || r.ID_Rail_Line_Segment ||
                        ' | siding ' || r.ID_Siding
                );
            END IF;
        END LOOP;
END;