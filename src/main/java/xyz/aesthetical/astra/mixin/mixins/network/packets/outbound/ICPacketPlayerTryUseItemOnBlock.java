package xyz.aesthetical.astra.mixin.mixins.network.packets.outbound;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayerTryUseItemOnBlock.class)
public interface ICPacketPlayerTryUseItemOnBlock {
    @Accessor("placedBlockDirection")
    void setPlacedBlockDirection(EnumFacing placedBlockDirection);
}
