package xyz.aesthetical.eclipse.mixin.mixins.world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.eclipse.features.modules.miscellaneous.LiquidInteract;

@Mixin(BlockLiquid.class)
public abstract class MixinBlockLiquid extends Block {
    public MixinBlockLiquid(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "canCollideCheck", at = @At("TAIL"), cancellable = true)
    public void onCanCollideCheck(IBlockState state, boolean hitIfLiquid, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(info.getReturnValue() || LiquidInteract.instance.isToggled());
    }
}
