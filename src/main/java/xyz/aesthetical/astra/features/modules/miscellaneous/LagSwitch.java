package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.Timer;

import java.util.ArrayList;

@Module.Mod(name = "LagSwitch", description = "Makes you lag a bit more than you actually are")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class LagSwitch extends Module {
    public final NumberSetting delay = register(new NumberSetting("Delay", 0.4).setMin(0.1).setMax(5.0f).setDescription("How long in S to hold back keepalive packets"));

    private final ArrayList<CPacketKeepAlive> packets = new ArrayList<>();
    private final Timer timer = new Timer();

    @Override
    public String getDisplay() {
        return delay.getValue() + "s";
    }

    @Override
    public void onDisabled() {
        if (Module.fullNullCheck()) {
            for (int i = 0; i < packets.size(); ++i) {
                CPacketKeepAlive packet = packets.get(i);
                Astra.mc.player.connection.sendPacket(packet);
                packets.remove(packet);
            }
        }

        timer.reset();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        packets.clear();
        timer.reset();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && !packets.isEmpty() && timer.passedS(delay.getValue().doubleValue())) {
            for (int i = 0; i < packets.size(); ++i) {
                CPacketKeepAlive packet = packets.get(i);
                Astra.mc.player.connection.sendPacket(packet);
                packets.remove(packet);
            }

            timer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof CPacketKeepAlive && !timer.passedS(delay.getValue().doubleValue())) {
            packets.add((CPacketKeepAlive) event.getPacket());
            event.setCanceled(true);
        }
    }
}
