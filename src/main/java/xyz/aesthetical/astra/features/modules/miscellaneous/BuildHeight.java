package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.mixin.mixins.network.packets.outbound.ICPacketPlayerTryUseItemOnBlock;

@Module.Mod(name = "BuildHeight", description = "Allows you to place blocks at build height")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class BuildHeight extends Module {
    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        CPacketPlayerTryUseItemOnBlock packet;
        if (Module.fullNullCheck() && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket()).getDirection() == EnumFacing.UP && packet.getPos().getY() >= 255) {
            ((ICPacketPlayerTryUseItemOnBlock) packet).setPlacedBlockDirection(EnumFacing.DOWN);
        }
    }
}
