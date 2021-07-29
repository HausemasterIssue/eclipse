package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.client.ShutdownEvent;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.Pair;
import xyz.aesthetical.astra.util.RotationUtils;

@Module.Mod(name = "Freecam", description = "Have an out of body experience")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class Freecam extends Module {
    public final NumberSetting horizontalSpeed = register(new NumberSetting("Horizontal Speed", 1.0f).setMin(0.1f).setMax(5.0f).setDescription("How fast to go horizontally"));
    public final NumberSetting verticalSpeed = register(new NumberSetting("Vertical Speed", 1.0f).setMin(0.1f).setMax(5.0f).setDescription("How fast to go vertically"));

    private EntityOtherPlayerMP fake = null;

    @Override
    public void onEnabled() {
        if (Module.fullNullCheck()) {
            if (fake != null) {
                dispose();
            }

            fake = new EntityOtherPlayerMP(Astra.mc.world, Astra.mc.player.getGameProfile());
            fake.setEntityId(694201337);
            fake.copyLocationAndAnglesFrom(Astra.mc.player);
            fake.inventory.copyInventory(Astra.mc.player.inventory);

            Astra.mc.world.spawnEntity(fake);
        } else {
            toggle();
        }
    }

    @Override
    public void onDisabled() {
        if (Module.fullNullCheck() && fake != null) {
            Astra.mc.player.setPosition(fake.posX, fake.posY, fake.posZ);
            dispose();
        }

        fake = null;
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        toggle(); // so that it doesnt throw an NPE on start
    }

    @SubscribeEvent
    public void onShutdown(ShutdownEvent event) {
        toggle(); // also shutdown here
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() &&
                (event.getPacket() instanceof CPacketPlayer.Position ||
                        event.getPacket() instanceof CPacketPlayer.PositionRotation ||
                        event.getPacket() instanceof CPacketPlayer.Rotation)
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            Astra.mc.player.capabilities.isFlying = true;
            Astra.mc.player.noClip = true;

            if (Keyboard.isKeyDown(Astra.mc.gameSettings.keyBindJump.getKeyCode())) {
                Astra.mc.player.motionY += (verticalSpeed.getValue().doubleValue() / 10.0);
            } else if (Keyboard.isKeyDown(Astra.mc.gameSettings.keyBindSneak.getKeyCode())) {
                Astra.mc.player.motionY -= (verticalSpeed.getValue().doubleValue() / 10.0);
            }

            if (Astra.mc.player.movementInput.moveForward != 0.0f || Astra.mc.player.movementInput.moveStrafe != 0.0f) {
                Pair<Double, Double> shitters = RotationUtils.getDirectionalSpeed(horizontalSpeed.getValue().doubleValue() / 10.0);

                Astra.mc.player.motionX += shitters.getKey();
                Astra.mc.player.motionZ += shitters.getValue();
            }
        }
    }

    private void dispose() {
        Astra.mc.world.removeEntityFromWorld(fake.getEntityId());
        Astra.mc.world.removeEntityDangerously(fake);
    }
}
