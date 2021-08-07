package xyz.aesthetical.astra.features.modules.miscellaneous;

import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "MultiTask", description = "Allows you to do things at the same time")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class MultiTask extends Module {
    public static MultiTask instance;

    public MultiTask() {
        instance = this;
    }
}
