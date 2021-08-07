package xyz.aesthetical.astra.features.modules.miscellaneous;

import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "ExtraTab", description = "Stops minecraft from shortening the tab menu")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class ExtraTab extends Module {
    public static ExtraTab instance;

    public final NumberSetting players = register(new NumberSetting("Players", 100).setMin(1).setMax(1000).setDescription("How many players to show on the tab overlay"));
    public final Setting<Boolean> highlightFriends = register(new Setting<>("Highlight Friends", true).setDescription("If to highlight your friends on the tab list in blue"));

    public ExtraTab() {
        instance = this;
    }
}
