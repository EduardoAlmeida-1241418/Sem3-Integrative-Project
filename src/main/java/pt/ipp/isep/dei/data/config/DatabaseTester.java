package pt.ipp.isep.dei.data.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Small utility class with manual database tests used during development.
 *
 * <p>The methods in this class execute a selection of diagnostic SQL
 * queries against the currently configured {@link ActualDatabaseConnection}.
 * They print results to standard output and are intended for quick,
 * local verification rather than automated testing. All comments are in
 * British English and only documentary — the executable code is
 * unchanged.</p>
 */
public class DatabaseTester {

    /**
     * Execute a set of quick diagnostic tests against the configured
     * database connection.
     *
     * <p>This convenience method obtains a JDBC {@link Connection} from
     * {@link ActualDatabaseConnection}, creates a statement and invokes
     * the individual test helpers that follow. Errors are propagated to
     * the caller as exceptions.</p>
     *
     * @throws Exception if a database error or other exception occurs
     */
    public static void test() throws Exception {

        Connection con = ActualDatabaseConnection.getDb().getConnection();

        Statement st = con.createStatement();

        testUSBD23_ListLineSegments(st, 5421);
        testUSBD25_ListEndpointsNoStop(st, 5421);
        testUSBD27_GrainWagonsUsedInEveryTrain(st,
                "2025-10-01", "2025-10-31");
    }

    /**
     * Print line segment information for a given train.
     *
     * <p>The method runs a query that joins the {@code Train},
     * {@code Rail_Line_Rail_Line_Segment} and {@code Rail_Line_Segment}
     * tables to list each rail line segment composing the route of the
     * supplied train. Results are printed to standard output in a
     * compact, human‑readable format.</p>
     *
     * @param st the JDBC statement to use for executing the query
     * @param trainId the identifier of the train to inspect
     * @throws Exception if an error occurs while executing the query
     */
    public static void testUSBD23_ListLineSegments(Statement st, int trainId) throws Exception {

        System.out.println("USBD23: Line segments of Train " + trainId);

        ResultSet rs = st.executeQuery(
                "SELECT rls.Rail_Line_SegmentID_Rail_Line_Segment AS Segment_ID, " +
                        "       rlseg.Length, rlseg.Speed_Limit, rlseg.Number_Tracks, rlseg.Is_Electrified_Line " +
                        "FROM Train t " +
                        "JOIN Rail_Line_Rail_Line_Segment rls ON t.RouteID_Route = rls.Rail_LineID_Rail_Line " +
                        "JOIN Rail_Line_Segment rlseg ON rlseg.ID_Rail_Line_Segment = rls.Rail_Line_SegmentID_Rail_Line_Segment " +
                        "WHERE t.ID_Train = " + trainId + " " +
                        "ORDER BY rls.Order_Line"
        );

        while (rs.next()) {
            System.out.println(
                    rs.getInt("Segment_ID") + " | " +
                            rs.getInt("Length") + " | " +
                            rs.getInt("Speed_Limit") + " | " +
                            rs.getInt("Number_Tracks") + " | " +
                            rs.getInt("Is_Electrified_Line")
            );
        }
        System.out.println();
    }

    /**
     * List the facilities on a route where a specific train does not stop.
     *
     * <p>The query uses the train's route and path to enumerate facilities
     * and excludes those that appear as origins or destinations of
     * freights assigned to the same train. Results are ordered according
     * to the facility position on the path and printed to standard output.</p>
     *
     * @param st the JDBC statement to use for executing the query
     * @param trainId the identifier of the train to inspect
     * @throws Exception if an error occurs while executing the query
     */
    public static void testUSBD25_ListEndpointsNoStop(Statement st, int trainId) throws Exception {

        System.out.println("USBD25: Endpoints where Train " + trainId + " does NOT stop");

        ResultSet rs = st.executeQuery(
                "SELECT DISTINCT pf.FacilityID_Facility AS Facility_ID, " +
                        "       f.Name, " +
                        "       pf.Position_Facility_Path " +   // ← NECESSÁRIO PARA O ORDER BY
                        "FROM Train t " +
                        "JOIN Route r ON t.RouteID_Route = r.ID_Route " +
                        "JOIN Path p ON p.ID_Path = r.PathID_Path " +
                        "JOIN Path_Facility pf ON pf.PathID_Path = p.ID_Path " +
                        "JOIN Facility f ON f.ID_Facility = pf.FacilityID_Facility " +
                        "WHERE t.ID_Train = " + trainId + " " +
                        "AND pf.FacilityID_Facility NOT IN ( " +
                        "    SELECT OriginFacilityID_Facility FROM Freight WHERE TrainID_Train = " + trainId +
                        "    UNION " +
                        "    SELECT DestinationFacilityID_Facility FROM Freight WHERE TrainID_Train = " + trainId +
                        ") " +
                        "ORDER BY pf.Position_Facility_Path"
        );

        while (rs.next()) {
            System.out.println(
                    rs.getInt("Facility_ID") + " | " +
                            rs.getString("Name")
            );
        }
        System.out.println();
    }

    /**
     * Find grain wagons that were used in every train during a date range.
     *
     * <p>The method builds a query that first identifies trains which used
     * grain wagons in the supplied date range and then counts how many
     * distinct trains each grain wagon appeared in. Wagons that appear
     * in the same number of trains as the total distinct trains are
     * considered to have been used in every train and are printed.</p>
     *
     * @param st the JDBC statement to use for executing the query
     * @param dateStart the inclusive start date in ISO format (YYYY-MM-DD)
     * @param dateEnd the inclusive end date in ISO format (YYYY-MM-DD)
     * @throws Exception if an error occurs while executing the query
     */
    public static void testUSBD27_GrainWagonsUsedInEveryTrain(
            Statement st, String dateStart, String dateEnd) throws Exception {

        System.out.println("USBD27: Grain wagons used in EVERY train between " +
                dateStart + " and " + dateEnd);

        ResultSet rs = st.executeQuery(
                "WITH Grain_Train AS ( " +
                        "    SELECT DISTINCT f.TrainID_Train " +
                        "    FROM Wagon_Freight wf " +
                        "    JOIN Freight f ON f.ID_Freight = wf.FreightID_Freight " +
                        "    JOIN Wagon w ON w.ID_Wagon = wf.WagonID_Wagon " +
                        "    JOIN Wagon_Model wm ON wm.ID_Wagon_Model = w.Wagon_ModelID_Wagon_Model " +
                        "    WHERE wm.Wagon_TypeID_Wagon_Type = 1 " +   // grain wagons
                        "      AND f.\"Date\" BETWEEN DATE '" + dateStart +
                        "' AND DATE '" + dateEnd + "' " +
                        "), " +
                        "Wagon_Usage AS ( " +
                        "    SELECT wf.WagonID_Wagon, COUNT(DISTINCT f.TrainID_Train) AS train_count " +
                        "    FROM Wagon_Freight wf " +
                        "    JOIN Freight f ON f.ID_Freight = wf.FreightID_Freight " +
                        "    JOIN Wagon w ON w.ID_Wagon = wf.WagonID_Wagon " +
                        "    JOIN Wagon_Model wm ON wm.ID_Wagon_Model = w.Wagon_ModelID_Wagon_Model " +
                        "    WHERE wm.Wagon_TypeID_Wagon_Type = 1 " +
                        "      AND f.\"Date\" BETWEEN DATE '" + dateStart +
                        "' AND DATE '" + dateEnd + "' " +
                        "    GROUP BY wf.WagonID_Wagon " +
                        ") " +
                        "SELECT WagonID_Wagon " +
                        "FROM Wagon_Usage " +
                        "WHERE train_count = (SELECT COUNT(*) FROM Grain_Train)"
        );

        while (rs.next()) {
            System.out.println("Wagon: " + rs.getInt("WagonID_Wagon"));
        }
        System.out.println();
    }
}
