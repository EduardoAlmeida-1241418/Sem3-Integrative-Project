package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a country.
 * A country has a unique identifier, a name and a list of associated stations.
 * Countries are comparable by name.
 */
public class Country implements Comparable<Country> {

    /** Unique identifier of the country */
    private int id;
    /** Name of the country */
    private String name;
    /** Static counter used to generate country identifiers */
    private static int idCounter = 1;
    /** List of stations associated with the country */
    private List<StationEsinf> stationList;

    /**
     * Constructs a country with the given name.
     * An identifier is automatically assigned.
     *
     * @param name country name
     */
    public Country(String name) {
        this.id = idCounter++;
        this.name = name;
        this.stationList = new ArrayList<>();
    }

    /**
     * Returns the country identifier.
     *
     * @return country id
     */
    public int getId() { return id; }

    /**
     * Returns the country name.
     *
     * @return country name
     */
    public String getName() { return name; }

    /**
     * Sets the country name.
     *
     * @param name country name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the list of stations associated with the country.
     *
     * @return station list
     */
    public List<StationEsinf> getStationList() { return stationList; }

    /**
     * Adds a station to the country if it is not already present.
     *
     * @param station station to add
     */
    public void addStation(StationEsinf station) {
        if (station != null && stationList.stream().noneMatch(s -> s.getId().equals(station.getId())))
            stationList.add(station);
    }

    /**
     * Removes a station from the country.
     *
     * @param station station to remove
     */
    public void removeStation(StationEsinf station) {
        stationList.removeIf(s -> s.getId().equals(station.getId()));
    }

    /**
     * Compares two countries by name, ignoring case.
     *
     * @param other country to compare
     * @return comparison result
     */
    @Override
    public int compareTo(Country other) {
        if (other == null) return 1;
        return this.name.compareToIgnoreCase(other.name);
    }

    /**
     * Checks equality based on country name, ignoring case.
     *
     * @param o object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country c = (Country) o;
        return this.name.equalsIgnoreCase(c.name);
    }

    /**
     * Computes the hash code based on the country name.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name.toUpperCase());
    }

    /**
     * Returns the string representation of the country.
     *
     * @return country name
     */
    @Override
    public String toString() {
        return name;
    }
}
