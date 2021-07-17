package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "NoSwing", description = "Stops you from swinging server side")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class NoSwing extends Module {
    public final Setting<Boolean> mainHand = register(new Setting<>("Main Hand", true).setDescription("If to cancel main hand swing packets"));
    public final Setting<Boolean> offhand = register(new Setting<>("Off Hand", true).setDescription("If to cancel off hand swing packets"));

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof CPacketAnimation) {
            CPacketAnimation packet = (CPacketAnimation) event.getPacket();
            if ((packet.getHand() == EnumHand.MAIN_HAND && mainHand.getValue()) || (packet.getHand() == EnumHand.OFF_HAND && offhand.getValue())) {
                event.setCanceled(true);
            }
        }
    }
}
