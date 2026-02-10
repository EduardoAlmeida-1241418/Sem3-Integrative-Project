package pt.ipp.isep.dei.domain;

/**
 * Represents a railway operator entity.
 * Contains basic identification details such as name, short name, and VAT number.
 */
public class Operator {

    private String id;
    /** Full name of the operator. */
    private String name;

    /** Abbreviated or short name of the operator. */
    private String shortName;

    /**
     * Constructs an Operator instance.
     *
     * @param name the full name of the operator
     * @param shortName the short name or abbreviation of the operator
     * @param id the VAT number of the operator
     */
    public Operator(String id, String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
        this.id = id;
    }

    /** @return the operator’s full name */
    public String getName() {
        return name;
    }

    /** @param name sets the operator’s full name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the operator’s short name */
    public String getShortName() {
        return shortName;
    }

    /** @param shortName sets the operator’s short name */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /** @return the operator’s VAT number */
    public String getId() {
        return id;
    }

    /** @param id sets the operator’s VAT number */
    public void setId(String id) {
        this.id = id;
    }
}
