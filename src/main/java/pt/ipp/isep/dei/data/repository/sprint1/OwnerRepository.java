package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.Owner;

import java.util.*;

/**
 * Repository responsible for managing {@link Owner} entities.
 * Provides CRUD operations and ensures owner uniqueness based on VAT number.
 */
public class OwnerRepository {

    /** Internal map storing owners, indexed by their unique VAT number. */
    private final Map<String, Owner> owners = new HashMap<>();

    /**
     * Adds a new owner to the repository.
     *
     * @param owner the owner to add
     * @throws IllegalArgumentException if the owner or its VAT number is null or empty
     * @throws IllegalStateException if an owner with the same VAT number already exists
     */
    public void add(Owner owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null.");
        }

        String vat = owner.getVatNumber();

        if (vat == null || vat.isEmpty()) {
            throw new IllegalArgumentException("Owner VAT number cannot be null or empty.");
        }

        if (owners.containsKey(vat)) {
            throw new IllegalStateException("An owner with the same VAT number already exists: " + vat);
        }

        owners.put(vat, owner);
    }

    /**
     * Finds an owner by their VAT number.
     *
     * @param vatNumber the VAT number
     * @return the corresponding {@link Owner}
     * @throws IllegalArgumentException if the VAT number is null or empty
     * @throws NoSuchElementException if no owner exists with the given VAT number
     */
    public Owner findById(String vatNumber) {
        if (vatNumber == null || vatNumber.isEmpty()) {
            throw new IllegalArgumentException("Owner VAT number cannot be null or empty.");
        }

        Owner owner = owners.get(vatNumber);

        if (owner == null) {
            throw new NoSuchElementException("Owner not found with VAT number: " + vatNumber);
        }

        return owner;
    }

    /**
     * Checks whether an owner with the specified VAT number exists.
     *
     * @param vatNumber the VAT number
     * @return true if the owner exists, false otherwise
     * @throws IllegalArgumentException if the VAT number is null or empty
     */
    public boolean existsById(String vatNumber) {
        if (vatNumber == null || vatNumber.isEmpty()) {
            throw new IllegalArgumentException("Owner VAT number cannot be null or empty.");
        }

        return owners.containsKey(vatNumber);
    }

    /**
     * Removes an owner from the repository by their VAT number.
     *
     * @param vatNumber the VAT number
     * @throws IllegalArgumentException if the VAT number is null or empty
     * @throws NoSuchElementException if no owner exists with the given VAT number
     */
    public void remove(String vatNumber) {
        if (vatNumber == null || vatNumber.isEmpty()) {
            throw new IllegalArgumentException("Owner VAT number cannot be null or empty.");
        }

        if (!owners.containsKey(vatNumber)) {
            throw new NoSuchElementException("Owner not found with VAT number: " + vatNumber);
        }

        owners.remove(vatNumber);
    }

    /**
     * Retrieves all owners stored in the repository.
     *
     * @return an unmodifiable collection of all owners
     */
    public Collection<Owner> findAll() {
        return Collections.unmodifiableCollection(owners.values());
    }

    /**
     * Removes all owners from the repository.
     */
    public void clear() {
        owners.clear();
    }

    /**
     * Counts the total number of owners currently stored.
     *
     * @return total count of owners
     */
    public int count() {
        return owners.size();
    }

    public boolean existsByShortName(String shortName) {
        if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("Owner short name cannot be null or empty.");
        }

        for (Owner owner : owners.values()) {
            if (shortName.equals(owner.getShortName())) {
                return true;
            }
        }
        return false;
    }

    public String getOwnerVatNumberByShortName(String shortName) {
        if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("Owner short name cannot be null or empty.");
        }

        for (Owner owner : owners.values()) {
            if (shortName.equals(owner.getShortName())) {
                return owner.getVatNumber();
            }
        }
        throw new NoSuchElementException("Owner not found with short name: " + shortName);
    }
}
