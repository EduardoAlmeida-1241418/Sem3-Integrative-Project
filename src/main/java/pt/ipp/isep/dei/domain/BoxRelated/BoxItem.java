package pt.ipp.isep.dei.domain.BoxRelated;

import pt.ipp.isep.dei.domain.Item;

import java.util.Map;

/**
 * Represents an item grouping inside a box, identified by an SKU and quantity,
 * containing a map of specific {@link Item} instances and their respective quantities.
 */
public class BoxItem {
    private String skuItem;
    private int quantity;
    private Map<Item, Integer> items = new java.util.TreeMap<>();

    /**
     * Constructs a BoxItem with the given SKU identifier and quantity.
     *
     * @param skuItem  the SKU identifier of the item
     * @param quantity the quantity of the item in the box
     */
    public BoxItem(String skuItem, int quantity) {
        this.skuItem = skuItem;
        this.quantity = quantity;
    }

    /**
     * @return the SKU identifier of the item
     */
    public String getSkuItem() {
        return skuItem;
    }

    /**
     * @param skuItem the SKU identifier to set
     */
    public void setSkuItem(String skuItem) {
        this.skuItem = skuItem;
    }

    /**
     * @return the quantity of this item in the box
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set for this item
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return a map of items contained in the box and their respective quantities
     */
    public Map<Item, Integer> getItems() {
        return items;
    }

    /**
     * @param items a map of items and quantities to set for this box item
     */
    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    /**
     * Adds an item and its quantity to the internal map.
     * If the item already exists, its quantity is increased by the given amount.
     *
     * @param item     the item to add
     * @param quantity the quantity of the item to add
     */
    public void addItem(Item item, int quantity) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + quantity);
        } else {
            items.put(item, quantity);
        }
    }
}
