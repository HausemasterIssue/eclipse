package xyz.aesthetical.eclipse.features.modules.render;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "Yaw", description = "Locks your Yaw")
@Module.Info(category = Module.Category.RENDER)
public class Yaw extends Module {
    public final Setting<Direction> yawDirection = register(new Setting<>("Yaw Direction", Direction.NORTH).setDescription("What cardinal direction to turn to"));
    public final NumberSetting customYaw = register(new NumberSetting("Custom Yaw", 0.0f).setMin(0.0f).setMax(360.0f).setDescription("The custom yaw value"));
    public final Setting<Boolean> lockPitch = register(new Setting<>("Lock Pitch", false).setDescription("If to lock the pitch as well"));
    public final NumberSetting customPitch = register(new NumberSetting("Custom Pitch", 0.0f).setMin(-90.0f).setMax(90.0f).setDescription("The custom pitch value").setVisibility((m) -> lockPitch.getValue()));

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (yawDirection.getValue() == Direction.CUSTOM) {
                Eclipse.mc.player.rotationYaw = customYaw.getValue().floatValue();
            } else {
                Eclipse.mc.player.rotationYaw = yawDirection.getValue().getYaw();
            }

            if (lockPitch.getValue()) {
                Eclipse.mc.player.rotationPitch = customPitch.getValue().floatValue();
            }
        }
    }

    public enum Direction {
        NORTH(180.0f),
        NE(255.0f),
        EAST(270.0f),
        SE(315.0f),
        SOUTH(0.0f),
        SW(45.0f),
        WEST(90.0f),
        NW(135.0f),
        CUSTOM(-1.0f);

        private final float yaw;
        Direction(float yaw) {
            this.yaw = yaw;
        }

        public float getYaw() {
            return yaw;
        }
    }
}
