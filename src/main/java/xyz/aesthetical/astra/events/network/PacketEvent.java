package xyz.aesthetical.astra.events.network;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketEvent extends Event {
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public boolean isTypeOf(Class<? extends Packet<?>> clazz) {
        return clazz.isInstance(packet);
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public static class Inbound extends PacketEvent {
        public Inbound(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Outbound extends PacketEvent {
        public Outbound(Packet<?> packet) {
            super(packet);
        }
    }
}
