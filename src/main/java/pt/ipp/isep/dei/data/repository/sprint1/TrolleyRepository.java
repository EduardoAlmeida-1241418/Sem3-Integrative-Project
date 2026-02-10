package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;

import java.util.*;

/**
 * Repository responsible for managing {@link Trolley} entities.
 * Provides CRUD operations and ensures case-insensitive ordering by trolley ID.
 */
public class TrolleyRepository {

    /** Internal map storing trolleys, ordered case-insensitively by ID. */
    private final Map<String, Trolley> trolleys = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Adds a new trolley to the repository.
     *
     * @param trolley the trolley to add
     * @throws IllegalArgumentException if the trolley is null
     * @throws IllegalStateException if a trolley with the same ID already exists
     */
    public void add(Trolley trolley) {
        if (trolley == null) {
            throw new IllegalArgumentException("Trolley cannot be null.");
        }

        String idKey = trolley.getTrolleyId();
        if (trolleys.containsKey(idKey)) {
            throw new IllegalStateException("This trolley already exists.");
        }

        trolleys.put(idKey, trolley);
    }

    /**
     * Finds a trolley by its unique identifier.
     *
     * @param id the ID of the trolley
     * @return the corresponding {@link Trolley}, or {@code null} if not found
     * @throws IllegalArgumentException if the ID is null or empty
     */
    public Trolley findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        }
        return trolleys.get(id);
    }

    /**
     * Retrieves all trolleys stored in the repository.
     *
     * @return an unmodifiable list of all trolleys
     */
    public List<Trolley> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(trolleys.values()));
    }

    /**
     * Removes a trolley from the repository.
     *
     * @param trolley the trolley to remove
     * @return true if the trolley was successfully removed, false otherwise
     * @throws IllegalArgumentException if the trolley is null
     * @throws NoSuchElementException if the trolley does not exist in the repository
     */
    public boolean remove(Trolley trolley) {
        if (trolley == null) {
            throw new IllegalArgumentException("Trolley cannot be null.");
        }

        String idKey = trolley.getTrolleyId();
        if (!trolleys.containsKey(idKey)) {
            throw new NoSuchElementException("Trolley not found.");
        }

        return trolleys.remove(idKey) != null;
    }

    /**
     * Checks whether a given trolley already exists in the repository.
     *
     * @param trolley the trolley to check
     * @return true if the trolley exists, false otherwise
     * @throws IllegalArgumentException if the trolley is null
     */
    public boolean exists(Trolley trolley) {
        if (trolley == null) {
            throw new IllegalArgumentException("Trolley cannot be null.");
        }

        return trolleys.containsKey(trolley.getTrolleyId());
    }

    /**
     * Removes all trolleys from the repository.
     */
    public void clear() {
        trolleys.clear();
    }

    /**
     * Counts the total number of trolleys currently stored.
     *
     * @return total count of trolleys
     */
    public int count() {
        return trolleys.size();
    }
}
