package xyz.aesthetical.astra.gui.hudeditor.elements;

import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.gui.hudeditor.HudElement;
import xyz.aesthetical.astra.managers.commands.text.ChatColor;
import xyz.aesthetical.astra.managers.commands.text.TextBuilder;
import xyz.aesthetical.astra.managers.modules.Module;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Arraylist extends HudElement {
    public final Setting<Sorting> sorting = register(new Setting<>("Sorting", Sorting.DOWN).setDescription("How to sort the module list"));
    public final Setting<Boolean> showDisplay = register(new Setting<>("Show Display", true).setDescription("If to show the extra information with modules"));
    public final Setting<Boolean> colors = register(new Setting<>("Colors", false).setDescription("If to use the custom colors for modules"));

    public Arraylist() {
        super("Arraylist");
    }

    @Override
    public void init() {
        x = 2.0;
        y = 2.0;

        width = 100; // @todo
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        // @todo find out where we are via the middle and change it like that yeah idfk how to explain it
        List<Module> modules = getSorted();
        if (!modules.isEmpty()) {
            height = ((Astra.textManager.getHeight() + 2) * modules.size()) + 2; // yes yes
            width = modules.stream().map(m -> Astra.textManager.getWidth(toDisplay(m))).sorted(Comparator.comparingInt(a -> -a)).collect(Collectors.toList()).get(0);

            int posY = (int) y;
            for (Module module : modules) {
                Astra.textManager.draw(toDisplay(module), (int) x, posY, colors.getValue() ? module.getColor() : -1);
                posY += Astra.textManager.getHeight() + 2;
            }
        }
    }

    private List<Module> getSorted() {
        List<Module> enabled = Astra.moduleManager.getEnabledModules();

        if (sorting.getValue() == Sorting.ALPHABETICAL) {
            enabled = enabled.stream()
                    .map(Module::getName)
                    .sorted()
                    .map(name -> Astra.moduleManager.<Module>getModule(name))
                    .collect(Collectors.toList());
        } else if (sorting.getValue() == Sorting.UP || sorting.getValue() == Sorting.DOWN) {
            enabled = enabled.stream()
                    .sorted(Comparator.comparingInt(a -> sorting.getValue() == Sorting.UP ? Astra.textManager.getWidth(toDisplay(a)) : -Astra.textManager.getWidth(toDisplay(a))))
                    .collect(Collectors.toList());
        }

        return enabled;
    }

    private String toDisplay(Module module) {
        String name = module.getName();
        if (showDisplay.getValue() && module.getDisplay() != null) {
            name += new TextBuilder()
                    .append(" ")
                    .append(ChatColor.Dark_Gray, "[")
                    .append(module.getDisplay())
                    .append(ChatColor.Dark_Gray, "]")
                    .build();
        }

        return name;
    }

    public enum Sorting {
        ALPHABETICAL,
        UP,
        DOWN
    }
}
