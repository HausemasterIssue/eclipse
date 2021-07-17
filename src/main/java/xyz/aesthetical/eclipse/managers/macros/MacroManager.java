package xyz.aesthetical.eclipse.managers.macros;

import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class MacroManager {
    private final ArrayList<Macro> macros = new ArrayList<>();

    public void add(Macro macro) {
        macros.add(macro);
        MinecraftForge.EVENT_BUS.register(macro);
    }

    public void remove(Macro macro) {
        macros.remove(macro);
        MinecraftForge.EVENT_BUS.unregister(macro);
    }

    public void remove(String name) {
        for (Macro macro : macros) {
            if (macro.getName().equalsIgnoreCase(name)) {
                remove(macro);
                return;
            }
        }
    }
}
