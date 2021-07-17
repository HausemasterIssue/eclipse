package xyz.aesthetical.eclipse.util;

import net.minecraft.entity.player.EntityPlayer;
import xyz.aesthetical.eclipse.Eclipse;

import java.util.ArrayList;
import java.util.Comparator;

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

    public static float getTotalHealth(EntityPlayer player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public enum Sorting {
        NONE,
        LOW_HEALTH,
        HIGH_HEALTH
    }
}
