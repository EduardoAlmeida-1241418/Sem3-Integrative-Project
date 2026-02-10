package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.comparator.AlphaNumericComparator;
import pt.ipp.isep.dei.domain.Locomotive;

import java.util.*;

public class LocomotiveStoreInMemory implements Persistable {

    private static final Map<String, Locomotive> locomotives =
            new TreeMap<>(new AlphaNumericComparator());

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Locomotive l = (Locomotive) obj;
        locomotives.put(String.valueOf(l.getId()), l);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Locomotive l = (Locomotive) obj;
        locomotives.remove(String.valueOf(l.getId()));
        return true;
    }

    @Override
    public Locomotive findById(DatabaseConnection db, String id) {
        return locomotives.get(id);
    }

    public Collection<Locomotive> findAll() {
        return Collections.unmodifiableCollection(locomotives.values());
    }

    public void clear() {
        locomotives.clear();
    }
}
