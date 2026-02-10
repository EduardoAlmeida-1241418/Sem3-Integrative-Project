package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RailwayLineStore implements Persistable {

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        return false;
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        return false;
    }

    @Override
    public RailwayLine findById(DatabaseConnection db, String id) {
        Connection c = db.getConnection();

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner FROM Rail_Line WHERE ID_Rail_Line = ?")) {

            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new RailwayLine(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5)
                );
            }
        } catch (SQLException e) {
            db.registerError(e);
        }

        return null;
    }

    public List<RailwayLine> findAll(DatabaseConnection db) {
        Connection c = db.getConnection();
        List<RailwayLine> list = new ArrayList<>();

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID_Rail_Line, Name, Start_FacilityID_Facility, End_FacilityID_Facility, OwnerID_Owner FROM Rail_Line ORDER BY ID_Rail_Line");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new RailwayLine(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5)
                ));
            }

        } catch (SQLException e) {
            db.registerError(e);
        }

        return list;
    }

    public List<Integer> findSegmentIdsForLine(DatabaseConnection db, int lineId) {
        List<Integer> ids = new ArrayList<>();
        Connection c = db.getConnection();

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT Rail_Line_SegmentID_Rail_Line_Segment FROM Rail_Line_Rail_Line_Segment WHERE Rail_LineID_Rail_Line = ? ORDER BY Order_Line")) {

            ps.setInt(1, lineId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            db.registerError(e);
        }

        return ids;
    }
}
