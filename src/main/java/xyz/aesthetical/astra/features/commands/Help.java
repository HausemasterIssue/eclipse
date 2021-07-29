package xyz.aesthetical.astra.features.commands;

import net.minecraft.client.Minecraft;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.commands.Command;
import xyz.aesthetical.astra.managers.commands.text.ChatColor;
import xyz.aesthetical.astra.managers.commands.text.TextBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Command.Cmd(triggers = {"help", "h", "halp", "commands"}, description = "Shows all of the commands")
public class Help extends Command {
    @Override
    public void execute(Minecraft client, List<String> args) throws Exception {
        String first = getOrNull(args, 0);
        if (first == null) {
            sendCommands(0);
            return;
        }

        Command command = Astra.commandManager.getCommand(first);
        if (command != null) {
            sendCommand(command);
            return;
        }

        int page = isInt(first) ? Integer.parseInt(first) - 1 : 0;
        sendCommands(page);
    }

    private void sendCommand(Command command) {
        send(new TextBuilder()
                .append(ChatColor.Dark_Gray, "Help for")
                .append(" ")
                .append(ChatColor.Dark_Gray, command.getTriggers().get(0))
                .append(ChatColor.Dark_Gray, ":")
                .append("\n")
                .append("\n")
                .append(ChatColor.Blue, "Triggers")
                .append(ChatColor.Dark_Gray, ": ")
                .append(command.getTriggers().stream().map(ChatColor.Gold::text).collect(Collectors.joining(ChatColor.Dark_Gray.text(", "))))
                .append("\n")
                .append(ChatColor.Blue, "Description")
                .append(ChatColor.Dark_Gray, ": ")
                .append(ChatColor.Gold, command.getDescription())
        );
    }

    private void sendCommands(int page) {
        ArrayList<Command> commands = Astra.commandManager.getCommands();

        int itemLimit = 5;
        int max = (int) Math.ceil(commands.size() / (double) itemLimit);

        if (page - 1 > max || page < 0) {
            page = 0;
        }

        List<Command> display = commands.subList(page * itemLimit, Math.min((page + 1) * itemLimit, commands.size()));

        send(new TextBuilder()
                .append(ChatColor.Dark_Gray, "Command List")
                .append(" ")
                .append(ChatColor.Dark_Gray, String.valueOf(page + 1))
                .append(ChatColor.Dark_Gray, "/")
                .append(ChatColor.Dark_Gray, String.valueOf(max))
                .append("\n")
                .append("\n")
                .append(display.stream().map((c) -> ChatColor.Gold.text(c.getTriggers().get(0))).collect(Collectors.joining(ChatColor.Dark_Gray.text(", "))))
        );
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
