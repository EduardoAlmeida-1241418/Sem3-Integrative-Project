package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.wagonRelated.WagonModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WagonModelStore implements Persistable {

    private static final Logger LOGGER =
            Logger.getLogger(WagonModelStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        WagonModel model = (WagonModel) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, model.getId())) {
                update(connection, model);
            } else {
                insert(connection, model);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving WagonModel", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        WagonModel model = (WagonModel) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Wagon_Model WHERE ID_Wagon_Model = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, model.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting WagonModel", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public WagonModel findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT 
                ID_Wagon_Model, Name, Payload, Volume_Capacity, 
                Number_Wheels, Max_Speed, MakerID_Maker,
                DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type
            FROM Wagon_Model
            WHERE ID_Wagon_Model = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new WagonModel(
                            rs.getInt("ID_Wagon_Model"),
                            rs.getString("Name"),
                            rs.getDouble("Payload"),
                            rs.getDouble("Volume_Capacity"),
                            rs.getInt("Number_Wheels"),
                            rs.getInt("Max_Speed"),
                            rs.getInt("MakerID_Maker"),
                            rs.getInt("DimensionsID_Dimensions"),
                            rs.getInt("Wagon_TypeID_Wagon_Type")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding WagonModel", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private boolean exists(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Wagon_Model WHERE ID_Wagon_Model = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, WagonModel model) throws SQLException {
        String sql = """
            INSERT INTO Wagon_Model
            (ID_Wagon_Model, Name, Payload, Volume_Capacity,
             Number_Wheels, Max_Speed, MakerID_Maker,
             DimensionsID_Dimensions, Wagon_TypeID_Wagon_Type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, model.getId());
            ps.setString(2, model.getName());
            ps.setDouble(3, model.getPayload());
            ps.setDouble(4, model.getVolumeCapacity());
            ps.setInt(5, model.getnWheels());
            ps.setInt(6, model.getMaxSpeed());
            ps.setInt(7, model.getMakerId());
            ps.setInt(8, model.getDimensionsId());
            ps.setInt(9, model.getWagonTypeId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Wagon Model not saved: ID=" + model.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Wagon Model NOT saved (ID=" + model.getId() + ")", e
            );
        }
    }

    private void update(Connection connection, WagonModel model) throws SQLException {
        String sql = """
            UPDATE Wagon_Model
            SET Name = ?, Payload = ?, Volume_Capacity = ?,
                Number_Wheels = ?, Max_Speed = ?, MakerID_Maker = ?,
                DimensionsID_Dimensions = ?, Wagon_TypeID_Wagon_Type = ?
            WHERE ID_Wagon_Model = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, model.getName());
            ps.setDouble(2, model.getPayload());
            ps.setDouble(3, model.getVolumeCapacity());
            ps.setInt(4, model.getnWheels());
            ps.setInt(5, model.getMaxSpeed());
            ps.setInt(6, model.getMakerId());
            ps.setInt(7, model.getDimensionsId());
            ps.setInt(8, model.getWagonTypeId());
            ps.setInt(9, model.getId());
            ps.executeUpdate();
        }
    }

    public List<WagonModel> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<WagonModel> list = new ArrayList<>();

        String sql = """
        SELECT 
            ID_Wagon_Model, 
            Name, 
            Payload, 
            Volume_Capacity, 
            Number_Wheels, 
            Max_Speed, 
            MakerID_Maker,
            DimensionsID_Dimensions, 
            Wagon_TypeID_Wagon_Type
        FROM Wagon_Model
        ORDER BY ID_Wagon_Model
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new WagonModel(
                        rs.getInt("ID_Wagon_Model"),
                        rs.getString("Name"),
                        rs.getDouble("Payload"),
                        rs.getDouble("Volume_Capacity"),
                        rs.getInt("Number_Wheels"),
                        rs.getInt("Max_Speed"),
                        rs.getInt("MakerID_Maker"),
                        rs.getInt("DimensionsID_Dimensions"),
                        rs.getInt("Wagon_TypeID_Wagon_Type")
                ));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading WagonModels", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }

}
