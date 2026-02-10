package pt.ipp.isep.dei.data.config;

import java.sql.*;

/**
 * Utility class to print database schema and table contents to standard output.
 *
 * <p>This class obtains the application's current {@link DatabaseConnection}
 * (via {@link ActualDatabaseConnection}) and uses JDBC metadata and simple
 * queries to print each table's name and rows. It is primarily intended for
 * development and debugging; no production assumptions are made. Only
 * documentation comments were added — the code behaviour remains unchanged.</p>
 */
public class DatabasePrinter {

    static DatabaseConnection db = ActualDatabaseConnection.getDb();

    /**
     * Print the names and contents of all tables in the current database.
     *
     * <p>The method obtains a JDBC {@link Connection} from the configured
     * {@link DatabaseConnection}, uses {@link DatabaseMetaData#getTables}
     * to enumerate tables and for each table calls {@link #printTable(String)}
     * to print its rows. SQLExceptions are reported to the configured
     * connection via {@link DatabaseConnection#registerError}.</p>
     */
    public static void printAll() {
        Connection conn = db.getConnection();

        try {
            DatabaseMetaData meta = conn.getMetaData();

            try (ResultSet tables = meta.getTables(
                    null,
                    conn.getSchema(),
                    "%",
                    new String[]{"TABLE"})) {

                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");

                    System.out.println("\n==============================");
                    System.out.println("TABLE: " + tableName);
                    System.out.println("==============================");

                    printTable(tableName);
                }
            }

        } catch (SQLException e) {
            db.registerError(e);
        }
    }

    /**
     * Print all rows of the named table to standard output.
     *
     * <p>This method executes a simple "SELECT * FROM {tableName}" query,
     * prints a tab-separated header with column names and then each row's
     * values. Null values are rendered as the literal "NULL". If the
     * table contains no rows the method prints a short message indicating
     * that. SQLExceptions are caught and a brief error message is written
     * to standard output; exceptions are not rethrown.</p>
     *
     * @param tableName the name of the table to print
     */
    public static void printTable(String tableName) {
        String sql = "SELECT * FROM " + tableName;

        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Header
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println();

            // Rows
            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    System.out.print((value != null ? value : "NULL") + "\t");
                }
                System.out.println();
            }

            if (!hasRows) {
                System.out.println("(no rows)");
            }

        } catch (SQLException e) {
            System.out.println("Error reading table " + tableName);
        }
    }
}
