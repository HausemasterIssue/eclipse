package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.HoleManager;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.RotationUtils;
import xyz.aesthetical.eclipse.util.WorldUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Module.Mod(name = "Surround", description = "Surrounds you with obsidian")
@Module.Info(category = Module.Category.COMBAT)
public class Surround extends Module {
    public final Setting<Boolean> center = register(new Setting<>("Center", false).setDescription("If to center you in the middle of the block"));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to send rotation packets when placing the blocks"));
    public final NumberSetting rotationPackets = register(new NumberSetting("Rotation Packets", 2).setMin(0).setMax(10).setDescription("How many rotation packets to send"));
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing your arm client side"));

    @Override
    public void onEnabled() {
        EnumHand hand = EnumHand.MAIN_HAND;
        int oldSlot = -1;

        ItemStack offHand = Eclipse.mc.player.getHeldItemOffhand();
        if (!offHand.isEmpty() && offHand.getItem() instanceof ItemBlock && ((ItemBlock) offHand.getItem()).getBlock() == Blocks.OBSIDIAN) {
            hand = EnumHand.OFF_HAND;
        } else {
            // @todo make this an inventory util
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.OBSIDIAN) {
                    oldSlot = Eclipse.mc.player.inventory.currentItem;
                    Eclipse.mc.player.inventory.currentItem = i;
                    break;
                }
            }
        }

        if (oldSlot == -1 && hand != EnumHand.OFF_HAND) {
            return;
        }

        if (center.getValue()) {
            Eclipse.mc.player.motionX = 0.0;
            Eclipse.mc.player.motionZ = 0.0;

            WorldUtils.center();
        }

        BlockPos actualBlockPos = new BlockPos(Eclipse.mc.player.getPositionVector());
        for (BlockPos pos : Arrays.stream(HoleManager.SURROUNDING_POSITIONS).map(actualBlockPos::add).collect(Collectors.toList())) {
            if (!Eclipse.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
                continue;
            }

            if (rotate.getValue()) {
                for (int i = 0; i < rotationPackets.getValue().intValue(); ++i) {
                    RotationUtils.rotate(pos, true);
                }
            }

            WorldUtils.place(pos, hand, swing.getValue(), true);
        }

        Eclipse.mc.player.inventory.currentItem = oldSlot;

        toggle();
    }
}
