package xyz.aesthetical.astra.features.modules.client;

import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.TextManager;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "CustomFont", description = "Uses a custom font over the default one")
@Module.Info(category = Module.Category.CLIENT)
public class CustomFont extends Module {
    public static CustomFont instance;

    public final Setting<Boolean> shadow = register(new Setting<>("Shadow", true).setDescription("If to add shadows to the text"));

    public CustomFont() {
        instance = this;
    }

    @Override
    public String getDisplay() {
        return TextManager.customFontRenderer.getFont().getFontName();
    }
}
