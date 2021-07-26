package xyz.aesthetical.astra.mixin.mixins.entity.ridden;

import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.astra.features.modules.movement.EntityControl;

@Mixin(AbstractHorse.class)
public abstract class MixinAbstractHorse extends EntityAnimal implements IInventoryChangedListener, IJumpingMount {
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
