package pt.ipp.isep.dei.domain;

/**
 * Represents the owner entity of railway assets such as locomotives or facilities.
 * Contains identification data including name, short name, and VAT number.
 */
public class Owner {

    /** Full name of the owner. */
    private String name;

    /** Abbreviated or short name of the owner. */
    private String shortName;

    /** VAT (tax identification) number of the owner. */
    private String vatNumber;

    /**
     * Constructs an Owner instance.
     *
     * @param name the full name of the owner
     * @param shortName the short or abbreviated name of the owner
     * @param vatNumber the VAT number of the owner
     */
    public Owner(String name, String shortName, String vatNumber) {
        this.name = name;
        this.shortName = shortName;
        this.vatNumber = vatNumber;
    }

    /** @return the owner’s full name */
    public String getName() {
        return name;
    }

    /** @param name sets the owner’s full name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the owner’s short name */
    public String getShortName() {
        return shortName;
    }

    /** @param shortName sets the owner’s short name */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /** @return the owner’s VAT number */
    public String getVatNumber() {
        return vatNumber;
    }

    /** @param vatNumber sets the owner’s VAT number */
    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }
}
