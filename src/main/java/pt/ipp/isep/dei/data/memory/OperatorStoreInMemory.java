package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.Operator;

import java.util.*;

public class OperatorStoreInMemory implements Persistable {

    private static final Map<String, Operator> operators = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Operator operator = (Operator) object;

        if (operator == null || operator.getId() == null || operator.getId().isEmpty()) {
            throw new IllegalArgumentException("Invalid operator or ID.");
        }

        operators.put(operator.getId(), operator);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Operator operator = (Operator) object;

        if (operator == null || operator.getId() == null || operator.getId().isEmpty()) {
            throw new IllegalArgumentException("Invalid operator or ID.");
        }

        if (!operators.containsKey(operator.getId())) {
            throw new NoSuchElementException("Operator not found: " + operator.getId());
        }

        operators.remove(operator.getId());
        return true;
    }

    @Override
    public Operator findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        Operator operator = operators.get(id);

        if (operator == null) {
            throw new NoSuchElementException("Operator not found: " + id);
        }

        return operator;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return operators.containsKey(key);
    }

    public Collection<Operator> findAll() {
        return Collections.unmodifiableCollection(operators.values());
    }

    public int count() {
        return operators.size();
    }

    public void clear() {
        operators.clear();
    }
}
