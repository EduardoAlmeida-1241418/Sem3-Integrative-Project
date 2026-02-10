package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.FacilityStoreInMemory;
import pt.ipp.isep.dei.data.memory.LocomotiveModelStoreInMemory;
import pt.ipp.isep.dei.data.memory.OperatorStoreInMemory;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.Operator;
import pt.ipp.isep.dei.domain.trainRelated.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LocomotiveStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(LocomotiveStore.class.getName());

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Locomotive l = (Locomotive) obj;
        Connection conn = db.getConnection();

        try {
            if (exists(db, l.getId())) update(conn, l);
            else insert(conn, l);
            return true;

        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Locomotive l = (Locomotive) obj;
        Connection conn = db.getConnection();

        String sql = "DELETE FROM Locomotive WHERE ID_Locomotive = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, l.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    @Override
    public Locomotive findById(DatabaseConnection db, String id) {
        Connection conn = db.getConnection();
        LocomotiveModelStoreInMemory modelStore = new LocomotiveModelStoreInMemory();
        OperatorStoreInMemory opStore = new OperatorStoreInMemory();

        String sql = """
        SELECT ID_Locomotive, Name, Starting_Date_Service,
               Locomotive_ModelID_Locomotive_Model, OperatorID_Operator
        FROM Locomotive
        WHERE ID_Locomotive = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    java.sql.Date sqlDate = rs.getDate("Starting_Date_Service");

                    pt.ipp.isep.dei.domain.Date serviceDate =
                            new pt.ipp.isep.dei.domain.Date(
                                    sqlDate.toLocalDate().getDayOfMonth(),
                                    sqlDate.toLocalDate().getMonthValue(),
                                    sqlDate.toLocalDate().getYear()
                            );

                    Locomotive loco = new Locomotive(
                            rs.getInt("ID_Locomotive"),
                            rs.getString("Name"),
                            serviceDate,
                            modelStore.findById(
                                    db,
                                    String.valueOf(rs.getInt("Locomotive_ModelID_Locomotive_Model"))
                            ),
                            opStore.findById(
                                    db,
                                    rs.getString("OperatorID_Operator")
                            )
                    );

                    loco.setTrains(loadTrains(db, loco.getId()));
                    return loco;
                }
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return null;
    }

    public List<Locomotive> findAll(DatabaseConnection db) {
        Connection conn = db.getConnection();
        List<Locomotive> list = new ArrayList<>();

        LocomotiveModelStoreInMemory modelStore = new LocomotiveModelStoreInMemory();
        OperatorStoreInMemory opStore = new OperatorStoreInMemory();
        FacilityStoreInMemory facilityStore = new FacilityStoreInMemory();

        String sql = """
        SELECT ID_Locomotive, Name, Starting_Date_Service,
               Locomotive_ModelID_Locomotive_Model,
               OperatorID_Operator, Start_Facility_ID
        FROM Locomotive
        ORDER BY ID_Locomotive
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                java.sql.Date sqlDate = rs.getDate("Starting_Date_Service");

                pt.ipp.isep.dei.domain.Date serviceDate =
                        new pt.ipp.isep.dei.domain.Date(
                                sqlDate.toLocalDate().getDayOfMonth(),
                                sqlDate.toLocalDate().getMonthValue(),
                                sqlDate.toLocalDate().getYear()
                        );

                Locomotive loco = new Locomotive(
                        rs.getInt("ID_Locomotive"),
                        rs.getString("Name"),
                        serviceDate,
                        modelStore.findById(db, String.valueOf(rs.getInt("Locomotive_ModelID_Locomotive_Model"))),
                        opStore.findById(db, rs.getString("OperatorID_Operator")),
                        facilityStore.findById(db, String.valueOf(rs.getInt("Start_Facility_ID")))
                );

                loco.setTrains(loadTrains(db, loco.getId()));
                list.add(loco);
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return list;
    }

    private List<Train> loadTrains(DatabaseConnection db, int locoId) throws SQLException {
        Connection conn = db.getConnection();
        List<Train> list = new ArrayList<>();

        TrainStore trainStore = new TrainStore();

        String sql = """
            SELECT TrainID_Train
            FROM Locomotive_Train
            WHERE LocomotiveID_Locomotive = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, locoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Train t = trainStore.findById(db, String.valueOf(rs.getInt("TrainID_Train")));
                    if (t != null) list.add(t);
                }
            }
        }

        return list;
    }

    private boolean exists(DatabaseConnection db, int id) throws SQLException {
        Connection conn = db.getConnection();

        String sql = "SELECT 1 FROM Locomotive WHERE ID_Locomotive = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection conn, Locomotive l) throws SQLException {
        String sql = """
        INSERT INTO Locomotive
        (ID_Locomotive, Name, Starting_Date_Service,
         Locomotive_ModelID_Locomotive_Model, OperatorID_Operator, Start_Facility_ID)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            pt.ipp.isep.dei.domain.Date d = l.getStartingDate();

            java.sql.Date sqlDate = java.sql.Date.valueOf(
                    java.time.LocalDate.of(
                            d.getYear(),
                            d.getMonth(),
                            d.getDay()
                    )
            );

            ps.setInt(1, l.getId());
            ps.setString(2, l.getName());
            ps.setDate(3, sqlDate);
            ps.setInt(4, l.getLocomotiveModel().getId());
            ps.setString(5, l.getOperator().getId());
            ps.setInt(6, l.getStartFacility().getId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Locomotive not saved: ID=" + l.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Locomotive NOT saved (ID=" + l.getId() + ")", e
            );
        }
    }

    private void update(Connection conn, Locomotive l) throws SQLException {
        String sql = """
        UPDATE Locomotive
        SET Name = ?, Starting_Date_Service = ?,
            Locomotive_ModelID_Locomotive_Model = ?, OperatorID_Operator = ?
        WHERE ID_Locomotive = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            pt.ipp.isep.dei.domain.Date d = l.getStartingDate();

            java.sql.Date sqlDate = java.sql.Date.valueOf(
                    java.time.LocalDate.of(
                            d.getYear(),
                            d.getMonth(),
                            d.getDay()
                    )
            );

            ps.setString(1, l.getName());
            ps.setDate(2, sqlDate);
            ps.setInt(3, l.getLocomotiveModel().getId());
            ps.setString(4, l.getOperator().getId());
            ps.setInt(5, l.getId());

            ps.executeUpdate();
        }
    }
}
