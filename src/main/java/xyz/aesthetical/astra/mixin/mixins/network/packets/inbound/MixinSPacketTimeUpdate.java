package xyz.aesthetical.astra.mixin.mixins.network.packets.inbound;

import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.astra.features.modules.miscellaneous.Time;

@Mixin(SPacketTimeUpdate.class)
public abstract class MixinSPacketTimeUpdate implements Packet<INetHandlerPlayClient> {
    public void onProcessPacket(INetHandlerPlayClient handler, CallbackInfo info) {
        if (Time.instance.isToggled()) {
            info.cancel();
        }
    }
}
