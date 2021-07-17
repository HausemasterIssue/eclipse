package xyz.aesthetical.eclipse.features.modules.movement;

import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.mixin.mixins.network.packets.inbound.ISPacketEntityVelocity;
import xyz.aesthetical.eclipse.mixin.mixins.network.packets.inbound.ISPacketExplosion;

@Module.Mod(name = "Velocity", description = "Changes how you handle velocity")
@Module.Info(category = Module.Category.MOVEMENT)
public class Velocity extends Module {
    public final NumberSetting horizontal = register(new NumberSetting("Horizontal", 0.0f).setMin(0.0f).setMax(100.0f).setDescription("What percent of horizontal velocity you take"));
    public final NumberSetting vertical = register(new NumberSetting("Vertical", 0.0f).setMin(0.0f).setMax(100.0f).setDescription("What percent of vertical velocity you take"));
    public final Setting<Boolean> explosions = register(new Setting<>("Explosions", false).setDescription("If to stop knockback from explosions"));
    public final Setting<Boolean> liquids = register(new Setting<>("Liquids", false).setDescription("If to stop liquids from slowing you down"));
    public final Setting<Boolean> noPush = register(new Setting<>("No Push", false).setDescription("If to stop things from pushing you"));
    public final Setting<Boolean> noKnockback = register(new Setting<>("Anti-Knockback", false).setDescription("If to take knockback"));
    public final Setting<Boolean> fishingHooks = register(new Setting<>("Fishing Hooks", false).setDescription("If to stop from being pulled by fishhooks"));

    @SubscribeEvent
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (Module.fullNullCheck()) {
            if (event.getPacket() instanceof SPacketExplosion && explosions.getValue()) {
                SPacketExplosion packet = (SPacketExplosion) event.getPacket();
                ((ISPacketExplosion) packet).setMotionX(packet.getMotionX() * horizontal.getValue().floatValue() / 100.0f);
                ((ISPacketExplosion) packet).setMotionY(packet.getMotionY() * vertical.getValue().floatValue() / 100.0f);
                ((ISPacketExplosion) packet).setMotionZ(packet.getMotionZ() * horizontal.getValue().floatValue() / 100.0f);
            }

            if (event.getPacket() instanceof SPacketEntityVelocity && noKnockback.getValue()) {
                SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();
                if (packet.getEntityID() == Eclipse.mc.player.getEntityId()) {
                    ((ISPacketEntityVelocity) packet).setMotionX(packet.getMotionX() * horizontal.getValue().intValue() / 100);
                    ((ISPacketEntityVelocity) packet).setMotionY(packet.getMotionY() * vertical.getValue().intValue() / 100);
                    ((ISPacketEntityVelocity) packet).setMotionZ(packet.getMotionZ() * horizontal.getValue().intValue() / 100);
                }
            }

            if (event.getPacket() instanceof SPacketEntityStatus && fishingHooks.getValue()) {
                SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
                if (packet.getOpCode() == 31 && packet.getEntity(Eclipse.mc.world) instanceof EntityFishHook) {
                    EntityFishHook hook = (EntityFishHook) packet.getEntity(Eclipse.mc.world);
                    if (hook.caughtEntity == Eclipse.mc.player) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
