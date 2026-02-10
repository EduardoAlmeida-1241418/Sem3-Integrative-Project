package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.schedule.GeneralSchedule;
import pt.ipp.isep.dei.domain.schedule.ScheduleEvent;
import pt.ipp.isep.dei.domain.schedule.ScheduleEventType;
import pt.ipp.isep.dei.domain.trackRelated.*;
import pt.ipp.isep.dei.domain.trainRelated.Train;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleEventStore implements Persistable {

    private static final Logger LOGGER = Logger.getLogger(ScheduleEventStore.class.getName());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        ScheduleEvent scheduleEvent = (ScheduleEvent) object;
        Connection connection = databaseConnection.getConnection();
        try {
            insert(connection, scheduleEvent);
            connection.commit();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving schedule event", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        ScheduleEvent scheduleEvent = (ScheduleEvent) object;
        Connection connection = databaseConnection.getConnection();
        String sql = "DELETE FROM Schedule_Event WHERE ID_Schedule_Event = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, scheduleEvent.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting schedule event", ex);
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public ScheduleEvent findById(DatabaseConnection databaseConnection, String id) {
        return null;
    }

    private void insert(Connection connection, ScheduleEvent scheduleEvent) throws SQLException {

        String sql = """
            INSERT INTO Schedule_Event
            (ID_Schedule_Event, Start_Time, End_Time, Type,
             Start_Position, End_Position, TrainID_Train, General_ScheduleID)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, scheduleEvent.getId());
            ps.setTimestamp(2, Timestamp.valueOf(toLocalDateTime(scheduleEvent.getStartDateTime())));
            ps.setTimestamp(3, Timestamp.valueOf(toLocalDateTime(scheduleEvent.getEndDateTime())));
            ps.setString(4, scheduleEvent.getScheduleEventType().name());
            ps.setInt(5, getPositionId(scheduleEvent.getStartPosition(), connection));
            ps.setInt(6, getPositionId(scheduleEvent.getEndPosition(), connection));
            ps.setInt(7, scheduleEvent.getTrain().getId());
            ps.setInt(8, scheduleEvent.getGeneralSchedule().getId());
            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Schedule Event not saved: ID=" + scheduleEvent.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Schedule Event NOT saved (ID=" + scheduleEvent.getId() + ")", e
            );
        }
    }

    private LocalDateTime toLocalDateTime(DateTime dt) {
        return LocalDateTime.of(
                dt.getDate().getYear(),
                dt.getDate().getMonth(),
                dt.getDate().getDay(),
                dt.getTime().getHour(),
                dt.getTime().getMinute(),
                dt.getTime().getSecond()
        );
    }

    private int getNextPositionId(Connection connection) throws SQLException {
        String sql = """
            SELECT COALESCE(
                (
                    SELECT MIN(p.ID_POSITION + 1)
                    FROM Position p
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM Position
                        WHERE ID_POSITION = p.ID_POSITION + 1
                    )
                ),
                1
            )
            FROM dual
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private int getPositionId(TrackLocation position, Connection connection) throws SQLException {

        int positionId = getNextPositionId(connection);

        int type;
        Integer facilityId = null;
        Integer sidingId = null;
        Integer dividedId = null;

        if (position instanceof Facility) {
            type = 1;
            facilityId = ((Facility) position).getId();
        } else if (position instanceof JointRailwayLineSegments) {
            type = 2;
        } else if (position instanceof RailwayLineSegment) {
            type = 3;
        } else if (position instanceof SegmentLineDivided) {
            type = 4;
            dividedId = ((SegmentLineDivided) position).getId_order();
            insertDividedSegment(connection, (SegmentLineDivided) position);
        } else if (position instanceof Siding) {
            type = 5;
            sidingId = ((Siding) position).getId();
        } else {
            throw new SQLException("Unknown TrackLocation");
        }

        String sql = """
            INSERT INTO Position
            (ID_POSITION, Type, FacilityID_Facility, SidingID_Siding, DividedID)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, positionId);
            ps.setInt(2, type);
            ps.setObject(3, facilityId, Types.INTEGER);
            ps.setObject(4, sidingId, Types.INTEGER);
            ps.setObject(5, dividedId, Types.INTEGER);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Position NOT saved (ID=" + positionId + ")", e
            );
        }

        if (position instanceof JointRailwayLineSegments) {
            for (RailwayLineSegment seg : ((JointRailwayLineSegments) position).getSegments()) {
                insertPositionRailLineSegment(connection, positionId, seg);
            }
        }

        if (position instanceof RailwayLineSegment) {
            insertPositionRailLineSegment(connection, positionId, (RailwayLineSegment) position);
        }

        return positionId;
    }

    private void insertPositionRailLineSegment(Connection connection, int positionId, RailwayLineSegment segment)
            throws SQLException {

        String sql = """
            INSERT INTO Position_Rail_Line_Segment
            (PositionId, Rail_Line_SegmentID_Rail_Line_Segment)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, positionId);
            ps.setInt(2, segment.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Position Rail Line Segment NOT saved (Position ID=" + positionId +
                            ", Segment ID=" + segment.getId() + ")", e
            );
        }
    }

    private void insertDividedSegment(Connection connection, SegmentLineDivided segment) throws SQLException {

        String sql = """
            INSERT INTO Divided
            (ID_Divided, ID_Order, Length, ID_Rail_Line_Segment)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, segment.getId());
            ps.setInt(2, segment.getId_order());
            ps.setInt(3, segment.getLength());
            ps.setInt(4, segment.getOwnerSegment().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Divided Segment NOT saved (ID=" + segment.getId() + ")", e
            );
        }
    }

    public List<ScheduleEvent> findAll(DatabaseConnection databaseConnection) {

        Connection connection = databaseConnection.getConnection();
        List<ScheduleEvent> list = new ArrayList<>();

        String sql = """
            SELECT ID_Schedule_Event, Start_Time, End_Time, Type,
                   Start_Position, End_Position, TrainID_Train, General_ScheduleID
            FROM Schedule_Event
            ORDER BY ID_Schedule_Event
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("ID_Schedule_Event");

                LocalDateTime start = rs.getTimestamp("Start_Time").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End_Time").toLocalDateTime();

                DateTime startDT = new DateTime(
                        new Date(start.getDayOfMonth(), start.getMonthValue(), start.getYear()),
                        new Time(start.getHour(), start.getMinute(), start.getSecond())
                );

                DateTime endDT = new DateTime(
                        new Date(end.getDayOfMonth(), end.getMonthValue(), end.getYear()),
                        new Time(end.getHour(), end.getMinute(), end.getSecond())
                );

                TimeInterval interval = new TimeInterval(startDT, endDT);

                ScheduleEventType type = ScheduleEventType.valueOf(rs.getString("Type"));

                TrackLocation startPos = loadPosition(rs.getInt("Start_Position"), connection);
                TrackLocation endPos = loadPosition(rs.getInt("End_Position"), connection);

                Train train = new TrainStoreInMemory().findById(
                        databaseConnection,
                        String.valueOf(rs.getInt("TrainID_Train"))
                );

                GeneralSchedule schedule = new GeneralScheduleStoreInMemory().findById(
                        databaseConnection,
                        String.valueOf(rs.getInt("General_ScheduleID"))
                );

                ScheduleEvent event = new ScheduleEvent(
                        id, schedule, interval, startPos, endPos, train, type
                );

                schedule.addEvent(event);
                list.add(event);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading schedule events", ex);
            databaseConnection.registerError(ex);
        }

        return list;
    }

    private TrackLocation loadPosition(int positionId, Connection connection) throws SQLException {

        String sql = """
            SELECT Type, FacilityID_Facility, DividedID, SidingID_Siding
            FROM Position
            WHERE ID_Position = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, positionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return switch (rs.getInt("Type")) {
                    case 1 -> new FacilityStoreInMemory().findById(null, String.valueOf(rs.getInt("FacilityID_Facility")));
                    case 2 -> loadJointSegments(positionId, connection);
                    case 3 -> loadSingleSegment(positionId, connection);
                    case 4 -> loadDivided(rs.getInt("DividedID"), connection);
                    case 5 -> new SidingStoreInMemory().findById(null, String.valueOf(rs.getInt("SidingID_Siding")));
                    default -> throw new SQLException("Unknown Position type");
                };
            }
        }
    }

    private RailwayLineSegment loadSingleSegment(int positionId, Connection connection) throws SQLException {

        String sql = """
            SELECT Rail_Line_SegmentID_Rail_Line_Segment
            FROM Position_Rail_Line_Segment
            WHERE PositionId = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, positionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new RailwayLineSegmentStoreInMemory().findById(
                        null,
                        String.valueOf(rs.getInt("Rail_Line_SegmentID_Rail_Line_Segment"))
                );
            }
        }
    }

    private JointRailwayLineSegments loadJointSegments(int positionId, Connection connection) throws SQLException {

        String sql = """
            SELECT Rail_Line_SegmentID_Rail_Line_Segment
            FROM Position_Rail_Line_Segment
            WHERE PositionId = ?
        """;

        List<RailwayLineSegment> segments = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, positionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    segments.add(new RailwayLineSegmentStoreInMemory().findById(
                            null,
                            String.valueOf(rs.getInt("Rail_Line_SegmentID_Rail_Line_Segment"))
                    ));
                }
            }
        }

        return new JointRailwayLineSegments(positionId, segments);
    }

    private SegmentLineDivided loadDivided(int id, Connection connection) throws SQLException {

        String sql = """
            SELECT ID_Order, Length, ID_Rail_Line_Segment
            FROM Divided
            WHERE ID_Divided = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                RailwayLineSegment owner = new RailwayLineSegmentStoreInMemory().findById(
                        null,
                        String.valueOf(rs.getInt("ID_Rail_Line_Segment"))
                );

                return new SegmentLineDivided(
                        id,
                        rs.getInt("ID_Order"),
                        owner,
                        rs.getInt("Length")
                );
            }
        }
    }

    public boolean deleteAll(DatabaseConnection db) {
        Connection conn = db.getConnection();

        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM Position_Rail_Line_Segment");
            stmt.executeUpdate("DELETE FROM Divided");
            stmt.executeUpdate("DELETE FROM Schedule_Event");
            stmt.executeUpdate("DELETE FROM Position");
            conn.commit();

            return true;

        } catch (SQLException e) {
            System.out.println("Error deleting all Schedule Events: " + e.getMessage());
            db.registerError(e);
            return false;
        }
    }

}
