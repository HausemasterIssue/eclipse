package xyz.aesthetical.eclipse.events.eclipse;

import net.minecraftforge.fml.common.eventhandler.Event;
import xyz.aesthetical.eclipse.managers.modules.Module;

public class ModuleToggledEvent extends Event {
    private final Module module;

    public ModuleToggledEvent(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}
