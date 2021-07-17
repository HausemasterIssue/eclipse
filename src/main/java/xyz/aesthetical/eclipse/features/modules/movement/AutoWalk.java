package xyz.aesthetical.eclipse.features.modules.movement;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalInverted;
import baritone.api.pathing.goals.GoalXZ;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "AutoWalk", description = "Automatically walks")
@Module.Info(category = Module.Category.MOVEMENT)
public class AutoWalk extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.VANILLA).setDescription("How to walk automatically"));

    @Override
    public void onEnabled() {
        if (Module.fullNullCheck() && mode.getValue() == Mode.BARITONE) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalInverted(new GoalXZ(0, 0)));
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (mode.getValue().equals(Mode.VANILLA) && !Eclipse.mc.gameSettings.keyBindForward.isPressed()) {
                KeyBinding.setKeyBindState(Eclipse.mc.gameSettings.keyBindForward.getKeyCode(), true);
            }
        }
    }

    public enum Mode {
        VANILLA,
        BARITONE
    }
}
