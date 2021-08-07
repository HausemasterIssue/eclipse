package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "NoSwing", description = "Stops you from swinging server side")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class NoSwing extends Module {
    public final Setting<Boolean> mainHand = register(new Setting<>("Main Hand", true).setDescription("If to cancel main hand swing packets"));
    public final Setting<Boolean> offhand = register(new Setting<>("Off Hand", true).setDescription("If to cancel off hand swing packets"));

    @Override
    public String getDisplay() {
        String text = "";

        boolean comma = false;
        if (mainHand.getValue()) {
            text += "Main";
            comma = true;
        }

        if (offhand.getValue()) {
            text += (comma ? ", " : "") + "Offhand";
        }

        return text;
    }

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
