package xyz.aesthetical.eclipse.mixin.mixins.network.packets.inbound;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketExplosion.class)
public interface ISPacketExplosion {
    @Accessor("motionX")
    void setMotionX(float motionX);

    @Accessor("motionY")
    void setMotionY(float motionY);

    @Accessor("motionZ")
    void setMotionZ(float motionZ);
}
