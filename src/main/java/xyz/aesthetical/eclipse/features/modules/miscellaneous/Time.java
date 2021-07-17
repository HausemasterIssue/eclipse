package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "Time", description = "Changes the time client side")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class Time extends Module {
    public static Time instance;

    public final NumberSetting time = register(new NumberSetting("Time", 0).setMin(0).setMax(24000).setDescription("The time to use"));

    private long oldTime = 0L;

    public Time() {
        instance = this;
    }

    @Override
    public void onDisabled() {
        if (Module.fullNullCheck()) {
            Eclipse.mc.world.setWorldTime(oldTime);
            oldTime = 0L;
        }
    }

    @SubscribeEvent
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof SPacketTimeUpdate) {
            SPacketTimeUpdate packet = (SPacketTimeUpdate) event.getPacket();
            oldTime = packet.getWorldTime();

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            Eclipse.mc.world.setWorldTime(time.getValue().longValue());
        }
    }
}
