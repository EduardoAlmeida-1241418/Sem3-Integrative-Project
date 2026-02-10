package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.FacilityType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacilityStore implements Persistable {

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Facility facility = (Facility) object;
        Connection connection = databaseConnection.getConnection();

        try {
            if (exists(databaseConnection, facility.getId())) {
                update(connection, facility);
            } else {
                insert(connection, facility);
            }
            return true;

        } catch (SQLException ex) {
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Facility facility = (Facility) object;
        Connection connection = databaseConnection.getConnection();

        String sql = "DELETE FROM Facility WHERE ID_Facility = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, facility.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            databaseConnection.registerError(ex);
            return false;
        }
    }

    @Override
    public Facility findById(DatabaseConnection databaseConnection, String id) {
        Connection connection = databaseConnection.getConnection();

        String sql = """
            SELECT ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type
            FROM Facility
            WHERE ID_Facility = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    FacilityType type = FacilityType.values()[
                            rs.getInt("Facility_TypeID_Facility_Type") - 1
                            ];

                    return new Facility(
                            rs.getInt("ID_Facility"),
                            rs.getString("Name"),
                            rs.getInt("Intermodal") == 1,
                            type
                    );
                }
            }

        } catch (SQLException ex) {
            databaseConnection.registerError(ex);
        }

        return null;
    }

    private boolean exists(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sql = "SELECT 1 FROM Facility WHERE ID_Facility = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insert(Connection connection, Facility facility) throws SQLException {
        String sql = """
            INSERT INTO Facility
            (ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, facility.getId());
            ps.setString(2, facility.getName());
            ps.setInt(3, facility.isIntermodal() ? 1 : 0);
            ps.setInt(4, facility.getFacilityType().getId());

            int rows = ps.executeUpdate();

            if (rows != 1) {
                System.out.println("Facility not saved: ID=" + facility.getId());
            }
        }  catch (SQLException e) {
            throw new RuntimeException(
                    "Facility NOT saved (ID=" + facility.getId() + ")", e
            );
        }
    }

    private void update(Connection connection, Facility facility) throws SQLException {
        String sql = """
            UPDATE Facility
            SET Name = ?, Intermodal = ?, Facility_TypeID_Facility_Type = ?
            WHERE ID_Facility = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, facility.getName());
            ps.setInt(2, facility.isIntermodal() ? 1 : 0);
            ps.setInt(3, facility.getFacilityType().getId());
            ps.setInt(4, facility.getId());
            ps.executeUpdate();
        }
    }

    public List<Facility> findAll(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Facility> list = new ArrayList<>();

        String sql = """
            SELECT ID_Facility, Name, Intermodal, Facility_TypeID_Facility_Type
            FROM Facility
            ORDER BY ID_Facility
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                FacilityType type = FacilityType.values()[
                        rs.getInt("Facility_TypeID_Facility_Type") - 1
                        ];

                list.add(new Facility(
                        rs.getInt("ID_Facility"),
                        rs.getString("Name"),
                        rs.getInt("Intermodal") == 1,
                        type
                ));
            }

        } catch (SQLException ex) {
            databaseConnection.registerError(ex);
        }

        return list;
    }
}
