package xyz.aesthetical.eclipse.managers.notifications;

import java.util.List;

public class Notification {
    private final String name;
    private final List<String> lines;
    private final Type type;
    private final long startedAt;
    private final long expiresIn;

    public Notification(String name, List<String> lines, Type type) {
        this(name, lines, type, -1);
    }

    public Notification(String name, List<String> lines, Type type, long expiresIn) {
        this.name = name;
        this.lines = lines;
        this.type = type;
        this.startedAt = System.currentTimeMillis();
        this.expiresIn = expiresIn;
    }

    public String getName() {
        return name;
    }

    public List<String> getLines() {
        return lines;
    }

    public Type getType() {
        return type;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public boolean hasExpired() {
        if (expiresIn == -1L) {
            return false;
        }

        return System.currentTimeMillis() >= startedAt + expiresIn;
    }

    public enum Type {
        NEUTRAL,
        ERROR,
        WARNING,
        OTHER;
    }
}
