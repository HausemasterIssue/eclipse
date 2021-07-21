package xyz.aesthetical.eclipse.mixin.mixins.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.aesthetical.eclipse.events.entity.PushEvent;

@Mixin(World.class)
public class MixinWorld {
    @Redirect(method = "handleMaterialAcceleration", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
    public boolean handleMaterialAcceleration(Entity entity) {
        if (entity instanceof EntityPlayer) {
            PushEvent event = new PushEvent(PushEvent.Type.LIQUID, (EntityPlayer) entity);
            MinecraftForge.EVENT_BUS.post(event);
            return entity.isPushedByWater() && !event.isCanceled();
        }

        return entity.isPushedByWater();
    }
}
