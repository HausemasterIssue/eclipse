package xyz.aesthetical.eclipse.features.modules.render;

import net.minecraft.util.math.BlockPos;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

import java.awt.*;

@Module.Mod(name = "CityESP", description = "Displays where if a block is broken, someone can be cited")
@Module.Info(category = Module.Category.RENDER)
public class CityESP extends Module {
    private static final BlockPos[] CITY_OFFSETS = new BlockPos[] {
            new BlockPos(-1, 0, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, -1)
    };

    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.FILLED).setDescription("How to render blocks that someone can be cityed with"));
    // @todo public final Setting<Boolean> burrow = register(new Setting<>("Burrow", false).setDescription("If to check for burrowed players"));
    public final Setting<Boolean> self = register(new Setting<>("Self", false).setDescription("If to show where you could be cityed"));
    public final Setting<Boolean> oneDotThirteen = register(new Setting<>("1.13", false).setDescription("If 1.13 placement of crystals is allowed"));
    public final Setting<Color> color = register(new Setting<>("Color", new Color(255, 60, 0)).setDescription("The color to render city blocks"));

    // there was code here but it was so shit that it didnt work, so thats another @todo on my list

    public enum Mode {
        FILLED,
        OUTLINE,
        FILLED_OUTLINE
    }
}
