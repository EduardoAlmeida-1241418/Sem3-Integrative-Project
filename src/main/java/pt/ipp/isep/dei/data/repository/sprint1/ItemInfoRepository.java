package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.Item;

import java.util.*;

/**
 * Repository responsible for managing {@link Item} entities.
 * Provides CRUD operations and search functionality by SKU.
 */
public class ItemInfoRepository {

    /** Internal map storing items indexed by their unique SKU identifiers. */
    private final Map<String, Item> items = new HashMap<>();

    /**
     * Adds a new item to the repository.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if the item or its SKU is null or empty
     * @throws IllegalStateException if an item with the same SKU already exists
     */
    public void add(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        String sku = item.getSku();
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("Item SKU cannot be null or empty.");
        }
        if (items.containsKey(sku)) {
            throw new IllegalStateException("An item with the same SKU already exists: " + sku);
        }
        items.put(sku, item);
    }

    /**
     * Finds an item by its unique SKU.
     *
     * @param sku the SKU identifier
     * @return the corresponding {@link Item}
     * @throws IllegalArgumentException if the SKU is null or empty
     * @throws NoSuchElementException if no item exists for the given SKU
     */
    public Item findBySku(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty.");
        }
        Item item = items.get(sku);
        if (item == null) {
            throw new NoSuchElementException("Item not found with SKU: " + sku);
        }
        return item;
    }

    /**
     * Checks if an item with the specified SKU exists.
     *
     * @param sku the SKU identifier
     * @return true if the item exists, false otherwise
     * @throws IllegalArgumentException if the SKU is null or empty
     */
    public boolean existsBySku(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty.");
        }
        return items.containsKey(sku);
    }

    /**
     * Removes an item from the repository by its SKU.
     *
     * @param sku the SKU identifier
     * @throws IllegalArgumentException if the SKU is null or empty
     * @throws NoSuchElementException if no item exists for the given SKU
     */
    public void remove(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty.");
        }
        if (!items.containsKey(sku)) {
            throw new NoSuchElementException("Item not found with SKU: " + sku);
        }
        items.remove(sku);
    }

    /**
     * Retrieves all items in the repository.
     *
     * @return unmodifiable collection of all stored items
     */
    public Collection<Item> findAll() {
        return Collections.unmodifiableCollection(items.values());
    }

    /**
     * Removes all items from the repository.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Counts the total number of items currently stored in the repository.
     *
     * @return total count of items
     */
    public int count() {
        return items.size();
    }
}
