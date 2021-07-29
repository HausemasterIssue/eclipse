package xyz.aesthetical.astra.features.modules.movement;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.MoveEvent;
import xyz.aesthetical.astra.features.modules.miscellaneous.Freecam;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "Parkour", description = "Jumps off the edge of blocks")
@Module.Info(category = Module.Category.MOVEMENT)
public class Parkour extends Module {
    public final Setting<Boolean> sprint = register(new Setting<>("Auto Sprint", true).setDescription("If to automatically sprint if not already"));

    private boolean shouldJump = false;

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (Module.fullNullCheck() && Astra.mc.player.onGround && !shouldPause()) {
            if (!shouldJump && SafeWalk.isBoundingBoxOffsetEmpty(event.getX(), -1, event.getZ())) {
                shouldJump = true;
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && shouldJump) {
            if (sprint.getValue() && !Astra.mc.player.isSprinting()) {
                Astra.mc.player.setSprinting(true);
            }

            if (Astra.mc.player.onGround) {
                Astra.mc.player.jump();
            }

            shouldJump = false;
        }
    }

    private boolean shouldPause() {
        return Astra.moduleManager.getModule(Freecam.class).isToggled() || Astra.moduleManager.getModule(Jesus.class).isToggled();
    }
}
