package xyz.aesthetical.astra.events.astra;

import net.minecraftforge.fml.common.eventhandler.Event;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.gui.hudeditor.HudElement;
import xyz.aesthetical.astra.managers.modules.Module;

public class SettingChangeEvent extends Event {
    private final Setting previous;
    private final Setting current;
    private Module module;
    private HudElement element;

    public SettingChangeEvent(Setting previous, Setting current) {
        this.previous = previous;
        this.current = current;
    }

    public SettingChangeEvent(Setting previous, Setting current, Module module) {
        this(previous, current);
        this.module = module;
    }

    public SettingChangeEvent(Setting previous, Setting current, HudElement element) {
        this(previous, current);
        this.element = element;
    }

    public Setting getPrevious() {
        return previous;
    }

    public Setting getCurrent() {
        return current;
    }

    public Module getModule() {
        return module;
    }

    public HudElement getElement() {
        return element;
    }
}
