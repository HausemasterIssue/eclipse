package xyz.aesthetical.astra.features.modules.movement;

import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "BoatFly", description = "Fly with boats")
public class BoatFly extends Module {
    public final Setting<Boolean> noKick = register(new Setting<>("No Kick", false).setDescription("If to stop you from getting kicked"));
}
