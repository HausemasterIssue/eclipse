package xyz.aesthetical.astra.mixin.mixins.network.packets.outbound;

import net.minecraft.network.play.client.CPacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketChatMessage.class)
public interface ICPacketChatMessage {
    @Accessor("message")
    void setMessage(String message);
}
