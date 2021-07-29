package xyz.aesthetical.astra.features.modules.movement;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.modules.miscellaneous.Freecam;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "ReverseStep", description = "Makes you step down blocks faster")
@Module.Info(category = Module.Category.MOVEMENT)
public class ReverseStep extends Module {
    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (Astra.mc.player.onGround && !shouldPause()) {
                Astra.mc.player.motionY -= 1.0;
            }
        }
    }

    private boolean shouldPause() {
        return Astra.moduleManager.getModule(Freecam.class).isToggled();
    }
}
