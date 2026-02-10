package pt.ipp.isep.dei.domain.Tree.KDTree;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.TimeZone;

import java.awt.geom.Point2D;
import java.util.*;

public class KD2TreeStation extends KD2Tree<KD2TreeStation.ListStation> {

    private int height;

    public KD2NodeStation getRoot() {
        return (KD2NodeStation) super.root;
    }

    public void setRoot(KD2NodeStation root) {
        super.root = root;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void build(Map<String, StationEsinf> stations) {
        if (stations == null || stations.isEmpty()) {   // O(1)
            root = null;
            return;
        }

        List<List<StationEsinf>> stationsList = getStationsList(stations);   // O(n), [O(n) + O(1) = O(n)]

        root = buildRec(stationsList, 0, stationsList.size() - 1, true, 1); // O(n log^2 n), [O(n log^2 n) + O(n) = O(n log^2 n)]
    }

    private KD2NodeStation buildRec(List<List<StationEsinf>> stations, int min, int max, boolean latitudeOrder, int height) {
        if (min > max) return null; //O(1)

        this.height = Math.max(height, this.height); // O(1), [O(1) + O(1) = O(1)]

        mergeSortRange(stations, min, max, latitudeOrder); // O(n log n), [O(1) + O(n log n) = O(n log n)]
        int median = (min + max) / 2;   // O(1), [O(1) + O(n log n) = O(n log n)]

        KD2NodeStation node = new KD2NodeStation(stations.get(median), height); // O(1), [O(1) + O(n log n) = O(n log n)]

        node.setLeft(buildRec(stations, min, median - 1, !latitudeOrder, height + 1));    // O(log n), [O(log n) * O(n log n) = O(n log^2 n)]
        node.setRight(buildRec(stations, median + 1, max, !latitudeOrder, height + 1));  // O(log n), [O(log n) * O(n log n) = O(n log^2 n)]

        return node;
    }

    private List<List<StationEsinf>> getStationsList(Map<String, StationEsinf> stations) {
        Map<String, List<StationEsinf>> groups = new HashMap<>();   // O(1)

        for (StationEsinf st : stations.values()) {   // O(n), [O(n) + O(1) = O(n)]

            String key = st.getX() + "_" + st.getY();   // O(1)

            List<StationEsinf> group = groups.get(key);  // O(1), [O(1) + O(1) = O(1)]
            if (group == null) {    // O(1), [O(1) + O(1) = O(1)]
                group = new ArrayList<>();  // O(1), [O(1) + O(1) = O(1)]
                groups.put(key, group); // O(1), [O(1) + O(1) = O(1)]
            }

            group.add(st);   // O(1), [O(1) + O(1) = O(1)]
        }

        for (List<StationEsinf> group : groups.values()) {   // O(n log n), [O(n) + O(n log n) = O(n log n)]
            group.sort(new Comparator<StationEsinf>() {  // O(n log n)
                @Override
                public int compare(StationEsinf a, StationEsinf b) {   // O(1), [O(1) + O(n log n) = O(n log n)]
                    return a.getStationName().compareTo(b.getStationName());   // O(1), [O(1) + O(n log n) = O(n log n)]
                }
            });
        }

        return new ArrayList<>(groups.values());
    }

    private void mergeSortRange(List<List <StationEsinf>> stations, int left, int right, boolean latitudeOrder) {   // O(n log n)
        if (left >= right) return; // O(1)

        int mid = (left + right) / 2;   // O(1), [O(1) + O(1) = O(1)]
        mergeSortRange(stations, left, mid, latitudeOrder);    // O(n log n), [O(n log n) + O(1) = O(n log n)]
        mergeSortRange(stations, mid + 1, right, latitudeOrder);    // O(n log n), [O(n log n) + O(n log n) = O(n log n)]
        merge(stations, left, mid, right, latitudeOrder);   // O(n), [O(n) + O(n log n) = O(n log n)]
    }

    private void merge(List<List<StationEsinf>> stations, int left, int mid, int right, boolean latitudeOrder) {   // O(n)

        List<List<StationEsinf>> temp = new ArrayList<>();
        int i = left, j = mid + 1;  // O(1)

        while (i <= mid && j <= right) {    // O(n), [O(n) + O(1) = O(n)]
            double vi = latitudeOrder ? stations.get(i).getFirst().getLatitude() : stations.get(i).getFirst().getLongitude();  // O(1)
            double vj = latitudeOrder ? stations.get(j).getFirst().getLatitude() : stations.get(j).getFirst().getLongitude(); // O(1)

            if (vi <= vj) temp.add(stations.get(i++));  // O(1)
            else temp.add(stations.get(j++));   // O(1)
        }

        while (i <= mid) temp.add(stations.get(i++));   // O(n), [O(n) + O(n) = O(n)]
        while (j <= right) temp.add(stations.get(j++));  // O(n), [O(n) + O(n) = O(n)]

        for (int k = 0; k < temp.size(); k++) {  // O(n), [O(n) + O(n) = O(n)]
            stations.set(left + k, temp.get(k));    // O(1)
        }
    }

    public void printTree() {
        System.out.println("KD2-Tree:");
        printTreeRec((KD2NodeStation) root, "");
    }

    private void printTreeRec(KD2NodeStation node, String indent) {
        if (node == null) return;
        System.out.println(indent + " (" + node + ")");
        printTreeRec(node.getLeft(), indent + "   L─");
        printTreeRec(node.getRight(), indent + "   R─");
    }

    public static class KD2NodeStation extends KD2Node<ListStation> { // complexidade total = O(1)

        private int height; // O(1)
        private String locationNodeString; // O(1)

        public KD2NodeStation(List<StationEsinf> stations, int height) {
            ListStation listStation = new ListStation(stations); // O(1)
            super(listStation);  // O(1)
            this.height = height;  // O(1)
        }

        public int getHeight() {  // O(1)
            return height;
        }

        public void setHeight(int height) {  // O(1)
            this.height = height;
        }

        public String getLocationNodeString() {  // O(1)
            return locationNodeString;
        }

        public void setLocationNodeString(String locationNodeString) {  // O(1)
            this.locationNodeString = locationNodeString;
        }

        @Override
        public KD2NodeStation getLeft() { return (KD2NodeStation) super.getLeft(); }  // O(1)

        @Override
        public KD2NodeStation getRight() { return (KD2NodeStation) super.getRight(); }  // O(1)

        @Override public String toString() {  // O(1) + O(1) + O(1) = O(1)
            return super.getX() + ", " + super.getY() + " | Stations: " + super.getElement().getStations().size();
        }
    }

    public static class ListStation implements Comparable<ListStation>, KDTreeNodeInterface { // complexidade total = O(1)

        private List<StationEsinf> stations; // O(1)
        private final double x; // O(1)
        private final double y; // O(1)

        public ListStation(List<StationEsinf> stations) {
            this.stations = stations; // O(1)
            StationEsinf first = stations.getFirst(); // O(1)
            this.x = first.getLatitude(); // O(1)
            this.y = first.getLongitude(); // O(1)
        }

        @Override
        public double getX() {  // O(1)
            return x;
        }

        @Override
        public double getY() {  // O(1)
            return y;
        }

        public List<StationEsinf> getStations() {  // O(1)
            return stations;
        }

        public void setStations(List<StationEsinf> stations) {  // O(1)
            this.stations = stations;
        }

        @Override
        public int compareTo(ListStation o) { // O(1)
            double distanceThis = Math.sqrt(x * x + y * y); // O(1)
            double distanceOther = Math.sqrt(o.getX() * o.getX() + o.getY() * o.getY()); // O(1)
            return Double.compare(distanceThis, distanceOther); // O(1)
        }
    }

    public List<StationEsinf> findKNearestStations(double x, double y, int k, TimeZone timeZone) { // complexidade total = O(log n + k log k + S + min(k, S))
        List<ListStation> nearestLists = findKNearestNeighbours(x, y, k, timeZone); // O(log n + k log k)
        List<StationEsinf> stations = new ArrayList<>(); // O(1)

        for (ListStation ls : nearestLists) { // O(S), S = total de estações copiadas
            stations.addAll(ls.getStations()); // O(tamaho de ls.getStations()) para cada iteração, total = O(S)
        }

        List<StationEsinf> stationsResult = new ArrayList<>(); // O(1)

        for (int i = 0; i < Math.min(k, stations.size()); i++) { // O(min(k,S)): se k < S, k iterações - complexidade O(k); se k ≥ S, S iterações - complexidade O(S)
            stationsResult.add(stations.get(i)); // get O(1), add O(1) = O(1)
        }

        return stationsResult; // O(1)
    }

    public List<ListStation> findKNearestNeighbours(double x, double y, int k, TimeZone timeZone) { // O(log n + k log k + k^2)
        if (root == null || k <= 0) // O(1)
            return new ArrayList<>(); // O(1)

        PriorityQueue<KD2Node<ListStation>> heap = new PriorityQueue<>(k, new HaversineDistanceComparator(x, y)); // O(1)
        findKNearestNeighboursTimeZone((KD2Node<ListStation>) root, x, y, k, true, heap, timeZone); // O(log n + k log k)

        List<ListStation> result = new ArrayList<>(heap.size()); // O(1)
        while (!heap.isEmpty()) { // O(k)
            result.addFirst(heap.poll().getElement()); // heap.poll() - O(log k); addFirts() O(k)
        }

        return result; // O(1)
    }

    public void findKNearestNeighboursTimeZone(KD2Node<ListStation> node, double x, double y, int k, boolean divX, PriorityQueue<KD2Node<ListStation>> heap, TimeZone timeZone) { // complexidade total = O(log n + k log k)
        if (node == null) // O(1)
            return;

        if (node.getElement() != null && (timeZone == null) || node.getElement().getStations().getFirst().getTimeZone() == timeZone) { // O(1) - get(0)
            double distSq = Point2D.distanceSq(node.getX(), node.getY(), x, y); // O(1)

            if (heap.size() < k) { // O(1)
                heap.offer(node); // O(log k)
            } else {
                double farthestDist = Point2D.distanceSq(heap.peek().getX(), heap.peek().getY(), x, y); // O(1)
                if (distSq < farthestDist) {
                    heap.poll(); // O(log k)
                    heap.offer(node); // O(log k)
                }
            }
        }

        double delta;
        KD2Node<ListStation> nearNode;
        KD2Node<ListStation> farNode;

        if (divX) { // O(1)

            delta = x - node.getX();
        } else {
            delta = y - node.getY();
        }

        double deltaSquared = delta * delta;

        if (delta < 0) { // O(1)
            nearNode = (KD2Node<ListStation>) node.getLeft();
            farNode = (KD2Node<ListStation>) node.getRight();
        } else {
            nearNode = (KD2Node<ListStation>) node.getRight();
            farNode = (KD2Node<ListStation>) node.getLeft();
        }

        findKNearestNeighboursTimeZone(nearNode, x, y, k, !divX, heap, timeZone); // O(log n)

        double farthestDistNow;
        if (heap.size() < k) {
            farthestDistNow = Double.MAX_VALUE;
        } else {
            KD2Node<ListStation> farthest = heap.peek();
            farthestDistNow = Point2D.distanceSq(farthest.getX(), farthest.getY(), x, y);
        } // O(1)

        if (deltaSquared < farthestDistNow) {
            findKNearestNeighboursTimeZone(farNode, x, y, k, !divX, heap, timeZone); // O(log n)
        }
    }


    public Map<ListStation, Double> rangeSearchRadius(double lat, double lon, double radiusKm) { // Complexidade final O(log(n) + s)
        final int METERS_PER_DEGREE_LAT = 111000; // O(1)
        final int METERS_PER_DEGREE_LON = 111000; // O(1)
        final int METERS_PER_KM = 1000; // O(1)

        double radiusMeters = radiusKm * METERS_PER_KM; // O(1)
        double deltaLat = radiusMeters / METERS_PER_DEGREE_LAT; // O(1)
        double deltaLon = radiusMeters / (METERS_PER_DEGREE_LON * Math.cos(Math.toRadians(lat))); // O(1)

        List<ListStation> candidates = rangeSearch(lat - deltaLat, lat + deltaLat, lon - deltaLon, lon + deltaLon); // O(log n)

        Map<ListStation, Double> result = new HashMap<>(); // O(1)
        HaversineDistanceComparator h = new HaversineDistanceComparator(lat, lon); // O(1)

        for (ListStation ls : candidates) { // O(s)     s representa as estações candidatas
            double distMeters = h.haversine(lat, lon, ls.getX(), ls.getY()); // O(1)

            if (distMeters <= radiusKm) { // O(1)
                result.put(ls, distMeters); // O(1)
            }
        }

        return result; // O(1)
    }

    public static class HaversineDistanceComparator implements Comparator<KD2Node<ListStation>> { // complexidade total = O(1)
        private final double queryX;
        private final double queryY;

        private final double EARTH_RADIUS = 6371.0088; // km

        public HaversineDistanceComparator(double queryX, double queryY) { // complexidade total = O(1)
            this.queryX = queryX;
            this.queryY = queryY;
        }

        @Override
        public int compare(KD2Node<ListStation> n1, KD2Node<ListStation> n2) { // complexidade total = O(1)
            double d1 = haversine(n1.getX(), n1.getY(), queryX, queryY);
            double d2 = haversine(n2.getX(), n2.getY(), queryX, queryY);
            return Double.compare(d2, d1);
        }

        public double haversine(double lat1, double lon1, double lat2, double lon2) { // complexidade total = O(1)

            double phi1 = Math.toRadians(lat1);
            double phi2 = Math.toRadians(lat2);
            double dPhi = Math.toRadians(lat2 - lat1);
            double dLambda = Math.toRadians(lon2 - lon1);

            double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2)
                    + Math.cos(phi1) * Math.cos(phi2)
                    * Math.sin(dLambda / 2) * Math.sin(dLambda / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return EARTH_RADIUS * c;
        }
    }
}