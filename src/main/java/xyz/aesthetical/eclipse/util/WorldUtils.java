package xyz.aesthetical.eclipse.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import xyz.aesthetical.eclipse.Eclipse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldUtils {
    public static ArrayList<BlockPos> getSphere(BlockPos pos, int radius, int height, boolean hollow, boolean sphere, int yOffset) {
        final ArrayList<BlockPos> blocks = new ArrayList<>();
        float cx = pos.getX(), cy = pos.getY(), cz = pos.getZ(), x = cx - radius;

        while (x <= cx + radius) {
            float z = cz - radius;
            while (z <= cz + radius) {
                float y = sphere ? cy - radius : cy;
                while (true) {
                    float f = y;
                    float f2 = sphere ? cy + radius : (cy + height);
                    if (!(f < f2)) {
                        break;
                    }

                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < Math.pow(radius, 2)) || hollow && dist < ((radius - 1f) * (radius - 1f)))) {
                        blocks.add(new BlockPos(x, y + yOffset, z));
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }

        return blocks;
    }

    public static List<BlockPos> getCrystalPlacePositions(Vec3d pos, int range, boolean oneDotThirteen) {
        return getSphere(new BlockPos(pos), range, range, false, true, 0)
                .stream().filter(blockPos -> canCrystalBePlacedAt(blockPos, oneDotThirteen))
                .collect(Collectors.toList());
    }

    public static boolean canCrystalBePlacedAt(BlockPos blockPos, boolean oneDotThirteen) {
        try {
            if (!isValidCrystalPlaceBlock(getBlockFromPos(blockPos))) {
                return false;
            }

            BlockPos pos = blockPos.add(0.0, 1.0, 0.0);
            BlockPos pos1 = blockPos.add(0.0, 2.0, 0.0);

            if ((!oneDotThirteen && getBlockFromPos(pos1) != Blocks.AIR) || getBlockFromPos(pos) != Blocks.AIR) {
                return false;
            }

            for (Entity entity : Eclipse.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                if (entity.isDead || entity instanceof EntityEnderCrystal) {
                    continue;
                }

                return false;
            }

            if (!oneDotThirteen) {
                for (Entity entity : Eclipse.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos1))) {
                    if (entity.isDead || entity instanceof EntityEnderCrystal) {
                        continue;
                    }

                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isValidCrystalPlaceBlock(Block block) {
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK;
    }

    public static Block getBlockFromPos(BlockPos pos) {
        return Eclipse.mc.world.getBlockState(pos).getBlock();
    }

    public static EnumActionResult place(BlockPos pos, EnumHand hand, boolean swing, boolean sneak) {
        EnumFacing facing = getFacing(pos);
        Vec3d hitVector = new Vec3d(pos.add(facing.getDirectionVec())).scale(0.5);

        if (sneak && !Eclipse.mc.player.isSneaking()) {
            Eclipse.mc.player.setSneaking(true);
            Eclipse.mc.player.connection.sendPacket(new CPacketEntityAction(Eclipse.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }

        EnumActionResult result = Eclipse.mc.playerController.processRightClickBlock(Eclipse.mc.player, Eclipse.mc.world, pos, facing, hitVector, hand);

        if (swing) {
            swingArm(hand);
        }

        return result;
    }

    public static EnumFacing getFacing(BlockPos pos) {
        for (EnumFacing direction : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(direction);
            EnumFacing side = direction.getOpposite();

            IBlockState state = Eclipse.mc.world.getBlockState(neighbor);
            if (state.getBlock() == Blocks.AIR || state.getMaterial().isReplaceable()) {
                continue;
            }

            if (state.getMaterial() == Material.WATER || state.getMaterial() == Material.WATER) {
                continue;
            }

            return side;
        }

        return EnumFacing.UP;
    }

    public static void swingArm(EnumHand hand) {
        Eclipse.mc.player.swingArm(hand);
        Eclipse.mc.player.connection.sendPacket(new CPacketAnimation(hand));
    }

    public static void center() {
        BlockPos actualPos = new BlockPos(Eclipse.mc.player.getPositionVector());

        double x = Math.abs(actualPos.getX() - Eclipse.mc.player.posX);
        double z = Math.abs(actualPos.getZ() - Eclipse.mc.player.posZ);
        if (x >= 0.1 && z >= 0.1) {
            Eclipse.mc.player.motionX = (actualPos.getX() - Eclipse.mc.player.posX) / 2.0;
            Eclipse.mc.player.motionZ = (actualPos.getZ() - Eclipse.mc.player.posZ) / 2.0;
        }
    }
}
