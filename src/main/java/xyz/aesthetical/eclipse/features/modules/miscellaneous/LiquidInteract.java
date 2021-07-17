package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "LiquidInteract", description = "Allows you to place blocks on liquids")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class LiquidInteract extends Module {
    public static LiquidInteract instance;

    public LiquidInteract() {
        instance = this;
    }
}
