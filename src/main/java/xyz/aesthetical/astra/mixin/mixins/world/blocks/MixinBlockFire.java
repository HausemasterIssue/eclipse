package xyz.aesthetical.astra.mixin.mixins.world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.astra.features.modules.miscellaneous.Avoid;

@Mixin(BlockFire.class)
public abstract class MixinBlockFire extends Block {
    public MixinBlockFire(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void hookGetCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> info) {
        if (Avoid.instance.isToggled() && Avoid.instance.fire.getValue()) {
            info.setReturnValue(Block.FULL_BLOCK_AABB);
        }
    }
}
