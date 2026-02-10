package pt.ipp.isep.dei.data.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Factory responsible for creating and providing database connections.
 *
 * <p>This class implements a simple singleton connection-factory that
 * initialises application properties from {@code application.properties}
 * and supplies {@code DatabaseConnection} instances via a small pool.
 */
public class ConnectionFactory {

    private static ConnectionFactory instance = null;

    private final Integer connectionPoolCount = 1;
    private final List<DatabaseConnection> databaseConnectionList = new ArrayList<>();
    private Integer connectionPoolRequest = 0;

    /**
     * Construct a new ConnectionFactory and load application properties.
     *
     * @throws IOException if the properties file cannot be read
     */
    public ConnectionFactory() throws IOException {
        loadProperties();
    }

    /**
     * Return the singleton instance of this factory.
     *
     * <p>The method is synchronized to ensure thread-safety during initial
     * creation of the instance.
     *
     * @return the {@code ConnectionFactory} singleton
     * @throws IOException if initialisation fails when the instance is created
     */
    public static synchronized ConnectionFactory getInstance() throws IOException {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    /**
     * Load application properties from the classpath resource
     * {@code application.properties} and merge them into the system
     * properties.
     *
     * @throws IOException if the properties resource cannot be read
     */
    private void loadProperties() throws IOException {
        Properties properties = new Properties(System.getProperties());

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("application.properties");

        if (inputStream != null) {
            properties.load(inputStream);
            inputStream.close();
        }

        // Resolver ${VAR} com variáveis de ambiente
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (value != null && value.startsWith("${") && value.endsWith("}")) {
                String envKey = value.substring(2, value.length() - 1);
                String envValue = System.getenv(envKey);
                if (envValue != null) {
                    properties.setProperty(key, envValue);
                }
            }
        }

        System.setProperties(properties);
    }

    /**
     * Return a {@code DatabaseConnection} from the internal pool.
     *
     * <p>The method uses a simple round-robin policy and will create new
     * {@code DatabaseConnection} instances where necessary up to the
     * configured pool count.
     *
     * @return a {@code DatabaseConnection} instance
     */
    public DatabaseConnection getDatabaseConnection() {
        DatabaseConnection databaseConnection;
        if (++connectionPoolRequest > connectionPoolCount) {
            connectionPoolRequest = 1;
        }
        if (connectionPoolRequest > databaseConnectionList.size()) {
            databaseConnection = new DatabaseConnection(url(), user(), password());
            ActualDatabaseConnection.setDb(databaseConnection);
            databaseConnectionList.add(databaseConnection);
        } else {
            databaseConnection = databaseConnectionList.get(connectionPoolRequest - 1);
        }
        return databaseConnection;
    }

    /**
     * Obtain the database URL from the system properties.
     *
     * @return the configured JDBC URL
     */
    private String url() {
        return System.getProperty("database.url");
    }

    /**
     * Obtain the database user name from the system properties.
     *
     * @return the configured database user
     */
    private String user() {
        return System.getProperty("database.user");
    }

    /**
     * Obtain the database password from the system properties.
     *
     * @return the configured database password
     */
    private String password() {
        return System.getProperty("database.password");
    }
}
