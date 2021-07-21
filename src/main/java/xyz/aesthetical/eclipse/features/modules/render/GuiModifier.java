package xyz.aesthetical.eclipse.features.modules.render;

import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

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
