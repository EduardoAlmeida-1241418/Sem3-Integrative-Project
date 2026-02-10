package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.FacilityStoreInMemory;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;
import pt.ipp.isep.dei.domain.wagonRelated.WagonModel;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WagonStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(WagonStore.class.getName());

    @Override
    public boolean save(DatabaseConnection db, Object object) {
        Wagon wagon = (Wagon) object;
        Connection conn = db.getConnection();

        try {
            if (exists(db, wagon.getWagonID())) update(conn, wagon);
            else insert(conn, wagon);
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving wagon", ex);
            db.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection db, Object object) {
        Wagon wagon = (Wagon) object;
        Connection conn = db.getConnection();

        String sql = "DELETE FROM Wagon WHERE ID_Wagon = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, wagon.getWagonID());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting wagon", ex);
            db.registerError(ex);
            return false;
        }
    }

    @Override
    public Wagon findById(DatabaseConnection db, String id) {
        Connection conn = db.getConnection();

        String sql = """
            SELECT ID_Wagon, Starting_Year_Service,
                   OperatorID_Operator, Wagon_ModelID_Wagon_Model
            FROM Wagon
            WHERE ID_Wagon = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    int wagonId = rs.getInt("ID_Wagon");
                    LocalDate localDate = rs.getDate("Starting_Date_Service").toLocalDate();

                    Date serviceDate = new Date(
                            localDate.getDayOfMonth(),
                            localDate.getMonthValue(),
                            localDate.getYear()
                    );

                    String operator = rs.getString("OperatorID_Operator");
                    int modelId = rs.getInt("Wagon_ModelID_Wagon_Model");

                    WagonModelStore modelStore = new WagonModelStore();
                    WagonModel model = modelStore.findById(db, String.valueOf(modelId));

                    return new Wagon(wagonId, model, operator, serviceDate);
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding wagon", ex);
            db.registerError(ex);
        }
        return null;
    }

    private boolean exists(DatabaseConnection db, int id) throws SQLException {
        Connection conn = db.getConnection();

        String sql = "SELECT 1 FROM Wagon WHERE ID_Wagon = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection conn, Wagon wagon) throws SQLException {
        String sql = """
        INSERT INTO Wagon
        (ID_Wagon, Starting_Date_Service, OperatorID_Operator,
         Wagon_ModelID_Wagon_Model, Start_Facility_ID)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            pt.ipp.isep.dei.domain.Date d = wagon.getServiceDate();

            java.sql.Date sqlDate = java.sql.Date.valueOf(
                    java.time.LocalDate.of(
                            d.getYear(),
                            d.getMonth(),
                            d.getDay()
                    )
            );

            ps.setInt(1, wagon.getWagonID());
            ps.setDate(2, sqlDate);
            ps.setString(3, wagon.getOperator());
            ps.setInt(4, wagon.getWagonModel().getId());
            ps.setInt(5, wagon.getStartFacility().getId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Wagon not saved: ID=" + wagon.getWagonID());
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Wagon NOT saved (ID=" + wagon.getWagonID() + ")", e
            );
        }
    }

    private void update(Connection conn, Wagon wagon) throws SQLException {
        String sql = """
        UPDATE Wagon
        SET Starting_Date_Service = ?, 
            OperatorID_Operator = ?, 
            Wagon_ModelID_Wagon_Model = ?
        WHERE ID_Wagon = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            pt.ipp.isep.dei.domain.Date d = wagon.getServiceDate();

            java.sql.Date sqlDate = java.sql.Date.valueOf(
                    java.time.LocalDate.of(
                            d.getYear(),
                            d.getMonth(),
                            d.getDay()
                    )
            );

            ps.setDate(1, sqlDate);
            ps.setString(2, wagon.getOperator());
            ps.setInt(3, wagon.getWagonModel().getId());
            ps.setInt(4, wagon.getWagonID());

            ps.executeUpdate();
        }
    }

    public List<Wagon> findAll(DatabaseConnection db) {
        Connection conn = db.getConnection();
        List<Wagon> list = new ArrayList<>();

        String sql = """
            SELECT ID_Wagon, Starting_Date_Service,
                   OperatorID_Operator, Wagon_ModelID_Wagon_Model, Start_Facility_ID
            FROM Wagon
            ORDER BY ID_Wagon
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            WagonModelStore modelStore = new WagonModelStore();

            while (rs.next()) {

                int wagonId = rs.getInt("ID_Wagon");
                LocalDate localDate = rs.getDate("Starting_Date_Service").toLocalDate();

                Date serviceDate = new Date(
                        localDate.getDayOfMonth(),
                        localDate.getMonthValue(),
                        localDate.getYear()
                );

                String operator = rs.getString("OperatorID_Operator");
                int modelId = rs.getInt("Wagon_ModelID_Wagon_Model");
                int startFacilityId = rs.getInt("Start_Facility_ID");

                WagonModel model = modelStore.findById(db, String.valueOf(modelId));

                list.add(new Wagon(wagonId, model, operator, serviceDate, new FacilityStoreInMemory().findById(db, String.valueOf(startFacilityId))));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading wagons", ex);
            db.registerError(ex);
        }

        return list;
    }
}
