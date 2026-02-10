package pt.ipp.isep.dei.domain.pickingPath;

import pt.ipp.isep.dei.domain.BoxRelated.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific point in a picking path, containing one or more boxes
 * and positional information relative to the previous point.
 */
public class PathPoint {
    private List<Box> boxList = new ArrayList<>();
    private double distanceFromPrevious;
    private double aisleCoord;
    private double bayCoord;

    /**
     * Constructs a {@code PathPoint} with an initial box and location details.
     *
     * @param box                 the box located at this point
     * @param distanceFromPrevious the distance from the previous point in the path
     * @param aisleCoord          the coordinate representing the aisle position
     * @param bayCoord            the coordinate representing the bay position
     */
    public PathPoint(Box box, double distanceFromPrevious, double aisleCoord, double bayCoord) {
        boxList.add(box);
        this.distanceFromPrevious = distanceFromPrevious;
        this.aisleCoord = aisleCoord;
        this.bayCoord = bayCoord;
    }

    /**
     * @return the list of boxes located at this point
     */
    public List<Box> getBoxList() {
        return boxList;
    }

    /**
     * @param boxList the list of boxes to associate with this point
     */
    public void setBoxList(List<Box> boxList) {
        this.boxList = boxList;
    }

    /**
     * @param distanceFromPrevious the distance from the previous path point
     */
    public void setDistanceFromPrevious(double distanceFromPrevious) {
        this.distanceFromPrevious = distanceFromPrevious;
    }

    /**
     * @return the distance from the previous path point
     */
    public double getDistanceFromPrevious() {
        return distanceFromPrevious;
    }

    /**
     * Adds a box to the list of boxes at this path point.
     *
     * @param box the box to add
     */
    public void addBox(Box box) {
        boxList.add(box);
    }

    /**
     * @return a string representation of this path point, including box IDs and coordinates
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PathPoint{");
        sb.append("boxes=[");

        for (int i = 0; i < boxList.size(); i++) {
            sb.append(boxList.get(i).getBoxId());
            if (i < boxList.size() - 1) sb.append(", ");
        }

        sb.append("], distanceFromPrevious=")
                .append(distanceFromPrevious)
                .append(", aisleCoord=")
                .append(aisleCoord)
                .append(", bayCoord=")
                .append(bayCoord)
                .append('}');

        return sb.toString();
    }
}
