package xyz.aesthetical.eclipse.mixin.mixins.world.blocks;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.modules.movement.NoSlow;

@Mixin(BlockSlime.class)
public class MixinBlockSlime extends BlockBreakable {
    protected MixinBlockSlime(Material materialIn, boolean ignoreSimilarityIn, MapColor mapColorIn) {
        super(materialIn, ignoreSimilarityIn, mapColorIn);
    }

    @Inject(method = "onEntityWalk", at = @At("HEAD"), cancellable = true)
    public void hookOnEntityWalk(World worldIn, BlockPos pos, Entity entityIn, CallbackInfo info) {
        if (NoSlow.instance.isToggled() && NoSlow.instance.slime.getValue() && entityIn == Eclipse.mc.player) {
            info.cancel();
        }
    }
}
