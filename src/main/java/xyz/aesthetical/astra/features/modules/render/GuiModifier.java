package xyz.aesthetical.astra.features.modules.render;

import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "GuiModifier", description = "Modifies how GUIs look")
@Module.Info(category = Module.Category.RENDER)
public class GuiModifier extends Module {
    public static GuiModifier instance;

    public final Setting<Background> background = register(new Setting<>("Background", Background.DEFAULT).setDescription("How the gui background should be"));
    public final Setting<Particles> particles = register(new Setting<>("Particles", Particles.NONE).setDescription("The particles in the background"));
    public final Setting<Boolean> customFont = register(new Setting<>("Custom Font", false).setDescription("If to use a custom font instead of minecrafts default one in guis"));

    // tint
    public final NumberSetting tint = register(new NumberSetting("Tint", 0).setMin(0).setMax(5).setDescription("The tint of the default background").setVisibility((m) -> background.getValue() == Background.DEFAULT));

    // blur
    public final NumberSetting intensity = register(new NumberSetting("Intensity", 9.0f).setMin(1.0f).setMax(12.0f).setDescription("The intensity of the blur").setVisibility((m) -> background.getValue() == Background.BLUR));
    public final NumberSetting fadeTime = register(new NumberSetting("Fade Time", 200).setMin(1).setMax(1500).setDescription("How long it should take in MS to fade the blur").setVisibility((m) -> background.getValue() == Background.BLUR));

    public GuiModifier() {
        instance = this;
    }

    public enum Background {
        NONE,
        DEFAULT,
        BLUR
    }

    public enum Particles {
        NONE,
        SNOW,
        WTF_IS_THIS_CALLED
    }
}
