package xyz.aesthetical.eclipse.util.searcher;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

@FunctionalInterface
public interface ChunkListener {
    void finished(ArrayList<BlockPos> positions);
}
