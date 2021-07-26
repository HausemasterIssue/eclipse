package xyz.aesthetical.astra.features.modules.movement.speed;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Mode;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.Pair;
import xyz.aesthetical.astra.util.RotationUtils;

public class Bhop extends Mode {
    private static Bhop instance;

    public Bhop(Module module) {
        super("Bhop", module);

        instance = this;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (Astra.mc.player.onGround && (Astra.mc.player.moveForward != 0.0f || Astra.mc.player.moveStrafing != 0.0f)) {
                Astra.mc.player.jump();
            }

            Pair<Double, Double> directionalSpeed = RotationUtils.getDirectionalSpeed(0.32);
            Astra.mc.player.motionX = directionalSpeed.getKey();
            Astra.mc.player.motionZ = directionalSpeed.getValue();
        }
    }

    public static void setInstance(Module module) {
        if (instance == null) {
            instance = new Bhop(module);
        }
    }

    public static Bhop getInstance() {
        return instance;
    }
}
