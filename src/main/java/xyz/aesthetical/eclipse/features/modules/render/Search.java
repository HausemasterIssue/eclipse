package xyz.aesthetical.eclipse.features.modules.render;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.render.RenderEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.ColorUtil;
import xyz.aesthetical.eclipse.util.RenderUtils;
import xyz.aesthetical.eclipse.util.Timer;
import xyz.aesthetical.eclipse.util.WorldUtils;
import xyz.aesthetical.eclipse.util.searcher.ChunkSearcher;

import java.util.ArrayList;

@Module.Mod(name = "Search", description = "Searches for blocks around you")
@Module.Info(category = Module.Category.RENDER)
public class Search extends Module {
    public static ArrayList<Block> blocks = new ArrayList<>();

    public final NumberSetting range = register(new NumberSetting("Range", 5).setMin(1).setMax(20).setDescription("How many chunks to search"));
    public final NumberSetting delay = register(new NumberSetting("Delay", 1.5f).setMin(0.5f).setMax(10.0f).setDescription("How long to wait before searching again"));

    private final ArrayList<BlockPos> positions = new ArrayList<>();
    private final ArrayList<ChunkSearcher> searchers = new ArrayList<>();
    private final Timer timer = new Timer();

    @Override
    public void onEnabled() {
        timer.reset();
        positions.clear();
        cancelSearchers();
    }

    @Override
    public void onDisabled() {
        timer.reset();
        positions.clear();
        cancelSearchers();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player && !blocks.isEmpty()) {
            if (timer.passedS(delay.getValue().doubleValue())) {
                timer.reset();
                cancelSearchers();

                for (int i = 0; i < positions.size(); ++i) {
                    BlockPos pos = positions.get(i);

                    if (!blocks.contains(WorldUtils.getBlockFromPos(pos))) {
                        positions.remove(pos);
                    }
                }

                ArrayList<Chunk> chunks = new ArrayList<>();
                Chunk currentChunk = Eclipse.mc.world.getChunk(Eclipse.mc.player.chunkCoordX, Eclipse.mc.player.chunkCoordZ);

                for (int x = currentChunk.x - range.getValue().intValue(); x <= currentChunk.x + range.getValue().intValue(); ++x) {
                    for (int z = currentChunk.z - range.getValue().intValue(); z <= currentChunk.z + range.getValue().intValue(); ++z) {
                        Chunk chunk = Eclipse.mc.world.getChunk(x, z);
                        if (chunk instanceof EmptyChunk || chunks.contains(chunk)) {
                            continue;
                        }

                        chunks.add(chunk);
                    }
                }

                if (!chunks.isEmpty()) {
                    for (Chunk chunk : chunks) {
                        ChunkSearcher searcher = new ChunkSearcher(chunk, blocks);
                        searcher.search((blocks) -> {
                            for (BlockPos pos : blocks) {
                                if (!positions.contains(pos)) {
                                    positions.add(pos);
                                }
                            }
                        });

                        searchers.add(searcher);
                    }
                }
            } else {
                for (int i = 0; i < searchers.size(); ++i) {
                    ChunkSearcher searcher = searchers.get(i);
                    ChunkPos pos = searcher.getChunk().getPos();
                    Chunk currentChunk = Eclipse.mc.world.getChunk(Eclipse.mc.player.chunkCoordX, Eclipse.mc.player.chunkCoordZ);

                    if (Math.abs(pos.x - currentChunk.x) <= range.getValue().intValue() && Math.abs(pos.z - currentChunk.z) <= range.getValue().intValue()) {
                        continue;
                    }

                    searcher.cancel();
                    searchers.remove(searcher);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && !positions.isEmpty() && !blocks.isEmpty()) {
            for (int i = 0; i < positions.size(); ++i) {
                BlockPos pos = positions.get(i);
                RenderUtils.drawFilledBox(new AxisAlignedBB(pos).offset(RenderUtils.getCameraPos()), ColorUtil.toRGBA(255, 255, 255, 80));
            }
        }
    }

    private void cancelSearchers() {
        for (int i = 0; i < searchers.size(); ++i) {
            ChunkSearcher searcher = searchers.get(i);
            searcher.cancel();
            searchers.remove(searcher);
        }
    }
}
