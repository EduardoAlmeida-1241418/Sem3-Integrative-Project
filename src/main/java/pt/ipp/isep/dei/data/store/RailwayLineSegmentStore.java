package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RailwayLineSegmentStore implements Persistable {

    @Override
    public boolean save(DatabaseConnection db, Object object) {
        return false;
    }

    @Override
    public boolean delete(DatabaseConnection db, Object object) {
        return false;
    }

    @Override
    public RailwayLineSegment findById(DatabaseConnection db, String id) {
        Connection c = db.getConnection();

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge FROM Rail_Line_Segment WHERE ID_Rail_Line_Segment = ?")) {

            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new RailwayLineSegment(
                        rs.getInt(1),
                        rs.getInt(2) == 1,
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getInt(7)
                );
            }
        } catch (SQLException e) {
            db.registerError(e);
        }

        return null;
    }

    public List<RailwayLineSegment> findAll(DatabaseConnection db) {
        Connection c = db.getConnection();
        List<RailwayLineSegment> list = new ArrayList<>();

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID_Rail_Line_Segment, Is_Electrified_Line, Maximum_Weight, Length, Number_Tracks, Speed_Limit, Track_GaugeID_Track_Gauge FROM Rail_Line_Segment ORDER BY ID_Rail_Line_Segment");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new RailwayLineSegment(
                        rs.getInt(1),
                        rs.getInt(2) == 1,
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getInt(7)
                ));
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return list;
    }

    public List<Integer> findLineIdsForSegment(DatabaseConnection db, int segmentId) {
        List<Integer> ids = new ArrayList<>();
        Connection c = db.getConnection();

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT Rail_LineID_Rail_Line FROM Rail_Line_Rail_Line_Segment WHERE Rail_Line_SegmentID_Rail_Line_Segment = ? ORDER BY Order_Line")) {

            ps.setInt(1, segmentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            db.registerError(e);
        }

        return ids;
    }
}
