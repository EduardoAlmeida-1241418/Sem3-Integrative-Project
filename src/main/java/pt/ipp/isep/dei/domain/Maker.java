package pt.ipp.isep.dei.domain;

/**
 * Represents a manufacturer (maker).
 * A maker is identified by an id and has an associated name.
 */
public class Maker {

    /** Unique identifier of the maker */
    private int id;
    /** Name of the maker */
    private String name;

    /**
     * Constructs a maker with the given identifier and name.
     *
     * @param id maker identifier
     * @param name maker name
     */
    public Maker(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the maker identifier.
     *
     * @return maker id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the maker identifier.
     *
     * @param id maker id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the maker name.
     *
     * @return maker name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the maker name.
     *
     * @param name maker name
     */
    public void setName(String name) {
        this.name = name;
    }
}
