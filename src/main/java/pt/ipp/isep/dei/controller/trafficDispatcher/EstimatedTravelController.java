package pt.ipp.isep.dei.controller.trafficDispatcher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.comparator.FacilityDistanceComparator;
import pt.ipp.isep.dei.data.memory.FacilityStoreInMemory;
import pt.ipp.isep.dei.data.memory.LocomotiveStoreInMemory;
import pt.ipp.isep.dei.data.memory.RailwayLineSegmentStoreInMemory;
import pt.ipp.isep.dei.data.memory.RailwayLineStoreInMemory;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;

import java.util.*;

/**
 * Controller responsible for estimating routes and travel times between railway facilities.
 * Builds a graph of railway lines and calculates distances and travel durations
 * based on the selected locomotive and line or segment constraints.
 */
public class EstimatedTravelController {

    /** Repository for managing railway line data. */
    private RailwayLineStoreInMemory railwayLineStoreInMemory;

    /** Repository for managing railway line segments. */
    private RailwayLineSegmentStoreInMemory railwayLineSegmentStoreInMemory;

    /** Repository for managing locomotive data. */
    private LocomotiveStoreInMemory locomotiveStoreInMemory;

    /** Repository for managing facility data. */
    private FacilityStoreInMemory facilityStoreInMemory;

    /** Stores the traversed path composed of lines. */
    private List<TraversedLine> path = new ArrayList<>();

    /** Graph representation where facilities are nodes and connections are edges. */
    private final Map<String, List<Edge>> graph = new HashMap<>();

    /** The locomotive used for the estimation. */
    private Locomotive locomotive;

    /** The ID of the start facility. */
    private int startFacilityId;

    /** The ID of the end facility. */
    private int endFacilityId;

    /** The ID of the selected locomotive. */
    private int locomotiveId;

    /** Facility where the journey starts. */
    private Facility startFacility;

    /** Facility where the journey ends. */
    private Facility endFacility;

    /** Selected locomotive object. */
    private Locomotive selectedLocomotive;

    /** List containing results for each line segment traversed. */
    private final List<LineResult> lineResults = new ArrayList<>();

    /** Total travel distance in kilometers. */
    private double totalDistanceKm;

    /** Total estimated travel time. */
    private Time totalEstimatedTime;

    /**
     * Constructor.
     * Initializes repositories and builds the railway graph.
     */
    public EstimatedTravelController() {
        railwayLineStoreInMemory = new RailwayLineStoreInMemory();
        railwayLineSegmentStoreInMemory = new RailwayLineSegmentStoreInMemory();
        locomotiveStoreInMemory = new LocomotiveStoreInMemory();
        facilityStoreInMemory = new FacilityStoreInMemory();
        buildGraph();
    }

    public int getStartFacilityId() {
        return startFacilityId;
    }

    public void setStartFacilityId(int startFacilityId) {
        this.startFacilityId = startFacilityId;
    }

    public int getEndFacilityId() {
        return endFacilityId;
    }

    public void setEndFacilityId(int endFacilityId) {
        this.endFacilityId = endFacilityId;
    }

    public int getLocomotiveId() {
        return locomotiveId;
    }

    public void setLocomotiveId(int locomotiveId) {
        this.locomotiveId = locomotiveId;
    }

    /** @return the total distance in kilometers */
    public double getTotalDistanceKm() { return totalDistanceKm; }

    /** @return the total estimated travel time */
    public Time getTotalEstimatedTime() { return totalEstimatedTime; }

    /** @return the locomotive used for the route */
    public Locomotive getLocomotive() { return locomotive; }

    /** @param locomotive the locomotive to use */
    public void setLocomotive(Locomotive locomotive) { this.locomotive = locomotive; }

    /** @return the start facility */
    public Facility getStartFacility() { return startFacility; }

    /** @param startFacility the start facility */
    public void setStartFacility(Facility startFacility) { this.startFacility = startFacility; }

    /** @return the end facility */
    public Facility getEndFacility() { return endFacility; }

    /** @param endFacility the end facility */
    public void setEndFacility(Facility endFacility) { this.endFacility = endFacility; }

    /** @return the selected locomotive */
    public Locomotive getSelectedLocomotive() { return selectedLocomotive; }

    /** @param selectedLocomotive the selected locomotive */
    public void setSelectedLocomotive(Locomotive selectedLocomotive) { this.selectedLocomotive = selectedLocomotive; }

    /** @param totalDistanceKm the total distance in kilometers */
    public void setTotalDistanceKm(double totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }

    /** @param totalEstimatedTime the total estimated time */
    public void setTotalEstimatedTime(Time totalEstimatedTime) { this.totalEstimatedTime = totalEstimatedTime; }

    /**
     * Retrieves all facilities.
     * @return observable list of all facilities
     */
    public ObservableList<Facility> getAllFacilities() {
        return FXCollections.observableArrayList(facilityStoreInMemory.findAll());
    }

    /**
     * Retrieves all facilities excluding the start facility.
     * @return observable list of facilities
     */
    public ObservableList<Facility> getAllFacilitiesExcluding() {
        List<Facility> facilities = new ArrayList<>();
        for (Facility facility : facilityStoreInMemory.findAll()) {
            if (facility.getId() != (startFacilityId)) facilities.add(facility);
        }
        return FXCollections.observableArrayList(facilities);
    }

    /**
     * Retrieves all locomotives.
     * @return observable list of locomotives
     */
    public ObservableList<Locomotive> getAllLocomotives() {
        return FXCollections.observableArrayList(locomotiveStoreInMemory.findAll());
    }

    /** Builds the internal railway graph from repository data. */
    private void buildGraph() {
        graph.clear();
        for (RailwayLine railwayLine : railwayLineStoreInMemory.findAll()) {
            Facility startStation = facilityStoreInMemory.findById(null, String.valueOf(railwayLine.getStartFacilityId()));
            Facility endStation = facilityStoreInMemory.findById(null, String.valueOf(railwayLine.getEndFacilityId()));
            double distanceMeters = 0.0;

            for (RailwayLineSegment segment : railwayLineSegmentStoreInMemory.findAll()) {
                if (!segment.getRailwayLines().isEmpty() && segment.getRailwayLines().getFirst().getId() == railwayLine.getId()) {
                    distanceMeters += segment.getLength();
                }
            }

            if (!graph.containsKey(startStation.getId() + "")) {
                graph.put(startStation.getId() + "", new ArrayList<>());
            }
            graph.get(startStation.getId() + "").add(new Edge(endStation, distanceMeters));

            if (!graph.containsKey(endStation.getId() + "")) {
                graph.put(endStation.getId() + "", new ArrayList<>());
            }
            graph.get(endStation.getId() + "").add(new Edge(startStation, distanceMeters));
        }
    }

    /**
     * Creates the optimal path between the selected facilities using Dijkstra's algorithm.
     * @return true if a valid path was found
     */
    public boolean createPath() {
        startFacility = facilityStoreInMemory.findById(null, String.valueOf(startFacilityId));
        endFacility = facilityStoreInMemory.findById(null, String.valueOf(endFacilityId));
        path = dijkstraWithDirection(startFacility, endFacility);
        if (path != null) {
            totalDistanceKm = getDistancePath(path) / 1000.0;
            return true;
        }
        return false;
    }

    /**
     * Executes Dijkstra's algorithm to find the shortest path between two facilities.
     * @param origin start facility
     * @param destination end facility
     * @return list of traversed lines or null if no path exists
     */
    private List<TraversedLine> dijkstraWithDirection(Facility origin, Facility destination) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, TraversedLine> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<String> queue = new PriorityQueue<>(new FacilityDistanceComparator(distances));

        for (Facility f : facilityStoreInMemory.findAll()) distances.put(f.getId() + "", Double.POSITIVE_INFINITY);
        distances.put(origin.getId() + "", 0.0);
        queue.add(origin.getId() + "");

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            if (!visited.add(currentId)) continue;

            List<Edge> edges = graph.get(currentId);
            if (edges != null) {
                for (Edge edge : edges) {
                    String destId = edge.getDestination().getId() + "";
                    double newDist = distances.get(currentId) + edge.getDistanceMeters();
                    if (newDist < distances.get(destId)) {
                        distances.put(destId, newDist);
                        RailwayLine line = findRailwayLine(currentId, destId);
                        Facility start = facilityStoreInMemory.findById(null, currentId);
                        Facility end = facilityStoreInMemory.findById(null, destId);
                        previous.put(destId, new TraversedLine(line, start, end));
                        queue.add(destId);
                    }
                }
            }
        }

        if (!previous.containsKey(destination.getId() + "")) return null;

        List<TraversedLine> result = new LinkedList<>();
        String nodeId = destination.getId() + "";
        while (!nodeId.equals(origin.getId() + "")) {
            TraversedLine traversed = previous.get(nodeId);
            result.addFirst(traversed);
            nodeId = String.valueOf(traversed.getStart().getId());
        }
        return result;
    }

    /** Calculates the total estimated travel time. */
    public void calculateEstimatedTravelTime() {
        locomotive = locomotiveStoreInMemory.findById(null, String.valueOf(locomotiveId));
        totalEstimatedTime = convertHoursToTime(calculateTravelTime());
        setLineResults();
    }

    /**
     * Calculates the total travel time in hours.
     * @return total time in hours
     */
    private double calculateTravelTime() {
        double totalTime = 0.0;
        for (TraversedLine tLine : path) {
            for (RailwayLineSegment segment : railwayLineSegmentStoreInMemory.findAll()) {
                if (!segment.getRailwayLines().isEmpty() && segment.getRailwayLines().getFirst().getId() == (tLine.getLine().getId())) {
                    double allowedSpeed = Math.min(segment.getSpeedLimit(), locomotive.getLocomotiveModel().getOperationalSpeed());
                    double distanceKm = segment.getLength() / 1000.0;
                    totalTime += distanceKm / allowedSpeed;
                }
            }
        }
        return totalTime;
    }

    /**
     * Computes the total path distance.
     * @param path list of traversed lines
     * @return total distance in meters
     */
    private double getDistancePath(List<TraversedLine> path) {
        double totalDistance = 0.0;
        for (TraversedLine tLine : path) {
            for (RailwayLineSegment segment : railwayLineSegmentStoreInMemory.findAll()) {
                if (!segment.getRailwayLines().isEmpty() && segment.getRailwayLines().getFirst().getId() == (tLine.getLine().getId())) totalDistance += segment.getLength();
            }
        }
        return totalDistance;
    }

    /**
     * Converts total hours to a {@link Time} object.
     * @param totalHours total hours
     * @return time object
     */
    private Time convertHoursToTime(double totalHours) {
        int totalSeconds = (int) Math.round(totalHours * 3600);
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return new Time(hours, minutes, seconds);
    }

    /**
     * Finds the railway line connecting two facilities.
     * @param startId start facility ID
     * @param endId end facility ID
     * @return matching railway line or null
     */
    private RailwayLine findRailwayLine(String startId, String endId) {
        for (RailwayLine line : railwayLineStoreInMemory.findAll()) {
            if ((line.getStartFacilityId() + "").equals(startId) && (line.getEndFacilityId() + "").equals(endId) ||
                    (line.getStartFacilityId() + "").equals(endId) && (line.getEndFacilityId() + "").equals(startId)) {
                return line;
            }

        }
        return null;
    }

    /** Fills the list of detailed results for each traversed line. */
    public void setLineResults() {
        lineResults.clear();
        for (TraversedLine tLine : path) {
            double distanceKm = 0.0;
            double totalTimeH = 0.0;
            for (RailwayLineSegment segment : railwayLineSegmentStoreInMemory.findAll()) {
                if (!segment.getRailwayLines().isEmpty() && segment.getRailwayLines().getFirst().getId() == tLine.getLine().getId()) {
                    double allowedSpeed = Math.min(segment.getSpeedLimit(), locomotive.getLocomotiveModel().getOperationalSpeed());
                    double distance = segment.getLength() / 1000.0;
                    distanceKm += distance;
                    totalTimeH += distance / allowedSpeed;
                }
            }
            lineResults.add(new LineResult(tLine, distanceKm, totalTimeH));
        }
    }

    /** @return observable list of line results */
    public ObservableList<LineResult> getLineResults() {
        return FXCollections.observableArrayList(lineResults);
    }

    /** @return start facility name */
    public String getStartFacilityName() { return startFacility.getName(); }

    /** @return end facility name */
    public String getEndFacilityName() { return endFacility.getName(); }

    /** @return list of traversed lines */
    public List<TraversedLine> getPath() { return path; }

    /**
     * Gets all traversed segments for a specific line.
     * @param lineId line ID
     * @return list of traversed segments
     */
    public List<TraversedSegment> getTraversedSegmentsByLine(String lineId) {
        List<RailwayLineSegment> lineSegments = new ArrayList<>();

        for (RailwayLineSegment segment : railwayLineSegmentStoreInMemory.findAll()) {
            if (!segment.getRailwayLines().isEmpty() && (segment.getRailwayLines().getFirst().getId() + "").equals(lineId)) {
                lineSegments.add(segment);
            }
        }

        RailwayLine line = lineSegments.getFirst().getRailwayLines().getFirst();
        for (int i = 0; i < lineSegments.size() - 1; i++) {
            for (int j = i + 1; j < lineSegments.size(); j++) {
                if (line.getSegmentOrder(lineSegments.get(i)) > line.getSegmentOrder(lineSegments.get(j))) {
                    RailwayLineSegment temp = lineSegments.get(i);
                    lineSegments.set(i, lineSegments.get(j));
                    lineSegments.set(j, temp);
                }
            }
        }

        List<TraversedSegment> segments = new ArrayList<>();
        for (RailwayLineSegment seg : lineSegments) {
            segments.add(new TraversedSegment(seg, locomotive));
        }

        RailwayLine realRailwayLine = railwayLineStoreInMemory.findById(null, lineId);
        TraversedLine traversedLine = getLineIdInPath(lineId);

        if (!(realRailwayLine.getStartFacilityId() + "").equals(
                Objects.requireNonNull(traversedLine).getStartFacilityId())) {

            for (int i = 0, j = segments.size() - 1; i < j; i++, j--) {
                TraversedSegment temp = segments.get(i);
                segments.set(i, segments.get(j));
                segments.set(j, temp);
            }
        }

        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).setActualOrderPosition(i + 1);
        }

        return segments;
    }

    /**
     * Finds a traversed line within the current path by its line ID.
     * @param lineId the line identifier
     * @return the traversed line object or null if not found
     */
    private TraversedLine getLineIdInPath(String lineId) {
        for (TraversedLine tLine : path) {
            if ((tLine.getLine().getId() + "").equals(lineId)) {
                return tLine;
            }
        }
        return null;
    }

    /** Represents a traversed railway line in the path. */
    public class TraversedLine {
        private final RailwayLine line;
        private final Facility start;
        private final Facility end;

        /**
         * Creates a traversed line representation.
         * @param line the railway line
         * @param start the start facility
         * @param end the end facility
         */
        public TraversedLine(RailwayLine line, Facility start, Facility end) {
            this.line = line;
            this.start = start;
            this.end = end;
        }

        /** @return the railway line */
        public RailwayLine getLine() { return line; }

        /** @return the start facility */
        public Facility getStart() { return start; }

        /** @return the end facility */
        public Facility getEnd() { return end; }

        /** @return the ID of the start facility */
        public int getStartFacilityId() { return start.getId(); }
    }

    /** Represents a traversed segment within a line. */
    public class TraversedSegment {
        private final RailwayLineSegment segment;
        private final double distanceKm;
        private final Time travelTime;
        private int actualOrderPosition;

        /**
         * Constructs a traversed segment based on a line segment and locomotive.
         * @param segment the railway line segment
         * @param loco the locomotive used
         */
        public TraversedSegment(RailwayLineSegment segment, Locomotive loco) {
            this.segment = segment;
            this.distanceKm = segment.getLength() / 1000.0;
            double allowedSpeed = Math.min(segment.getSpeedLimit(), loco.getLocomotiveModel().getOperationalSpeed());
            double travelTimeH = distanceKm / allowedSpeed;
            this.travelTime = convertHoursToTime(travelTimeH);
        }

        /** @return the line ID */
        public String getLineId() { return segment.getRailwayLines().getFirst().getId() + ""; }

        /** @return true if the segment is electrified */
        public boolean isElectrified() { return segment.isElectrifiedLine(); }

        /** @return the segment speed limit */
        public int getSpeedLimit() { return segment.getSpeedLimit(); }

        /** @return the formatted segment length */
        public String getLengthString() {
            double lengthKm = (double) segment.getLength() / 1000;
            return String.format("%.2f km", lengthKm);
        }

        /** @return formatted estimated travel time */
        public String getEstimatedTimeHours() { return travelTime.toString(); }

        /** @return actual order position of the segment */
        public int getActualOrderPosition() { return actualOrderPosition; }

        /** @param actualOrderPosition sets the segment's actual order */
        public void setActualOrderPosition(int actualOrderPosition) { this.actualOrderPosition = actualOrderPosition; }
    }

    public class LineResult {
        private final TraversedLine traversedLine;
        private final double distanceKm;
        private final Time travelTime;

        /**
         * @param traversedLine traversed line
         * @param distanceKm total distance in kilometers
         * @param travelTime total travel time in hours
         */
        public LineResult(TraversedLine traversedLine, double distanceKm, double travelTime) {
            this.traversedLine = traversedLine;
            this.distanceKm = distanceKm;
            this.travelTime = convertHoursToTime(travelTime);
        }

        /** @return the line ID */
        public int getLineId() {
            return traversedLine.getLine().getId();
        }

        /** @return start and end facility names */
        public String getStartEndFacilities() {
            return traversedLine.getStart().getName() + "\n" + traversedLine.getEnd().getName();
        }

        /** @return formatted distance */
        public String getDistance() {
            return String.format("%.2f km", distanceKm);
        }

        /** @return formatted travel time */
        public String getTravelTimeHours() {
            return travelTime.toString();
        }
    }
}