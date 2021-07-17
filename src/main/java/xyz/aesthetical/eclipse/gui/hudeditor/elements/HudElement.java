package xyz.aesthetical.eclipse.gui.hudeditor.elements;

import xyz.aesthetical.eclipse.features.settings.Setting;

import java.util.ArrayList;

public abstract class HudElement {
    protected final String name;
    protected final Category category;
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected boolean enabled = false;

    protected final ArrayList<Setting> settings = new ArrayList<>();

    public HudElement(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public abstract void init();
    public abstract void draw(int mouseX, int mouseY, float tickDelta);

    public <T> T register(Setting setting) {
        settings.add(setting);
        return (T) setting;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }
}
