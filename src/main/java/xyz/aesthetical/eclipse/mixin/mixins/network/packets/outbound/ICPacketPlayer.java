package xyz.aesthetical.eclipse.mixin.mixins.network.packets.outbound;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayer.class)
public interface ICPacketPlayer {
    @Accessor("y")
    double getY();

    @Accessor("y")
    void setY(double y);
}
