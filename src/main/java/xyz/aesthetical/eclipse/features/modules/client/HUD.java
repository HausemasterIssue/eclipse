package xyz.aesthetical.eclipse.features.modules.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.gui.hudeditor.GuiHUDEditor;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.HudElement;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "HUD", description = "Shows the client hud")
@Module.Info(category = Module.Category.CLIENT, preToggled = true)
public class HUD extends Module {
    @SubscribeEvent
    public void onRenderHudText(RenderGameOverlayEvent.Text event) {
        if (Module.fullNullCheck()) {
            GlStateManager.pushMatrix();

            if (GuiHUDEditor.instance != null) {
                for (HudElement element : GuiHUDEditor.getInstance().getElements()) {
                    if (!element.isEnabled()) {
                        continue;
                    }

                    element.draw(0, 0, event.getPartialTicks());
                }
            }

            GlStateManager.popMatrix();
        }
    }
}
