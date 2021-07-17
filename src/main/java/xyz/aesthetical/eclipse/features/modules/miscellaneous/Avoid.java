package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "Avoid", description = "Avoids things")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class Avoid extends Module {
    public static Avoid instance;

    public final Setting<Boolean> cacti = register(new Setting<>("Cacti", false).setDescription("If to stop you from taking damage on cacti"));
    public final Setting<Boolean> fire = register(new Setting<>("Fire", false).setDescription("If to stop you from walking into fire"));

    public Avoid() {
        instance = this;
    }
}
