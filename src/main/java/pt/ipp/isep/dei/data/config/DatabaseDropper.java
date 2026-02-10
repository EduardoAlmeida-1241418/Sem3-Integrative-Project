package pt.ipp.isep.dei.data.config;

import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility that drops all database tables and their referential constraints.
 *
 * <p>This helper class connects to the configured database and executes a
 * PL/SQL block which first removes referential constraints and then drops
 * every table in the schema. It is intended for use in test or development
 * environments where a full schema reset is required. Use with caution: the
 * operation is destructive and irreversible.
 */
public class DatabaseDropper {

    /**
     * Drop all referential constraints and then drop all tables in the current
     * schema.
     *
     * <p>The method obtains a {@code DatabaseConnection} from
     * {@code ConnectionFactory}, disables auto-commit, executes a PL/SQL
     * anonymous block that first removes foreign-key constraints and then
     * drops every table (cascade constraints), and finally commits the
     * transaction. Any exception is propagated to the caller.
     *
     * @throws Exception if a database access error occurs
     */
    public static void dropAll() throws Exception {
        Connection con = ActualDatabaseConnection.getDb().getConnection();

        Statement st = con.createStatement();

        String sql =
                "BEGIN " +
                        "FOR c IN (SELECT constraint_name, table_name FROM user_constraints WHERE constraint_type = 'R') LOOP " +
                        "EXECUTE IMMEDIATE 'ALTER TABLE ' || c.table_name || ' DROP CONSTRAINT ' || c.constraint_name; " +
                        "END LOOP; " +
                        "FOR t IN (SELECT table_name FROM user_tables) LOOP " +
                        "EXECUTE IMMEDIATE 'DROP TABLE ' || t.table_name || ' CASCADE CONSTRAINTS'; " +
                        "END LOOP; " +
                        "END;";

        st.execute(sql);
        con.commit();

        drollAllInMemory();
    }

    private static void drollAllInMemory() {
        new BuildingStoreInMemory().clear();
        new DimensionsStoreInMemory().clear();
        new FacilityStoreInMemory().clear();
        new FreightStoreInMemory().clear();
        new GeneralScheduleStoreInMemory().clear();
        new LocomotiveModelStoreInMemory().clear();
        new LocomotiveStoreInMemory().clear();
        new MakerStoreInMemory().clear();
        new OperatorStoreInMemory().clear();
        new OperatorStoreInMemory().clear();
        new PathStoreInMemory().clear();
        new RailwayLineSegmentStoreInMemory().clear();
        new RailwayLineStoreInMemory().clear();
        new RouteStoreInMemory().clear();
        new SidingStoreInMemory().clear();
        new TrackGaugeStoreInMemory().clear();
        new TrainStoreInMemory().clear();
        new WagonModelStoreInMemory().clear();
        new WagonStoreInMemory().clear();
        new WagonTypeStoreInMemory().clear();
    }
}
