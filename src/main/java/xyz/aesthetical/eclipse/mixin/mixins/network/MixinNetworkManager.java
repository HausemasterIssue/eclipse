package xyz.aesthetical.eclipse.mixin.mixins.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.eclipse.events.network.PacketEvent;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager extends SimpleChannelInboundHandler<Packet<?>> {
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo info) throws Exception {
        PacketEvent.Inbound event = new PacketEvent.Inbound(p_channelRead0_2_);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    public void onSendPacket(Packet<?> packetIn, CallbackInfo info) {
        PacketEvent.Outbound event = new PacketEvent.Outbound(packetIn);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }
}
