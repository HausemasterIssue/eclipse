package xyz.aesthetical.eclipse.features.modules.render;

import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

import java.awt.*;

@Module.Mod(name = "Tracers", description = "Draws lines to entities")
@Module.Info(category = Module.Category.RENDER)
public class Tracers extends Module {
    public final NumberSetting width = register(new NumberSetting("Width", 1.5f).setMin(0.1f).setMax(5.0f).setDescription("The width of the line"));
    public final Setting<Boolean> smooth = register(new Setting<>("Smooth", true).setDescription("If the line should be smoothed out"));

    // entities to draw tracers to
    public final Setting<Boolean> players = register(new Setting<>("Players", true).setDescription("If to draw a tracer line to players").setGroup("entities"));
    public final Setting<Boolean> friends = register(new Setting<>("Friends", true).setDescription("If to draw a tracer line to friends").setGroup("entities"));
    public final Setting<Boolean> enemies = register(new Setting<>("Enemies", true).setDescription("If to draw a tracer line to enemies").setGroup("entities"));
    public final Setting<Boolean> passive = register(new Setting<>("Passive", true).setDescription("If to draw a tracer line to passive mobs").setGroup("entities"));
    public final Setting<Boolean> hostile = register(new Setting<>("Hostile", true).setDescription("If to draw a tracer line to hostile mobs").setGroup("entities"));
    public final Setting<Boolean> invisible = register(new Setting<>("Invisible", true).setDescription("If to draw a tracer line to invisible entities").setGroup("entities"));

    // colors for the entities
    public final Setting<Color> playerColor = register(new Setting<>("Player Color", new Color(247, 99, 30)).setDescription("What color to render the player tracer").setGroup("colors"));
    public final Setting<Color> friendColor = register(new Setting<>("Friend Color", new Color(30, 91, 247)).setDescription("What color to render the friend tracer").setGroup("colors"));
    public final Setting<Color> enemyColor = register(new Setting<>("Enemies Color", new Color(247, 30, 30)).setDescription("What color to render the enemy tracer").setGroup("colors"));
    public final Setting<Color> passiveColor = register(new Setting<>("Enemies Color", new Color(30, 247, 99)).setDescription("What color to render the passive tracer").setGroup("colors"));
    public final Setting<Color> hostileColor = register(new Setting<>("Enemies Color", new Color(247, 30, 63)).setDescription("What color to render the hostile tracer").setGroup("colors"));
    public final Setting<Color> invisibleColor = register(new Setting<>("Invisible Color", new Color(176, 176, 176)).setDescription("What color to render the invisible tracer").setGroup("colors"));


}
