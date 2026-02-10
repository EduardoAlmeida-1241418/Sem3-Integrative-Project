package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.util.*;

public class FreightStoreInMemory implements Persistable {

    private static final Map<String, Freight> freights = new LinkedHashMap<>();
    private RouteStoreInMemory routeStoreInMemory = new RouteStoreInMemory();

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Freight f = (Freight) obj;
        freights.put(String.valueOf(f.getId()), f);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Freight f = (Freight) obj;
        freights.remove(String.valueOf(f.getId()));
        return true;
    }

    @Override
    public Freight findById(DatabaseConnection db, String id) {
        return freights.get(id);
    }

    public List<Freight> findAll() {
        return new ArrayList<>(freights.values());
    }

    public List<Freight> findUnusedFreights(){
        List<Freight> freightList = new ArrayList<>(freights.values());

        for (Route route : routeStoreInMemory.findAll()){
            for (Freight freight : route.getFreights()){
                freightList.remove(freight);
            }
        }

        return freightList;
    }

    public void clear() {
        freights.clear();
    }
}
