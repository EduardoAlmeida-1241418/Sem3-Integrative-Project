package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.Dimensions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DimensionsStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(DimensionsStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Dimensions dim = (Dimensions) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, dim.getId())) {
                update(connection, dim);
            } else {
                insert(connection, dim);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving dimensions", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Dimensions dim = (Dimensions) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Dimensions WHERE ID_Dimensions = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dim.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting dimensions", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public Dimensions findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT ID_Dimensions, Length, Width, Height, Weight_Tare
            FROM Dimensions
            WHERE ID_Dimensions = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Dimensions(
                            rs.getInt("ID_Dimensions"),
                            rs.getDouble("Length"),
                            rs.getDouble("Width"),
                            rs.getDouble("Height"),
                            rs.getDouble("Weight_Tare")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding dimensions", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private boolean exists(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Dimensions WHERE ID_Dimensions = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, Dimensions dim) throws SQLException {
        String sql = """
            INSERT INTO Dimensions
            (ID_Dimensions, Length, Width, Height, Weight_Tare)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dim.getId());
            ps.setDouble(2, dim.getLength());
            ps.setDouble(3, dim.getWidth());
            ps.setDouble(4, dim.getHeight());
            ps.setDouble(5, dim.getWeightTare());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Dimensions not saved: ID=" + dim.getId());
            }
        }  catch (SQLException e) {
            throw new RuntimeException(
                    "Dimensions NOT saved (ID=" + dim.getId() + ")", e
            );
        }
    }

    private void update(Connection connection, Dimensions dim) throws SQLException {
        String sql = """
            UPDATE Dimensions
            SET Length = ?, Width = ?, Height = ?, Weight_Tare = ?
            WHERE ID_Dimensions = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, dim.getLength());
            ps.setDouble(2, dim.getWidth());
            ps.setDouble(3, dim.getHeight());
            ps.setDouble(4, dim.getWeightTare());
            ps.setInt(5, dim.getId());
            ps.executeUpdate();
        }
    }

    public List<Dimensions> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Dimensions> list = new ArrayList<>();

        String sql = """
            SELECT ID_Dimensions, Length, Width, Height, Weight_Tare
            FROM Dimensions
            ORDER BY ID_Dimensions
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Dimensions(
                        rs.getInt("ID_Dimensions"),
                        rs.getDouble("Length"),
                        rs.getDouble("Width"),
                        rs.getDouble("Height"),
                        rs.getDouble("Weight_Tare")
                ));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading dimensions", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }
}
