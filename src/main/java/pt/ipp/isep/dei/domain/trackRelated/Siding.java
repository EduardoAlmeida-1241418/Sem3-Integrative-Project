package pt.ipp.isep.dei.domain.trackRelated;

/**
 * Represents a siding in a railway track.
 * A siding is a secondary track section defined by its position and length.
 */
public class Siding implements TrackLocation{

    /** Unique identifier of the siding */
    private int id;
    /** Position of the siding within the segment */
    private int position;
    /** Length of the siding */
    private int length;

    /**
     * Constructs a siding with the given parameters.
     *
     * @param id siding identifier
     * @param position position of the siding
     * @param length length of the siding
     */
    public Siding(int id, int position, int length) {
        this.id = id;
        this.position = position;
        this.length = length;
    }

    /**
     * Returns the siding identifier.
     *
     * @return siding id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the siding identifier.
     *
     * @param id new siding id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the position of the siding.
     *
     * @return siding position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position of the siding.
     *
     * @param position siding position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Returns the length of the siding.
     *
     * @return siding length
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the length of the siding.
     *
     * @param length siding length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Returns the name of the siding.
     *
     * @return siding name
     */
    @Override
    public String name() {
        return "Siding" + this.id;
    }

    /**
     * Returns the string representation of the siding.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Siding: " + id;
    }

}
