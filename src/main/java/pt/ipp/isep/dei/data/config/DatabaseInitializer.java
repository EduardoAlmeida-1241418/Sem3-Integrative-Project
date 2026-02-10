package pt.ipp.isep.dei.data.config;

import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;
import pt.ipp.isep.dei.data.memory.TrainStoreInMemory;
import pt.ipp.isep.dei.domain.schedule.ScheduleGenerator;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Helper that initialises the application's database schema and seeded data.
 *
 * <p>This utility reads the SQL statements provided by {@code DatabaseSchema}
 * (schema and inserts), splits them into individual commands and executes
 * them sequentially. It is intended for test and development environments
 * where an initial schema and seed data are required.
 */
public class DatabaseInitializer {

    /**
     * Initialise the database by executing the schema creation statements
     * followed by the insert statements.
     *
     * <p>The method obtains a {@code DatabaseConnection} from
     * {@code ConnectionFactory}, creates a JDBC {@code Statement} and runs the
     * SQL commands returned by {@code DatabaseSchema.schema} and
     * {@code DatabaseSchema.inserts}. The caller receives any exception
     * encountered so that it may handle startup failures appropriately.
     *
     * @throws Exception if a database access error occurs while initialising
     */
    public static void initialize() throws Exception {

        Connection con = ActualDatabaseConnection.getDb().getConnection();

        Statement st = con.createStatement();

        runCommands(DatabaseSchema.schema.split(";"), st);
        runCommands(DatabaseSchema.inserts.split(";"), st);

        con.commit();
    }

    /**
     * Execute the provided SQL commands using the supplied statement.
     *
     * <p>Empty commands (after trimming) are ignored. Each non-empty
     * command is executed with {@code Statement.execute(String)} in sequence.
     * Any exception is propagated to the caller.
     *
     * @param commands the array of SQL command strings to execute
     * @param st the JDBC {@code Statement} used to execute the commands
     * @throws Exception if an error occurs while executing a command
     */
    private static void runCommands(String[] commands, Statement st) throws Exception {
        for (String c : commands) {
            String cmd = c.trim();
            if (!cmd.isEmpty()) {
                st.execute(cmd);
            }
        }
    }
}
