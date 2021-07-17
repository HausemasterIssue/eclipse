package xyz.aesthetical.eclipse.features.modules.render;

import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "CameraClip", description = "Allows you to clip your camera through blocks")
@Module.Info(category = Module.Category.RENDER)
public class CameraClip extends Module {
    public static CameraClip instance;

    public final NumberSetting range = register(new NumberSetting("Range", 10.0f).setMax(1.0f).setMax(50.0f).setDescription("How far to clip your camera"));

    public CameraClip() {
        instance = this;
    }
}
