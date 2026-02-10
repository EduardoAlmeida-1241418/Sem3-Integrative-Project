package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.Owner;

import java.util.*;

public class OwnerStoreInMemory implements Persistable {

    private static final Map<String, Owner> owners = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Owner owner = (Owner) object;

        if (owner == null || owner.getVatNumber() == null || owner.getVatNumber().isEmpty()) {
            throw new IllegalArgumentException("Invalid owner or ID.");
        }

        owners.put(owner.getVatNumber(), owner);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Owner owner = (Owner) object;

        if (owner == null || owner.getVatNumber() == null || owner.getVatNumber().isEmpty()) {
            throw new IllegalArgumentException("Invalid owner or ID.");
        }

        if (!owners.containsKey(owner.getVatNumber())) {
            throw new NoSuchElementException("Owner not found: " + owner.getVatNumber());
        }

        owners.remove(owner.getVatNumber());
        return true;
    }

    @Override
    public Owner findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        Owner owner = owners.get(id);

        if (owner == null) {
            throw new NoSuchElementException("Owner not found: " + id);
        }

        return owner;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return owners.containsKey(key);
    }

    public Collection<Owner> findAll() {
        return Collections.unmodifiableCollection(owners.values());
    }

    public int count() {
        return owners.size();
    }

    public void clear() {
        owners.clear();
    }
}
