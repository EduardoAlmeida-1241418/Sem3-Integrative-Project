package pt.ipp.isep.dei.domain.transportationRelated;

import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private RouteStoreInMemory routeStoreInMemory = new RouteStoreInMemory();

    private int id;
    private RouteType type;   // can be simple or complex
    private List<Freight> freights;
    private Path path;

    /**
     * Constructor that autogenerates the route ID.
     */
    public Route(RouteType type, List<Freight> freights) {
        this.id = routeStoreInMemory.getNextId();
        this.type = type;

        this.freights = new ArrayList<>(freights);
    }

    public Route(List<Freight> freights) {
        this.id = routeStoreInMemory.getNextId();

        this.freights = new ArrayList<>(freights);

        RouteType type;
        if (freights.size() == 1) {
            type = RouteType.SIMPLE;
        } else {
            type = RouteType.COMPLEX;
        }

        this.type = type;
    }

    public Route(List<Freight> freights, Path path, int id) {
        this.freights = new ArrayList<>(freights);
        this.path = path;
        this.id = id;

        if (routeStoreInMemory.exists(id)) {
            throw new IllegalArgumentException("Route ID already exists.");
        }

        RouteType type;
        if (freights.size() == 1) {
            type = RouteType.SIMPLE;
        } else {
            type = RouteType.COMPLEX;
        }

        this.type = type;
    }

    /**
     * Constructor that accepts a fixed ID.
     */
    public Route(int id, RouteType type, List<Freight> freights) {
        this.id = id;

        if (routeStoreInMemory.exists(id)) {
            throw new IllegalArgumentException("Route ID already exists.");
        }

        this.type = type;

        this.freights = new ArrayList<>(freights);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }

    public List<Freight> getFreights() {
        return freights;
    }

    public void setFreights(List<Freight> freights) {
        this.freights = freights;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
