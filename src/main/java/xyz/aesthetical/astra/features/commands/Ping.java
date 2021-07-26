package xyz.aesthetical.astra.features.commands;

import net.minecraft.client.Minecraft;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.commands.Command;
import xyz.aesthetical.astra.managers.commands.text.ChatColor;
import xyz.aesthetical.astra.managers.commands.text.TextBuilder;

import java.util.List;

@Command.Cmd(triggers = {"ping", "latency"}, description = "Tells you your latency to the server")
public class Ping extends Command {
    @Override
    public void execute(Minecraft client, List<String> args) throws Exception {
        send(new TextBuilder()
                .append(ChatColor.Dark_Gray, "Your ping is")
                .append(" ")
                .append(ChatColor.Red, String.valueOf(Astra.serverManager.getPing(Astra.mc.player)) + "ms")
                .append(ChatColor.Dark_Gray, ".")
        );
    }
}
