package xyz.aesthetical.astra.features.commands;

import net.minecraft.client.Minecraft;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.commands.Command;
import xyz.aesthetical.astra.managers.commands.exceptions.InvalidArgumentException;
import xyz.aesthetical.astra.managers.commands.text.ChatColor;
import xyz.aesthetical.astra.managers.commands.text.TextBuilder;

import java.util.List;

@Command.Cmd(triggers = {"setprefix", "prefix", "prfx", "setpr"}, description = "Sets the client's command prefix")
public class SetPrefix extends Command {
    @Override
    public void execute(Minecraft client, List<String> args) throws Exception {
        if (args.isEmpty()) {
            send(new TextBuilder()
                    .append(ChatColor.Dark_Gray, "The current client command prefix is")
                    .append(" ")
                    .append(ChatColor.Red, Astra.commandManager.getPrefix())
                    .append(ChatColor.Dark_Gray, ".")
            );

            return;
        }

        if (args.get(0).length() > 5) {
            throw new InvalidArgumentException("prefix", "Prefix must be less than 5 characters in length");
        }

        Astra.commandManager.setPrefix(args.get(0).toLowerCase());

        send(new TextBuilder()
                .append(ChatColor.Dark_Gray, "Set the command prefix to")
                .append(" ")
                .append(ChatColor.Red, args.get(0).toLowerCase())
                .append(ChatColor.Dark_Gray, ".")
        );
    }
}
