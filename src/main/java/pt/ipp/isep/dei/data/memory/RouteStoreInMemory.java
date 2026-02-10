package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.util.*;

public class RouteStoreInMemory implements Persistable {

    private static final Map<Integer, Route> routes = new LinkedHashMap<>();

    public RouteStoreInMemory() {}

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Route route = (Route) obj;
        routes.put(route.getId(), route);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Route route = (Route) obj;
        routes.remove(route.getId());
        return true;
    }

    @Override
    public Route findById(DatabaseConnection db, String id) {
        if (id == null) return null;
        return routes.get(Integer.parseInt(id));
    }

    public Collection<Route> findAll() {
        return Collections.unmodifiableCollection(routes.values());
    }

    public void clear() {
        routes.clear();
    }

    public int getNextId() {
        for (int i = 1; ; i++) {
            if (!routes.containsKey(i)) {
                return i;
            }
        }
    }

    public boolean exists(int id) {
        return routes.containsKey(id);
    }
}
