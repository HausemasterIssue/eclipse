package xyz.aesthetical.eclipse.mixin.mixins.world;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.eclipse.events.world.EntityRemoveEvent;
import xyz.aesthetical.eclipse.events.world.EntitySpawnEvent;

@Mixin(WorldClient.class)
public class MixinWorldClient {
    @Inject(method = "spawnEntity", at = @At("HEAD"))
    public void spawnEntity(Entity entityIn, CallbackInfoReturnable<Boolean> info) {
        MinecraftForge.EVENT_BUS.post(new EntitySpawnEvent(entityIn));
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    public void removeEntity(Entity entityIn, CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new EntityRemoveEvent(entityIn));
    }
}
