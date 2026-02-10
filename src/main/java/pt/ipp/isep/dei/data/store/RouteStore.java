package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;
import pt.ipp.isep.dei.domain.transportationRelated.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteStore implements Persistable {

    /* =========================
       SAVE / DELETE
       ========================= */

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Route route = (Route) obj;
        Connection conn = db.getConnection();

        try {
            insertRoute(conn, route);
            updateFreightWithRoute(conn, route);
            conn.commit();
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Route route = (Route) obj;

        try (PreparedStatement ps =
                     db.getConnection().prepareStatement(
                             "DELETE FROM Route WHERE ID_Route = ?")) {

            ps.setInt(1, route.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    /* =========================
       FIND BY ID
       ========================= */

    @Override
    public Route findById(DatabaseConnection db, String id) {
        Connection conn = db.getConnection();

        String sql = """
                    SELECT ID_Route, PathID_Path
                    FROM Route
                    WHERE ID_Route = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    int routeId = rs.getInt("ID_Route");
                    int pathId = rs.getInt("PathID_Path");

                    Path path = new Path(pathId);

                    List<Freight> freights = loadFreights(db, routeId);

                    RouteType type =
                            freights.size() <= 1
                                    ? RouteType.SIMPLE
                                    : RouteType.COMPLEX;

                    return new Route(freights, path, routeId);
                }
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return null;
    }

    /* =========================
       FIND ALL
       ========================= */

    public List<Route> findAll(DatabaseConnection db) {
        Connection conn = db.getConnection();
        List<Route> routes = new ArrayList<>();

        String sql = """
                    SELECT ID_Route, PathID_Path
                    FROM Route
                    ORDER BY ID_Route
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int routeId = rs.getInt("ID_Route");
                int pathId = rs.getInt("PathID_Path");

                Path path;
                if (pathId != 0) {
                    path = new Path(pathId);
                } else {
                    path = null; // or handle as appropriate
                }

                List<Freight> freights = loadFreights(db, routeId);

                RouteType type =
                        freights.size() <= 1
                                ? RouteType.SIMPLE
                                : RouteType.COMPLEX;

                routes.add(new Route(freights, path, routeId));
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return routes;
    }

    private void insertRoute(Connection conn, Route r) throws SQLException {
        String sql = """
                    INSERT INTO Route (ID_Route)
                    VALUES (?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Route not saved: ID=" + r.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Route NOT saved (ID=" + r.getId() + ")", e
            );
        }
    }

    private void updateFreightWithRoute(Connection conn, Route route) throws SQLException {
        for (Freight freight : route.getFreights()) {
            String sql = """
                    UPDATE Freight
                    SET RouteID_Route = ?
                    WHERE ID_Freight = ?
                    """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, route.getId());
                ps.setInt(2, freight.getId());

                int rows = ps.executeUpdate();

                if (rows != 1) {
                    System.out.println("Freight not saved: ID=" + freight.getId());
                }
            } catch (SQLException e) {
                throw new RuntimeException(
                        "Freight NOT saved (ID=" + freight.getId() + ")", e
                );
            }
        }
    }

    private void updateRoute(Connection conn, Route r) throws SQLException {
        if (r.getPath() == null) {
            String sql = """
                        UPDATE Route
                        SET PathID_Path = NULL
                        WHERE ID_Route = ?
                    """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }
        }

        String sql = """
                    UPDATE Route
                    SET PathID_Path = ?
                    WHERE ID_Route = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getPath().getId());
            ps.setInt(2, r.getId());
            ps.executeUpdate();
        }
    }

    private List<Freight> loadFreights(DatabaseConnection db, int routeId) {
        List<Freight> freights = new ArrayList<>();

        String sql = """
                    SELECT ID_Freight
                    FROM Freight
                    WHERE RouteID_Route = ?
                    ORDER BY ID_Freight
                """;

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, routeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    freights.add(new Freight(rs.getInt("ID_Freight")));
                }
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return freights;
    }

    public boolean deleteAll(DatabaseConnection db) {
        Connection conn = db.getConnection();

        String unlinkFreights = "UPDATE Freight SET RouteID_Route = NULL";
        String deleteRoute = "DELETE FROM Route";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(unlinkFreights);
            stmt.executeUpdate(deleteRoute);
            conn.commit();
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            try { conn.rollback(); } catch (SQLException ignored) {}
            return false;
        }
    }
}
