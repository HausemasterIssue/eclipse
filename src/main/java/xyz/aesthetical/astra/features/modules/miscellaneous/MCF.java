package xyz.aesthetical.astra.features.modules.miscellaneous;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.input.MouseEvent;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.friends.Friend;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.managers.notifications.Notification;

@Module.Mod(name = "MCF", description = "Allows you to middle click players to add them as a friend")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class MCF extends Module {
    public final Setting<Boolean> unfriend = register(new Setting<>("Unfriend", true).setDescription("If to allow unfriending by middle clicking again if they're already a friend"));
    public final Setting<Boolean> notify = register(new Setting<>("Notify", true).setDescription("If to notify you once an action is done"));

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (Module.fullNullCheck() && event.getButton() == MouseEvent.Button.MIDDLE) {
            if (Astra.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) Astra.mc.objectMouseOver.entityHit;

                if (Astra.friendManager.isFriend(player.getUniqueID())) {
                    if (unfriend.getValue()) {
                        Friend friend = Astra.friendManager.getFriend(player.getUniqueID());
                        if (friend == null) { // this should never happen as per the above check, but justttt in case
                            return;
                        }

                        Astra.friendManager.remove(friend);
                    }
                } else {
                    Astra.friendManager.add(new Friend(player.getUniqueID()));
                }

                if (notify.getValue()) {
                    Astra.notificationManager.createNotification(new Notification(
                            "Friend " + (Astra.friendManager.isFriend(player.getUniqueID()) ? "Added" : "Removed"),
                            Lists.newArrayList("You have added " + player.getName() + " as a friend."),
                            Notification.Type.NEUTRAL
                    ));
                }
            }
        }
    }
}
