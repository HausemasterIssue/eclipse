package xyz.aesthetical.astra.features.modules.movement.jesus;

import xyz.aesthetical.astra.managers.modules.Mode;
import xyz.aesthetical.astra.managers.modules.Module;

public class Solid extends Mode {
    private static Solid instance;

    public Solid(Module module) {
        super("Solid", module);
    }

    public static void setInstance(Module module) {
        if (instance == null) {
            instance = new Solid(module);
        }
    }

    public static Solid getInstance() {
        return instance;
    }
}
