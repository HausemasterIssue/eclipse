package xyz.aesthetical.eclipse.managers.commands;

import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.commands.*;
import xyz.aesthetical.eclipse.managers.commands.exceptions.InvalidArgumentException;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    private final ArrayList<Command> commands = new ArrayList<>();
    private String prefix = ",";

    public CommandManager() {
        commands.add(new Ping());
        commands.add(new Search());
        commands.add(new SetFont());
        commands.add(new SetPrefix());
        commands.add(new Toggle());
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onInput(InputEvent.KeyInputEvent event) {
        int key = Keyboard.getEventKey();
        if (!Keyboard.getEventKeyState() && key != Keyboard.KEY_NONE) {
            if (Eclipse.commandManager.getPrefix().equalsIgnoreCase(Keyboard.getKeyName(key))) {
                Eclipse.mc.displayGuiScreen(new GuiChat(Eclipse.commandManager.getPrefix()));
            }
        }
    }

    public void execute(String message) {
        List<String> args = Arrays.asList(message.substring(prefix.length()).trim().split(" "));
        if (!args.isEmpty()) {
            Command command = getCommand(args.get(0));
            if (command != null) {
                try {
                    command.execute(Eclipse.mc, args.subList(1, args.size()));
                } catch (Exception e) {
                    if (e instanceof InvalidArgumentException) {
                        InvalidArgumentException exception = (InvalidArgumentException) e;

                        TextBuilder builder = new TextBuilder()
                                .append(ChatColor.Dark_Gray, "You provided an invalid argument for the argument")
                                .append(" ")
                                .append(ChatColor.Red, exception.getArgName())
                                .append(ChatColor.Dark_Gray, ".");

                        if (exception.getReason() != null) {
                            builder
                                    .append("\n")
                                    .append(ChatColor.Dark_Gray, exception.getReason() + ".");
                        }

                        Command.send(builder);
                        return;
                    }

                    Command.send(new TextBuilder().append(ChatColor.Dark_Gray, "Unknown exception while executing the command."));
                }
            } else {
                Command.send(new TextBuilder()
                        .append(ChatColor.Dark_Gray, "Invalid command. Run the help command for more information.")
                );
            }
        }
    }

    public String getSentCommandPrefix() {
        return new TextBuilder()
                .append(ChatColor.Dark_Gray, "[")
                .append(ChatColor.Red, Eclipse.MOD_NAME)
                .append(ChatColor.Dark_Gray, "]")
                .append(" ")
                .build();
    }

    public Command getCommand(String name) {
        for (Command command : commands) {
            for (String trigger : command.getTriggers()) {
                if (trigger.equalsIgnoreCase(name)) {
                    return command;
                }
            }
        }

        return null;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
