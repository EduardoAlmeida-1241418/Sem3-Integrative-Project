package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.util.*;

/**
 * Repository responsible for managing {@link Box} entities.
 * Provides CRUD operations and search functionality by box ID or SKU item.
 */
public class BoxRepository {

    /** Internal map storing boxes indexed by their unique identifiers. */
    private final Map<String, Box> boxes = new HashMap<>();

    /**
     * Adds a new box to the repository.
     *
     * @param box the box to add
     * @return true if added successfully, false otherwise
     */
    public boolean add(Box box) {
        if (box == null) {
            UIUtils.addLog("Box not found", LogType.ERROR, RoleType.GLOBAL);
            return false;
        }
        String id = box.getBoxId();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Box ID cannot be null or empty.");
        }
        if (boxes.containsKey(id)) {
            throw new IllegalStateException("A box with the same ID already exists: " + id);
        }
        boxes.put(id, box);
        return true;
    }

    /**
     * Finds a box by its unique identifier.
     *
     * @param boxId the box ID
     * @return the corresponding {@link Box} object, or null if not found
     */
    public Box findById(String boxId) {
        if (boxId == null || boxId.isEmpty()) {
            throw new IllegalArgumentException("Box ID cannot be null or empty.");
        }
        return boxes.get(boxId); // devolve null se não existir
    }

    /**
     * Checks if a box exists by its unique identifier.
     *
     * @param boxId the box ID
     * @return true if the box exists, false otherwise
     */
    public boolean existsById(String boxId) {
        if (boxId == null || boxId.isEmpty()) {
            throw new IllegalArgumentException("Box ID cannot be null or empty.");
        }
        return boxes.containsKey(boxId);
    }

    /**
     * Removes a box from the repository by its ID.
     *
     * @param boxId the box ID
     */
    public void remove(String boxId) {
        if (boxId == null || boxId.isEmpty()) {
            throw new IllegalArgumentException("Box ID cannot be null or empty.");
        }
        boxes.remove(boxId);
    }

    /**
     * Retrieves all boxes stored in the repository.
     *
     * @return unmodifiable collection of all boxes
     */
    public Collection<Box> findAll() {
        return Collections.unmodifiableCollection(boxes.values());
    }

    /**
     * Finds all boxes that contain a given SKU item.
     *
     * @param skuItem SKU identifier of the item
     * @return list of boxes containing the specified SKU (may be empty)
     */
    public List<Box> findBySkuItem(String skuItem) {
        if (skuItem == null || skuItem.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty.");
        }
        List<Box> result = new ArrayList<>();
        for (Box box : boxes.values()) {
            if (box.getSkuItem().equalsIgnoreCase(skuItem)) {
                result.add(box);
            }
        }
        return result;
    }

    /**
     * Removes all boxes from the repository.
     */
    public void clear() {
        boxes.clear();
    }

    /**
     * Counts the number of boxes currently stored in the repository.
     *
     * @return total number of boxes
     */
    public int count() {
        return boxes.size();
    }
}
