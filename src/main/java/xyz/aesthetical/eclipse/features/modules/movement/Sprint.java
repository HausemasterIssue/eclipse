package xyz.aesthetical.eclipse.features.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "Sprint", description = "Automatically sprints for you")
@Module.Info(category = Module.Category.MOVEMENT)
public class Sprint extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.VANILLA).setDescription("How to do the sprint"));

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player && !Eclipse.mc.player.isSprinting()) {
            switch (mode.getValue()) {
                case VANILLA:
                case LEGIT: {
                    if (Eclipse.mc.player.movementInput.moveForward != 0.0f) {
                        if (mode.getValue().equals(Mode.LEGIT)) {
                            KeyBinding.setKeyBindState(Eclipse.mc.gameSettings.keyBindSprint.getKeyCode(), true);
                        }

                        Eclipse.mc.player.setSprinting(true);
                    }
                    break;
                }

                case ALWAYS: {
                    Eclipse.mc.player.setSprinting(true);
                    break;
                }
            }
        }
    }

    public enum Mode {
        VANILLA,
        LEGIT,
        ALWAYS
    }
}
