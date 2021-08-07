package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.util.EnumHand;
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
import xyz.aesthetical.astra.mixin.mixins.entity.IPlayerControllerMP;
import xyz.aesthetical.astra.util.*;

import java.util.ArrayList;

@Module.Mod(name = "Nuker", description = "Breaks blocks around you")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class Nuker extends Module {
    public final NumberSetting range = register(new NumberSetting("Range", 5).setMin(1).setMax(10).setDescription("How far to try to break blocks"));
    public final NumberSetting delay = register(new NumberSetting("Delay", 0.5f).setMin(0.0f).setMax(5.0f).setDescription("How long to wait in S before breaking another block"));
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing client side"));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to send a rotation packet"));
    public final Setting<Boolean> panic = register(new Setting<>("Panic", true).setDescription("Turn off Nuker when the world unloads"));
    public final Setting<Boolean> visualize = register(new Setting<>("Visualize", true).setDescription("If to visualize the block attempting to be broken"));

    private final Timer timer = new Timer();
    private ArrayList<BlockPos> blocks = new ArrayList<>();
    private BlockPos breakingPos = null;

    @Override
    public String getDisplay() {
        return String.valueOf(range.getValue());
    }

    @Override
    public void onDisabled() {
        blocks.clear();
        breakingPos = null;
        timer.reset();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (panic.getValue()) {
            toggle();
        }
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && visualize.getValue() && breakingPos != null) {
            RenderUtils.drawFilledBox(new AxisAlignedBB(breakingPos).offset(RenderUtils.getCameraPos()), ColorUtils.toRGBA(255, 255, 255, 80));
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && !Astra.moduleManager.getModule(Freecam.class).isToggled()) {
            if (breakingPos == null || blocks.isEmpty()) {
                blocks = WorldUtils.getDisc(Astra.mc.player.getPosition(), range.getValue().intValue(), -1);
                if (blocks.isEmpty() || blocks.stream().allMatch(pos -> Astra.mc.world.getBlockState(pos).getMaterial().isReplaceable())) {
                    return;
                }

                breakingPos = blocks.get(0);
                blocks.remove(breakingPos);
            }

            if (Astra.mc.world.getBlockState(breakingPos).getMaterial().isReplaceable()) {
                if (timer.passedS(delay.getValue().doubleValue()) && !blocks.isEmpty()) {
                    timer.reset();
                    stop();

                    breakingPos = blocks.isEmpty() ? null : blocks.get(0);

                    if (breakingPos == null) {
                        blocks.clear();
                        timer.reset();

                        IPlayerControllerMP controllerMP = (IPlayerControllerMP) Astra.mc.playerController;

                        if (controllerMP.getCurBlockDamageMP() != 0.0f && !controllerMP.getCurrentBlock().equals(new BlockPos(-1, -1, -1))) {
                            stop();
                        }
                    } else {
                        blocks.remove(breakingPos);
                    }
                }
            }

            if (breakingPos != null) {
                boolean keepBreaking = Astra.mc.playerController.onPlayerDamageBlock(breakingPos, WorldUtils.getFacing(breakingPos));
                if (!keepBreaking) {
                    breakingPos = null;
                    return;
                }

                if (swing.getValue()) {
                    Astra.mc.player.swingArm(EnumHand.MAIN_HAND);
                }

                if (rotate.getValue()) {
                    RotationUtils.rotate(breakingPos, true);
                }
            }
        }
    }

    private void stop() {
        Astra.mc.playerController.resetBlockRemoving();
    }
}
