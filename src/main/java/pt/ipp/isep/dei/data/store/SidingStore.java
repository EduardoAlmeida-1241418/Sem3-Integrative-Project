package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.trackRelated.Siding;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SidingStore implements Persistable {

    private static final Logger LOGGER =
            Logger.getLogger(SidingStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Siding siding = (Siding) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, siding.getId())) {
                update(connection, siding);
            } else {
                insert(connection, siding);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving siding", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }


    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Siding siding = (Siding) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Siding WHERE ID_Siding = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, siding.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting siding", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public Siding findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
        SELECT ID_Siding, Position, Length
        FROM Siding
        WHERE ID_Siding = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Siding(
                            rs.getInt("ID_Siding"),
                            rs.getInt("Position"),
                            rs.getInt("Length")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding siding", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }


    private boolean exists(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Siding WHERE ID_Siding = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, Siding siding) throws SQLException {

        String sql = """
        INSERT INTO Siding (ID_Siding, Position, Length)
        VALUES (?, ?, ?)
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, siding.getId());
            ps.setInt(2, siding.getPosition());
            ps.setInt(3, siding.getLength());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Siding not saved: ID=" + siding.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Siding NOT saved (ID=" + siding.getId() + ")", e
            );
        }
    }


    private void update(Connection connection, Siding siding) throws SQLException {

        String sql = """
        UPDATE Siding
        SET Position = ?, Length = ?
        WHERE ID_Siding = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, siding.getPosition());
            ps.setInt(2, siding.getLength());
            ps.setInt(3, siding.getId());
            ps.executeUpdate();
        }
    }


    public List<Siding> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Siding> list = new ArrayList<>();

        String sql = """
        SELECT ID_Siding, Position, Length
        FROM Siding
        ORDER BY ID_Siding
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Siding(
                        rs.getInt("ID_Siding"),
                        rs.getInt("Position"),
                        rs.getInt("Length")
                ));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading sidings", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }
}
