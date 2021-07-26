package xyz.aesthetical.astra.managers.friends;

import java.util.UUID;

public class Friend {
    private final UUID uuid;
    private final String alias;

    public Friend(UUID uuid) {
        this.uuid = uuid;
        this.alias = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Friend)) {
            return false;
        }

        return ((Friend) o).getUuid().equals(getUuid());
    }
}
