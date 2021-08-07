package xyz.aesthetical.astra.features.modules.render;

import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.EnumConverter;

@Module.Mod(name = "Chams", description = "See entities through walls")
@Module.Info(category = Module.Category.RENDER)
public class Chams extends Module {
    public static Chams instance;

    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.NORMAL).setDescription("How to render the chams"));
    public final Setting<Boolean> crystals = register(new Setting<>("Crystals", false).setDescription("If to render Crystal Chams"));

    public Chams() {
        instance = this;
    }

    @Override
    public String getDisplay() {
        return EnumConverter.getActualName(mode.getValue());
    }

    public enum Mode {
        NORMAL,
        WALLHACK
    }
}
