package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.modules.miscellaneous.NoSwing;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.HoleManager;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.EntityUtil;
import xyz.aesthetical.eclipse.util.WorldUtils;

// @todo make this pause modules like CrystalAura etc
@Module.Mod(name = "AutoBreaker", description = "Automatically breaks surrounding safe blocks to city another person")
@Module.Info(category = Module.Category.COMBAT)
public class AutoBreaker extends Module {
    public static boolean isBreaking = false;

    public final NumberSetting range = register(new NumberSetting("Range", 5.0f).setMin(0.0f).setMax(6.0f).setDescription("How far to look for blocks to break"));
    public final Setting<Boolean> noSwing = register(new Setting<>("No Swing", false).setDescription("If to turn on NoSwing to mask you breaking the block"));
    public final Setting<Boolean> switchToPickaxe = register(new Setting<>("Auto Switch", true).setDescription("If to automatically switch to a pickaxe"));
    public final Setting<Boolean> onlyWhenStationary = register(new Setting<>("No Move", true).setDescription("If to only break when you're not moving"));

    private BlockPos breakPos = null;
    private boolean stateNoSwingBefore = false;

    @Override
    public void onDisabled() {
        breakPos = null;
        isBreaking = false;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (breakPos != null) {
                if (shouldBreak()) {
                    isBreaking = true;

                    boolean keepBreaking = Eclipse.mc.playerController.onPlayerDamageBlock(breakPos, WorldUtils.getFacing(breakPos));
                    if (!keepBreaking) {
                        breakPos = null;
                        isBreaking = false;

                        if (!stateNoSwingBefore && noSwing.getValue()) {
                            Eclipse.moduleManager.<NoSwing>getModule(NoSwing.class).toggle();
                            stateNoSwingBefore = false;
                        }

                        return;
                    }
                } else {
                    Eclipse.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, breakPos, WorldUtils.getFacing(breakPos)));
                    return;
                }
            }

            // find closest entity
            EntityPlayer possibleTarget = EntityUtil.findTarget(range.getValue().floatValue(), false, false, false, EntityUtil.Sorting.NONE);
            if (possibleTarget == null) {
                return;
            }

            HoleManager.HoleInfo info = Eclipse.holeManager.getEntityHole(possibleTarget);
            if (info != null) {
                EnumFacing opposite = possibleTarget.getHorizontalFacing().getOpposite();
                breakPos = info.getPos().add(opposite.getDirectionVec());

                if (noSwing.getValue()) {
                    NoSwing module = Eclipse.moduleManager.<NoSwing>getModule(NoSwing.class);
                    stateNoSwingBefore = module.isToggled();

                    if (!module.isToggled()) {
                        module.toggle();
                    }
                }
            }
        }
    }

    private boolean shouldBreak() {
        return onlyWhenStationary.getValue() && (Eclipse.mc.player.movementInput.moveForward == 0.0f && Eclipse.mc.player.movementInput.moveStrafe == 0.0f);
    }

    public enum Mode {
        VANILLA,
        PACKET
    }
}
