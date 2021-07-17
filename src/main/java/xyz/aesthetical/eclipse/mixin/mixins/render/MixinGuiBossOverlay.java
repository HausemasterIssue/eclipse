package xyz.aesthetical.eclipse.mixin.mixins.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.eclipse.features.modules.render.NoRender;

@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay extends Gui {
    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    public void renderBossHealth(CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.overlay.getValue()) {
            info.cancel();
        }
    }
}
