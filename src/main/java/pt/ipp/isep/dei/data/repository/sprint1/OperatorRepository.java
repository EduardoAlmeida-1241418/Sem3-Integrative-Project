package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.Operator;

import java.util.*;

/**
 * Repository responsible for managing {@link Operator} entities.
 * Provides CRUD operations and ensures operator uniqueness based on ID.
 */
public class OperatorRepository {

    /** Internal map storing operators, indexed by their unique VAT/ID. */
    private final Map<String, Operator> operators = new HashMap<>();

    /**
     * Adds a new operator to the repository.
     *
     * @param operator the operator to add
     * @throws IllegalArgumentException if the operator or its ID is null or empty
     * @throws IllegalStateException if an operator with the same ID already exists
     */
    public void add(Operator operator) {
        if (operator == null) {
            throw new IllegalArgumentException("Operator cannot be null.");
        }

        String id = operator.getId();

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Operator ID cannot be null or empty.");
        }

        if (operators.containsKey(id)) {
            throw new IllegalStateException("An operator with the same ID already exists: " + id);
        }

        operators.put(id, operator);
    }

    /**
     * Finds an operator by its unique identifier.
     *
     * @param operatorId the operator ID
     * @return the corresponding {@link Operator}
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no operator exists with the given ID
     */
    public Operator findById(String operatorId) {
        if (operatorId == null || operatorId.isEmpty()) {
            throw new IllegalArgumentException("Operator ID cannot be null or empty.");
        }

        Operator operator = operators.get(operatorId);

        if (operator == null) {
            throw new NoSuchElementException("Operator not found with ID: " + operatorId);
        }

        return operator;
    }

    /**
     * Checks whether an operator with the specified ID exists.
     *
     * @param operatorId the operator ID
     * @return true if the operator exists, false otherwise
     * @throws IllegalArgumentException if the ID is null or empty
     */
    public boolean existsById(String operatorId) {
        if (operatorId == null || operatorId.isEmpty()) {
            throw new IllegalArgumentException("Operator ID cannot be null or empty.");
        }
        return operators.containsKey(operatorId);
    }

    /**
     * Removes an operator from the repository by its ID.
     *
     * @param operatorId the operator ID
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no operator exists with the given ID
     */
    public void remove(String operatorId) {
        if (operatorId == null || operatorId.isEmpty()) {
            throw new IllegalArgumentException("Operator ID cannot be null or empty.");
        }

        if (!operators.containsKey(operatorId)) {
            throw new NoSuchElementException("Operator not found with ID: " + operatorId);
        }

        operators.remove(operatorId);
    }

    /**
     * Retrieves all operators stored in the repository.
     *
     * @return an unmodifiable collection of all operators
     */
    public Collection<Operator> findAll() {
        return Collections.unmodifiableCollection(operators.values());
    }

    /**
     * Removes all operators from the repository.
     */
    public void clear() {
        operators.clear();
    }

    /**
     * Counts the total number of operators currently stored.
     *
     * @return total count of operators
     */
    public int count() {
        return operators.size();
    }

    public boolean existOperatorWithShortName(String shortName) {
        for (Operator operator : operators.values()) {
            if (operator.getShortName().equals(shortName)) {
                return true;
            }
        }
        return false;
    }

    public Operator getOperatorByShortName(String shortName) {
        for (Operator operator : operators.values()) {
            if (operator.getShortName().equals(shortName)) {
                return operator;
            }
        }
        return null;
    }
}
