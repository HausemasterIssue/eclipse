package xyz.aesthetical.eclipse.features.modules.movement;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "ReverseStep", description = "Makes you step down blocks faster")
@Module.Info(category = Module.Category.MOVEMENT)
public class ReverseStep extends Module {
    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (Eclipse.mc.player.onGround) {
                Eclipse.mc.player.motionY -= 1.0;
            }
        }
    }
}
