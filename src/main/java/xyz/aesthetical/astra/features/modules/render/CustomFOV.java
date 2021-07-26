package xyz.aesthetical.astra.features.modules.render;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "CustomFOV", description = "Makes your FOV go lower or higher than normal")
@Module.Info(category = Module.Category.RENDER)
public class CustomFOV extends Module {
    public static float oldFov = -1.0f;

    // the 179.0f has a reason, it fucks up if you go 180+, so we limit there
    public final NumberSetting fov = register(new NumberSetting("FOV", 110.0f).setMin(1.0f).setMax(179.0f).setDescription("The FOV to use"));

    @Override
    public void onDisabled() {
        if (Module.fullNullCheck() && oldFov != -1.0f) {
            Astra.mc.gameSettings.fovSetting = oldFov;
            oldFov = -1.0f;
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (oldFov == -1.0f) {
                oldFov = Astra.mc.gameSettings.fovSetting;
            }

            Astra.mc.gameSettings.fovSetting = fov.getValue().floatValue();
        }
    }
}
