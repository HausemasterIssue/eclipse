package xyz.aesthetical.astra.features.modules.combat;

import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "AutoArmor", description = "Automatically equips armor pieces")
@Module.Info(category = Module.Category.COMBAT)
public class AutoArmor extends Module {
    // @todo automend?
    public final NumberSetting delay = register(new NumberSetting("Delay", 250.0f).setMin(0.0f).setMax(2500.0f).setDescription("How long to wait before adding another armor piece"));

}
