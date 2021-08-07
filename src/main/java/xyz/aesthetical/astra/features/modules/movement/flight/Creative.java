package xyz.aesthetical.astra.features.modules.movement.flight;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Mode;
import xyz.aesthetical.astra.managers.modules.Module;

public class Creative extends Mode {
    private static Creative instance;

    public Creative(Module module) {
        super("Creative", module);
    }

    @Override
    public void onDisabled() {
        Astra.mc.player.capabilities.isFlying = false;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (!Astra.mc.player.capabilities.isFlying) {
                Astra.mc.player.capabilities.isFlying = true;
            }
        }
    }

    public static void setInstance(Module module) {
        if (instance != null) {
            instance = new Creative(module);
        }
    }

    public static Creative getInstance() {
        return instance;
    }
}
