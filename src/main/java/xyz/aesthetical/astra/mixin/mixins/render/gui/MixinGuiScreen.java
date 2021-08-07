package xyz.aesthetical.astra.mixin.mixins.render.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.aesthetical.astra.features.modules.render.GuiModifier;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui implements GuiYesNoCallback {
    // @todo drawGradientRect make it whatever color idfk (endColor, startColor args)
    @ModifyArg(method = "drawWorldBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawBackground(I)V"), index = 0)
    private int hookDrawWorldBackground(int o, int i) {
        if (GuiModifier.instance.isToggled() && GuiModifier.instance.background.getValue() == GuiModifier.Background.DEFAULT) {
            return GuiModifier.instance.tint.getValue().intValue();
        }

        return o;
    }
}
