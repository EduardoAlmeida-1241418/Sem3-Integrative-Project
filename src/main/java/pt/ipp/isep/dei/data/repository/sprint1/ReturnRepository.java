package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.Return;
import pt.ipp.isep.dei.comparator.ReturnInspectionComparator;

import java.util.*;

/**
 * Repository responsible for managing {@link Return} entities.
 * Provides CRUD operations and sorted retrieval using {@link ReturnInspectionComparator}.
 */
public class ReturnRepository {

    /** Internal map storing all returns, indexed by their unique IDs. */
    private final Map<String, Return> returns = new HashMap<>();

    /** Comparator used for sorting returns by inspection order or criteria. */
    private final Comparator<String> comparator = new ReturnInspectionComparator();

    /**
     * Adds a new return record to the repository.
     *
     * @param ret the {@link Return} to add
     * @throws IllegalArgumentException if the return or its ID is null or empty
     * @throws IllegalStateException if a return with the same ID already exists
     */
    public void add(Return ret) {
        if (ret == null) {
            throw new IllegalArgumentException("Return cannot be null.");
        }
        String id = ret.getReturnId();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Return ID cannot be null or empty.");
        }
        if (returns.containsKey(id)) {
            throw new IllegalStateException("A return with the same ID already exists: " + id);
        }
        returns.put(id, ret);
    }

    /**
     * Finds a return by its unique identifier.
     *
     * @param returnId the return ID
     * @return the {@link Return} object with the specified ID
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no return exists with the given ID
     */
    public Return findById(String returnId) {
        if (returnId == null || returnId.isEmpty()) {
            throw new IllegalArgumentException("Return ID cannot be null or empty.");
        }
        Return ret = returns.get(returnId);
        if (ret == null) {
            throw new NoSuchElementException("Return not found with ID: " + returnId);
        }
        return ret;
    }

    /**
     * Checks if a return with the specified ID exists.
     *
     * @param returnId the return ID
     * @return true if a return with the given ID exists, false otherwise
     * @throws IllegalArgumentException if the ID is null or empty
     */
    public boolean existsById(String returnId) {
        if (returnId == null || returnId.isEmpty()) {
            throw new IllegalArgumentException("Return ID cannot be null or empty.");
        }
        return returns.containsKey(returnId);
    }

    /**
     * Removes a return from the repository by its ID.
     *
     * @param returnId the return ID
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no return exists with the given ID
     */
    public void remove(String returnId) {
        if (returnId == null || returnId.isEmpty()) {
            throw new IllegalArgumentException("Return ID cannot be null or empty.");
        }
        if (!returns.containsKey(returnId)) {
            throw new NoSuchElementException("Return not found with ID: " + returnId);
        }
        returns.remove(returnId);
    }

    /**
     * Retrieves all returns sorted according to the {@link ReturnInspectionComparator}.
     *
     * @return an unmodifiable list of sorted returns
     */
    public List<Return> findAllSorted() {
        List<String> ids = new ArrayList<>(returns.keySet());
        ids.sort(comparator);
        List<Return> sortedReturns = new ArrayList<>();
        for (String id : ids) {
            sortedReturns.add(returns.get(id));
        }
        return Collections.unmodifiableList(sortedReturns);
    }

    /**
     * Retrieves all returns stored in the repository.
     *
     * @return an unmodifiable collection of all returns
     */
    public Collection<Return> findAll() {
        return Collections.unmodifiableCollection(returns.values());
    }

    /**
     * Removes all returns from the repository.
     */
    public void clear() {
        returns.clear();
    }

    /**
     * Counts the total number of returns currently stored.
     *
     * @return total count of return records
     */
    public int count() {
        return returns.size();
    }
}
