package xyz.aesthetical.eclipse.events.eclipse;

import net.minecraftforge.fml.common.eventhandler.Event;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.HudElement;
import xyz.aesthetical.eclipse.managers.modules.Module;

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
