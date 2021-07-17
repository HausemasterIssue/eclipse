package xyz.aesthetical.eclipse.managers.notifications;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.modules.client.Notifications;
import xyz.aesthetical.eclipse.features.sounds.NotificationSound;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.RenderUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationManager {
    private static final ISound NOTIFICATION_SOUND = NotificationSound.getSound();

    private final ArrayList<Notification> notifications = new ArrayList<>();

    @SubscribeEvent
    public void onRenderHudEvent(RenderGameOverlayEvent.Text event) {
        if (Module.fullNullCheck() && !notifications.isEmpty() && Notifications.instance.mode.getValue() == Notifications.Mode.ONSCREEN) {
            GlStateManager.pushMatrix();

            float y = event.getResolution().getScaledHeight() - getHeight(notifications.get(0).getLines()) - 4.0f;

            for (int i = 0; i < notifications.size(); ++i) {
                Notification notification = notifications.get(i);
                if (notification.hasExpired()) {
                    notifications.remove(notification);
                }

                float height = getHeight(notification.getLines());
                float x = event.getResolution().getScaledWidth() - getWidth(notification.getLines()) - 4.0f;

                RenderUtils.drawRect(x, y, getWidth(notification.getLines()), height, 0x77000000);

                float textY = y + 4.0f;
                for (String line : notification.getLines()) {
                    Eclipse.textManager.draw(line, x + 2.3f, textY, -1);
                    textY += Eclipse.textManager.getHeight() + 2.0f;
                }

                y -= height + 8.0f;
            }

            GlStateManager.popMatrix();
        }
    }

    private float getHeight(List<String> lines) {
        return lines.size() * (Eclipse.textManager.getHeight() + 3.0f);
    }

    private int getWidth(List<String> lines) {
        return (int) lines.stream().map(l -> Eclipse.textManager.getWidth(l)).sorted().toArray()[0];
    }

    public void createNotification(Notification notification) {
        if (Notifications.instance.mode.getValue() == Notifications.Mode.CHAT) {
            Eclipse.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(
                    new TextComponentString(Eclipse.commandManager.getSentCommandPrefix() + String.join("\n", notification.getLines())),
                    notification.getName().length() * 100
            );
        } else {
            notifications.add(notification);
        }

        if (Notifications.instance.sound.getValue() != Notifications.Sound.NONE) {
            playNotificationSound();
        }
    }

    public void clearNotifications() {
        notifications.clear();
    }

    public void playNotificationSound() {
        switch (Notifications.instance.sound.getValue()) {
            case ANVIL:
                Eclipse.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_ANVIL_LAND, 1.0f));
                break;

            case ECLIPSE:
                Eclipse.mc.getSoundHandler().playSound(NOTIFICATION_SOUND);
                break;

            case CUSTOM:
                // @todo
                break;
        }
    }
}
