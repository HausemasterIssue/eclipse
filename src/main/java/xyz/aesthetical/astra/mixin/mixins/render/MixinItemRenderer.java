package xyz.aesthetical.astra.mixin.mixins.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.astra.features.modules.render.NoRender;
import xyz.aesthetical.astra.features.modules.render.ViewModel;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    public void onRenderFireInFirstPerson(CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.fire.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "transformFirstPerson", at = @At("HEAD"))
    public void onTransformFirstPerson(EnumHandSide handSide, float p_187459_2_, CallbackInfo info) {
        if (ViewModel.instance.isToggled()) {
            Vec3d offset = ViewModel.instance.getOffsets(handSide);
            GlStateManager.translate(offset.x, offset.y, offset.z);
            // @todo GlStateManager#scale and rotate for ViewModel
        }
    }

    @Inject(method = "transformSideFirstPerson", at = @At("HEAD"), cancellable = true)
    public void onTransformSideFirstPerson(EnumHandSide handSide, float p_187459_2_, CallbackInfo info) {
        if (ViewModel.instance.isToggled()) {
            Vec3d offset = ViewModel.instance.getOffsets(handSide);
            GlStateManager.translate(offset.x, offset.y, offset.z);
            // @todo GlStateManager#scale and rotate for ViewModel
        }
    }
}
