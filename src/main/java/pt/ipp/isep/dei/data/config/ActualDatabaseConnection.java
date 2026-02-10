package pt.ipp.isep.dei.data.config;

public class ActualDatabaseConnection {

    private static DatabaseConnection db;

    /**
     * Public no‑argument constructor.
     *
     * <p>This class is used as a holder for the application's current
     * {@link DatabaseConnection}. The constructor does not perform any
     * initialisation because the underlying reference is managed via the
     * static accessor methods.</p>
     */
    public ActualDatabaseConnection() {
    }

    /**
     * Obtain the currently configured {@code DatabaseConnection} instance.
     *
     * <p>Returns the connection object that has been previously set using
     * {@link #setDb(DatabaseConnection)}. The return value may be
     * {@code null} if no connection has been configured.</p>
     *
     * @return the current {@code DatabaseConnection} or {@code null} if none is set
     */
    public static DatabaseConnection getDb() {
        return db;
    }

    /**
     * Set the application's current {@code DatabaseConnection} instance.
     *
     * <p>This method replaces the previously stored connection reference
     * with the supplied one. It does not perform validation or initialisation
     * of the connection object — callers are responsible for ensuring the
     * supplied instance is usable.</p>
     *
     * @param db the {@code DatabaseConnection} to store as the active instance
     */
    public static void setDb(DatabaseConnection db) {
        ActualDatabaseConnection.db = db;
    }
}
