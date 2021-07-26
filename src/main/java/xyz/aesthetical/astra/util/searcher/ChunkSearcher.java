package xyz.aesthetical.astra.util.searcher;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import xyz.aesthetical.astra.util.WorldUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChunkSearcher {
    private final Chunk chunk;
    private final ArrayList<Block> blocks;
    private final ArrayList<BlockPos> positions = new ArrayList<>();
    private final ExecutorService service;
    private Future<?> future;

    public ChunkSearcher(Chunk chunk, ArrayList<Block> blocks) {
        this(chunk, blocks, Executors.newSingleThreadExecutor());
    }

    public ChunkSearcher(Chunk chunk, ArrayList<Block> blocks, ExecutorService service) {
        this.service = service;
        this.chunk = chunk;
        this.blocks = blocks;
    }

    public void search(ChunkListener listener) {
        future = service.submit(() -> {
            ChunkPos position = chunk.getPos();

            for (int x = position.getXStart(); x <= position.getXEnd(); ++x) {
                for (int y = 0; y <= 256; ++ y) {
                    for (int z = position.getZStart(); z <= position.getZEnd(); ++z) {
                        if (future == null || Thread.interrupted()) {
                            return;
                        }

                        BlockPos pos = new BlockPos(x, y, z);
                        if (!blocks.contains(WorldUtils.getBlockFromPos(pos))) {
                            continue;
                        }

                        positions.add(pos);
                    }
                }
            }

            if (future != null) {
                listener.finished(positions);
            }
        });
    }

    public void cancel() {
        if (future != null) {
            try {
                future.get();
                future = null;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        positions.clear();
    }

    public Chunk getChunk() {
        return chunk;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public ArrayList<BlockPos> getPositions() {
        return positions;
    }
}
