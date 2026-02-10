package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class LocomotiveModelStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(LocomotiveModelStore.class.getName());

    @Override
    public boolean save(DatabaseConnection db, Object o) {
        LocomotiveModel m = (LocomotiveModel) o;

        try {
            if (exists(db, m.getId())) update(db.getConnection(), m);
            else insert(db.getConnection(), m);

            return true;
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving locomotive model", e);
            db.registerError((SQLException) e);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection db, Object o) {
        LocomotiveModel m = (LocomotiveModel) o;
        String sql = "DELETE FROM Locomotive_Model WHERE ID_Locomotive_Model = ?";

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, m.getId());
            ps.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    @Override
    public LocomotiveModel findById(DatabaseConnection db, String id) {

        String sql = """
        SELECT lm.*,
               fd.Fuel_Capacity AS Diesel_Capacity,
               fe.Voltage AS Electric_Voltage,
               fe.Frequency AS Electric_Frequency
        FROM Locomotive_Model lm
        LEFT JOIN Fuel_Diesel fd ON fd.Fuel_TypeID_Fuel_Type = lm.Fuel_TypeID_Fuel_Type
        LEFT JOIN Fuel_Electric fe ON fe.Fuel_TypeID_Fuel_Type = lm.Fuel_TypeID_Fuel_Type
        WHERE ID_Locomotive_Model = ?
        """;

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildModel(db, rs);

        } catch (SQLException e) {
            db.registerError(e);
        }

        return null;
    }

    public List<LocomotiveModel> findAll(DatabaseConnection db) {
        List<LocomotiveModel> list = new ArrayList<>();

        String sql = """
        SELECT lm.*,
               fd.Fuel_Capacity AS Diesel_Capacity,
               fe.Voltage AS Electric_Voltage,
               fe.Frequency AS Electric_Frequency
        FROM Locomotive_Model lm
        LEFT JOIN Fuel_Diesel fd ON fd.Fuel_TypeID_Fuel_Type = lm.Fuel_TypeID_Fuel_Type
        LEFT JOIN Fuel_Electric fe ON fe.Fuel_TypeID_Fuel_Type = lm.Fuel_TypeID_Fuel_Type
        ORDER BY lm.ID_Locomotive_Model
        """;

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(buildModel(db, rs));

        } catch (SQLException e) {
            db.registerError(e);
        }

        return list;
    }

    private boolean exists(DatabaseConnection db, int id) throws SQLException {
        String sql = "SELECT 1 FROM Locomotive_Model WHERE ID_Locomotive_Model = ?";

        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private LocomotiveModel buildModel(DatabaseConnection db, ResultSet rs) throws SQLException {

        MakerStore makerStore = new MakerStore();
        DimensionsStore dimStore = new DimensionsStore();
        TrackGaugeStore tgStore = new TrackGaugeStore();

        int makerId = rs.getInt("MakerID_Maker");
        int dimId   = rs.getInt("DimensionsID_Dimensions");
        int fuelId  = rs.getInt("Fuel_TypeID_Fuel_Type");

        Maker maker = makerStore.findById(db, String.valueOf(makerId));
        Dimensions dim = dimStore.findById(db, String.valueOf(dimId));

        FuelType fuelType = FuelType.fromId(fuelId);

        int fuelCapacity = rs.getInt("Diesel_Capacity");
        int voltage = rs.getInt("Electric_Voltage");
        int frequency = rs.getInt("Electric_Frequency");

        List<TrackGauge> gauges = new ArrayList<>();

        String sqlTG = """
            SELECT Track_GaugeID_Track_Gauge
            FROM Locomotive_Model_Track_Gauge
            WHERE Locomotive_ModelID_Locomotive_Model = ?
        """;

        PreparedStatement ps = db.getConnection().prepareStatement(sqlTG);
        ps.setInt(1, rs.getInt("ID_Locomotive_Model"));

        ResultSet tgRS = ps.executeQuery();
        while (tgRS.next()) {
            TrackGauge g = tgStore.findById(db, String.valueOf(tgRS.getInt(1)));
            if (g != null) gauges.add(g);
        }

        return new LocomotiveModel(
                rs.getInt("ID_Locomotive_Model"),
                rs.getString("Name"),
                rs.getInt("Power"),
                rs.getInt("Maximum_Weight"),
                rs.getDouble("Acceleration"),
                rs.getInt("Number_Wheels"),
                rs.getInt("Max_Speed"),
                rs.getDouble("Operational_Speed"),
                rs.getInt("Traction"),
                maker,
                dim,
                fuelType,
                gauges,
                fuelCapacity,
                voltage,
                frequency
        );
    }

    // ----------------------- INSERT / UPDATE ------------------------------

    private void insert(Connection conn, LocomotiveModel m) throws SQLException {
        String sql = """
            INSERT INTO Locomotive_Model
            (ID_Locomotive_Model, Name, Power, Maximum_Weight, Acceleration,
             Number_Wheels, Max_Speed, Operational_Speed, Traction,
             MakerID_Maker, DimensionsID_Dimensions, Fuel_TypeID_Fuel_Type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            fillBase(ps, m);
            ps.executeUpdate();

            updateGauges(conn, m);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Locomotive Model NOT saved (ID=" + m.getId() + ")", e
            );
        }
    }

    private void update(Connection conn, LocomotiveModel m) throws SQLException {
        String sql = """
            UPDATE Locomotive_Model
            SET Name=?, Power=?, Maximum_Weight=?, Acceleration=?,
                Number_Wheels=?, Max_Speed=?, Operational_Speed=?,
                Traction=?, MakerID_Maker=?, DimensionsID_Dimensions=?,
                Fuel_TypeID_Fuel_Type=?
            WHERE ID_Locomotive_Model=?
        """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, m.getName());
        ps.setInt(2, m.getPower());
        ps.setInt(3, m.getMaximumWeight());
        ps.setDouble(4, m.getAcceleration());
        ps.setInt(5, m.getNumberWheels());
        ps.setInt(6, m.getMaxSpeed());
        ps.setDouble(7, m.getOperationalSpeed());
        ps.setInt(8, m.getTraction());
        ps.setInt(9, m.getMaker().getId());
        ps.setInt(10, m.getDimensions().getId());
        ps.setInt(11, m.getFuelType().getId());
        ps.setInt(12, m.getId());

        int rows = ps.executeUpdate();

        if (rows != 1) {
            System.out.println("Locomotive Model not saved: ID=" + m.getId());
        }

        updateGauges(conn, m);
    }

    private void fillBase(PreparedStatement ps, LocomotiveModel m) throws SQLException {
        ps.setInt(1, m.getId());
        ps.setString(2, m.getName());
        ps.setInt(3, m.getPower());
        ps.setInt(4, m.getMaximumWeight());
        ps.setDouble(5, m.getAcceleration());
        ps.setInt(6, m.getNumberWheels());
        ps.setInt(7, m.getMaxSpeed());
        ps.setDouble(8, m.getOperationalSpeed());
        ps.setInt(9, m.getTraction());
        ps.setInt(10, m.getMaker().getId());
        ps.setInt(11, m.getDimensions().getId());
        ps.setInt(12, m.getFuelType().getId());
    }

    private void updateGauges(Connection conn, LocomotiveModel m) throws SQLException {

        PreparedStatement del = conn.prepareStatement(
                "DELETE FROM Locomotive_Model_Track_Gauge WHERE Locomotive_ModelID_Locomotive_Model = ?"
        );
        del.setInt(1, m.getId());
        del.executeUpdate();

        PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO Locomotive_Model_Track_Gauge VALUES (?, ?)"
        );

        for (TrackGauge g : m.getTrackGauges()) {
            ins.setInt(1, m.getId());
            ins.setInt(2, g.getId());
            ins.executeUpdate();
        }
    }
}
