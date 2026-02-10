package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.config.DatabasePrinter;
import pt.ipp.isep.dei.domain.schedule.GeneralSchedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneralScheduleStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(GeneralScheduleStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        GeneralSchedule schedule = (GeneralSchedule) object;
        Connection connection = databaseConnection.getConnection();

        try {
            insert(connection, schedule);
            connection.commit();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving general schedule", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        GeneralSchedule schedule = (GeneralSchedule) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM General_Schedule WHERE ID_General_Schedule = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, schedule.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting general schedule", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public GeneralSchedule findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
                    SELECT ID_General_Schedule
                    FROM General_Schedule
                    WHERE ID_General_Schedule = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GeneralSchedule(
                            rs.getInt("ID_General_Schedule")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error finding general schedule", ex);
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private void insert(Connection connection, GeneralSchedule schedule) throws SQLException {
        String sql = """
                    INSERT INTO General_Schedule
                    (ID_General_Schedule)
                    VALUES (?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, schedule.getId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("General Schedule not saved: ID=" + schedule.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "General Schedule NOT saved (ID=" + schedule.getId() + ")", e
            );
        }
    }

    public List<GeneralSchedule> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<GeneralSchedule> list = new ArrayList<>();

        String sql = """
                    SELECT ID_General_Schedule
                    FROM General_Schedule
                    ORDER BY ID_General_Schedule
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new GeneralSchedule(
                        rs.getInt("ID_General_Schedule")));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading general schedules", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }

    public boolean deleteAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        String sql = "DELETE FROM General_Schedule";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting all general schedules", ex);
            System.err.println("Error deleting all general schedules: " + ex.getMessage());
            databaseConnection.registerError(ex);
            return false;
        }
    }

}
