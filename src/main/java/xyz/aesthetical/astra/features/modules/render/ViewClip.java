package xyz.aesthetical.astra.features.modules.render;

import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "ViewClip", description = "Allows you to clip your camera through blocks")
@Module.Info(category = Module.Category.RENDER)
public class ViewClip extends Module {
    public static ViewClip instance;

    public final NumberSetting range = register(new NumberSetting("Range", 10.0f).setMax(1.0f).setMax(50.0f).setDescription("How far to clip your camera"));

    public ViewClip() {
        instance = this;
    }
}
