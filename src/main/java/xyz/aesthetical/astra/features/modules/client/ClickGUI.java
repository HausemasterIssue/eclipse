package xyz.aesthetical.astra.features.modules.client;

import org.lwjgl.input.Keyboard;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.gui.clickgui.ClickGuiScreen;
import xyz.aesthetical.astra.managers.modules.Module;

import java.awt.*;

@Module.Mod(name = "ClickGUI", description = "Opens up the ClickGUI")
@Module.Info(category = Module.Category.CLIENT, bind = Keyboard.KEY_RSHIFT)
public class ClickGUI extends Module {
    public static ClickGUI instance;

    public final Setting<Boolean> pause = register(new Setting<>("Pause", false).setDescription("If the ClickGUI should pause the game."));
    public final Setting<Boolean> outline = register(new Setting<>("Outline", false).setDescription("If to draw an outline around the containers").setGroup("outline"));
    public final Setting<Color> outlineColor = register(new Setting<>("Outline Color", new Color(255, 255, 255, 255)).setDescription("The color to use in the outline").setGroup("outline"));

    public ClickGUI() {
        instance = this;
    }

    @Override
    public void onEnabled() {
        if (Module.fullNullCheck()) {
            Astra.mc.displayGuiScreen(ClickGuiScreen.getInstance());
        }

        toggle();
    }
}
