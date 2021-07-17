package xyz.aesthetical.eclipse.features.commands.client;

import net.minecraft.client.Minecraft;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.commands.Command;
import xyz.aesthetical.eclipse.managers.commands.exceptions.InvalidArgumentException;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;

import java.util.List;

@Command.Cmd(triggers = {"setprefix", "prefix", "prfx", "setpr"}, description = "Sets the client's command prefix")
public class SetPrefix extends Command {
    @Override
    public void execute(Minecraft client, List<String> args) throws Exception {
        if (args.isEmpty()) {
            send(new TextBuilder()
                    .append(ChatColor.Dark_Gray, "The current client command prefix is")
                    .append(" ")
                    .append(ChatColor.Red, Eclipse.commandManager.getPrefix())
                    .append(ChatColor.Dark_Gray, ".")
            );

            return;
        }

        if (args.get(0).length() > 5) {
            throw new InvalidArgumentException("prefix", "Prefix must be less than 5 characters in length");
        }

        Eclipse.commandManager.setPrefix(args.get(0).toLowerCase());

        send(new TextBuilder()
                .append(ChatColor.Dark_Gray, "Set the command prefix to")
                .append(" ")
                .append(ChatColor.Red, args.get(0).toLowerCase())
                .append(ChatColor.Dark_Gray, ".")
        );
    }
}
