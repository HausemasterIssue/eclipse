package xyz.aesthetical.eclipse.mixin.mixins.world.blocks.state;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.eclipse.events.world.blocks.LiquidCollisionEvent;
import xyz.aesthetical.eclipse.features.modules.render.Xray;
import xyz.aesthetical.eclipse.managers.XrayManager;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(BlockStateContainer.StateImplementation.class)
public abstract class MixinStateImplementation extends BlockStateBase {
    @Shadow
    @Final
    private Block block;

    @Redirect(method = "addCollisionBoxToList", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V"))
    public void hookAddCollisionBoxToList(Block bl, IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185908_6_) {
        if (block instanceof BlockLiquid) {
            LiquidCollisionEvent event = new LiquidCollisionEvent(collidingBoxes, block, pos, entityBox, entityIn);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                block.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_);
            }
        } else {
            block.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_);
        }
    }

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLightValue(CallbackInfoReturnable<Integer> info) {
        if (Xray.instance.isToggled() && XrayManager.instance.isValidBlock(block)) {
            info.setReturnValue(100000);
        }
    }
}
