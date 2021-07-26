package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.RotationUtils;
import xyz.aesthetical.astra.util.Timer;
import xyz.aesthetical.astra.util.WorldUtils;

import java.util.ArrayList;

@Module.Mod(name = "AutoTunnel", description = "Automatically digs a tunnel")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class AutoTunnel extends Module {
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to send rotation packets"));
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing your arm when breaking the block"));
    public final NumberSetting delay = register(new NumberSetting("Delay", 0.0f).setMin(0.0f).setMax(5.0f).setDescription("How long to wait in S until breaking the next block"));
    public final Setting<Boolean> pauseAutoWalk = register(new Setting<>("Pause AutoWalk", false).setDescription("If AutoWalk is on the vanilla setting, pause it when mining"));

    private BlockPos breakPos = null;
    private final ArrayList<BlockPos> blocks = new ArrayList<>();
    private final Timer timer = new Timer();

    @Override
    public void onEnabled() {
        breakPos = null;
        blocks.clear();
        timer.reset();
    }

    @Override
    public void onDisabled() {
        breakPos = null;
        blocks.clear();
        timer.reset();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (!blocks.isEmpty()) {
                if (breakPos != null) {
                    boolean keepBreaking = Astra.mc.playerController.onPlayerDamageBlock(breakPos, WorldUtils.getFacing(breakPos));
                    if (!keepBreaking) {
                        blocks.remove(breakPos);
                        breakPos = null;
                        return;
                    }

                    if (rotate.getValue()) {
                        RotationUtils.rotate(breakPos, true);
                    }

                    if (swing.getValue()) {
                        WorldUtils.swingArm(EnumHand.MAIN_HAND);
                    }
                } else {
                    if (timer.passedS(delay.getValue().doubleValue())) {
                        timer.reset();

                        for (int i = 0; i < blocks.size(); ++i) {
                            BlockPos pos = blocks.get(i);
                            if (WorldUtils.getBlockFromPos(pos) == Blocks.AIR) {
                                blocks.remove(pos);
                                continue;
                            }

                            breakPos = pos;
                        }
                    }
                }
            } else {
                EnumFacing facing = Astra.mc.player.getHorizontalFacing();
                BlockPos actualPos = new BlockPos(Astra.mc.player.getPositionVector());

                if (WorldUtils.getBlockFromPos(actualPos.offset(facing)) != Blocks.AIR) {
                    blocks.add(actualPos.offset(facing));
                }

                if (WorldUtils.getBlockFromPos(actualPos.offset(facing).up()) != Blocks.AIR) {
                    blocks.add(actualPos.offset(facing).up());
                }
            }
        }
    }

    private boolean shouldBreak() {
        return Astra.mc.player.movementInput.moveForward == 0.0f && Astra.mc.player.movementInput.moveStrafe == 0.0f;
    }
}
