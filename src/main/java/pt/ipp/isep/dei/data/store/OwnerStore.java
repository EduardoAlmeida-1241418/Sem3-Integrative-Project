package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.Owner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OwnerStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(OwnerStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Owner owner = (Owner) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, owner.getVatNumber())) {
                update(connection, owner);
            } else {
                insert(connection, owner);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving owner", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Owner owner = (Owner) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Owner WHERE ID_Owner = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.getVatNumber());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting owner", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public Owner findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT ID_Owner, Name, Short_Name
            FROM Owner
            WHERE ID_Owner = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Owner(
                            rs.getString("ID_Owner"),
                            rs.getString("Name"),
                            rs.getString("Short_Name")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding owner", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private boolean exists(DatabaseConnection databaseConnection, String id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Owner WHERE ID_Owner = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, Owner owner) throws SQLException {
        String sql = """
            INSERT INTO Owner
            (ID_Owner, Name, Short_Name)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.getVatNumber());
            ps.setString(2, owner.getName());
            ps.setString(3, owner.getShortName());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Owner not saved: ID=" + owner.getVatNumber());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Owner NOT saved (ID=" + owner.getVatNumber() + ")", e
            );
        }
    }

    private void update(Connection connection, Owner owner) throws SQLException {
        String sql = """
            UPDATE Owner
            SET Name = ?, Short_Name = ?
            WHERE ID_Owner = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.getName());
            ps.setString(2, owner.getShortName());
            ps.setString(3, owner.getVatNumber());
            ps.executeUpdate();
        }
    }

    public List<Owner> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Owner> list = new ArrayList<>();

        String sql = """
            SELECT ID_Owner, Name, Short_Name
            FROM Owner
            ORDER BY ID_Owner
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Owner(
                        rs.getString("ID_Owner"),
                        rs.getString("Name"),
                        rs.getString("Short_Name")
                ));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading owners", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }
}
