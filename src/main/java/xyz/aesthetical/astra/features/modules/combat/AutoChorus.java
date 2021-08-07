package xyz.aesthetical.astra.features.modules.combat;

import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.modules.miscellaneous.Freecam;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "AutoChorus", description = "Automatically eats chorus fruit when it detects you're trapped")
@Module.Info(category = Module.Category.COMBAT)
public class AutoChorus extends Module {
    public final Setting<Boolean> targetCheck = register(new Setting<>("Target Check", true).setDescription("Checks for entities and if a target is detected, rotate away from them"));


    public boolean shouldStop() {
        return Astra.moduleManager.getModule(Freecam.class).isToggled() || AutoBreaker.isBreaking;
    }
}
