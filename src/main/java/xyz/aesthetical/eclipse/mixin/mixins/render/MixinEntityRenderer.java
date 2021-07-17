package xyz.aesthetical.eclipse.mixin.mixins.render;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.eclipse.features.modules.render.CameraClip;
import xyz.aesthetical.eclipse.features.modules.render.NoRender;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements IResourceManagerReloadListener {
    @Shadow
    private ItemStack itemActivationItem;

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void onHurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (NoRender.instance.isToggled() && NoRender.instance.hurtCamera.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderItemActivation", at = @At("HEAD"), cancellable = true)
    private void renderItemActivation(int p_190563_1_, int p_190563_2_, float p_190563_3_, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.totemUse.getValue() && itemActivationItem != null && !itemActivationItem.isEmpty() && itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private void setupFog(int startCoords, float partialTicks, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.fog.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "addRainParticles", at = @At("HEAD"), cancellable = true)
    public void onAddRainParticles(CallbackInfo ci) {
        if (NoRender.instance.isToggled() && NoRender.instance.weather.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderRainSnow", at = @At("HEAD"), cancellable = true)
    public void onRenderRainSnow(CallbackInfo ci) {
        if (NoRender.instance.isToggled() && NoRender.instance.weather.getValue()) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), ordinal = 3, require = 1)
    private double preOrientCamera(double range) {
        return CameraClip.instance.isToggled() ? CameraClip.instance.range.getValue().floatValue() : range;
    }

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), ordinal = 7, require = 1)
    private double postOrientCamera(double range) {
        return CameraClip.instance.isToggled() ? CameraClip.instance.range.getValue().floatValue() : range;
    }
}
