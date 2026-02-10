package pt.ipp.isep.dei.controller.dataEnginner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Controller responsible for organising KD-tree station nodes for presentation.
 *
 * <p>This controller obtains the shared {@code StationEsinfRepository},
 * retrieves its KD-tree and produces an ordered list of KD-tree nodes suitable
 * for display in the UI. It also provides simple statistics about the tree,
 * such as the number of nodes, the number of leaves and the tree height.
 */
public class OrganizedStationsController {
    private StationEsinf2Repository stationRepository;

    private KD2TreeStation kd2TreeStation;

    private List<KD2TreeStation.KD2NodeStation> kd2TreeNodes;

    private int nNodes;

    private int nLeaves;

    /**
     * Initialise the controller.
     *
     * <p>The constructor obtains the application station repository, reads the
     * KD-tree, initialises the internal node list and computes node/leaf
     * counts. No external resources are modified by this operation.
     */
    public OrganizedStationsController() {
        this.stationRepository = Repositories.getInstance().getStationEsinf2Repository();
        kd2TreeStation = stationRepository.getKdTree();

        setKd2TreeNodes();
        this.nNodes = kd2TreeNodes.size();

        setnLeaves();
    }

    /**
     * Return the KD-tree nodes as an observable list for use by JavaFX UI
     * components.
     *
     * @return an {@code ObservableList} containing the KD-tree node objects
     */
    public ObservableList<KD2TreeStation.KD2NodeStation> getKD2TreeStationNodes() {
        return FXCollections.observableArrayList(kd2TreeNodes);
    }

    /**
     * Populate the controller's internal list of KD-tree nodes by performing
     * a breadth-first traversal of the tree.
     *
     * <p>This method initialises the list then delegates to
     * {@link #setKD2TreeNodesRec(KD2TreeStation.KD2NodeStation, List)} to
     * perform the traversal.
     */
    private void setKd2TreeNodes() {
        kd2TreeNodes = new ArrayList<>();
        setKD2TreeNodesRec(kd2TreeStation.getRoot(), kd2TreeNodes);
    }

    /**
     * Helper that traverses the KD-tree in level-order (breadth-first) and
     * appends each visited node to the provided result list.
     *
     * <p>Each node's location string is initialised for the root and updated
     * for left/right children so that the UI can display a human-friendly
     * node identifier.
     *
     * @param node   the starting node for the traversal (may be {@code null})
     * @param result the list to populate with visited nodes
     */
    private void setKD2TreeNodesRec(KD2TreeStation.KD2NodeStation node, List<KD2TreeStation.KD2NodeStation> result) {
        if (node == null) return;

        Queue<KD2TreeStation.KD2NodeStation> queue = new LinkedList<>();
        queue.add(node);
        node.setLocationNodeString("R-");

        while (!queue.isEmpty()) {
            KD2TreeStation.KD2NodeStation current = queue.poll();
            result.add(current);

            if (current.getLeft() != null) {
                KD2TreeStation.KD2NodeStation currentLeft = current.getLeft();

                currentLeft.setLocationNodeString(current.getLocationNodeString() + "l");
                queue.add(currentLeft);
            }

            if (current.getRight() != null) {
                KD2TreeStation.KD2NodeStation currentRight = current.getRight();

                currentRight.setLocationNodeString(current.getLocationNodeString() + "r");
                queue.add(currentRight);
            }
        }
    }

    /**
     * Compute and set the number of leaves in the KD-tree.
     *
     * <p>The result is stored in {@link #nLeaves} and can be retrieved via
     * {@link #getnLeaves()}.
     */
    public void setnLeaves() {
        nLeaves = 0;
        countLeavesRec(kd2TreeStation.getRoot());
    }

    /**
     * Recursive helper that counts leaf nodes in the KD-tree.
     *
     * @param node the node to examine (may be {@code null})
     */
    private void countLeavesRec(KD2TreeStation.KD2NodeStation node) {
        if (node == null) return;

        if (node.getLeft() == null && node.getRight() == null) {
            nLeaves++;
        } else {
            countLeavesRec(node.getLeft());
            countLeavesRec(node.getRight());
        }
    }

    /**
     * Return the height of the underlying KD-tree.
     *
     * @return the tree height as reported by the KD-tree implementation
     */
    public int getTreeHeight() {
        return kd2TreeStation.getHeight();
    }

    /**
     * Return the number of KD-tree nodes that were discovered during
     * initialisation.
     *
     * @return the number of nodes
     */
    public int getnNodes() {
        return nNodes;
    }

    /**
     * Set the number of nodes stored in this controller. This setter is
     * provided for completeness and does not alter the underlying KD-tree.
     *
     * @param nNodes the new node count
     */
    public void setnNodes(int nNodes) {
        this.nNodes = nNodes;
    }

    /**
     * Return the number of leaves counted in the KD-tree.
     *
     * @return the number of leaf nodes
     */
    public int getnLeaves() {
        return nLeaves;
    }
}
