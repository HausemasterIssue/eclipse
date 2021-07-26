package xyz.aesthetical.astra.util.searcher;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

@FunctionalInterface
public interface ChunkListener {
    void finished(ArrayList<BlockPos> positions);
}
