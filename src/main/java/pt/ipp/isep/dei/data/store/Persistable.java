package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;

import java.util.List;

public interface Persistable {
    /**
     * Save an objet to the database.
     *
     * @param databaseConnection
     * @param object
     * @return Operation success.
     */
    boolean save(DatabaseConnection databaseConnection, Object object);

    /**
     * Delete an object from the database.
     *
     * @param databaseConnection
     * @param object
     * @return Operation success.
     */
    boolean delete(DatabaseConnection databaseConnection, Object object);

    /**
     * Retrieve an object from the database by its ID.
     *
     * @param databaseConnection The active database connection.
     * @param id The object's identifier.
     * @return The corresponding object if found, null otherwise.
     */
    Object findById(DatabaseConnection databaseConnection, String id);
}
