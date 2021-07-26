package xyz.aesthetical.astra.managers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.WorldUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HoleManager {
    public static final BlockPos[] SURROUNDING_POSITIONS = new BlockPos[] {
        new BlockPos(-1, 0, 0),
        new BlockPos(1, 0, 0),
        new BlockPos(0, -1, 0),
        new BlockPos(0, 0, 1),
        new BlockPos(0, 0, -1)
    };

    private ArrayList<HoleInfo> holes = new ArrayList<>();

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (!holes.isEmpty()) {
            holes.clear();
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            ArrayList<HoleInfo> newHoles = new ArrayList<>();

            // @todo make the range a setting somewhere
            List<BlockPos> blocks = WorldUtils.getSphere(Astra.mc.player.getPosition().add(-0.5, -0.5, -0.5), 5, 5, false, true, 0);
            loop0: for (BlockPos pos : blocks) {
                if (WorldUtils.getBlockFromPos(pos) != Blocks.AIR || WorldUtils.getBlockFromPos(pos.add(0.0, 1.0, 0.0)) != Blocks.AIR) {
                    continue;
                }

                int safe = 0, unsafe = 0;
                for (BlockPos surroundBlockPos : Arrays.stream(SURROUNDING_POSITIONS).map(pos::add).collect(Collectors.toList())) {
                    Block block = WorldUtils.getBlockFromPos(surroundBlockPos);
                    if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK) {
                        continue loop0;
                    }

                    if (block == Blocks.OBSIDIAN) {
                        ++unsafe;
                    } else {
                        ++safe;
                    }
                }

                if (safe + unsafe != 5) {
                    continue;
                }

                newHoles.add(new HoleInfo(pos, safe, unsafe));
            }

            holes = newHoles;
        }
    }

    public HoleInfo getEntityHole(EntityPlayer player) {
        for (HoleInfo info : getHoles()) {
            if (info.isEntityInHole() && new AxisAlignedBB(info.getPos()).intersects(player.getEntityBoundingBox())) {
                return info;
            }
        }

        return null;
    }

    public ArrayList<HoleInfo> getHoles() {
        return holes;
    }

    public static class HoleInfo {
        private final BlockPos pos;
        private final boolean safe;

        public HoleInfo(BlockPos pos, int safe, int unsafe) {
            this.pos = pos;
            this.safe = safe == 5 && unsafe == 0;
        }

        public BlockPos getPos() {
            return pos;
        }

        public boolean isSafe() {
            return safe;
        }

        public boolean isEntityInHole() {
            return !Astra.mc.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos)).isEmpty();
        }
    }
}
