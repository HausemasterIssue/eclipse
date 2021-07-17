package xyz.aesthetical.eclipse.managers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.util.Timer;

import java.text.DecimalFormat;
import java.util.UUID;

public class ServerManager {
    private final Timer packetTimer = new Timer();
    private final DecimalFormat format = new DecimalFormat("##.0#");

    public ServerManager() {
        packetTimer.reset();
    }

    @SubscribeEvent
    public void onPacketInbound(PacketEvent.Inbound event) {
        packetTimer.reset();
    }

    public boolean isServerUnresponsive() {
        return packetTimer.passedMs(750L);
    }

    public long getServerRespondingTime() {
        return packetTimer.getPassedTimeMs();
    }

    public int getPing(EntityPlayer player) {
        try {
            return Eclipse.mc.player.connection.getPlayerInfo(player.getUniqueID()).getResponseTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public EntityPlayer getPlayer(UUID uuid) {
        return Eclipse.mc.world.getPlayerEntityByUUID(uuid);
    }

    public String format(double value) {
        return format.format(value);
    }
}