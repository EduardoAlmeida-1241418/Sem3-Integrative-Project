package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;
import pt.ipp.isep.dei.data.memory.WagonStoreInMemory;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FreightStore implements Persistable {

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        Freight freight = (Freight) obj;
        Connection conn = db.getConnection();

        try {
            if (exists(db, freight.getId())) {
                updateFreight(conn, freight);
                deleteWagonFreight(conn, freight.getId());
                insertWagonFreight(conn, freight);
            } else {
                insertFreight(conn, freight);
                insertWagonFreight(conn, freight);
            }
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        Freight freight = (Freight) obj;
        Connection conn = db.getConnection();

        try {
            deleteWagonFreight(conn, freight.getId());
            deleteFreight(conn, freight.getId());
            return true;
        } catch (SQLException e) {
            db.registerError(e);
            return false;
        }
    }

    @Override
    public Freight findById(DatabaseConnection db, String id) {
        Connection conn = db.getConnection();

        String sql = """
            SELECT ID_Freight, "Date",
                   OriginFacilityID_Facility,
                   DestinationFacilityID_Facility
            FROM Freight
            WHERE ID_Freight = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    int fid = rs.getInt("ID_Freight");

                    Facility origin = loadFacility(db,
                            rs.getInt("OriginFacilityID_Facility"));

                    Facility destination = loadFacility(db,
                            rs.getInt("DestinationFacilityID_Facility"));

                    java.sql.Date sqlDate = rs.getDate("Date");

                    Date date = new Date(
                            sqlDate.toLocalDate().getDayOfMonth(),
                            sqlDate.toLocalDate().getMonthValue(),
                            sqlDate.toLocalDate().getYear()
                    );

                    List<Wagon> wagons = loadWagons(db, fid);

                    return new Freight(fid, wagons, origin, destination, date);
                }
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return null;
    }

    public List<Freight> findAll(DatabaseConnection db) {
        Connection conn = db.getConnection();
        List<Freight> list = new ArrayList<>();

        String sql = """
            SELECT ID_Freight, "Date",
                   OriginFacilityID_Facility,
                   DestinationFacilityID_Facility
            FROM Freight
            ORDER BY ID_Freight
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int fid = rs.getInt("ID_Freight");

                Facility origin = loadFacility(db,
                        rs.getInt("OriginFacilityID_Facility"));

                Facility destination = loadFacility(db,
                        rs.getInt("DestinationFacilityID_Facility"));

                java.sql.Date sqlDate = rs.getDate("Date");

                Date date = new Date(
                        sqlDate.toLocalDate().getDayOfMonth(),
                        sqlDate.toLocalDate().getMonthValue(),
                        sqlDate.toLocalDate().getYear()
                );

                List<Wagon> wagons = loadWagons(db, fid);
                Freight f = new Freight(fid, wagons, origin, destination, date);
                loadRoutes(f);

                list.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Route r : new RouteStoreInMemory().findAll()) {
            for (Freight f : new ArrayList<>(r.getFreights())) {
                if (f.getDate() == null) {
                    r.getFreights().remove(f);
                }
            }
        }

        return list;
    }

    private void loadRoutes(Freight f) {
        for (Route r : new RouteStoreInMemory().findAll()) {
            for (Freight fr : r.getFreights()) {
                if (fr.getId() == f.getId()) {
                    r.getFreights().remove(fr);
                    r.getFreights().add(f);
                    return;
                }
            }
        }
    }

    private boolean exists(DatabaseConnection db, int id) throws SQLException {
        String sql = "SELECT 1 FROM Freight WHERE ID_Freight = ?";

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insertFreight(Connection conn, Freight f) throws SQLException {
        String sql = """
            INSERT INTO Freight
            (ID_Freight, "Date",
             OriginFacilityID_Facility,
             DestinationFacilityID_Facility,
             RouteID_Route)
            VALUES (?, ?, ?, ?, NULL)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, f.getId());
            ps.setDate(2, java.sql.Date.valueOf(
                    java.time.LocalDate.of(
                            f.getDate().getYear(),
                            f.getDate().getMonth(),
                            f.getDate().getDay()
                    )));
            ps.setInt(3, f.getOriginFacility().getId());
            ps.setInt(4, f.getDestinationFacility().getId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Freight not saved: ID=" + f.getId());
            }
        }  catch (SQLException e) {
            throw new RuntimeException(
                    "Freight NOT saved (ID=" + f.getId() + ")", e
            );
        }
    }

    private void updateFreight(Connection conn, Freight f) throws SQLException {
        String sql = """
            UPDATE Freight
            SET "Date" = ?,
                OriginFacilityID_Facility = ?,
                DestinationFacilityID_Facility = ?
            WHERE ID_Freight = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(
                    java.time.LocalDate.of(
                            f.getDate().getYear(),
                            f.getDate().getMonth(),
                            f.getDate().getDay()
                    )));
            ps.setInt(2, f.getOriginFacility().getId());
            ps.setInt(3, f.getDestinationFacility().getId());
            ps.setInt(4, f.getId());
            ps.executeUpdate();
        }
    }

    private void deleteFreight(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM Freight WHERE ID_Freight = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void insertWagonFreight(Connection conn, Freight f) throws SQLException {
        String sql = """
            INSERT INTO Wagon_Freight
            (WagonID_Wagon, FreightID_Freight)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Wagon w : f.getWagons()) {
                ps.setInt(1, w.getWagonID());
                ps.setInt(2, f.getId());
                ps.executeUpdate();
            }
        }  catch (SQLException e) {
            throw new RuntimeException(
                    "Wagon Freight NOT saved (FreightID=" + f.getId() + ", WagonID=" + f.getWagons() + ")", e
            );
        }
    }

    private void deleteWagonFreight(Connection conn, int freightId) throws SQLException {
        try (PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM Wagon_Freight WHERE FreightID_Freight = ?")) {
            ps.setInt(1, freightId);
            ps.executeUpdate();
        }
    }

    private Facility loadFacility(DatabaseConnection db, int id) {
        FacilityStore store = new FacilityStore();
        return store.findById(db, String.valueOf(id));
    }

    private List<Wagon> loadWagons(DatabaseConnection db, int freightId) {
        List<Wagon> wagons = new ArrayList<>();

        String sql = "SELECT WagonID_Wagon FROM Wagon_Freight WHERE FreightID_Freight = ?";

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, freightId);

            WagonStoreInMemory store = new WagonStoreInMemory();

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Wagon wagon = store.findById(db, String.valueOf(rs.getInt("WagonID_Wagon")));
                    if (wagon != null) {
                        wagons.add(wagon);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wagons;
    }
}
