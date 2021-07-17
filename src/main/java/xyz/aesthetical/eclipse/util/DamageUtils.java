package xyz.aesthetical.eclipse.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import xyz.aesthetical.eclipse.Eclipse;

public class DamageUtils {
    public static float calculateDamage(double x, double y, double z, EntityPlayer target) {
        double doublePower = 12; // 6 is power of end crystals, so 6*2 = 12
        double distancedSize = target.getDistanceSq(x, y, z) / doublePower;
        double blast = target.world.getBlockDensity(new Vec3d(x, y, z), target.getEntityBoundingBox());

        double impact = (1.0 - distancedSize) * blast;
        float damage = (float) ((impact * impact + impact) / 2.0f * 7.0f * doublePower + 1.0);

        return getBlastReduction(target, getDamageMultiplier(damage), new Explosion(target.world, target, x, y, z, ((float) doublePower) / 2.0f, false, true));
    }

    public static float getBlastReduction(EntityPlayer target, float damage, Explosion explosion) {
        DamageSource src = DamageSource.causeExplosionDamage(explosion);
        damage = CombatRules.getDamageAfterAbsorb(damage, target.getTotalArmorValue(), (float) target.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

        int enchantModifier = EnchantmentHelper.getEnchantmentModifierDamage(target.getArmorInventoryList(), src);
        float f = MathHelper.clamp(enchantModifier, 0.0f, 20.0f);

        damage *= 1.0f - f / 25.0f;

        if (target.isPotionActive(MobEffects.RESISTANCE)) {
            damage -= damage / 4.0f;
        }

        return Math.max(0.0f, damage);
    }

    public static float getDamageMultiplier(float damage) {
        int difficulty = Eclipse.mc.world.getDifficulty().getId();
        return damage * (difficulty == 0 ? 0 : (difficulty == 2 ? 1 : (difficulty == 1 ? 0.5f : 1.5f)));
    }

    public static boolean willPopTotem(EntityPlayer player, float damage) {
        return EntityUtil.getTotalHealth(player) <= damage;
    }

    public static boolean isNaked(EntityPlayer player) {
        return player.getTotalArmorValue() == 0;
    }
}
