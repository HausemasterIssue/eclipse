package xyz.aesthetical.astra.features.modules.miscellaneous;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.client.ScreenChangeEvent;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.managers.notifications.Notification;

import java.util.ArrayList;

@Module.Mod(name = "AutoRespawn", description = "Automatically respawns you")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class AutoRespawn extends Module {
    public static final ArrayList<BlockPos> deaths = new ArrayList<>();

    public final Setting<Boolean> noDeathScreen = register(new Setting<>("Anti-Death Screen", true).setDescription("If to stop the death screen from rendering"));
    // @todo file saving
    public final Setting<Boolean> deathCoords = register(new Setting<>("Death Coords", false).setDescription("If to notify you and save your death coordinates"));

    @SubscribeEvent
    public void onScreenChange(ScreenChangeEvent event) {
        if (Module.fullNullCheck() && event.getRequested() instanceof GuiGameOver && Astra.mc.player.isDead) {
            BlockPos playerPos = Astra.mc.player.getPosition();
            if (deathCoords.getValue() && !deaths.contains(playerPos)) {
                deaths.add(playerPos);
                Astra.notificationManager.createNotification(new Notification("Death Coordinates", Lists.newArrayList("You died at @todo"), Notification.Type.NEUTRAL));
            }

            if (noDeathScreen.getValue()) {
                event.setCanceled(true);
            }

            Astra.mc.player.respawnPlayer();
        }
    }
}
