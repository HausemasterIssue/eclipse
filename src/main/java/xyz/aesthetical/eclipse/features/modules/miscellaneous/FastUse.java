package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.mixin.mixins.IMinecraft;
import xyz.aesthetical.eclipse.util.Timer;

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
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (Eclipse.mc.player.isHandActive()) {
                if (timer.passedMs(delay.getValue().longValue())) {
                    ((IMinecraft) Eclipse.mc).setRightClickDelayTimer(0);
                    timer.reset();
                } else {
                    ((IMinecraft) Eclipse.mc).setRightClickDelayTimer(4);
                }
            }
        }
    }
}
