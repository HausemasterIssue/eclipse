package xyz.aesthetical.astra.events.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class ConnectionEvent extends Event {
    private final Action action;
    private final EntityPlayer player;
    private final UUID uuid;
    private final String username;

    public ConnectionEvent(Action action, EntityPlayer player, UUID uuid, String username) {
        this.action = action;
        this.player = player;
        this.uuid = uuid;
        this.username = username;
    }

    public Action getAction() {
        return action;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public enum Action {
        CONNECT,
        DISCONNECT
    }
}
