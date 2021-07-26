package xyz.aesthetical.astra.features.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "Sprint", description = "Automatically sprints for you")
@Module.Info(category = Module.Category.MOVEMENT)
public class Sprint extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.VANILLA).setDescription("How to do the sprint"));

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && !Astra.mc.player.isSprinting()) {
            switch (mode.getValue()) {
                case VANILLA:
                case LEGIT: {
                    if (Astra.mc.player.movementInput.moveForward != 0.0f) {
                        if (mode.getValue().equals(Mode.LEGIT)) {
                            KeyBinding.setKeyBindState(Astra.mc.gameSettings.keyBindSprint.getKeyCode(), true);
                        }

                        Astra.mc.player.setSprinting(true);
                    }
                    break;
                }

                case ALWAYS: {
                    Astra.mc.player.setSprinting(true);
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
