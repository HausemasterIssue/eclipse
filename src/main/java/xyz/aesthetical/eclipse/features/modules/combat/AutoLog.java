package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.world.EntitySpawnEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.EntityUtil;

@Module.Mod(name = "AutoLog", description = "Automatically logs out when something happens")
@Module.Info(category = Module.Category.COMBAT)
public class AutoLog extends Module {
    public final NumberSetting health = register(new NumberSetting("Health", 6.0f).setMin(1.0f).setMax(36.0f).setDescription("The health to logout at"));
    public final Setting<Boolean> totems = register(new Setting<>("Totems", false).setDescription("If to check for totems"));
    public final NumberSetting totemCount = register(new NumberSetting("Totem Count", 0).setMin(0).setMax(36).setDescription("How many totems to logout with").setVisibility((m) -> totems.getValue()));
    public final Setting<Boolean> newPlayers = register(new Setting<>("New Players", false).setDescription("If to logout when a new player comes in range"));
    public final Setting<Boolean> friends = register(new Setting<>("Friends", false).setDescription("If to logout when a friend comes in range").setVisibility((m) -> newPlayers.getValue()));
    public final Setting<Boolean> shutdown = register(new Setting<>("Shutdown", false).setDescription("If to completely shutdown the client"));

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            boolean shouldLog = doChecks();
            if (shouldLog) {
                log();
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (Module.fullNullCheck() && newPlayers.getValue() && event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!friends.getValue() && Eclipse.friendManager.isFriend(player.getUniqueID())) {
                return;
            }

            log();
        }
    }

    private void log() {
        if (shutdown.getValue()) {
            Eclipse.mc.shutdown();
        } else {
            Eclipse.mc.player.connection.getNetworkManager().closeChannel(new TextBuilder.ChatMessage("[&aAutoLog&r] Parameter met to logout."));
        }
    }

    private boolean doChecks() {
        boolean shouldLog = false;

        if (EntityUtil.getTotalHealth(Eclipse.mc.player) <= health.getValue().floatValue()) {
            shouldLog = true;
        }

        if (totems.getValue()) {
            if (AutoTotem.getTotemCount() <= totemCount.getValue().intValue()) {
                shouldLog = true;
            } else {
                shouldLog = false;
            }
        }

        return shouldLog;
    }
}
