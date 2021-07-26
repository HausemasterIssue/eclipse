package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "AutoReconnect", description = "Automatically reconnects to the last server you were on")
public class AutoReconnect extends Module {
    public static AutoReconnect instance;

    public final NumberSetting delay = register(new NumberSetting("Delay", 5).setMin(0).setMax(15).setDescription("How long to wait before reconnecting"));

    public AutoReconnect() {
        instance = this;
    }
}
