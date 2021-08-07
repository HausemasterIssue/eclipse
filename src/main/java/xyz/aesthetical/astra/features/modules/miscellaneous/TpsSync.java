package xyz.aesthetical.astra.features.modules.miscellaneous;

import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "TpsSync", description = "Syncs actions with the servers TPS")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class TpsSync extends Module {
    public static TpsSync instance;

    public final Setting<Boolean> mining = register(new Setting<>("Mining", false).setDescription("If to sync mining speed with the TPS"));
    public final Setting<Boolean> attack = register(new Setting<>("Attack", false).setDescription("If to sync attack speed with the TPS"));
    public final Setting<Boolean> eating = register(new Setting<>("Eating", false).setDescription("If to sync eating speed with the TPS"));

    public TpsSync() {
        instance = this;
    }
}
