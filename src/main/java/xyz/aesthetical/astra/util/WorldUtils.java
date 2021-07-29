package xyz.aesthetical.astra.util;

import net.minecraft.block.Block;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import xyz.aesthetical.astra.Astra;

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

            for (Entity entity : Astra.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                if (entity.isDead || entity instanceof EntityEnderCrystal) {
                    continue;
                }

                return false;
            }

            if (!oneDotThirteen) {
                for (Entity entity : Astra.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos1))) {
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
        return Astra.mc.world.getBlockState(pos).getBlock();
    }

    public static EnumActionResult place(BlockPos pos, EnumHand hand, boolean swing, boolean sneak) {
        EnumFacing facing = getFacing(pos);
        Vec3d hitVector = new Vec3d(pos.offset(facing))
                .add(0.5, 0.5, 0.5)
                .add(new Vec3d(facing.getOpposite().getDirectionVec()).scale(0.5));

        if (sneak && !Astra.mc.player.isSneaking()) {
            Astra.mc.player.setSneaking(true);
            Astra.mc.player.connection.sendPacket(new CPacketEntityAction(Astra.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }

        EnumActionResult result = Astra.mc.playerController.processRightClickBlock(Astra.mc.player, Astra.mc.world, pos, facing.getOpposite(), hitVector, hand);

        if (swing) {
            swingArm(hand);
        }

        return result;
    }

    public static EnumFacing getFacing(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            Vec3d playerPos = Astra.mc.player.getPositionVector();
            RayTraceResult result = Astra.mc.world.rayTraceBlocks(
                    playerPos.add(0.0, Astra.mc.player.getEyeHeight(), 0.0),
                    new Vec3d(
                            pos.getX() + 0.5 + facing.getDirectionVec().getX() * 1.0 / 2.0,
                            pos.getY() + 0.5 + facing.getDirectionVec().getY() * 1.0 / 2.0,
                            pos.getZ() + 0.5 + facing.getDirectionVec().getZ() * 1.0 / 2.0
                    ),
                    false,
                    true,
                    false
            );

            if (result != null && (result.typeOfHit != RayTraceResult.Type.BLOCK || !result.getBlockPos().equals(pos))) {
                continue;
            }

            return facing;
        }

        if (pos.getY() > Astra.mc.player.posY + Astra.mc.player.getEyeHeight()) {
            return EnumFacing.DOWN;
        }

        return EnumFacing.UP;
    }

    public static void swingArm(EnumHand hand) {
        Astra.mc.player.swingArm(hand);
        Astra.mc.player.connection.sendPacket(new CPacketAnimation(hand));
    }

    public static void center() {
        BlockPos actualPos = new BlockPos(Astra.mc.player.getPositionVector());

        double x = Math.abs(actualPos.getX() - Astra.mc.player.posX);
        double z = Math.abs(actualPos.getZ() - Astra.mc.player.posZ);
        if (x >= 0.1 && z >= 0.1) {
            Astra.mc.player.motionX = (actualPos.getX() - Astra.mc.player.posX) / 2.0;
            Astra.mc.player.motionZ = (actualPos.getZ() - Astra.mc.player.posZ) / 2.0;
        }
    }
}
