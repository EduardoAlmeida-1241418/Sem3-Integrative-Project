package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.ActualDatabaseConnection;
import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.FacilityType;
import pt.ipp.isep.dei.domain.transportationRelated.Path;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PathStore implements Persistable {

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Path path = (Path) obj;
        Connection conn = db.getConnection();

        try {
            insertPath(conn, path);
            updateRouteWithPath(conn, path);
            insertFacilities(conn, path);
            conn.commit();
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            try { conn.rollback(); } catch (SQLException ignored) {}
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Path path = (Path) obj;
        Connection conn = db.getConnection();

        try {
            deleteFacilities(conn, path.getId());
            deletePath(conn, path.getId());
            conn.commit();
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            try { conn.rollback(); } catch (SQLException ignored) {}
            return false;
        }
    }

    @Override
    public Path findById(DatabaseConnection db, String id) {
        Connection conn = db.getConnection();

        String sql = """
                SELECT ID_Path
                FROM Path
                WHERE ID_Path = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Path path = new Path(rs.getInt("ID_Path"));
                    path.setFacilities(loadFacilities(db, path.getId()));
                    return path;
                }
            }
        } catch (SQLException e) {
            db.registerError(e);
        }

        return null;
    }

    public List<Path> findAll(DatabaseConnection db) {
        Connection conn = db.getConnection();
        List<Path> list = new ArrayList<>();

        String sql = """
                SELECT ID_Path
                FROM Path
                ORDER BY ID_Path
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Path path = new Path(rs.getInt("ID_Path"));
                path.setFacilities(loadFacilities(db, path.getId()));
                list.add(path);
            }
        } catch (SQLException e) {
            db.registerError(e);
        }

        return list;
    }

    private void insertPath(Connection conn, Path path) throws SQLException {
        String sql = "INSERT INTO Path (ID_Path) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, path.getId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Path not saved: ID=" + path.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Path NOT saved (ID=" + path.getId() + ")", e
            );
        }
    }

    private void updateRouteWithPath(Connection conn, Path path) throws SQLException {
        for (Route route : new RouteStoreInMemory().findAll()) {
            if (route.getPath() != null && route.getPath().getId() == path.getId()) {
                String sql = """
                        UPDATE Route
                        SET PathID_Path = ?
                        WHERE ID_Route = ?
                        """;

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, path.getId());
                    ps.setInt(2, route.getId());

                    int rows = ps.executeUpdate();

                    if (rows != 1) {
                        System.out.println("Route not saved: ID=" + route.getId());
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(
                            "Route NOT saved (ID=" + route.getId() + ")", e
                    );
                }
            }
        }
    }

    private void deletePath(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM Path WHERE ID_Path = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void insertFacilities(Connection conn, Path path) throws SQLException {
        String sql = """
                INSERT INTO Path_Facility
                (PathID_Path, FacilityID_Facility, Position_Facility_Path)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int position = 0;

            for (Facility facility : path.getFacilities()) {
                ps.setInt(1, path.getId());
                ps.setInt(2, facility.getId());
                ps.setInt(3, position++);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteFacilities(Connection conn, int pathId) throws SQLException {
        String sql = "DELETE FROM Path_Facility WHERE PathID_Path = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pathId);
            ps.executeUpdate();
        }
    }

    private List<Facility> loadFacilities(DatabaseConnection db, int pathId) {
        Connection conn = db.getConnection();
        List<Facility> list = new ArrayList<>();

        String sql = """
                SELECT f.ID_Facility, f.Name
                FROM Path_Facility pf
                JOIN Facility f ON f.ID_Facility = pf.FacilityID_Facility
                WHERE pf.PathID_Path = ?
                ORDER BY pf.Position_Facility_Path
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pathId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Facility(
                            rs.getInt("ID_Facility"),
                            rs.getString("Name"),
                            false,
                            FacilityType.STATION
                    ));
                }
            }
        } catch (SQLException e) {
            db.registerError(e);
        }

        return list;
    }

    public boolean deleteAll(DatabaseConnection db) {
        Connection conn = db.getConnection();

        String unlinkRoutes = "UPDATE Route SET PathID_Path = NULL";
        String deleteFacility = "DELETE FROM Path_Facility";
        String deletePath = "DELETE FROM Path";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(unlinkRoutes);
            stmt.executeUpdate(deleteFacility);
            stmt.executeUpdate(deletePath);
            conn.commit();
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            try { conn.rollback(); } catch (SQLException ignored) {}
            return false;
        }
    }
}
