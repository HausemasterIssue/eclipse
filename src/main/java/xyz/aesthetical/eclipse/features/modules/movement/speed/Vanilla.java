package xyz.aesthetical.eclipse.features.modules.movement.speed;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Mode;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.Pair;
import xyz.aesthetical.eclipse.util.RotationUtils;

public class Vanilla extends Mode {
    private static Vanilla instance;

    public final NumberSetting speed = register(new NumberSetting("Speed", 1.0f).setMin(1.0f).setMax(10.0f).setDescription("How fast to go"));

    public Vanilla(Module module) {
        super("Vanilla", module);

        instance = this;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            Pair<Double, Double> directionalSpeed = RotationUtils.getDirectionalSpeed(speed.getValue().doubleValue() / 10.0);

            Eclipse.mc.player.motionX = directionalSpeed.getKey();
            Eclipse.mc.player.motionZ = directionalSpeed.getValue();
        }
    }

    public static void setInstance(Module module) {
        if (instance == null) {
            instance = new Vanilla(module);
        }
    }

    public static Vanilla getInstance() {
        return instance;
    }
}
