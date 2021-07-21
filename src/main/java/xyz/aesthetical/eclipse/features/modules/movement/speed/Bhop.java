package xyz.aesthetical.eclipse.features.modules.movement.speed;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.modules.Mode;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.Pair;
import xyz.aesthetical.eclipse.util.RotationUtils;

public class Bhop extends Mode {
    private static Bhop instance;

    public Bhop(Module module) {
        super("Bhop", module);

        instance = this;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (Eclipse.mc.player.onGround && (Eclipse.mc.player.moveForward != 0.0f || Eclipse.mc.player.moveStrafing != 0.0f)) {
                Eclipse.mc.player.jump();
            }

            Pair<Double, Double> directionalSpeed = RotationUtils.getDirectionalSpeed(0.32);
            Eclipse.mc.player.motionX = directionalSpeed.getKey();
            Eclipse.mc.player.motionZ = directionalSpeed.getValue();
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
