package xyz.aesthetical.astra.mixin.mixins.render;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.astra.features.modules.render.NoRender;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    public void renderParticles(Entity entityIn, float partialTicks, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.particles.getValue()) {
            info.cancel();
        }
    }
}
