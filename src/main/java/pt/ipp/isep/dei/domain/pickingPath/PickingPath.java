package pt.ipp.isep.dei.domain.pickingPath;

import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a picking path assigned to a specific trolley.
 * Contains a sequence of {@link PathPoint} elements and supports distance calculations.
 */
public class PickingPath {
    private Trolley trolley;
    private List<PathPoint> pathPointList = new LinkedList<>();

    /**
     * Constructs a {@code PickingPath} for the given trolley.
     *
     * @param trolley the trolley assigned to this picking path
     */
    public PickingPath(Trolley trolley) {
        this.trolley = trolley;
    }

    /**
     * @return the trolley associated with this picking path
     */
    public Trolley getTrolley() {
        return trolley;
    }

    /**
     * @param trolley the trolley to associate with this picking path
     */
    public void setTrolley(Trolley trolley) {
        this.trolley = trolley;
    }

    /**
     * @return the list of path points defining this picking path
     */
    public List<PathPoint> getPathPointList() {
        return pathPointList;
    }

    /**
     * @param pathPointList the list of path points to set
     */
    public void setPathPointList(List<PathPoint> pathPointList) {
        this.pathPointList = pathPointList;
    }

    /**
     * Adds a path point to the picking path.
     *
     * @param pathPoint the path point to add
     */
    public void addPathPoint(PathPoint pathPoint) {
        this.pathPointList.add(pathPoint);
    }

    /**
     * Calculates the total travel distance of the picking path
     * by summing distances between consecutive path points.
     *
     * @return the total distance of the picking path
     */
    public double getTotalDistance() {
        double totalDistance = 0.0;
        for (PathPoint point : pathPointList) {
            totalDistance += point.getDistanceFromPrevious();
        }
        return totalDistance;
    }

    /**
     * @return a string representation of the picking path, including trolley ID,
     *         list of path points, and total distance
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PickingPath for Trolley: ").append(trolley.getTrolleyId()).append("\n");
        sb.append("Path Points:\n");
        for (PathPoint point : pathPointList) {
            sb.append("  ").append(point.toString()).append("\n");
        }
        sb.append("Total Distance: ").append(getTotalDistance()).append("\n");
        return sb.toString();
    }
}
