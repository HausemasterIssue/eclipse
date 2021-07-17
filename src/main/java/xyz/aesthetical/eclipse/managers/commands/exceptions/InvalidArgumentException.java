package xyz.aesthetical.eclipse.managers.commands.exceptions;

public class InvalidArgumentException extends Exception {
    private final String argName;
    private final String reason;

    public InvalidArgumentException(String argName) {
        this(argName, null);
    }

    public InvalidArgumentException(String argName, String reason) {
        this.argName = argName;
        this.reason = reason;
    }

    public String getArgName() {
        return argName;
    }

    public String getReason() {
        return reason;
    }
}
