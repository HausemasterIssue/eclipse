package xyz.aesthetical.eclipse.events.world.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

@Cancelable
public class LiquidCollisionEvent extends Event {
    private final List<AxisAlignedBB> boxes;
    private final Block block;
    private final BlockPos pos;
    private final AxisAlignedBB box;
    private final Entity entity;

    public LiquidCollisionEvent(List<AxisAlignedBB> boxes, Block block, BlockPos pos, AxisAlignedBB box, Entity entity) {
        this.boxes = boxes;
        this.block = block;
        this.pos = pos;
        this.box = box;
        this.entity = entity;
    }

    public void addBox(AxisAlignedBB box) {
        boxes.add(box);
    }

    public Block getBlock() {
        return block;
    }

    public BlockPos getPos() {
        return pos;
    }

    public AxisAlignedBB getBox() {
        return box;
    }

    public Entity getEntity() {
        return entity;
    }
}
