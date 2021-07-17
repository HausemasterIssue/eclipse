package xyz.aesthetical.eclipse.features.modules.client;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.eclipse.ModuleToggledEvent;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.managers.notifications.Notification;

@Module.Mod(name = "Notifications", description = "Manages notifications")
@Module.Info(category = Module.Category.CLIENT)
public class Notifications extends Module {
    public static Notifications instance;

    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.CHAT).setDescription("How to display the notifications"));
    public final Setting<Sound> sound = register(new Setting<>("Sound", Sound.NONE).setDescription("What sound to play when a notification goes off"));
    public final Setting<Boolean> moduleToggle = register(new Setting<>("Module Toggle", true).setDescription("If to notify you when a module is enabled"));

    public Notifications() {
        instance = this;
    }

    @SubscribeEvent
    public void onModuleToggled(ModuleToggledEvent event) {
        if (Module.fullNullCheck() && moduleToggle.getValue()) {
            Module module = event.getModule();

            if (mode.getValue() == Mode.CHAT) {
                Eclipse.notificationManager.createNotification(new Notification(
                        "Module Toggled (" + module.getName() + ")",
                        Lists.newArrayList((module.isToggled() ? ChatColor.Green : ChatColor.Red) + (module.isToggled() ? "Toggled" : "Disabled") + ChatColor.Reset + " " + ChatColor.Dark_Gray + "the module " + module.getName() + "." + ChatColor.Reset),
                        Notification.Type.OTHER
                ));
            } else if (mode.getValue() == Mode.ONSCREEN) {
                // @todo
            }
        }
    }

    public enum Mode {
        CHAT,
        ONSCREEN
    }

    public enum Sound {
        NONE,
        ANVIL,
        ECLIPSE,
        CUSTOM
    }
}
