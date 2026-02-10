package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.Building;

import java.util.*;

public class BuildingStoreInMemory implements Persistable {

    private static final Map<String, Building> buildings = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Building building = (Building) object;

        if (building == null || building.getId() == null || building.getId().isEmpty())
            throw new IllegalArgumentException("Invalid building or ID.");

        buildings.put(building.getId(), building);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Building building = (Building) object;

        if (building == null || building.getId() == null || building.getId().isEmpty())
            throw new IllegalArgumentException("Invalid building or ID.");

        if (!buildings.containsKey(building.getId()))
            throw new NoSuchElementException("Building not found: " + building.getId());

        buildings.remove(building.getId());
        return true;
    }

    @Override
    public Building findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("Invalid ID.");

        Building building = buildings.get(id);

        if (building == null)
            throw new NoSuchElementException("Building not found: " + id);

        return building;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Invalid key.");
        return buildings.containsKey(key);
    }

    public Collection<Building> findAll() {
        return Collections.unmodifiableCollection(buildings.values());
    }

    public int count() {
        return buildings.size();
    }

    public void clear() {
        buildings.clear();
    }
}
