package xyz.aesthetical.eclipse.features.commands;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import xyz.aesthetical.eclipse.managers.commands.Command;
import xyz.aesthetical.eclipse.managers.commands.exceptions.InvalidArgumentException;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;

import java.util.List;

@Command.Cmd(triggers = {"search", "se"}, description = "Configures the blocks used for the Search module")
public class Search extends Command {
    @Override
    public void execute(Minecraft client, List<String> args) throws Exception {
        if (args.isEmpty()) {
            send(new TextBuilder()
                    .append(ChatColor.Dark_Gray, "Please provide one of the following arguments: ")
                    .append(ChatColor.Red, "add")
                    .append(ChatColor.Dark_Gray, ", ")
                    .append(ChatColor.Red, "remove")
                    .append(ChatColor.Dark_Gray, ".")
            );

            return;
        }

        String action = getOrNull(args, 0);
        if (action == null || !Lists.newArrayList("add", "remove").contains(action.toLowerCase())) {
            throw new InvalidArgumentException("action");
        }

        action = action.toLowerCase();

        if (getOrNull(args, 1) == null) {
            throw new InvalidArgumentException("block name");
        }

        Block block = getBlock(String.join(" ", args.subList(1, args.size())));
        if (block == Blocks.AIR) {
            throw new InvalidArgumentException("block name");
        }

        if (action.equalsIgnoreCase("add")) {
            if (!xyz.aesthetical.eclipse.features.modules.render.Search.blocks.contains(block)) {
                xyz.aesthetical.eclipse.features.modules.render.Search.blocks.add(block);
            } else {
                throw new InvalidArgumentException("block name", "Already is a search block");
            }
        } else if (action.equalsIgnoreCase("remove")) {
            if (xyz.aesthetical.eclipse.features.modules.render.Search.blocks.contains(block)) {
                xyz.aesthetical.eclipse.features.modules.render.Search.blocks.remove(block);
            } else {
                throw new InvalidArgumentException("block name", "Isn't a search block");
            }
        }

        send(new TextBuilder()
                .append(ChatColor.Red, getRealName(action + "ed"))
                .append(" ")
                .append(ChatColor.Dark_Gray, "the block")
                .append(" ")
                .append(ChatColor.Red, Block.REGISTRY.getNameForObject(block).toString())
                .append(ChatColor.Dark_Gray, ".")
        );
    }

    private String getRealName(String name) {
        return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1).toLowerCase().replaceAll("_", " ");
    }

    private Block getBlock(String search) {
        return Block.REGISTRY.getObject(new ResourceLocation(search));
    }
}
