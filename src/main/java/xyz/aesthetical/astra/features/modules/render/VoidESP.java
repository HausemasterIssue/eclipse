package xyz.aesthetical.astra.features.modules.render;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.render.RenderEvent;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.ColorUtils;
import xyz.aesthetical.astra.util.RenderUtils;
import xyz.aesthetical.astra.util.Timer;
import xyz.aesthetical.astra.util.WorldUtils;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Module.Mod(name = "VoidESP", description = "Displays where you could fall into the void")
@Module.Info(category = Module.Category.RENDER)
public class VoidESP extends Module {
    public final NumberSetting range = register(new NumberSetting("Range", 5).setMin(1).setMin(15).setDescription("How far to look for void holes"));
    public final NumberSetting delay = register(new NumberSetting("Delay", 1000.0f).setMin(0.0f).setMax(5000.0f).setDescription("How long to wait in MS before searching for more void holes"));
    public final Setting<Color> color = register(new Setting<>("Color", new Color(255, 0, 0)).setDescription("What color to highlight void holes in"));

    private List<BlockPos> shitters;
    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        shitters.clear();
        timer.reset();
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && shitters != null && !shitters.isEmpty()) {
            for (int i = 0; i < shitters.size(); ++i) {
                BlockPos pos = shitters.get(i);
                RenderUtils.drawFilledBox(new AxisAlignedBB(pos).offset(RenderUtils.getCameraPos()), ColorUtils.toRGBA(color.getValue().getRed(), color.getValue().getGreen(), color.getValue().getBlue(), 80));
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && timer.passedMs(delay.getValue().longValue())) {
            timer.reset();

            shitters = WorldUtils.getSphere(Astra.mc.player.getPosition(), range.getValue().intValue(), range.getValue().intValue(), false, true, 0)
                    .stream()
                    .filter((pos) -> pos.getY() < 1 && pos.getZ() >= 0 && Astra.mc.world.getBlockState(pos).getBlock() == Blocks.AIR)
                    .collect(Collectors.toList());
        }
    }
}
