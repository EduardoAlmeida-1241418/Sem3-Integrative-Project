package pt.ipp.isep.dei.data.repository;

import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;

import java.util.*;

/**
 * Repository responsible for managing {@link Log} entities.
 * Provides operations for adding, removing, filtering, and retrieving logs.
 */
public class LogRepository {

    /** Internal list storing all log entries. */
    private final List<Log> logs = new ArrayList<>();

    /**
     * Adds a new log entry to the repository.
     *
     * @param log the log to add
     * @throws IllegalArgumentException if the log is null
     * @throws IllegalStateException if the log already exists in the repository
     */
    public void add(Log log) {
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null.");
        }
        if (logs.contains(log)) {
            throw new IllegalStateException("This log already exists.");
        }
        logs.add(log);
    }

    /**
     * Removes a log entry from the repository.
     *
     * @param log the log to remove
     * @throws IllegalArgumentException if the log is null
     * @throws NoSuchElementException if the log does not exist in the repository
     */
    public void remove(Log log) {
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null.");
        }
        if (!logs.contains(log)) {
            throw new NoSuchElementException("Log not found.");
        }
        logs.remove(log);
    }

    /**
     * Retrieves all log entries from the repository.
     *
     * @return unmodifiable collection of all logs
     */
    public Collection<Log> findAll() {
        return Collections.unmodifiableCollection(logs);
    }

    /**
     * Retrieves all logs of a specific {@link LogType}.
     *
     * @param type the type of logs to retrieve
     * @return list of logs matching the given type
     * @throws IllegalArgumentException if the type is null
     * @throws NoSuchElementException if no logs are found for the given type
     */
    public List<Log> findByType(LogType type) {
        if (type == null) {
            throw new IllegalArgumentException("Log type cannot be null.");
        }
        List<Log> result = new ArrayList<>();
        for (Log log : logs) {
            if (type.equals(log.getLogType())) {
                result.add(log);
            }
        }
        if (result.isEmpty()) {
            throw new NoSuchElementException("No logs found for type: " + type);
        }
        return result;
    }

    /**
     * Checks whether a given log exists in the repository.
     *
     * @param log the log to check
     * @return true if the log exists, false otherwise
     * @throws IllegalArgumentException if the log is null
     */
    public boolean exists(Log log) {
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null.");
        }
        return logs.contains(log);
    }

    /**
     * Removes all logs from the repository.
     */
    public void clear() {
        logs.clear();
    }

    /**
     * Counts the total number of logs currently stored.
     *
     * @return total count of logs
     */
    public int count() {
        return logs.size();
    }
}
