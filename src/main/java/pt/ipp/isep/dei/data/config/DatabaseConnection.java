package pt.ipp.isep.dei.data.config;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thin wrapper around an Oracle JDBC connection used by the application.
 *
 * <p>This class initialises an {@code OracleDataSource} using the supplied
 * JDBC URL, username and password and exposes the underlying
 * {@code java.sql.Connection}. It also provides simple error bookkeeping
 * and a utility method to validate that required database tables exist.
 */
public class DatabaseConnection {

    private OracleDataSource oracleDataSource;
    private Connection connection;
    private SQLException error;

    /**
     * Create and open a database connection using the provided credentials.
     *
     * @param url the JDBC URL for the Oracle database
     * @param username the database user name
     * @param password the database password
     */
    public DatabaseConnection(String url, String username, String password) {
        try {
            oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(url);
            connection = oracleDataSource.getConnection(username, password);
        } catch (SQLException e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    /**
     * Return the active {@code Connection}.
     *
     * @throws RuntimeException if the connection has not been initialised
     * @return the JDBC {@code Connection}
     */
    public Connection getConnection() {
        if (connection == null) {
            throw new RuntimeException("Connection does not exist");
        }
        return connection;
    }

    /**
     * Record the last {@code SQLException} that occurred on this connection.
     *
     * @param error the SQLException to record
     */
    public void registerError(SQLException error) {
        this.error = error;
    }

    /**
     * Retrieve and clear the last recorded {@code SQLException}.
     *
     * @return the last recorded SQLException, or {@code null} if none was recorded
     */
    public SQLException getLastError() {
        SQLException lastError = this.error;
        this.error = null;
        return lastError;
    }

    /**
     * Validate that the provided database tables exist and are queryable.
     *
     * <p>The method attempts a simple {@code SELECT COUNT(*)} on each table
     * and returns {@code false} if any query fails.
     *
     * @param tables the names of tables to validate
     * @return {@code true} if all tables appear valid, otherwise {@code false}
     * @throws Exception if an unexpected error occurs while obtaining a connection
     */
    public static boolean validateDatabase(String[] tables) throws Exception {
        Connection con = ActualDatabaseConnection.getDb().getConnection();

        Statement st = con.createStatement();

        for (String t : tables) {
            try {
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + t);
                rs.next();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
