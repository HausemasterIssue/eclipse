package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "NoRotate", description = "Stops from you processing server rotation packets")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class NoRotate extends Module {
    @SubscribeEvent
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof SPacketPlayerPosLook) {
            event.setCanceled(true);
        }
    }
}
