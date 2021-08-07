package xyz.aesthetical.astra.mixin.mixins.entity;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface IPlayerControllerMP {
    @Accessor("curBlockDamageMP")
    float getCurBlockDamageMP();

    @Accessor("currentBlock")
    BlockPos getCurrentBlock();
}
