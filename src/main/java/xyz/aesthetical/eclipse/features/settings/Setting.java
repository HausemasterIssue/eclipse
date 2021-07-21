package xyz.aesthetical.eclipse.features.settings;

import net.minecraftforge.common.MinecraftForge;
import xyz.aesthetical.eclipse.events.eclipse.ModuleToggledEvent;

import java.util.function.Predicate;

public class Setting<T> {
    private final String name;
    private String description;
    private final T defaultValue;
    private T value;
    private Predicate<T> visibility = null;
    private String group = null;

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public Setting<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public T getValue() {
        return value;
    }

    public Setting<T> setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Setting<T> setVisibility(Predicate<T> visibility) {
        this.visibility = visibility;
        return this;
    }

    public boolean isVisible() {
        return visibility == null || visibility.test(value);
    }

    public Setting<T> setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public Setting<T> createClone() {
        return new Setting<T>(name, defaultValue).setDescription(description).setVisibility(visibility);
    }
}
