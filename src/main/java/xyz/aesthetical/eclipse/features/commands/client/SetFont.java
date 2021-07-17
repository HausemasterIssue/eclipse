package xyz.aesthetical.eclipse.features.commands.client;

import net.minecraft.client.Minecraft;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.TextManager;
import xyz.aesthetical.eclipse.managers.commands.Command;
import xyz.aesthetical.eclipse.managers.commands.exceptions.InvalidArgumentException;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;

import java.awt.*;
import java.util.List;

@Command.Cmd(triggers = {"setfont", "font"}, description = "Sets the client custom font")
public class SetFont extends Command {
    @Override
    public void execute(Minecraft client, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new InvalidArgumentException("name", "No font name provided");
        }

        List<String> fonts = Eclipse.textManager.getAvailableFonts();
        for (String name :fonts) {
            if (name.equalsIgnoreCase(String.join(" ", args))) {
                TextManager.setCustomFontRenderer(new Font(name, Font.PLAIN, 18));

                send(new TextBuilder()
                        .append(ChatColor.Dark_Gray, "Set the custom font to")
                        .append(" ")
                        .append(ChatColor.Red, name)
                        .append(ChatColor.Dark_Gray, ".")
                );

                return;
            }
        }

        throw new InvalidArgumentException("name", "Invalid font name");
    }
}
