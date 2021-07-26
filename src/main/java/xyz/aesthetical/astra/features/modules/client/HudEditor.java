package xyz.aesthetical.astra.features.modules.client;

import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.gui.hudeditor.GuiHUDEditor;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "HudEditor", description = "Edits the client's hud components")
@Module.Info(category = Module.Category.CLIENT)
public class HudEditor extends Module {
    @Override
    public void onEnabled() {
        Astra.mc.displayGuiScreen(GuiHUDEditor.getInstance());
        toggle();
    }
}
