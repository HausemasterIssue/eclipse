package xyz.aesthetical.eclipse.features.modules.render;

import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "NoRender", description = "Stops things from rendering")
@Module.Info(category = Module.Category.RENDER)
public class NoRender extends Module {
    public static NoRender instance;

    public final Setting<Boolean> hurtCamera = register(new Setting<>("Hurt Camera", false).setDescription("If to stop the hurtcam from working"));
    public final Setting<Boolean> weather = register(new Setting<>("Weather", false).setDescription("If to stop weather from renderer"));
    public final Setting<Boolean> totemUse = register(new Setting<>("Totem Use", false).setDescription("If to stop the totem use animation from rendering"));
    public final Setting<Boolean> particles = register(new Setting<>("Particles", false).setDescription("If to stop particles from rendering"));
    public final Setting<Boolean> fog = register(new Setting<>("Fog", false).setDescription("If to stop fog from rendering"));
    public final Setting<Boolean> fire = register(new Setting<>("Fire", false).setDescription("If to stop the fire overlay from rendering"));

    public NoRender() {
        instance = this;
    }
}
