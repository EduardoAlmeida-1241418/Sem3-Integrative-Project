package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.comparator.AlphaNumericComparator;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;

import java.util.*;

public class WagonStoreInMemory implements Persistable {

    private static final Map<String, Wagon> wagons = new TreeMap<>(new AlphaNumericComparator());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Wagon wagon = (Wagon) object;

        if (wagon == null || wagon.getWagonID() <= 0) {
            throw new IllegalArgumentException("Invalid wagon or ID.");
        }

        wagons.put(String.valueOf(wagon.getWagonID()), wagon);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Wagon wagon = (Wagon) object;

        if (wagon == null || wagon.getWagonID() <= 0) {
            throw new IllegalArgumentException("Invalid wagon or ID.");
        }

        String key = String.valueOf(wagon.getWagonID());

        if (!wagons.containsKey(key)) {
            throw new NoSuchElementException("Wagon not found: " + key);
        }

        wagons.remove(key);
        return true;
    }

    @Override
    public Wagon findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        return wagons.get(id);
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return wagons.containsKey(key);
    }

    public ArrayList<Wagon> findAll() {
        return new ArrayList<>(wagons.values());
    }

    public void clear() {
        wagons.clear();
    }

    public int count() {
        return wagons.size();
    }
}
