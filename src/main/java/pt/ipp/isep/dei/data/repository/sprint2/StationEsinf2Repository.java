package pt.ipp.isep.dei.data.repository.sprint2;

import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Tree.AVL.AVLTreeStation;
import pt.ipp.isep.dei.domain.Tree.TZGKey;

import java.util.Map;
import java.util.TreeMap;

public class StationEsinf2Repository {

    private final AVLTreeStation<Double> latTree = new AVLTreeStation<>();
    private final AVLTreeStation<Double> lonTree = new AVLTreeStation<>();
    private final AVLTreeStation<TZGKey> tzgTree = new AVLTreeStation<>();
    private final KD2TreeStation kdTree = new KD2TreeStation();

    private final Map<String, StationEsinf> stations = new TreeMap<>();

    public void addStation(StationEsinf s) {
        if (s == null)
            throw new IllegalArgumentException("Station cannot be null.");
        if (s.getId() == null || s.getId().isEmpty())
            throw new IllegalArgumentException("Station ID cannot be null or empty.");

        latTree.insert(s.getLatitude(), s, true);
        lonTree.insert(s.getLongitude(), s, true);
        tzgTree.insert(new TZGKey(s.getTimeZoneGroup(), s.getCountry()), s, true);
        stations.put(s.getId(), s);
    }

    public void createKDTree() {
        kdTree.build(stations);
    }

    public boolean existsById(String id) {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("Station ID cannot be null or empty.");
        return stations.containsKey(id);
    }

    public AVLTreeStation<Double> getLatTree() {
        return latTree;
    }

    public AVLTreeStation<Double> getLonTree() {
        return lonTree;
    }

    public AVLTreeStation<TZGKey> getTzgTree() {
        return tzgTree;
    }

    public KD2TreeStation getKdTree() {
        return kdTree;
    }

    public Map<String, StationEsinf> getStations() {
        return stations;
    }

    public void clear() {
        stations.clear();
    }
}