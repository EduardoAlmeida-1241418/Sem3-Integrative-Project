package pt.ipp.isep.dei.domain;

/**
 * Represents a log entry used for recording system messages.
 * Each log includes a message, its severity or category ({@link LogType}),
 * and the role responsible for generating it ({@link RoleType}).
 */
public class Log {
    private String message;
    private LogType logType;
    private RoleType roleType;

    /**
     * Constructs a {@code Log} with the given message, type, and user role.
     *
     * @param message  the message content of the log entry
     * @param logType  the type or severity of the log entry
     * @param roleType the role of the user or system component creating the log
     */
    public Log(String message, LogType logType, RoleType roleType) {
        this.message = message;
        this.logType = logType;
        this.roleType = roleType;
    }

    /**
     * @return the log message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the new log message content
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the type or category of this log
     */
    public LogType getLogType() {
        return logType;
    }

    /**
     * @param logType the type or severity to set for this log
     */
    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    /**
     * @return the role associated with the log event
     */
    public RoleType getRoleType() {
        return roleType;
    }

    /**
     * @param roleType the role to assign to this log entry
     */
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    /**
     * @return a formatted string representation of the log entry
     */
    @Override
    public String toString() {
        return logType + ": " + message;
    }
}
