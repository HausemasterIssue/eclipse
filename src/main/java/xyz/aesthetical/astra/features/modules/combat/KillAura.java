package xyz.aesthetical.astra.features.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.EntityUtil;
import xyz.aesthetical.astra.util.RotationUtils;
import xyz.aesthetical.astra.util.Timer;
import xyz.aesthetical.astra.util.WorldUtils;

import java.util.ArrayList;

@Module.Mod(name = "KillAura", description = "Automatically attacks entities in your aura")
@Module.Info(category = Module.Category.COMBAT)
public class KillAura extends Module {
    // target shit
    public final Setting<Priority> priority = register(new Setting<>("Priority", Priority.CLOSEST).setDescription("How to target entities"));
    public final NumberSetting range = register(new NumberSetting("Range", 5.0f).setMin(1.0f).setMax(7.0f).setDescription("How far to target entities for"));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to rotate to the entity when attacking"));
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing your hand client side when attacking"));
    public final Setting<Delay> delay = register(new Setting<>("Delay", Delay.VANILLA).setDescription("How to delay your attacks"));
    public final NumberSetting time = register(new NumberSetting("Delay Time", 550.0f).setMin(0.0f).setMax(5000.0f).setDescription("How long to wait in MS before attacking again").setVisibility((m) -> delay.getValue() == Delay.CUSTOM));
    public final Setting<Boolean> autoSwitch = register(new Setting<>("Auto Switch", false).setDescription("If to automatically switch to a weapon"));
    public final Setting<Weapon> weapon = register(new Setting<>("Weapon", Weapon.SWORD).setDescription("What weapon to switch to").setVisibility((m) -> autoSwitch.getValue()));
    public final Setting<Boolean> sync = register(new Setting<>("Sync", false).setDescription("If to sync with the server by sending entity use packets"));

    // entities and shit
    public final Setting<Boolean> friends = register(new Setting<>("Friends", false).setDescription("If to attack friends"));
    public final Setting<Boolean> players = register(new Setting<>("Players", true).setDescription("If to attack other players"));
    public final Setting<Boolean> passive = register(new Setting<>("Passive", false).setDescription("If to attack passive entities"));
    public final Setting<Boolean> hostile = register(new Setting<>("Hostile", true).setDescription("If to attack hostile entities"));
    public final Setting<Boolean> invisible = register(new Setting<>("Invisible", false).setDescription("If to attack invisible entities"));

    private Entity target = null;
    private final Timer timer = new Timer();

    @Override
    public String getDisplay() {
        if (target != null) {
            return target.getName();
        }

        return super.getDisplay();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (target == null || target.isDead || Astra.mc.player.getDistance(target) > range.getValue().floatValue()) {
                Entity possibleTarget = findTarget();
                if (possibleTarget == null) {
                    return;
                }

                target = possibleTarget;
            }

            if (timeCheckPassed()) {
                if (delay.getValue() == Delay.CUSTOM) {
                    timer.reset();
                }

                if (rotate.getValue()) {
                    RotationUtils.rotate(target, true);
                }

                if (sync.getValue()) {
                    Astra.mc.player.connection.sendPacket(new CPacketUseEntity(target));
                } else {
                    Astra.mc.playerController.attackEntity(Astra.mc.player, target);
                }

                if (swing.getValue()) {
                    WorldUtils.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    private boolean timeCheckPassed() {
        switch (delay.getValue()) {
            case NONE:
                return true;

            case VANILLA:
                return Astra.mc.player.getCooledAttackStrength(0.0f) == 1.0f;

            case CUSTOM:
                return timer.passedMs(time.getValue().longValue());
        }

        return true;
    }

    private Entity findTarget() {
        if (priority.getValue() == Priority.CLOSEST) {
            ArrayList<Class<? extends Entity>> entities = new ArrayList<>();

            if (players.getValue()) {
                entities.add(EntityPlayer.class);
            }

            if (passive.getValue()) {
                entities.add(EntityAnimal.class);
                entities.add(EntityAmbientCreature.class);
                entities.add(EntitySquid.class);
            }

            if (hostile.getValue()) {
                entities.add(EntityMob.class);
            }

            return EntityUtil.findTarget(range.getValue().floatValue(), invisible.getValue(), friends.getValue(), true, entities);
        } else if (priority.getValue() == Priority.HEALTH) {
            return EntityUtil.findTarget(range.getValue().floatValue(), invisible.getValue(), false, friends.getValue(), EntityUtil.Sorting.LOW_HEALTH);
        }

        return null;
    }

    public enum Priority {
        CLOSEST,
        HEALTH
    }

    public enum Delay {
        VANILLA,
        NONE,
        CUSTOM
    }

    public enum Weapon {
        SWORD,
        AXE
    }
}
