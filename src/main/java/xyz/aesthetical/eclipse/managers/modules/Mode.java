package xyz.aesthetical.eclipse.managers.modules;

import net.minecraftforge.common.MinecraftForge;
import xyz.aesthetical.eclipse.features.settings.Setting;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Mode {
    protected final String name;
    protected final Module module;

    protected final ArrayList<Setting> settings = new ArrayList<>();

    public Mode(String name, Module module) {
        this.name = name;
        this.module = module;
    }

    public <T> T register(Setting setting) {
        settings.add(setting);
        return (T) setting;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public Module getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public void onEnabled() { }

    public void onDisabled() { }

    public void use() {
        System.out.println("penis");
        MinecraftForge.EVENT_BUS.register(this);
        onEnabled();
    }

    public void dispose() {
        MinecraftForge.EVENT_BUS.unregister(this);
        onDisabled();
    }
}
