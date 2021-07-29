package xyz.aesthetical.astra.mixin.mixins.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityEnderPearl.class)
public interface IEntityEnderPearl {
    @Accessor("perlThrower") // inconsistencies in the names of the Class and a field. smh
    EntityLivingBase getPerlThrower();
}
