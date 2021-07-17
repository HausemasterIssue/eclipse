package xyz.aesthetical.eclipse.features.commands.module;

import net.minecraft.client.Minecraft;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.commands.Command;
import xyz.aesthetical.eclipse.managers.commands.exceptions.InvalidArgumentException;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;
import xyz.aesthetical.eclipse.managers.modules.Module;

import java.util.List;

@Command.Cmd(triggers = {"toggle", "t"}, description = "Toggles a module")
public class Toggle extends Command {
    @Override
    public void execute(Minecraft client, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new InvalidArgumentException("module", "Invalid module name");
        }

        Module module = Eclipse.moduleManager.getModule(args.get(0));
        if (module == null) {
            throw new InvalidArgumentException("module", "Invalid module specified");
        }

        module.toggle();

        send(new TextBuilder()
                .append(ChatColor.Red, module.isToggled() ? "Enabled" : "Disabled")
                .append(" ")
                .append(ChatColor.Dark_Gray, "the module")
                .append(" ")
                .append(ChatColor.Red, module.getName())
                .append(ChatColor.Dark_Gray, ".")
        );
    }
}
