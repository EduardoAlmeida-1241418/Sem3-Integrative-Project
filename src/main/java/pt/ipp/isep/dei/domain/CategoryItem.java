package pt.ipp.isep.dei.domain;

/**
 * Enumeration representing the categories to which an item can belong.
 * Each category is associated with an identifier and a human-readable name.
 */
public enum CategoryItem {

    /** Category for beverages and drinks. */
    BEVERAGE(4, "Beverage"),

    /** Category for cleaning supplies and related products. */
    CLEANING(1, "Cleaning"),

    /** Category for electronic devices and components. */
    ELECTRONICS(2, "Electronics"),

    /** Category for groceries and food-related products. */
    GROCERY(6, "Grocery"),

    /** Category for hardware tools and materials. */
    HARDWARE(3, "Hardware"),

    /** Category for personal care and hygiene products. */
    PERSONALCARE(5, "Personal Care");

    private int id;
    private String categoryName;

    /**
     * Constructs a {@code CategoryItem} with a numeric ID and display name.
     *
     * @param id            the unique identifier of the category
     * @param categoryName  the human-readable name of the category
     */
    CategoryItem(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    /**
     * @return the unique identifier of the category
     */
    public int getId() {
        return id;
    }

    /**
     * @return the human-readable name of the category
     */
    public String getCategoryName() {
        return categoryName;
    }
}
