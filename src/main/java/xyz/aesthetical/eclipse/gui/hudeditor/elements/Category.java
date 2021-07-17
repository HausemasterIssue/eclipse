package xyz.aesthetical.eclipse.gui.hudeditor.elements;

public enum Category {
    COMBAT("Combat"),
    GENERAL("General"),
    WORLD("World");

    private final String display;
    Category(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
