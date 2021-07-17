package xyz.aesthetical.eclipse.features.modules.render;

import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "Chams", description = "See entities through walls")
@Module.Info(category = Module.Category.RENDER)
public class Chams extends Module {
    public static Chams instance;

    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.NORMAL).setDescription("How to render the chams"));

    public Chams() {
        instance = this;
    }

    public enum Mode {
        NORMAL,
        WALLHACK
    }
}
