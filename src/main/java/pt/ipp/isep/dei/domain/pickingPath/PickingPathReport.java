package pt.ipp.isep.dei.domain.pickingPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a report containing multiple {@link PickingPath} instances
 * generated using a specific {@link PickingStrategyType}.
 * Tracks total distance across all included paths.
 */
public class PickingPathReport {
    private List<PickingPath> pickingPaths = new ArrayList<>();
    private PickingStrategyType pickingStrategyType;
    private String id;

    /** Static counter for unique report ID generation. */
    public static int counter = 0;

    /**
     * Constructs a {@code PickingPathReport} for a given strategy.
     * Automatically assigns a unique ID based on the report counter.
     *
     * @param pickingStrategyType the picking strategy used for the report
     */
    public PickingPathReport(PickingStrategyType pickingStrategyType) {
        this.pickingStrategyType = pickingStrategyType;
        this.id = String.format("PP%03d", counter++);
    }

    /**
     * @return the list of picking paths contained in this report
     */
    public List<PickingPath> getPickingPaths() {
        return pickingPaths;
    }

    /**
     * @param pickingPaths the list of picking paths to set
     */
    public void setPickingPaths(List<PickingPath> pickingPaths) {
        this.pickingPaths = pickingPaths;
    }

    /**
     * @return the picking strategy type associated with this report
     */
    public PickingStrategyType getPickingStrategyType() {
        return pickingStrategyType;
    }

    /**
     * @param pickingStrategyType the picking strategy type to set
     */
    public void setPickingStrategyType(PickingStrategyType pickingStrategyType) {
        this.pickingStrategyType = pickingStrategyType;
    }

    /**
     * @return the unique identifier of this picking path report
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the identifier to set for this report
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Adds a picking path to the report.
     *
     * @param pickingPath the picking path to add
     */
    public void addPickingPath(PickingPath pickingPath) {
        this.pickingPaths.add(pickingPath);
    }

    /**
     * Calculates the total distance covered across all picking paths in the report.
     *
     * @return the total cumulative distance
     */
    public double getTotalDistance() {
        double totalDistance = 0.0;
        for (PickingPath path : pickingPaths) {
            for (PathPoint point : path.getPathPointList()) {
                totalDistance += point.getDistanceFromPrevious();
            }
        }
        return totalDistance;
    }

    /**
     * @return a string representation of the report including paths, strategy, and total distance
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PickingPathReport ID: ").append(id)
                .append(", Strategy: ").append(pickingStrategyType).append("\n");
        sb.append("Picking Paths:\n");
        for (PickingPath path : pickingPaths) {
            sb.append(path.toString()).append("\n");
        }
        sb.append("Total Distance of Report: ").append(getTotalDistance()).append("\n");
        return sb.toString();
    }
}
