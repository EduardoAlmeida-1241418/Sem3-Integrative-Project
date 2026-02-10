package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrackGaugeStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(TrackGaugeStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        TrackGauge gauge = (TrackGauge) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, gauge.getId())) {
                update(connection, gauge);
            } else {
                insert(connection, gauge);
            }
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving track gauge", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        TrackGauge gauge = (TrackGauge) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Track_Gauge WHERE ID_Track_Gauge = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, gauge.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting track gauge", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public TrackGauge findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT ID_Track_Gauge, Gauge_Size
            FROM Track_Gauge
            WHERE ID_Track_Gauge = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TrackGauge(
                            rs.getInt("ID_Track_Gauge"),
                            rs.getInt("Gauge_Size")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding track gauge", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private boolean exists(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Track_Gauge WHERE ID_Track_Gauge = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, TrackGauge gauge) throws SQLException {
        String sql = """
            INSERT INTO Track_Gauge
            (ID_Track_Gauge, Gauge_Size)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, gauge.getId());
            ps.setInt(2, gauge.getGaugeSize());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Track Gauge not saved: ID=" + gauge.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Track Gauge NOT saved (ID=" + gauge.getId() + ")", e
            );
        }
    }

    private void update(Connection connection, TrackGauge gauge) throws SQLException {
        String sql = """
            UPDATE Track_Gauge
            SET Gauge_Size = ?
            WHERE ID_Track_Gauge = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, gauge.getGaugeSize());
            ps.setInt(2, gauge.getId());
            ps.executeUpdate();
        }
    }

    public List<TrackGauge> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<TrackGauge> list = new ArrayList<>();

        String sql = """
            SELECT ID_Track_Gauge, Gauge_Size
            FROM Track_Gauge
            ORDER BY ID_Track_Gauge
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new TrackGauge(
                        rs.getInt("ID_Track_Gauge"),
                        rs.getInt("Gauge_Size")
                ));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading track gauges", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }
}
