package xyz.aesthetical.eclipse.features.modules.client;

import net.minecraftforge.common.MinecraftForge;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

// @todo add more features, alot of things can happen
@Module.Mod(name = "Safety", description = "Manages the safety manager")
@Module.Info(category = Module.Category.CLIENT)
public class Safety extends Module {
    public static Safety instance;

    public final Setting<Boolean> autoSurround = register(new Setting<>("Auto Surround", false).setDescription("If to automatically surround you if you're in danger"));
    public final Setting<Boolean> antiChainPop = register(new Setting<>("Anti-Chain Pop", false).setDescription("If to surround you when you pop a totem and are not surrounded"));
    public final Setting<Boolean> autoTotem = register(new Setting<>("Auto-Totem", false).setDescription("If Auto-Totem is not on when one is popped, turn it on"));
    public final Setting<Boolean> noLethalCrystalHit = register(new Setting<>("No Lethal Crystal", false).setDescription("If to stop you from hitting deadly crystals"));
    public final NumberSetting maxCrystalDamage = register(new NumberSetting("Max Crystal Damage", 6.0f).setMin(2.0f).setMax(36.0f).setDescription("The max amount of damage a crystal can do to you").setVisibility((e) -> noLethalCrystalHit.getValue()));

    public Safety() {
        instance = this;
    }

    @Override
    public void onEnabled() {
        MinecraftForge.EVENT_BUS.register(Eclipse.safetyManager);
    }

    @Override
    public void onDisabled() {
        MinecraftForge.EVENT_BUS.unregister(Eclipse.safetyManager);
    }
}
