package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.trainRelated.Train;

import java.util.*;

public class TrainStoreInMemory implements Persistable {

    private static final Map<Integer, Train> trains = new TreeMap<>();

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Train t = (Train) obj;
        trains.put(t.getId(), t);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Train t = (Train) obj;
        trains.remove(t.getId());
        return true;
    }

    @Override
    public Train findById(DatabaseConnection db, String id) {
        return trains.get(Integer.parseInt(id));
    }

    public Collection<Train> findAll() {
        return Collections.unmodifiableCollection(trains.values());
    }

    public boolean exists(int id) {
        return trains.containsKey(id);
    }

    public void clear() {
        trains.clear();
    }

    public int count() {
        return trains.size();
    }

    public int getNextId() {
        for (int id = 1; ; id++) {
            if (!trains.containsKey(id)) {
                return id;
            }
        }
    }
}
