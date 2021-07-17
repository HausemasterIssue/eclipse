package xyz.aesthetical.eclipse.features.modules.client;

import com.google.common.collect.Lists;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.managers.notifications.Notification;

@Module.Mod(name = "Test", description = "e")
@Module.Info(category = Module.Category.CLIENT)
public class Test extends Module {
    @Override
    public void onEnabled() {
        Eclipse.notificationManager.createNotification(new Notification("ex", Lists.<String>asList("penis idfk lmao ea sports or something", "yeah cock or something xd", new String[]{"smh"}), Notification.Type.NEUTRAL));
    }

    @Override
    public void onDisabled() {
        Eclipse.notificationManager.clearNotifications();
    }
}
