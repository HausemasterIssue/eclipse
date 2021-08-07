package xyz.aesthetical.astra.features.modules.movement;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.mixin.accessors.INetworkManager;
import xyz.aesthetical.astra.util.Timer;

import java.util.ArrayList;

@Module.Mod(name = "Blink", description = "Suspends movement packets")
@Module.Info(category = Module.Category.MOVEMENT)
public class Blink extends Module {
    public final NumberSetting delay = register(new NumberSetting("Delay", 2.5f).setMin(0.5f).setMax(20.0f).setDescription("How long to suspend your packets for in S"));
    public final Setting<Boolean> fakePlayer = register(new Setting<>("Fake Player", true).setDescription("If to spawn a fake player"));

    private final Timer timer = new Timer();
    private final ArrayList<CPacketPlayer> packets = new ArrayList<>();
    private EntityOtherPlayerMP fake = null;

    @Override
    public String getDisplay() {
        return String.valueOf(delay.getValue());
    }

    @Override
    public void onDisabled() {
        if (Module.fullNullCheck()) {
            sendAllSuspendedPackets();

            if (fake != null) {
                removeFakePlayer();
            }
        } else {
            packets.clear();
        }

        fake = null;
        timer.reset();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (timer.passedS(delay.getValue().doubleValue())) {
                sendAllSuspendedPackets();
                if (fakePlayer.getValue()) {
                    spawnFakePlayer();
                }

                timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof CPacketPlayer) {
            packets.add((CPacketPlayer) event.getPacket());
            event.setCanceled(true);
        }
    }

    private void spawnFakePlayer() {
        if (fake != null) {
            removeFakePlayer();
        }

        fake = new EntityOtherPlayerMP(Astra.mc.world, Astra.mc.player.getGameProfile());
        fake.copyLocationAndAnglesFrom(Astra.mc.player);
        fake.inventory.copyInventory(Astra.mc.player.inventory);
        fake.setEntityId(-69420);
        fake.setGameType(GameType.SURVIVAL);

        Astra.mc.world.spawnEntity(fake);
    }

    private void removeFakePlayer() {
        Astra.mc.world.removeEntityFromWorld(fake.getEntityId());
        Astra.mc.world.removeEntityDangerously(fake);
        fake = null;
    }

    private void sendAllSuspendedPackets() {
        for (int i = 0; i < packets.size(); ++i) {
            CPacketPlayer packet = packets.get(i);
            if (packet != null) {
                // this is done because if it becomes null during the loop it will probably crash, idk but how i made it it def probs would
                if (Module.fullNullCheck()) {
                    ((INetworkManager) Astra.mc.player.connection.getNetworkManager()).forceSendPacket(packet);
                }

                packets.remove(packet);
            }
        }
    }
}
