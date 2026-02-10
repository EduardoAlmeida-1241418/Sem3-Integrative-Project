package pt.ipp.isep.dei.domain.Tree.Node;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import java.util.ArrayList;
import java.util.List;

public class TreeNode<E extends Comparable<E>> extends GenericNode<E, TreeNode<E>> {

    private final List<StationEsinf> stations = new ArrayList<>();

    public TreeNode(E element, StationEsinf s) {
        super(element);
        this.stations.add(s);
    }

    public List<StationEsinf> getStations() { return stations; }

    // Complexidade Total = O(n)
    public void addStation(StationEsinf s, boolean ascending) {
        int i = 0;
        if (ascending) {
            while (i < stations.size() &&
                    stations.get(i).getStationName().compareTo(s.getStationName()) < 0) {
                i++;
            }
        } else {
            while (i < stations.size() &&
                    stations.get(i).getStationName().compareTo(s.getStationName()) > 0) {
                i++;
            }
        }
        stations.add(i, s);
    }
}
