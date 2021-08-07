package xyz.aesthetical.astra.features.modules.render;

import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

import java.awt.*;

@Module.Mod(name = "Nametags", description = "Shows custom nametags instead of the vanilla ones")
public class Nametags extends Module {
    public final Setting<Boolean> nameProtect = register(new Setting<>("Name Protect", false).setDescription("If to instead show custom, random names on other players"));
    public final Setting<Boolean> health = register(new Setting<>("Health", true).setDescription("If to show their health"));
    public final Setting<Boolean> ping = register(new Setting<>("Ping", false).setDescription("If to show their ping"));
    public final Setting<Color> sneakColor = register(new Setting<>("Sneak Color", new Color(240, 178, 34)).setDescription("The color to render the players name when they're sneaking"));
    public final Setting<Boolean> customFont = register(new Setting<>("Custom Font", true).setDescription("If to use the custom font rendering"));
}
