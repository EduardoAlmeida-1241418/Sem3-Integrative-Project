package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.Operator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OperatorStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(OperatorStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Operator operator = (Operator) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, operator.getId())) {
                update(connection, operator);
            } else {
                insert(connection, operator);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving operator", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Operator operator = (Operator) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Operator WHERE ID_Operator = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, operator.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting operator", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public Operator findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT ID_Operator, Name, Short_Name
            FROM Operator
            WHERE ID_Operator = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Operator(
                            rs.getString("ID_Operator"),
                            rs.getString("Name"),
                            rs.getString("Short_Name")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding operator", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private boolean exists(DatabaseConnection databaseConnection, String id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Operator WHERE ID_Operator = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, Operator operator) throws SQLException {
        String sql = """
            INSERT INTO Operator
            (ID_Operator, Name, Short_Name)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, operator.getId());
            ps.setString(2, operator.getName());
            ps.setString(3, operator.getShortName());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Operator not saved: ID=" + operator.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Operator NOT saved (ID=" + operator.getId() + ")", e
            );
        }
    }

    private void update(Connection connection, Operator operator) throws SQLException {
        String sql = """
            UPDATE Operator
            SET Name = ?, Short_Name = ?
            WHERE ID_Operator = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, operator.getName());
            ps.setString(2, operator.getShortName());
            ps.setString(3, operator.getId());
            ps.executeUpdate();
        }
    }

    public List<Operator> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Operator> list = new ArrayList<>();

        String sql = """
        SELECT ID_Operator, Name, Short_Name
        FROM Operator
        ORDER BY ID_Operator
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Operator(
                        rs.getString("ID_Operator"),
                        rs.getString("Name"),
                        rs.getString("Short_Name")
                ));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading operators", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }

}
