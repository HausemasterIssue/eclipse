package xyz.aesthetical.eclipse.mixin.mixins.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.eclipse.features.modules.render.NoRender;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {
    @Inject(method = "renderPotionEffects", at = @At("HEAD"), cancellable = true)
    protected void renderPotionEffects(ScaledResolution resolution, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.overlay.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    protected void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.overlay.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    protected void renderPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.pumpkin.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true)
    protected void renderPortal(float timeInPortal, ScaledResolution scaledRes, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.portals.getValue()) {
            info.cancel();
        }
    }
}
