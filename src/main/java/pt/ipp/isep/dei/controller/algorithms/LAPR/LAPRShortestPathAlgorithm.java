package pt.ipp.isep.dei.controller.algorithms.LAPR;

import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.util.*;

public class LAPRShortestPathAlgorithm {
    private static final double BOOST_DOUBLE_TRACK = 0.8;
    private static final double BOOST_HAS_SIDING = 0.9;


    public List<Facility> findGeneralShortestPath(Route route, List<Facility> facilities, List<RailwayLine> railwayLines) {

        if (route.getFreights().isEmpty()) return List.of();

        List<Facility> path = new ArrayList<>();

        Facility current = route.getFreights().get(0).getOriginFacility();
        path.add(current);

        for (Freight freight : route.getFreights()) {

            List<Facility> partial = shortestPath(current, freight.getDestinationFacility(), facilities, railwayLines);

            if (partial.isEmpty()) return List.of();

            path.addAll(partial.subList(1, partial.size()));
            current = freight.getDestinationFacility();
        }

        return path;
    }



        /* =========================
           Dijkstra principal
           ========================= */

    public List<Facility> shortestPath(Facility origin, Facility destination, List<Facility> allFacilities, List<RailwayLine> allLines) {

        Map<Facility, Integer> dist = new HashMap<>();
        Map<Facility, Facility> prev = new HashMap<>();

        PriorityQueue<Facility> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (Facility f : allFacilities) {
            dist.put(f, Integer.MAX_VALUE);
        }

        dist.put(origin, 0);
        pq.add(origin);

        while (!pq.isEmpty()) {

            Facility u = pq.poll();

            if (u.equals(destination)) {
                break;
            }

            for (RailwayLine line : allLines) {

                Facility v = getOtherEnd(u, line, allFacilities);
                if (v == null) continue;

                int cost = lineCost(line, u, v);
                if (cost == Integer.MAX_VALUE) continue;

                int alt = dist.get(u) + cost;

                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }

        return reconstructPath(prev, origin, destination);
    }

        /* =========================
           Custo de uma RailwayLine
           ========================= */

    private int lineCost(RailwayLine line, Facility from, Facility to) {

        List<RailwayLineSegment> segments =
                line.getSegmentsInOrder(from, to);

        if (segments == null || segments.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        int cost = 0;
        for (RailwayLineSegment s : segments) {
            cost += segmentCost(s);
        }

        return cost;
    }

    private int segmentCost(RailwayLineSegment segment) {
        int cost = segment.getLength();

        // Atribuir Boost's
        if (segment.getNumberTracks() >= 2) {
            cost *= BOOST_DOUBLE_TRACK;
        } else if (segment.hasSiding()) {
            cost *= BOOST_HAS_SIDING;
        }

        return cost;
    }


        /* =========================
           Descobrir o outro extremo
           ========================= */

    private Facility getOtherEnd(Facility current, RailwayLine line, List<Facility> allFacilities) {

        if (line.getStartFacilityId() == current.getId()) {
            return findFacilityById(line.getEndFacilityId(), allFacilities);
        }

        if (line.getEndFacilityId() == current.getId()) {
            return findFacilityById(line.getStartFacilityId(), allFacilities);
        }

        return null;
    }

    private Facility findFacilityById(int id, List<Facility> facilities) {
        for (Facility f : facilities) {
            if (f.getId() == id) return f;
        }
        return null;
    }

        /* =========================
           Reconstrução do caminho
           ========================= */

    private List<Facility> reconstructPath(Map<Facility, Facility> prev, Facility origin, Facility destination) {

        List<Facility> path = new LinkedList<>();

        if (!destination.equals(origin) && !prev.containsKey(destination)) {
            return List.of();
        }

        Facility step = destination;
        while (step != null) {
            path.add(0, step);
            step = prev.get(step);
        }

        return path;
    }

    public int totalPathCost(List<Facility> path, List<RailwayLine> allLines){
        if(path == null || path.size() < 2) return 0;

        int totalCost = 0;

        for(int i = 0; i < path.size() - 1; i++){
            Facility from = path.get(i);
            Facility to = path.get(i + 1);

            RailwayLine line = findLineBetween(from, to, allLines);
            if(line == null) return Integer.MAX_VALUE;

            int cost = lineCost(line, from, to);
            if(cost == Integer.MAX_VALUE) return Integer.MAX_VALUE;

            totalCost += cost;
        }

        return totalCost;
    }

    private RailwayLine findLineBetween(Facility a, Facility b, List<RailwayLine> allLines){
        for(RailwayLine line : allLines){
            if((line.getStartFacilityId() == a.getId() && line.getEndFacilityId() == b.getId()) ||
                    (line.getStartFacilityId() == b.getId() && line.getEndFacilityId() == a.getId())){
                return line;
            }
        }
        return null;
    }

}