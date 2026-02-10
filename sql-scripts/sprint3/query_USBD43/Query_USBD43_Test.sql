-- Locomotivas existentes nos inserts:
-- ID_Model: 1 | Nome: Eurosprinter | FuelType: 2 | Tipo: Eletric <- apenas esta é válida
-- ID_Model: 2 | Nome: CP 1900      | FuelType: 1 | Tipo: Diesel
-- ID_Model: 3 | Nome: E4000        | FuelType: 1 | Tipo: Diesel

-- No final, a tabela Locomotive_Model terá:
-- ID_Model: 1 | Nome: Eurosprinter      | FuelType: 2 | Tipo: Eletric
-- ID_Model: 4 | Nome: LocoModel_Teste1  | FuelType: 2 | Tipo: Eletric
-- ID_Model: 5 | Nome  LocoModel_Teste2  | Fueltype: 2 | Tipo: Eletric

-- Caso se tente inserir um modelo com um nome já existente, a função lança erro devido à constraint UNIQUE sobre Name

DECLARE
    v_result NUMBER;
BEGIN
    -- Teste 1: Adicionar um novo modelo
    v_result := add_electric_locomotive_model(
        'LocoModel_Teste1',
        5000,
        90,
        0.8,
        8,
        220,
        70,
        300,
        1,
        1
    );
    dbms_output.put_line('Test 1 - New model added with ID: ' || v_result);

    -- Teste 2: Adicionar outro novo modelo
    v_result := add_electric_locomotive_model(
        'LocoModel_Teste2',
        3000,
        75,
        0.9,
        6,
        180,
        60,
        250,
        1,
        2
    );
    dbms_output.put_line('Test 2 - New model added with ID: ' || v_result);

    -- Verificar o estado final
    dbms_output.put_line('');
    dbms_output.put_line('All electric locomotive models:');

    FOR r IN (
        SELECT ID_Locomotive_Model, Name, Power
        FROM Locomotive_Model
        WHERE Fuel_TypeID_Fuel_Type = 2
        ORDER BY ID_Locomotive_Model
    )
    LOOP
        dbms_output.put_line('ID_Model: ' || r.ID_Locomotive_Model || ' | Name: ' || r.Name);
    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('Error: Locomotive models with the same name already exist.');
END;
