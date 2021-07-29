package xyz.aesthetical.astra.features.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.MoveEvent;
import xyz.aesthetical.astra.features.modules.miscellaneous.Freecam;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "Safewalk", description = "Stops you from falling off of edges")
@Module.Info(category = Module.Category.MOVEMENT)
public class SafeWalk extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.RESTRICT).setDescription("How to stop you from falling off edges"));
    public final NumberSetting distance = register(new NumberSetting("Distance", 1).setMin(0).setMax(256).setDescription("The height before it checks"));

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (Module.fullNullCheck() && Astra.mc.player.onGround && !shouldPause()) {
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();

            double i;
            double fallDistance = -distance.getValue().doubleValue();

            for (i = 0.05; x != 0.0 && isBoundingBoxOffsetEmpty(x, fallDistance, 0.0);) {
                if (x < i && x >= -i) {
                    x = 0.0;
                } else if (x > 0.0) {
                    x -= i;
                } else {
                    x += i;
                }
            }

            while (z != 0.0 && isBoundingBoxOffsetEmpty(0.0, fallDistance, z)) {
                if (z < i && z >= -i) {
                    z = 0.0;
                } else if (z > 0.0) {
                    z -= i;
                } else {
                    z += i;
                }
            }

            while (x != 0.0 && z != 0.0 && isBoundingBoxOffsetEmpty(x, fallDistance, z)) {
                if (x < i && x >= -i) {
                    x = 0.0;
                } else if (x > 0.0) {
                    x -= i;
                } else {
                    x += i;
                }

                if (z < i && z >= -i) {
                    z = 0.0;
                } else if (z > 0.0) {
                    z -= i;
                } else {
                    z += i;
                }
            }

            event.setX(x);
            event.setY(y);
            event.setZ(z);
        }
    }

    private boolean shouldPause() {
        return Astra.moduleManager.getModule(Freecam.class).isToggled() || Astra.moduleManager.getModule(Jesus.class).isToggled();
    }

    public static boolean isBoundingBoxOffsetEmpty(double x, double y, double z) {
        return Astra.mc.world.getCollisionBoxes(Astra.mc.player, Astra.mc.player.getEntityBoundingBox().offset(x, y, z)).isEmpty();
    }

    public enum Mode {
        RESTRICT,
        SNEAK
    }
}
