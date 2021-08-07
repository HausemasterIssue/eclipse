package xyz.aesthetical.astra.features.modules.miscellaneous;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.world.EntityRemoveEvent;
import xyz.aesthetical.astra.events.world.EntitySpawnEvent;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.managers.notifications.Notification;

@Module.Mod(name = "VisualRange", description = "Tells you when a player comes in your visual range")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class VisualRange extends Module {
    public final Setting<Boolean> notification = register(new Setting<>("Notification", true).setDescription("If to notify you with a notification"));
    public final Setting<Boolean> friends = register(new Setting<>("Friends", false).setDescription("If to notify when friends come into your visual range"));

    @SubscribeEvent
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (Module.fullNullCheck() && event.getEntity() instanceof EntityPlayer && event.getEntity() != Astra.mc.player) {
            if (!friends.getValue() && Astra.friendManager.isFriend(event.getEntity().getUniqueID())) {
                return;
            }

            if (notification.getValue()) {
                Astra.notificationManager.createNotification(new Notification(
                        "Player Entered Visual Range",
                        Lists.newArrayList(event.getEntity().getName() + " has entered your visual range."),
                        Notification.Type.WARNING,
                        10L * 1000L
                ));
            }
        }
    }

    @SubscribeEvent
    public void onEntityRemove(EntityRemoveEvent event) {
        if (Module.fullNullCheck() && event.getEntity() instanceof EntityPlayer && event.getEntity() != Astra.mc.player) {
            if (!friends.getValue() && Astra.friendManager.isFriend(event.getEntity().getUniqueID())) {
                return;
            }

            if (notification.getValue()) {
                Astra.notificationManager.createNotification(new Notification(
                        "Player Left Visual Range",
                        Lists.newArrayList(event.getEntity().getName() + " has left your visual range."),
                        Notification.Type.WARNING,
                        10L * 1000L
                ));
            }
        }
    }
}
