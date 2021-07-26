package xyz.aesthetical.astra.features.modules.movement.speed;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Mode;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.Pair;
import xyz.aesthetical.astra.util.RotationUtils;

public class Vanilla extends Mode {
    private static Vanilla instance;

    public final NumberSetting speed = register(new NumberSetting("Speed", 1.0f).setMin(1.0f).setMax(10.0f).setDescription("How fast to go"));

    public Vanilla(Module module) {
        super("Vanilla", module);

        instance = this;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            Pair<Double, Double> directionalSpeed = RotationUtils.getDirectionalSpeed(speed.getValue().doubleValue() / 10.0);

            Astra.mc.player.motionX = directionalSpeed.getKey();
            Astra.mc.player.motionZ = directionalSpeed.getValue();
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
