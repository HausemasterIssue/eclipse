package xyz.aesthetical.astra.mixin.mixins.world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.astra.features.modules.render.Xray;
import xyz.aesthetical.astra.managers.XrayManager;

@Mixin(Block.class)
public abstract class MixinBlock extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Block> {
    @Inject(method = "getAmbientOcclusionLightValue", at = @At("HEAD"), cancellable = true)
    public void getAmbientOcclusionLightValue(IBlockState state, CallbackInfoReturnable<Float> info) {
        if (Xray.instance.isToggled() && XrayManager.instance.isValidBlock(state.getBlock())) {
            info.setReturnValue(1.0f);
        }
    }

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> info) {
        if (Xray.instance.isToggled()) {
            info.setReturnValue(XrayManager.instance.isValidBlock(blockState.getBlock()));
        }
    }
}
