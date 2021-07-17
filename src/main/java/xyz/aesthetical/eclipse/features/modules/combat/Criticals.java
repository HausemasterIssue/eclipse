package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "Criticals", description = "Makes your hits criticals")
@Module.Info(category = Module.Category.COMBAT)
public class Criticals extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.PACKET).setDescription("How to make the critical happen"));
    public final NumberSetting yOffset = register(new NumberSetting("Y Offset", 0.3f).setMin(0.1f).setMax(1.1f).setDescription("The y offset for the packet critical").setVisibility((m) -> mode.getValue() == Mode.PACKET));

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && Eclipse.mc.player.onGround) {
                if (mode.getValue() == Mode.PACKET) {
                    Eclipse.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Eclipse.mc.player.posX, Eclipse.mc.player.posY + yOffset.getValue().floatValue(), Eclipse.mc.player.posZ, Eclipse.mc.player.rotationYaw, Eclipse.mc.player.rotationPitch, false));
                    Eclipse.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Eclipse.mc.player.posX, Eclipse.mc.player.posY, Eclipse.mc.player.posZ, Eclipse.mc.player.rotationYaw, Eclipse.mc.player.rotationPitch, false));
                } else if (mode.getValue() == Mode.JUMP) {
                    Eclipse.mc.player.jump();
                    Eclipse.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Eclipse.mc.player.posX, Eclipse.mc.player.posY, Eclipse.mc.player.posZ, Eclipse.mc.player.rotationYaw, Eclipse.mc.player.rotationPitch, false));
                }
            }
        }
    }

    public enum Mode {
        PACKET,
        JUMP
    }
}
