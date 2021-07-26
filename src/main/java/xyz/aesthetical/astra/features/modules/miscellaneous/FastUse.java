package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.mixin.mixins.IMinecraft;
import xyz.aesthetical.astra.util.Timer;

// @todo fix
@Module.Mod(name = "FastUse", description = "Allows you to quickly use items")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class FastUse extends Module {
    public final NumberSetting delay = register(new NumberSetting("Delay", 0.0f).setMin(0.0f).setMax(2500.0f).setDescription("How long to wait in MS until being able use items fast"));

    private final Timer timer = new Timer();

    @Override
    public void onEnabled() {
        timer.reset();
    }

    @Override
    public void onDisabled() {
        timer.reset();
        ((IMinecraft) Astra.mc).setRightClickDelayTimer(4);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (Astra.mc.player.isHandActive()) {
                if (delay.getValue().doubleValue() > 0.0) {
                    if (!timer.passedMs(delay.getValue().longValue())) {
                        return;
                    }

                    ((IMinecraft) Astra.mc).setRightClickDelayTimer(4);
                    timer.reset();
                } else {
                    ((IMinecraft) Astra.mc).setRightClickDelayTimer(0);
                }
            }
        }
    }
}
