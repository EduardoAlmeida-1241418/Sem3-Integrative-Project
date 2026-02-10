package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.wagonRelated.WagonType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WagonTypeStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(WagonTypeStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        WagonType wagonType = (WagonType) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, wagonType.getId())) {
                update(connection, wagonType);
            } else {
                insert(connection, wagonType);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving wagon type", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        WagonType wagonType = (WagonType) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Wagon_Type WHERE ID_Wagon_Type = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, wagonType.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting wagon type", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public WagonType findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT ID_Wagon_Type, Name
            FROM Wagon_Type
            WHERE ID_Wagon_Type = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    WagonType wt = new WagonType(
                            rs.getInt("ID_Wagon_Type"),
                            rs.getString("Name")
                    );

                    List<CargoType> cargoTypes =
                            findCargoTypesForWagonType(databaseConnection, wt.getId());

                    wt.setCargoTypes(cargoTypes);

                    return wt;
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding wagon type", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    public List<CargoType> findCargoTypesForWagonType(DatabaseConnection db, int wagonTypeId) {
        Connection conn = db.getConnection();
        List<CargoType> list = new ArrayList<>();

        String sql = """
            SELECT Cargo_TypeID_Cargo_Type
            FROM Wagon_Type_Cargo_Type
            WHERE Wagon_TypeID_Wagon_Type = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, wagonTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cargoId = rs.getInt("Cargo_TypeID_Cargo_Type");

                    for (CargoType ct : CargoType.values()) {
                        if (ct.getId() == cargoId) {
                            list.add(ct);
                            break;
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading cargo types", ex);
            db.registerError(ex);
        }

        return list;
    }


    private boolean exists(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Wagon_Type WHERE ID_Wagon_Type = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, WagonType wagonType) throws SQLException {
        String sql = """
            INSERT INTO Wagon_Type
            (ID_Wagon_Type, Name)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, wagonType.getId());
            ps.setString(2, wagonType.getName());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Wagon Type not saved: ID=" + wagonType.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Wagon Type NOT saved (ID=" + wagonType.getId() + ")", e
            );
        }
    }

    private void update(Connection connection, WagonType wagonType) throws SQLException {
        String sql = """
            UPDATE Wagon_Type
            SET Name = ?
            WHERE ID_Wagon_Type = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, wagonType.getName());
            ps.setInt(2, wagonType.getId());
            ps.executeUpdate();
        }
    }

    public List<WagonType> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<WagonType> list = new ArrayList<>();

        String sql = """
            SELECT ID_Wagon_Type, Name
            FROM Wagon_Type
            ORDER BY ID_Wagon_Type
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                WagonType wt = new WagonType(
                        rs.getInt("ID_Wagon_Type"),
                        rs.getString("Name")
                );

                List<CargoType> cargoTypes =
                        findCargoTypesForWagonType(databaseConnection, wt.getId());

                wt.setCargoTypes(cargoTypes);

                list.add(wt);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading wagon types", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }
}
