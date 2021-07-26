package xyz.aesthetical.eclipse.mixin.mixins.render.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.modules.miscellaneous.AutoReconnect;
import xyz.aesthetical.eclipse.util.Timer;

import java.io.IOException;

@Mixin(GuiDisconnected.class)
public class MixinGuiDisconnected extends GuiScreen {
    @Shadow
    private int textHeight;

    private final Timer timer = new Timer();
    private GuiButton autoReconnectButton;

    @Inject(method = "initGui", at = @At("TAIL"))
    public void onInitGui(CallbackInfo info) {
        timer.reset();

        if (Eclipse.serverManager.getLastServer() != null) {
            addButton(new GuiButton(1, width / 2 - 100, Math.min(height / 2 + textHeight / 2 + fontRenderer.FONT_HEIGHT + 25, height - 55), "Reconnect"));
            addButton(autoReconnectButton = new GuiButton(2, width / 2 - 100, Math.min(height / 2 + textHeight / 2 + fontRenderer.FONT_HEIGHT + 50, height - 80), "AutoReconnect"));
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void onActionPerformed(GuiButton button, CallbackInfo info) throws IOException {
        if (Eclipse.serverManager.getLastServer() != null) {
            if (button.id == 1) {
                timer.reset();
                mc.displayGuiScreen(new GuiConnecting(this, mc, Eclipse.serverManager.getLastServer()));
            } else if (button.id == 2) {
                Eclipse.moduleManager.getModule(AutoReconnect.class).toggle();
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (Eclipse.serverManager.getLastServer() != null) {
            AutoReconnect module = Eclipse.moduleManager.getModule(AutoReconnect.class);
            if (!module.isToggled()) {
                autoReconnectButton.displayString = "AutoReconnect \u00A7cOff\u00A7r";
            } else {
                autoReconnectButton.displayString = "AutoReconnect \u00A7a" + Eclipse.serverManager.format(timer.getPassedTimeMs() * 1000L) + "s\u00A7r";

                if (timer.passedMs(module.delay.getValue().longValue())) {
                    mc.displayGuiScreen(new GuiConnecting(this, mc, Eclipse.serverManager.getLastServer()));
                }
            }
        }
    }
}
