package xyz.aesthetical.astra.features.modules.render;

import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "GuiModifier", description = "Modifies how GUIs work")
@Module.Info(category = Module.Category.RENDER)
public class GuiModifier extends Module {
    public static GuiModifier instance;

    public final Setting<Background> background = register(new Setting<>("Background", Background.NONE).setDescription("The background of the GUI"));
    public final NumberSetting intensity = register(new NumberSetting("Intensity", 6.0f).setMin(1.0f).setMax(12.0f).setDescription("How intense the blur is"));

    public GuiModifier() {
        instance = this;
    }

    public enum Background {
        DEFAULT,
        NONE,
        BLUR
    }
}
