package xyz.aesthetical.astra.managers;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.util.Timer;

import java.text.DecimalFormat;
import java.util.UUID;

public class ServerManager {
    private final Timer packetTimer = new Timer();
    private final DecimalFormat format = new DecimalFormat("##.0#");
    private ServerData lastServer;

    public ServerManager() {
        packetTimer.reset();
    }

    @SubscribeEvent
    public void onPacketInbound(PacketEvent.Inbound event) {
        packetTimer.reset();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (Astra.mc.getCurrentServerData() != null) {
            lastServer = Astra.mc.getCurrentServerData();
        }
    }

    public boolean isServerUnresponsive() {
        return packetTimer.passedMs(750L);
    }

    public long getServerRespondingTime() {
        return packetTimer.getPassedTimeMs();
    }

    public int getPing(EntityPlayer player) {
        try {
            return Astra.mc.player.connection.getPlayerInfo(player.getUniqueID()).getResponseTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public EntityPlayer getPlayer(UUID uuid) {
        return Astra.mc.world.getPlayerEntityByUUID(uuid);
    }

    public String format(double value) {
        return format.format(value);
    }

    public ServerData getLastServer() {
        return lastServer;
    }
}