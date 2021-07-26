package xyz.aesthetical.astra.mixin.mixins.world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.astra.features.modules.miscellaneous.Avoid;

@Mixin(BlockCactus.class)
public abstract class MixinBlockCactus extends Block implements net.minecraftforge.common.IPlantable {
    public MixinBlockCactus(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void hookGetCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> info) {
        if (Avoid.instance.isToggled() && Avoid.instance.cacti.getValue()) {
            info.setReturnValue(Block.FULL_BLOCK_AABB);
        }
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void hookOnEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn, CallbackInfo info) {
        if (Avoid.instance.isToggled() && Avoid.instance.cacti.getValue()) {
            info.cancel();
        }
    }
}
