package xyz.aesthetical.astra.events.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PushEvent extends Event {
    private final Type type;
    private final EntityPlayer player;

    public PushEvent(Type type, EntityPlayer player) {
        this.type = type;
        this.player = player;
    }

    public Type getType() {
        return type;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public enum Type {
        LIQUID,
    }
}
