package xyz.aesthetical.eclipse.managers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;

public class XrayManager {
    public static XrayManager instance;

    private final ArrayList<Block> blocks = new ArrayList<>();

    public XrayManager() {
        // @todo do saving shit
        // for now we'll add default blocks

        addDefault();

        instance = this;
    }

    private void addDefault() {
        // ores
        blocks.add(Blocks.COAL_ORE);
        blocks.add(Blocks.IRON_ORE);
        blocks.add(Blocks.GOLD_ORE);
        blocks.add(Blocks.LAPIS_ORE);
        blocks.add(Blocks.DIAMOND_ORE);

        // ore blocks
        blocks.add(Blocks.COAL_BLOCK);
        blocks.add(Blocks.IRON_BLOCK);
        blocks.add(Blocks.GOLD_BLOCK);
        blocks.add(Blocks.LAPIS_BLOCK);
        blocks.add(Blocks.DIAMOND_BLOCK);

        // other blocks
        blocks.add(Blocks.BEDROCK);
        blocks.add(Blocks.OBSIDIAN);
        blocks.add(Blocks.PORTAL);
        blocks.add(Blocks.BEACON);
        blocks.add(Blocks.BED);

        // liquids
        blocks.add(Blocks.WATER);
        blocks.add(Blocks.FLOWING_WATER);
        blocks.add(Blocks.LAVA);
        blocks.add(Blocks.FLOWING_LAVA);
    }

    public boolean isValidBlock(Block block) {
        return blocks.contains(block);
    }
}
