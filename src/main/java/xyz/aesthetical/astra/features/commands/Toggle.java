package xyz.aesthetical.astra.features.commands;

import net.minecraft.client.Minecraft;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.commands.Command;
import xyz.aesthetical.astra.managers.commands.exceptions.InvalidArgumentException;
import xyz.aesthetical.astra.managers.commands.text.ChatColor;
import xyz.aesthetical.astra.managers.commands.text.TextBuilder;
import xyz.aesthetical.astra.managers.modules.Module;

import java.util.List;

@Command.Cmd(triggers = {"toggle", "t"}, description = "Toggles a module")
public class Toggle extends Command {
    @Override
    public void execute(Minecraft client, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new InvalidArgumentException("module", "Invalid module name");
        }

        Module module = Astra.moduleManager.getModule(args.get(0));
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
