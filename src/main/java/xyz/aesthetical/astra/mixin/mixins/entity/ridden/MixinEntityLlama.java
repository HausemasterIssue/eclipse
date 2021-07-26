package xyz.aesthetical.astra.mixin.mixins.entity.ridden;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.astra.features.modules.movement.EntityControl;

@Mixin(EntityLlama.class)
public abstract class MixinEntityLlama extends AbstractChestHorse implements IRangedAttackMob {
    public MixinEntityLlama(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void hookCanBeSteered(CallbackInfoReturnable<Boolean> info) {
        if (EntityControl.instance.isToggled()) {
            info.setReturnValue(true);
        }
    }
}
