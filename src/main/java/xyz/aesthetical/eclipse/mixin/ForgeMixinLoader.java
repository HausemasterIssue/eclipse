package xyz.aesthetical.eclipse.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("ForgeMixinLoader")
@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class ForgeMixinLoader implements IFMLLoadingPlugin {
    public ForgeMixinLoader() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.eclipse.json");
        Mixins.addConfiguration("mixins.baritone.json"); // register baritone mixins and shit
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
