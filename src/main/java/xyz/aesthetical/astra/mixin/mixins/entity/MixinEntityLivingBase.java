package xyz.aesthetical.astra.mixin.mixins.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.modules.exploits.GhostHand;
import xyz.aesthetical.astra.managers.modules.Module;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "canEntityBeSeen", at = @At("HEAD"), cancellable = true)
    public void canEntityBeSeen(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (Module.fullNullCheck() && GhostHand.instance.isToggled() && GhostHand.instance.attack.getValue()) {
            info.setReturnValue(entity.getDistance(Astra.mc.player) <= GhostHand.instance.attackRange.getValue().floatValue());
        }
    }
}
