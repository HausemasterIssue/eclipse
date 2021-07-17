package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.commands.Command;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.mixin.mixins.IMinecraft;
import xyz.aesthetical.eclipse.util.RotationUtils;
import xyz.aesthetical.eclipse.util.WorldUtils;

@Module.Mod(name = "Burrow", description = "Puts you inside of a block")
@Module.Info(category = Module.Category.COMBAT)
public class Burrow extends Module {
    private static final double[] BURROW_PACKET_Y = new double[] {
            0.41999998688698,
            0.7531999805211997,
            1.00133597911214,
            1.16610926093821
    };

    public final Setting<BlockType> block = register(new Setting<>("Block", BlockType.ENDER_CHEST).setDescription("What block to burrow in"));
    public final Setting<JumpMode> jumpMode = register(new Setting<>("Jump Mode", JumpMode.PACKET).setDescription("How to advance up"));
    public final Setting<Boolean> center = register(new Setting<>("Center", true).setDescription("If to center yourself on the block you're standing on"));
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing when placing the blocks"));
    public final Setting<Boolean> sneak = register(new Setting<>("Sneak", true).setDescription("If to send a sneak packet when placing the block"));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to send a rotation packet"));
    public final NumberSetting rotationPackets = register(new NumberSetting("Rotation Packets", 1).setMin(1).setMax(10).setDescription("How many rotation packets to send").setVisibility((m) -> rotate.getValue()));
    public final NumberSetting offset = register(new NumberSetting("Offset", 3.0f).setMin(-5.0f).setMax(5.0f).setDescription("How much to lag back"));

    private BlockPos placePos = null;
    private int oldSlot = -1;

    @Override
    public void onEnabled() {
        if (Module.fullNullCheck()) {
//            if (isBurrowed(new BlockPos(Eclipse.mc.player.getPositionVector()).add(0, 1, 0))) {
//                Command.send(new TextBuilder().append(ChatColor.Dark_Gray, "You are already burrowed in a block. Toggling..."));
//                toggle();
//                return;
//            }

            int oldSlot = switchToBlock();
            if (oldSlot == -1) {
                Command.send(new TextBuilder().append(ChatColor.Dark_Gray, "No block found in hotbar, toggling..."));
                toggle();
                return;
            }

            placePos = new BlockPos(Eclipse.mc.player.getPositionVector());
            this.oldSlot = oldSlot;
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player && placePos != null && oldSlot != -1) {
            doBurrow(placePos, oldSlot);

            placePos = null;
            oldSlot = -1;

            toggle();
        }
    }

    private void doBurrow(BlockPos origin, int oldSlot) {
        Eclipse.mc.player.motionX = 0.0;
        Eclipse.mc.player.motionZ = 0.0;

        if (center.getValue()) {
            WorldUtils.center();
        }

        if (jumpMode.getValue() == JumpMode.VANILLA) {
            Eclipse.mc.player.jump();
        } else if (jumpMode.getValue() == JumpMode.PACKET) {
            for (double y : BURROW_PACKET_Y) {
                Eclipse.mc.player.connection.sendPacket(new CPacketPlayer.Position(Eclipse.mc.player.posX, Eclipse.mc.player.posY + y, Eclipse.mc.player.posZ, true));
            }
        }

        if (rotate.getValue()) {
            for (int i = 0; i < rotationPackets.getValue().intValue(); ++i) {
                RotationUtils.rotate(origin, true);
            }
        }

        WorldUtils.place(origin, EnumHand.MAIN_HAND, swing.getValue(), sneak.getValue());
        ((IMinecraft) Eclipse.mc).setRightClickDelayTimer(4);

        Eclipse.mc.player.inventory.currentItem = oldSlot;

        Eclipse.mc.player.connection.sendPacket(new CPacketPlayer.Position(Eclipse.mc.player.posX, Eclipse.mc.player.posY + offset.getValue().doubleValue(), Eclipse.mc.player.posZ, false));

        if (sneak.getValue() && !Eclipse.mc.player.isSneaking()) {
            Eclipse.mc.player.connection.sendPacket(new CPacketEntityAction(Eclipse.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            Eclipse.mc.player.setSneaking(true);

            Eclipse.mc.playerController.updateController();

            Eclipse.mc.player.connection.sendPacket(new CPacketEntityAction(Eclipse.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            Eclipse.mc.player.setSneaking(false);

            Eclipse.mc.playerController.updateController();
        }
    }

    private int switchToBlock() {
        if (block.getValue() == BlockType.HELD) {
            if (Eclipse.mc.player.inventory.getCurrentItem().isEmpty() || !(Eclipse.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBlock)) {
                return -1;
            }
        }

        Block b = block.getValue() == BlockType.OBSIDIAN ? Blocks.OBSIDIAN : Blocks.ENDER_CHEST;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == b) {
                int oldSlot = Eclipse.mc.player.inventory.currentItem;
                Eclipse.mc.player.inventory.currentItem = i;
                return oldSlot;
            }
        }

        return -1;
    }

    public boolean isBurrowed(BlockPos pos) {
        return new AxisAlignedBB(pos).intersects(Eclipse.mc.player.getEntityBoundingBox());
    }

    public enum JumpMode {
        PACKET,
        VANILLA
    }

    public enum BlockType {
        OBSIDIAN,
        ENDER_CHEST,
        HELD
    }
}
