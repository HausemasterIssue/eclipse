package xyz.aesthetical.eclipse.features.modules.render;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "Brightness", description = "Makes the game brighter")
@Module.Info(category = Module.Category.RENDER)
public class Brightness extends Module {
    public static float oldGamma = -1.0f;

    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.GAMMA).setDescription("How to make the game brighter"));

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck()) {
            if (mode.getValue() == Mode.GAMMA) {
                if (Eclipse.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                    Eclipse.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
                }

                if (oldGamma == -1.0f) {
                    oldGamma = Eclipse.mc.gameSettings.gammaSetting;
                }

                Eclipse.mc.gameSettings.gammaSetting = 100.0f;
            } else if (mode.getValue() == Mode.POTION) {
                if (Eclipse.mc.gameSettings.gammaSetting != oldGamma) {
                    Eclipse.mc.gameSettings.gammaSetting = oldGamma;
                    oldGamma = -1.0f;
                }

                if (!Eclipse.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                    Eclipse.mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 10000, 1));
                }
            }
        }
    }

    public enum Mode {
        GAMMA,
        POTION
    }
}
