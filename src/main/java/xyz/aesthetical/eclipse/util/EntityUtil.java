package xyz.aesthetical.eclipse.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import xyz.aesthetical.eclipse.Eclipse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntityUtil {
    public static EntityPlayer findTarget(float range, boolean invisible, boolean antiNaked, boolean includeFriends, Sorting sorting) {
        final ArrayList<EntityPlayer> players = new ArrayList<>();

        for (EntityPlayer player : Eclipse.mc.world.playerEntities) {
            if (player == Eclipse.mc.player) {
                continue;
            }

            if (player.isInvisibleToPlayer(Eclipse.mc.player) && !invisible) {
                continue;
            }

            if (player.getDistance(Eclipse.mc.player) > range) {
                continue;
            }

            players.add(player);
        }

        if (!players.isEmpty()) {
            if (sorting != Sorting.NONE) {
                switch (sorting) {
                    case LOW_HEALTH: {
                        players.sort(Comparator.comparingDouble(EntityUtil::getTotalHealth));
                        break;
                    }

                    case HIGH_HEALTH: {
                        players.sort((a, b) -> (int) getTotalHealth(b) - (int) getTotalHealth(a));
                        break;
                    }
                }
            }

            return players.get(0);
        }

        return null;
    }

    public static Entity findTarget(float range, boolean invisible, boolean friends, boolean checkHostile, List<Class<? extends Entity>> allowedEntities) {
        final ArrayList<Entity> entities = new ArrayList<>();

        loop: for (int i = 0; i < Eclipse.mc.world.loadedEntityList.size(); ++i) {
            Entity entity = Eclipse.mc.world.loadedEntityList.get(i);
            if (
                    !(entity instanceof EntityLivingBase) ||
                            entity == Eclipse.mc.player ||
                            Eclipse.mc.player.getDistance(entity) > range ||
                            (!invisible && entity.isInvisibleToPlayer(Eclipse.mc.player)) ||
                            (entity instanceof EntityPlayer && !friends && Eclipse.friendManager.isFriend(entity.getUniqueID()))
            ) {
                continue;
            }

            if (checkHostile) {
                if (entity instanceof EntityEnderman && ((EntityEnderman) entity).getRevengeTarget() != null) {
                    continue;
                }

                if (entity instanceof EntityPigZombie && ((EntityPigZombie) entity).getRevengeTarget() != null) {
                    continue;
                }

                if (entity instanceof EntityWolf && ((EntityWolf) entity).getRevengeTarget() != null) {
                    continue;
                }

                if (entity instanceof EntityIronGolem && ((EntityIronGolem) entity).getRevengeTarget() != null) {
                    continue;
                }

                // id 99 is the killer bunny type
                if (entity instanceof EntityRabbit && ((EntityRabbit) entity).getRabbitType() == 99) {
                    continue;
                }
            }

            for (Class<? extends Entity> clazz : allowedEntities) {
                if (clazz.isInstance(entity)) {
                    entities.add(entity);
                    continue loop;
                }
            }
        }

        if (!entities.isEmpty()) {
            return (Entity) entities.stream().sorted((a, b) -> (int) (Eclipse.mc.player.getDistance(b) - Eclipse.mc.player.getDistance(a))).toArray()[0];
        }

        return null;
    }

    public static float getTotalHealth(EntityPlayer player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public static boolean isHostile(Entity entity) {
        if (entity instanceof EntityEnderman && ((EntityEnderman) entity).getRevengeTarget() != null) {
            return true;
        }

        if (entity instanceof EntityPigZombie && ((EntityPigZombie) entity).getRevengeTarget() != null) {
            return true;
        }

        if (entity instanceof EntityWolf && ((EntityWolf) entity).getRevengeTarget() != null) {
            return true;
        }

        if (entity instanceof EntityIronGolem && ((EntityIronGolem) entity).getRevengeTarget() != null) {
            return true;
        }

        // id 99 is the killer bunny type
        if (entity instanceof EntityRabbit && ((EntityRabbit) entity).getRabbitType() == 99) {
            return true;
        }

        return entity instanceof EntityMob;
    }

    public static boolean isPassive(Entity entity) {
        return !isHostile(entity) && (entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid);
    }

    public enum Sorting {
        NONE,
        LOW_HEALTH,
        HIGH_HEALTH
    }
}
