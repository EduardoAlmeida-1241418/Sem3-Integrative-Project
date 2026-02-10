package pt.ipp.isep.dei.data.repository.sprint2;

import pt.ipp.isep.dei.domain.Country;

import java.util.*;

/**
 * Repository class that manages storage and retrieval of {@link Country} objects.
 * <p>
 * Internally uses a {@link TreeMap} keyed by the country ID to keep countries ordered.
 * Provides basic CRUD operations: add, find, check existence, remove, list all,
 * clear, and count.
 * </p>
 */
public class CountryRepository {

    private final Map<Integer, Country> countries = new TreeMap<>();

    /**
     * Adds a new country to the repository.
     *
     * @param country the country to add; must not be null and must have a valid id (> 0)
     * @throws IllegalArgumentException if the country is null or its id is invalid
     * @throws IllegalStateException    if a country with the same id already exists
     */
    public void add(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null.");
        }
        int id = country.getId();
        if (id <= 0) {
            throw new IllegalArgumentException("Country ID must be greater than zero.");
        }
        if (countries.containsKey(id)) {
            throw new IllegalStateException("A country with the same ID already exists: " + id);
        }
        countries.put(id, country);
    }

    /**
     * Finds and returns a country by its id.
     *
     * @param id the id of the country to find; must be greater than zero
     * @return the country with the given id
     * @throws IllegalArgumentException if the id is invalid
     * @throws NoSuchElementException   if no country with the given id is found
     */
    public Country findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Country ID must be greater than zero.");
        }
        Country country = countries.get(id);
        if (country == null) {
            throw new NoSuchElementException("Country not found with ID: " + id);
        }
        return country;
    }

    /**
     * Checks whether a country with the given id exists in the repository.
     *
     * @param id the id to check; must be greater than zero
     * @return true if a country with the given id exists, false otherwise
     * @throws IllegalArgumentException if the id is invalid
     */
    public boolean existsById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Country ID must be greater than zero.");
        }
        return countries.containsKey(id);
    }

    /**
     * Checks whether a country with the given name exists in the repository (case-insensitive).
     *
     * @param name the name to check; must not be null or empty
     * @return true if a country with the given name exists, false otherwise
     * @throws IllegalArgumentException if the name is null or empty
     */

    public boolean existsByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be null or empty.");
        }
        for (Country c : countries.values()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the country with the given id from the repository.
     *
     * @param id the id of the country to remove; must be greater than zero
     * @throws IllegalArgumentException if the id is invalid
     * @throws NoSuchElementException   if no country with the given id exists
     */
    public void remove(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Country ID must be greater than zero.");
        }
        if (!countries.containsKey(id)) {
            throw new NoSuchElementException("Country not found with ID: " + id);
        }
        countries.remove(id);
    }

    /**
     * Finds and returns a country by its name (case-insensitive).
     *
     * @param name the name of the country to find; must not be null or empty
     * @return the country with the given name
     * @throws IllegalArgumentException if the name is null or empty
     * @throws NoSuchElementException   if no country with the given name exists
     */
    public Country findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be null or empty.");
        }
        for (Country c : countries.values()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        throw new NoSuchElementException("Country not found with name: " + name);
    }

    /**
     * Returns an unmodifiable collection of all countries in the repository.
     *
     * @return an unmodifiable collection of countries
     */
    public Collection<Country> findAll() {
        return Collections.unmodifiableCollection(countries.values());
    }

    /**
     * Removes all countries from the repository.
     */
    public void clear() {
        countries.clear();
    }

    /**
     * Returns the number of countries currently stored in the repository.
     *
     * @return the count of countries
     */
    public int count() {
        return countries.size();
    }

    /**
     * Returns a list of all countries sorted alphabetically by name.
     *
     * @return a list of countries sorted alphabetically by name
     */
    public List<Country> findAllAlphabetically() {
        List<Country> sorted = new ArrayList<>(countries.values());
        Collections.sort(sorted, new Comparator<Country>() {
            @Override
            public int compare(Country c1, Country c2) {
                return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
        return Collections.unmodifiableList(sorted);
    }


}
