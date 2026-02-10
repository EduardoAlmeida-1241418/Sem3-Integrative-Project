package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.Building;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildingStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(BuildingStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Building building = (Building) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, building.getId())) {
                update(connection, building);
            } else {
                insert(connection, building);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving building", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Building building = (Building) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Building WHERE ID_Building = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, building.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting building", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public Building findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT ID_Building, Name
            FROM Building
            WHERE ID_Building = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Building(
                            rs.getString("ID_Building"),
                            rs.getString("Name")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding building", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private boolean exists(DatabaseConnection databaseConnection, String id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Building WHERE ID_Building = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, Building building) throws SQLException {
        String sql = """
            INSERT INTO Building
            (ID_Building, Name)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, building.getId());
            ps.setString(2, building.getName());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Building not saved: ID=" + building.getId());
            }
        }  catch (SQLException e) {
            throw new RuntimeException(
                    "Building NOT saved (ID=" + building.getId() + ")", e
            );
        }
    }

    private void update(Connection connection, Building building) throws SQLException {
        String sql = """
            UPDATE Building
            SET Name = ?
            WHERE ID_Building = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, building.getName());
            ps.setString(2, building.getId());
            ps.executeUpdate();
        }
    }

    public List<Building> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Building> list = new ArrayList<>();

        String sql = """
            SELECT ID_Building, Name
            FROM Building
            ORDER BY ID_Building
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Building(
                        rs.getString("ID_Building"),
                        rs.getString("Name")
                ));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading buildings", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }
}
