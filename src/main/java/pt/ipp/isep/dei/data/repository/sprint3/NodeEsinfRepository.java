package pt.ipp.isep.dei.data.repository.sprint3;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeEsinfRepository {
    private List<Node<StationEsinf>> nodeList = new ArrayList<>();

    public void addNode(Node<StationEsinf> node) {
        nodeList.add(node);
    }

    public void removeNode(Node<StationEsinf> node) {
        nodeList.remove(node);
    }

    public List<Node<StationEsinf>> getAllNodes() {
        return nodeList;
    }

    public Node<StationEsinf> getNodeByKey(String key) {
        for (Node<StationEsinf> node : nodeList) {
            if (node.getKey().equals(key)) {
                return node;
            }
        }
        return null;
    }

    public int getNodeCount() {
        return nodeList.size();
    }

    public void clear() {
        nodeList.clear();
    }
}
