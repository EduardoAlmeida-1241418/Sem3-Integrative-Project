package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrainStore implements Persistable {

    /* =========================
       SAVE / DELETE
       ========================= */

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Train train = (Train) obj;
        Connection conn = db.getConnection();

        try {
            insert(conn, train);
            conn.commit();
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Train train = (Train) obj;

        try (PreparedStatement ps =
                     db.getConnection().prepareStatement(
                             "DELETE FROM Train WHERE ID_Train = ?")) {

            ps.setInt(1, train.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    /* =========================
       FIND BY ID
       ========================= */

    @Override
    public Train findById(DatabaseConnection db, String id) {
        Connection conn = db.getConnection();

        OperatorStoreInMemory operatorStore = new OperatorStoreInMemory();
        RouteStoreInMemory routeStore = new RouteStoreInMemory();
        LocomotiveStoreInMemory locomotiveStore = new LocomotiveStoreInMemory();

        String sql = """
                    SELECT ID_Train,
                           Scheduled_Construction_Date,
                           Dispatched,
                           OperatorID_Operator,
                           RouteID_Route
                    FROM Train
                    WHERE ID_Train = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    int trainId = rs.getInt("ID_Train");

                    /* Operator */
                    Operator operator =
                            operatorStore.findById(
                                    db,
                                    rs.getString("OperatorID_Operator")
                            );

                    /* DateTime (TIMESTAMP -> Date + Time) */
                    Timestamp ts = rs.getTimestamp("Scheduled_Construction_Date");
                    LocalDateTime ldt = ts.toLocalDateTime();

                    Date date = new Date(
                            ldt.getDayOfMonth(),
                            ldt.getMonthValue(),
                            ldt.getYear()
                    );

                    Time time = new Time(
                            ldt.getHour(),
                            ldt.getMinute(),
                            ldt.getSecond()
                    );

                    DateTime dateTime = new DateTime(date, time);

                    /* Route (NUNCA null pelo schema) */
                    int routeId = rs.getInt("RouteID_Route");
                    Route route =
                            routeStore.findById(db, String.valueOf(routeId));

                    if (route == null) {
                        throw new IllegalStateException(
                                "Route " + routeId + " not found for Train " + trainId
                        );
                    }

                    /* Dispatched */
                    boolean dispatched =
                            rs.getString("Dispatched").equals("1");

                    /* Locomotives */
                    List<Locomotive> locomotives =
                            loadLocomotives(conn, locomotiveStore, trainId, db);

                    return new Train(
                            trainId,
                            operator,
                            dateTime,
                            route,
                            locomotives,
                            dispatched
                    );
                }
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return null;
    }

    /* =========================
       FIND ALL
       ========================= */

    public List<Train> findAll(DatabaseConnection db) {
        Connection conn = db.getConnection();
        List<Train> trains = new ArrayList<>();

        OperatorStoreInMemory operatorStore = new OperatorStoreInMemory();
        RouteStoreInMemory routeStore = new RouteStoreInMemory();
        LocomotiveStoreInMemory locomotiveStore = new LocomotiveStoreInMemory();

        String sql = """
                    SELECT ID_Train,
                           Scheduled_Construction_Date,
                           Dispatched,
                           OperatorID_Operator,
                           RouteID_Route
                    FROM Train
                    ORDER BY ID_Train
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int trainId = rs.getInt("ID_Train");

                Operator operator =
                        operatorStore.findById(
                                db,
                                rs.getString("OperatorID_Operator")
                        );

                Timestamp ts = rs.getTimestamp("Scheduled_Construction_Date");
                LocalDateTime ldt = ts.toLocalDateTime();

                Date date = new Date(
                        ldt.getDayOfMonth(),
                        ldt.getMonthValue(),
                        ldt.getYear()
                );

                Time time = new Time(
                        ldt.getHour(),
                        ldt.getMinute(),
                        ldt.getSecond()
                );

                DateTime dateTime = new DateTime(date, time);

                int routeId = rs.getInt("RouteID_Route");
                Route route =
                        routeStore.findById(db, String.valueOf(routeId));

                if (route == null) {
                    throw new IllegalStateException(
                            "Route " + routeId + " not found for Train " + trainId
                    );
                }

                boolean dispatched =
                        rs.getString("Dispatched").equals("1");

                List<Locomotive> locomotives =
                        loadLocomotives(conn, locomotiveStore, trainId, db);

                trains.add(new Train(
                        trainId,
                        operator,
                        dateTime,
                        route,
                        locomotives,
                        dispatched
                ));
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return trains;
    }

    private void insert(Connection conn, Train train) throws SQLException {
        String sql = """
                    INSERT INTO Train
                    (ID_Train,
                     Scheduled_Construction_Date,
                     Dispatched,
                     OperatorID_Operator,
                     RouteID_Route)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, train.getId());

            LocalDateTime ldt = LocalDateTime.of(
                    train.getDateTime().getDate().getYear(),
                    train.getDateTime().getDate().getMonth(),
                    train.getDateTime().getDate().getDay(),
                    train.getDateTime().getTime().getHour(),
                    train.getDateTime().getTime().getMinute(),
                    train.getDateTime().getTime().getSecond()
            );

            ps.setTimestamp(2, Timestamp.valueOf(ldt));
            ps.setString(3, train.isDispatched() ? "1" : "0");
            ps.setString(4, train.getOperator().getId());
            ps.setInt(5, train.getRoute().getId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Train not saved: ID=" + train.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Train NOT saved (ID=" + train.getId() + ")", e
            );
        }

        insertLocomotiveTrain(conn, train);
    }

    private void insertLocomotiveTrain(Connection conn, Train train) throws SQLException {
        int trainId = train.getId();

        for (Locomotive locomotive : train.getLocomotives()) {
            String locomotiveId = String.valueOf(locomotive.getId());

            String sql = """
                    INSERT INTO Locomotive_Train
                    (LocomotiveID_Locomotive, TrainID_Train)
                    VALUES (?, ?)
                """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, locomotiveId);
                ps.setInt(2, trainId);

                int rows = ps.executeUpdate();

                if (rows != 1) {
                    System.out.println("Locomotive_Train not saved: LocomotiveID=" + locomotiveId + ", TrainID=" + trainId);
                }
            } catch (SQLException e) {
                throw new RuntimeException(
                        "Locomotive_Train NOT saved (LocomotiveID=" + locomotiveId + ", TrainID=" + trainId + ")", e
                );
            }
        }
    }

    private void update(Connection conn, Train train) throws SQLException {
        String sql = """
                    UPDATE Train
                    SET Scheduled_Construction_Date = ?,
                        Dispatched = ?,
                        OperatorID_Operator = ?,
                        RouteID_Route = ?
                    WHERE ID_Train = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDateTime ldt = LocalDateTime.of(
                    train.getDateTime().getDate().getYear(),
                    train.getDateTime().getDate().getMonth(),
                    train.getDateTime().getDate().getDay(),
                    train.getDateTime().getTime().getHour(),
                    train.getDateTime().getTime().getMinute(),
                    train.getDateTime().getTime().getSecond()
            );

            ps.setTimestamp(1, Timestamp.valueOf(ldt));
            ps.setString(2, train.isDispatched() ? "1" : "0");
            ps.setString(3, train.getOperator().getId());
            ps.setInt(4, train.getRoute().getId());
            ps.setInt(5, train.getId());

            ps.executeUpdate();
        }
    }

    private List<Locomotive> loadLocomotives(
            Connection conn,
            LocomotiveStoreInMemory store,
            int trainId,
            DatabaseConnection db) throws SQLException {

        List<Locomotive> locomotives = new ArrayList<>();

        String sql = """
                    SELECT LocomotiveID_Locomotive
                    FROM Locomotive_Train
                    WHERE TrainID_Train = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    locomotives.add(
                            store.findById(
                                    db,
                                    rs.getString("LocomotiveID_Locomotive")
                            )
                    );
                }
            }
        }

        return locomotives;
    }

    public boolean deleteAll(DatabaseConnection db) {
        Connection conn = db.getConnection();

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM Locomotive_Train");
            stmt.executeUpdate("DELETE FROM Train");
            conn.commit();
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

}
