package xyz.aesthetical.astra.managers.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.commands.text.TextBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
    private final List<String> triggers;
    private final String description;

    public Command() {
        Cmd info = getClass().getDeclaredAnnotation(Cmd.class);

        this.triggers = Arrays.asList(info.triggers());
        this.description = info.description();
    }

    public abstract void execute(Minecraft client, List<String> args) throws Exception;

    public List<String> getTriggers() {
        return triggers;
    }

    public String getDescription() {
        return description;
    }

    public static void send(String message) {
        Astra.mc.player.sendMessage(new TextComponentString(Astra.commandManager.getSentCommandPrefix() + message));
    }

    public static String getOrNull(List<String> args, int index) {
        try {
            return args.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static void send(TextBuilder builder) {
        send(builder.build());
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cmd {
        String[] triggers();
        String description() default "Could not parse description.";
    }
}
