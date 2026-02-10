package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.Maker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MakerStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(MakerStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Maker maker = (Maker) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, maker.getId())) {
                update(connection, maker);
            } else {
                insert(connection, maker);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving maker", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Maker maker = (Maker) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Maker WHERE ID_Maker = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maker.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting maker", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public Maker findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT ID_Maker, Name
            FROM Maker
            WHERE ID_Maker = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Maker(
                            rs.getInt("ID_Maker"),
                            rs.getString("Name")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding maker", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private boolean exists(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Maker WHERE ID_Maker = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, Maker maker) throws SQLException {
        String sql = """
            INSERT INTO Maker
            (ID_Maker, Name)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maker.getId());
            ps.setString(2, maker.getName());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Maker not saved: ID=" + maker.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Maker NOT saved (ID=" + maker.getId() + ")", e
            );
        }
    }

    private void update(Connection connection, Maker maker) throws SQLException {
        String sql = """
            UPDATE Maker
            SET Name = ?
            WHERE ID_Maker = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, maker.getName());
            ps.setInt(2, maker.getId());
            ps.executeUpdate();
        }
    }

    public List<Maker> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Maker> list = new ArrayList<>();

        String sql = """
            SELECT ID_Maker, Name
            FROM Maker
            ORDER BY ID_Maker
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Maker(
                        rs.getInt("ID_Maker"),
                        rs.getString("Name")
                ));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading makers", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }
}
