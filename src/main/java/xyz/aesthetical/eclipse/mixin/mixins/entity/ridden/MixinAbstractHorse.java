package xyz.aesthetical.eclipse.mixin.mixins.entity.ridden;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.eclipse.features.modules.movement.EntityControl;

@Mixin(AbstractHorse.class)
public abstract class MixinAbstractHorse extends AbstractHorse {
    public MixinAbstractHorse(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "isHorseSaddled", at = @At("HEAD"), cancellable = true)
    public void hookIsHorseSaddled(CallbackInfoReturnable<Boolean> info) {
        if (EntityControl.instance.isToggled()) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void hookCanBeSteered(CallbackInfoReturnable<Boolean> info) {
        if (EntityControl.instance.isToggled()) {
            info.setReturnValue(true);
        }
    }
}
