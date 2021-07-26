package xyz.aesthetical.astra.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.TotemPopEvent;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.events.world.EntitySpawnEvent;
import xyz.aesthetical.astra.features.modules.client.Safety;
import xyz.aesthetical.astra.features.modules.combat.AutoTotem;
import xyz.aesthetical.astra.features.modules.combat.Surround;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.DamageUtils;
import xyz.aesthetical.astra.util.EntityUtil;

public class SafetyManager {
    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (event.getPlayer() == Astra.mc.player) {
            if (Safety.instance.autoTotem.getValue()) {
                Module autoTotem = Astra.moduleManager.getModule(AutoTotem.class);
                if (!autoTotem.isToggled()) {
                    autoTotem.toggle();
                }
            }

            if (Safety.instance.antiChainPop.getValue()) {
                // surround
                // @todo check if already surrounded
                Astra.moduleManager.getModule(Surround.class).toggle();
            }
        }
    }

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck()) {
            if (event.getPacket() instanceof CPacketUseEntity && Safety.instance.noLethalCrystalHit.getValue()) {
                CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();

                if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                    Entity entity = packet.getEntityFromWorld(Astra.mc.world);
                    if (entity instanceof EntityEnderCrystal) {
                        EntityEnderCrystal crystal = (EntityEnderCrystal) entity;

                        // check damage
                        float damage = DamageUtils.calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, Astra.mc.player);
                        if (damage >= Safety.instance.maxCrystalDamage.getValue().floatValue() || damage >= EntityUtil.getTotalHealth(Astra.mc.player) || DamageUtils.willPopTotem(Astra.mc.player, damage)) {
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
                float damage = DamageUtils.calculateDamage(crystal.posX + 0.5, crystal.posY + 1.0, crystal.posZ + 0.5, Astra.mc.player);

                // @todo make the 6.0f an option in Safety
                if (damage >= 6.0f || damage >= EntityUtil.getTotalHealth(Astra.mc.player) || DamageUtils.willPopTotem(Astra.mc.player, damage)) {
                    // @todo check if already surrounded
                    Astra.moduleManager.getModule(Surround.class).toggle();
                }
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {

        }
    }


}
