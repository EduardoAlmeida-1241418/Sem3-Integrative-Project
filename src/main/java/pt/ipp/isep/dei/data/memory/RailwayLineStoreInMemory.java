package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;

import java.util.*;

public class RailwayLineStoreInMemory implements Persistable {

    private static final Map<Integer, RailwayLine> map = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        RailwayLine line = (RailwayLine) obj;
        map.put(line.getId(), line);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        RailwayLine line = (RailwayLine) obj;
        map.remove(line.getId());
        return true;
    }

    @Override
    public RailwayLine findById(DatabaseConnection db, String id) {
        return map.get(Integer.parseInt(id));
    }

    public List<RailwayLine> findAll() {
        return new ArrayList<>(map.values());
    }

    public void clear() {
        map.clear();
    }
}
