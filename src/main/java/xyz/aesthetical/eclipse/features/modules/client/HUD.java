package xyz.aesthetical.eclipse.features.modules.client;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.modules.Module;

import java.awt.*;

@Module.Mod(name = "HUD", description = "Shows the client hud")
@Module.Info(category = Module.Category.CLIENT, preToggled = true)
public class HUD extends Module {
    @SubscribeEvent
    public void onRenderHudText(RenderGameOverlayEvent.Text event) {
        GlStateManager.pushMatrix();

        ScaledResolution resolution = event.getResolution();

        // @todo make this a hud element
        if (Eclipse.serverManager.isServerUnresponsive()) {
            float timeNotResponding = Eclipse.serverManager.getServerRespondingTime() / 1000.0f;
            String display = Eclipse.serverManager.format(timeNotResponding);

            Eclipse.textManager.drawCentered("Server has not been responding for " + display + "s", resolution.getScaledWidth() / 2.0f, 14.0f, new Color(71, 71, 71).getRGB());
        }

        GlStateManager.popMatrix();
    }
}
