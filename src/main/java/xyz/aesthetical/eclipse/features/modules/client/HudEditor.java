package xyz.aesthetical.eclipse.features.modules.client;

import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.gui.hudeditor.GuiHUDEditor;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "HudEditor", description = "Edits the client's hud components")
@Module.Info(category = Module.Category.CLIENT)
public class HudEditor extends Module {
    @Override
    public void onEnabled() {
        Eclipse.mc.displayGuiScreen(GuiHUDEditor.getInstance());
        toggle();
    }
}
