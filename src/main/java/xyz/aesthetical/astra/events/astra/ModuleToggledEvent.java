package xyz.aesthetical.astra.events.astra;

import net.minecraftforge.fml.common.eventhandler.Event;
import xyz.aesthetical.astra.managers.modules.Module;

public class ModuleToggledEvent extends Event {
    private final Module module;

    public ModuleToggledEvent(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}
