package xyz.aesthetical.astra.features.modules.combat;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.EnumConverter;

@Module.Mod(name = "Criticals", description = "Makes your hits criticals")
@Module.Info(category = Module.Category.COMBAT)
public class Criticals extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.PACKET).setDescription("How to make the critical happen"));
    public final NumberSetting yOffset = register(new NumberSetting("Y Offset", 0.3f).setMin(0.1f).setMax(1.1f).setDescription("The y offset for the packet critical").setVisibility((m) -> mode.getValue() == Mode.PACKET));

    @Override
    public String getDisplay() {
        return EnumConverter.getActualName(mode.getValue());
    }

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && Astra.mc.player.onGround) {
                if (mode.getValue() == Mode.PACKET) {
                    Astra.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Astra.mc.player.posX, Astra.mc.player.posY + yOffset.getValue().floatValue(), Astra.mc.player.posZ, Astra.mc.player.rotationYaw, Astra.mc.player.rotationPitch, false));
                    Astra.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Astra.mc.player.posX, Astra.mc.player.posY, Astra.mc.player.posZ, Astra.mc.player.rotationYaw, Astra.mc.player.rotationPitch, false));
                } else if (mode.getValue() == Mode.JUMP) {
                    Astra.mc.player.jump();
                    Astra.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Astra.mc.player.posX, Astra.mc.player.posY, Astra.mc.player.posZ, Astra.mc.player.rotationYaw, Astra.mc.player.rotationPitch, false));
                }
            }
        }
    }

    public enum Mode {
        PACKET,
        JUMP
    }
}
