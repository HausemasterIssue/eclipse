package xyz.aesthetical.astra.features.modules.render;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.render.RenderEvent;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.HoleManager;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.ColorUtil;
import xyz.aesthetical.astra.util.RenderUtils;

import java.awt.*;

@Module.Mod(name = "HoleESP", description = "Displays where safe holes are")
@Module.Info(category = Module.Category.RENDER)
public class HoleESP extends Module {
    public final Setting<RenderMode> mode = register(new Setting<>("Mode", RenderMode.FILLED).setDescription("How to render the ESP"));
    public final Setting<Boolean> safe = register(new Setting<>("Safe Holes", true).setDescription("If to show safe holes").setGroup("safe"));
    public final Setting<Color> safeColor = register(new Setting<>("Safe Color", new Color(0, 255, 0)).setDescription("The color to render safe holes").setGroup("safe"));
    public final Setting<Boolean> unsafe = register(new Setting<>("Unsafe Holes", true).setDescription("If to show unsafe holes").setGroup("unsafe"));
    public final Setting<Color> unsafeColor = register(new Setting<>("Unsafe Color", new Color(255, 0, 0)).setDescription("The color to render unsafe holes").setGroup("unsafe"));
    public final Setting<Boolean> selfHole = register(new Setting<>("Self Hole", false).setDescription("If to render in your own hole"));

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && !Astra.holeManager.getHoles().isEmpty()) {
            for (HoleManager.HoleInfo info : Astra.holeManager.getHoles()) {
                if ((info.isSafe() && !safe.getValue()) || (!info.isSafe() && !unsafe.getValue())) {
                    continue;
                }

                if (!selfHole.getValue() && info.isEntityInHole()) {
                    continue;
                }

                Color c = info.isSafe() ? safeColor.getValue() : unsafeColor.getValue();

                if (mode.getValue() == RenderMode.FILLED || mode.getValue() == RenderMode.BOTH) {
                    int color = ColorUtil.toRGBA(c.getRed(), c.getGreen(), c.getBlue(), 80);
                    AxisAlignedBB box = new AxisAlignedBB(info.getPos()).offset(RenderUtils.getCameraPos());
                    RenderUtils.drawFilledBox(box, color);
                }

                if (mode.getValue() == RenderMode.OUTLINE || mode.getValue() == RenderMode.BOTH) {

                }
            }
        }
    }

    public enum RenderMode {
        FILLED,
        OUTLINE,
        BOTH
    }
}
