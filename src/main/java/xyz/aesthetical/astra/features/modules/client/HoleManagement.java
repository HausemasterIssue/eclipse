package xyz.aesthetical.astra.features.modules.client;

import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "HoleManager", description = "Manages how safe holes are done on the client")
@Module.Info(category = Module.Category.CLIENT)
public class HoleManagement extends Module {
    public final NumberSetting range = register(new NumberSetting("Range", 6.0f).setMin(1.0f).setMax(50.0f).setDescription("How far to look for safe holes"));
    public final NumberSetting delay = register(new NumberSetting("Delay", 100.0f).setMin(0.0f).setMax(2500.0f).setDescription("How long to wait before looking for holes again"));
    public final Setting<Boolean> doubleHoles = register(new Setting<>("Double Holes", false).setDescription("If to highlight double holes"));

}
