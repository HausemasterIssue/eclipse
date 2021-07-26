package xyz.aesthetical.astra.features.modules.movement;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.modules.movement.speed.Bhop;
import xyz.aesthetical.astra.features.modules.movement.speed.Strafe;
import xyz.aesthetical.astra.features.modules.movement.speed.Vanilla;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

import java.util.HashMap;
import java.util.Map;

@Module.Mod(name = "Speed", description = "Speeds you up")
@Module.Info(category = Module.Category.MOVEMENT)
public class Speed extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.ONGROUND).setDescription("How to speed you up"));

    private final Map<Mode, xyz.aesthetical.astra.managers.modules.Mode> modes = new HashMap<>();
    private xyz.aesthetical.astra.managers.modules.Mode previous;

    public Speed() {
        Vanilla.setInstance(this);
        Vanilla.getInstance().getSettings().forEach(s -> s.setVisibility((e) -> mode.getValue() == Mode.VANILLA));

        Bhop.setInstance(this);
        Bhop.getInstance().getSettings().forEach(s -> s.setVisibility((e) -> mode.getValue() == Mode.BHOP));

        Strafe.setInstance(this);
        Strafe.getInstance().getSettings().forEach(s -> s.setVisibility((e) -> mode.getValue() == Mode.STRAFE));

        modes.put(Mode.VANILLA, Vanilla.getInstance());
        modes.put(Mode.BHOP, Bhop.getInstance());
        modes.put(Mode.STRAFE, Strafe.getInstance());

        modes.entrySet().forEach(e -> e.getValue().getSettings().forEach(this::register));
    }

    @Override
    public void onDisabled() {
        if (previous != null) {
            previous.dispose();
            previous = null;
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (previous == null || !previous.getName().equalsIgnoreCase(mode.getValue().name())) {
                if (previous != null) {
                    previous.dispose();
                    previous = null;
                }

                xyz.aesthetical.astra.managers.modules.Mode possibleMode = modes.get(mode.getValue());
                if (possibleMode == null) {
                    return;
                }

                previous = possibleMode;
                previous.use();
            }
        }
    }

    public enum Mode {
        ONGROUND,
        VANILLA,
        BHOP,
        STRAFE
    }
}
