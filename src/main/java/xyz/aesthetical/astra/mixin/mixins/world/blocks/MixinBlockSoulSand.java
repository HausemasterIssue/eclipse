package xyz.aesthetical.astra.mixin.mixins.world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.modules.movement.NoSlow;

@Mixin(BlockSoulSand.class)
public abstract class MixinBlockSoulSand extends Block {
    public MixinBlockSoulSand(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void hookOnEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn, CallbackInfo info) {
        if (NoSlow.instance.isToggled() && NoSlow.instance.soulsand.getValue() && entityIn == Astra.mc.player) {
            info.cancel();
        }
    }
}
