package xyz.aesthetical.eclipse.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.entity.TotemPopEvent;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.events.world.EntitySpawnEvent;
import xyz.aesthetical.eclipse.features.modules.client.Safety;
import xyz.aesthetical.eclipse.features.modules.combat.AutoTotem;
import xyz.aesthetical.eclipse.features.modules.combat.Surround;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.DamageUtils;
import xyz.aesthetical.eclipse.util.EntityUtil;

public class SafetyManager {
    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (event.getPlayer() == Eclipse.mc.player) {
            if (Safety.instance.autoTotem.getValue()) {
                Module autoTotem = Eclipse.moduleManager.getModule(AutoTotem.class);
                if (!autoTotem.isToggled()) {
                    autoTotem.toggle();
                }
            }

            if (Safety.instance.antiChainPop.getValue()) {
                // surround
                // @todo check if already surrounded
                Eclipse.moduleManager.getModule(Surround.class).toggle();
            }
        }
    }

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck()) {
            if (event.getPacket() instanceof CPacketUseEntity && Safety.instance.noLethalCrystalHit.getValue()) {
                CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();

                if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                    Entity entity = packet.getEntityFromWorld(Eclipse.mc.world);
                    if (entity instanceof EntityEnderCrystal) {
                        EntityEnderCrystal crystal = (EntityEnderCrystal) entity;

                        // check damage
                        float damage = DamageUtils.calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, Eclipse.mc.player);
                        if (damage >= Safety.instance.maxCrystalDamage.getValue().floatValue() || damage >= EntityUtil.getTotalHealth(Eclipse.mc.player) || DamageUtils.willPopTotem(Eclipse.mc.player, damage)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (Module.fullNullCheck()) {
            // a crystal was spawned
            if (event.getEntity() instanceof EntityEnderCrystal && Safety.instance.autoSurround.getValue()) {
                EntityEnderCrystal crystal = (EntityEnderCrystal) event.getEntity();
                // check the damage done to us
                float damage = DamageUtils.calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, Eclipse.mc.player);

                // @todo make the 6.0f an option in Safety
                if (damage >= 6.0f || damage >= EntityUtil.getTotalHealth(Eclipse.mc.player) || DamageUtils.willPopTotem(Eclipse.mc.player, damage)) {
                    // @todo check if already surrounded
                    Eclipse.moduleManager.getModule(Surround.class).toggle();
                }
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {

        }
    }


}
