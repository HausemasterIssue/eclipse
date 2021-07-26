package xyz.aesthetical.astra.features.modules.render;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "Brightness", description = "Makes the game brighter")
@Module.Info(category = Module.Category.RENDER)
public class Brightness extends Module {
    public static float oldGamma = -1.0f;

    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.GAMMA).setDescription("How to make the game brighter"));

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck()) {
            if (mode.getValue() == Mode.GAMMA) {
                if (Astra.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                    Astra.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
                }

                if (oldGamma == -1.0f) {
                    oldGamma = Astra.mc.gameSettings.gammaSetting;
                }

                Astra.mc.gameSettings.gammaSetting = 100.0f;
            } else if (mode.getValue() == Mode.POTION) {
                if (Astra.mc.gameSettings.gammaSetting != oldGamma) {
                    Astra.mc.gameSettings.gammaSetting = oldGamma;
                    oldGamma = -1.0f;
                }

                if (!Astra.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                    Astra.mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 10000, 1));
                }
            }
        }
    }

    public enum Mode {
        GAMMA,
        POTION
    }
}
