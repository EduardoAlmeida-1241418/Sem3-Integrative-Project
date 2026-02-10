package pt.ipp.isep.dei.domain;

import java.util.Objects;

/**
 * Represents an item stored or handled in the warehouse.
 * Each item has a unique SKU, belongs to a category, and includes physical and temporal attributes.
 */
public class Item {
    private String sku;
    private String name;
    private CategoryItem categoryItem;
    private UnitType unitType;
    private double volume;
    private double unitWeight;

    private Date expiryDate;
    private Date receivedDate;
    private Time receivedTime;

    /**
     * Constructs an {@code Item} with core product information.
     *
     * @param sku           the unique SKU identifier of the item
     * @param name          the name of the item
     * @param categoryItem  the category of the item
     * @param unitType      the measurement unit type of the item
     * @param volume        the volume per unit
     * @param unitWeight    the weight per unit
     */
    public Item(String sku, String name, CategoryItem categoryItem, UnitType unitType, double volume, double unitWeight) {
        this.sku = sku;
        this.name = name;
        this.categoryItem = categoryItem;
        this.unitType = unitType;
        this.volume = volume;
        this.unitWeight = unitWeight;
    }

    /**
     * Constructs an {@code Item} with complete information including received and expiry data.
     *
     * @param sku           the unique SKU identifier
     * @param name          the name of the item
     * @param categoryItem  the category of the item
     * @param unitType      the measurement unit type
     * @param volume        the volume per unit
     * @param receivedTime  the time the item was received
     * @param receivedDate  the date the item was received
     * @param expiryDate    the date the item expires
     * @param unitWeight    the weight per unit
     */
    public Item(String sku, String name, CategoryItem categoryItem, UnitType unitType, double volume,
                Time receivedTime, Date receivedDate, Date expiryDate, double unitWeight) {
        this.sku = sku;
        this.name = name;
        this.categoryItem = categoryItem;
        this.unitType = unitType;
        this.volume = volume;
        this.receivedTime = receivedTime;
        this.receivedDate = receivedDate;
        this.expiryDate = expiryDate;
        this.unitWeight = unitWeight;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CategoryItem getCategoryItem() { return categoryItem; }
    public void setCategoryItem(CategoryItem categoryItem) { this.categoryItem = categoryItem; }

    public UnitType getUnitType() { return unitType; }
    public void setUnitType(UnitType unitType) { this.unitType = unitType; }

    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }

    public double getUnitWeight() { return unitWeight; }
    public void setUnitWeight(double unitWeight) { this.unitWeight = unitWeight; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public Date getReceivedDate() { return receivedDate; }
    public void setReceivedDate(Date receivedDate) { this.receivedDate = receivedDate; }

    public Time getReceivedTime() { return receivedTime; }
    public void setReceivedTime(Time receivedTime) { this.receivedTime = receivedTime; }

    /**
     * @return a concise string representation of the item containing SKU, name, category, and physical properties
     */
    @Override
    public String toString() {
        return sku + " - " + name + " - " + categoryItem + " - " + unitType + " - " + volume + " - " + unitWeight;
    }

    /**
     * Compares two items for equality based on their identifying and descriptive attributes.
     *
     * @param o the object to compare
     * @return true if both items are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(volume, item.volume) == 0 &&
                Double.compare(unitWeight, item.unitWeight) == 0 &&
                Objects.equals(sku, item.sku) &&
                Objects.equals(name, item.name) &&
                categoryItem == item.categoryItem &&
                unitType == item.unitType &&
                Objects.equals(expiryDate, item.expiryDate) &&
                Objects.equals(receivedDate, item.receivedDate) &&
                Objects.equals(receivedTime, item.receivedTime);
    }

    /**
     * @return the hash code of this item, computed from its attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(sku, name, categoryItem, unitType, volume, unitWeight, expiryDate, receivedDate, receivedTime);
    }
}
