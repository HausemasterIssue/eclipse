package xyz.aesthetical.astra.features.modules.movement;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "EntityControl", description = "Allows you to control entities without taming them")
@Module.Info(category = Module.Category.MOVEMENT)
public class EntityControl extends Module {
    public static EntityControl instance;

    public final Setting<Boolean> maxHorseJumpPower = register(new Setting<>("Max Horse Jump", false).setDescription("If to always make the jump power to the max"));

    public EntityControl() {
        instance = this;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && maxHorseJumpPower.getValue()) {
            // @todo - look at EntityPlayerSP (horseJumpPower = 1.0f && horseJumpPowerCounter = -10)
        }
    }
}
