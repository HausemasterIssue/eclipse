package xyz.aesthetical.eclipse.features.modules.render;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "CustomFOV", description = "Makes your FOV go lower or higher than normal")
@Module.Info(category = Module.Category.RENDER)
public class CustomFOV extends Module {
    public static float oldFov = -1.0f;

    // the 179.0f has a reason, it fucks up if you go 180+, so we limit there
    public final NumberSetting fov = register(new NumberSetting("FOV", 110.0f).setMin(1.0f).setMax(179.0f).setDescription("The FOV to use"));

    @Override
    public void onDisabled() {
        if (Module.fullNullCheck() && oldFov != -1.0f) {
            Eclipse.mc.gameSettings.fovSetting = oldFov;
            oldFov = -1.0f;
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (oldFov == -1.0f) {
                oldFov = Eclipse.mc.gameSettings.fovSetting;
            }

            Eclipse.mc.gameSettings.fovSetting = fov.getValue().floatValue();
        }
    }
}
