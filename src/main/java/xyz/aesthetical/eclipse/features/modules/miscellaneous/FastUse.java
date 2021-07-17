package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.mixin.mixins.IMinecraft;

@Module.Mod(name = "FastUse", description = "Allows you to quickly use items")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class FastUse extends Module {
    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (Eclipse.mc.player.isHandActive()) {
                ((IMinecraft) Eclipse.mc).setRightClickDelayTimer(0);
            }
        }
    }
}
