package xyz.aesthetical.astra.managers.modules;

import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class Module {
    private final String name;
    private final String description;
    private final int color;

    private Category category = Category.MISCELLANEOUS;
    private int bind = Keyboard.KEY_NONE;
    private boolean visible = true;

    private boolean toggled = false;

    private final ArrayList<Setting> settings = new ArrayList<>();

    public Module() {
        // if it doesn't have it, we want it to error. so no check for this is intentional
        Mod mod = getClass().getDeclaredAnnotation(Mod.class);

        this.name = mod.name();
        this.description = mod.description();
        this.color = mod.color();

        if (getClass().isAnnotationPresent(Info.class)) {
            Info info = getClass().getDeclaredAnnotation(Info.class);

            this.category = info.category();
            this.bind = info.bind();
            this.visible = info.visible();

            if (info.preToggled()) {
                toggle();
            }
        }

        register(new Setting<>("Visible", true).setDescription("If this module should show up on the module array list."));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplay() {
        return null;
    }

    public int getColor() {
        return color;
    }

    public Category getCategory() {
        return category;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void onEnabled() {

    }

    public void onDisabled() {

    }

    public void toggle() {
        toggled = !toggled;

        if (toggled) {
            MinecraftForge.EVENT_BUS.register(this);
            onEnabled();
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
            onDisabled();
        }
    }

    public boolean isToggled() {
        return toggled;
    }

    public <T> T register(Setting setting) {
        settings.add(setting);
        return (T) setting;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public static boolean fullNullCheck() {
        return Astra.mc != null && Astra.mc.player != null && Astra.mc.world != null;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Mod {
        String name();
        String description() default "No description provided";
        int color() default -1;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        Category category() default Category.MISCELLANEOUS;
        int bind() default Keyboard.KEY_NONE;
        boolean visible() default true;
        boolean preToggled() default false;
    }

    public enum Category {
        CLIENT("Client"),
        COMBAT("Combat"),
        EXPLOITS("Exploits"),
        MISCELLANEOUS("Miscellaneous"),
        MOVEMENT("Movement"),
        RENDER("Render");

        private String display;
        Category(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
}
