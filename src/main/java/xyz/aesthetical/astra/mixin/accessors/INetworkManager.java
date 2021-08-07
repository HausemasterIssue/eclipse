package xyz.aesthetical.astra.mixin.accessors;

import net.minecraft.network.Packet;

public interface INetworkManager {
    void forceSendPacket(Packet<?> packet);
}
