package xyz.aesthetical.astra.features.modules.movement.speed;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.MoveEvent;
import xyz.aesthetical.astra.events.entity.MoveUpdateEvent;
import xyz.aesthetical.astra.features.modules.miscellaneous.Freecam;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Mode;
import xyz.aesthetical.astra.managers.modules.Module;

import java.util.Objects;

public class Strafe extends Mode {
    private static Strafe instance;

    public final NumberSetting speed = register(new NumberSetting("Speed", 100).setMin(0).setMax(150).setDescription("The speed of the strafe"));
    public final NumberSetting start = register(new NumberSetting("Start", 2).setMin(0).setMax(4).setDescription("What stage to start on"));

    private int stage = 1;
    public double lastDistance = 0.0;
    private double moveSpeed = 0.0;

    public Strafe(Module module) {
        super("Strafe", module);

        instance = this;
    }

    @Override
    public void onEnabled() {
        moveSpeed = getBaseMoveSpeed();
    }

    @Override
    public void onDisabled() {
        stage = start.getValue().intValue();
        moveSpeed = 0.0;
    }

    @SubscribeEvent
    public void onMoveUpdate(MoveUpdateEvent event) {
        if (Module.fullNullCheck() && event.getStage() == MoveUpdateEvent.Stage.PRE) {
            EntityPlayerSP player = Astra.mc.player;
            lastDistance = MathHelper.sqrt((player.posX - player.prevPosX) * (player.posX - player.prevPosX) + (player.posZ - player.prevPosZ) * (player.posZ - player.prevPosZ));
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (Module.fullNullCheck() && !Astra.moduleManager.getModule(Freecam.class).isToggled()) {
            if (Astra.mc.player.onGround) {
                stage = 2;
            }

            switch (stage) {
                case 0: {
                    ++stage;
                    lastDistance = 0.0;
                    break;
                }

                case 2: {
                    if ((Astra.mc.player.moveForward == 0.0f && Astra.mc.player.moveStrafing == 0.0f) || !Astra.mc.player.onGround) {
                        break;
                    }

                    double motionY = 0.40123128;
                    if (Astra.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                        motionY += (Astra.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1;
                    }

                    Astra.mc.player.motionY = motionY;
                    event.setY(Astra.mc.player.motionY);
                    moveSpeed *= 2.149;
                    break;
                }

                case 3: {
                    moveSpeed = lastDistance - 0.76 * (lastDistance - getBaseMoveSpeed());
                    break;
                }

                default: {
                    if (Astra.mc.world.getCollisionBoxes(Astra.mc.player, Astra.mc.player.getEntityBoundingBox().offset(0.0, Astra.mc.player.motionY, 0.0)).size() > 0 || Astra.mc.player.collidedVertically || stage > 0) {
                        stage = (Astra.mc.player.moveForward != 0.0f && Astra.mc.player.moveStrafing != 0.0f) ? 1 : 0;
                    }

                    moveSpeed = lastDistance - lastDistance / 159.0;
                }
            }

            moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());

            double forward = Astra.mc.player.movementInput.moveForward,
                    strafe = Astra.mc.player.movementInput.moveStrafe,
                    yaw = Astra.mc.player.rotationYaw;

            if (forward == 0.0f && strafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
            } else if (forward != 0.0 && strafe != 0.0) {
                forward *= Math.sin(0.7853981633974483);
                strafe *= Math.cos(0.7853981633974483);
            }

            event.setX((forward * moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
            event.setZ((forward * moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);

            ++stage;
        }
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;

        if (Astra.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(Astra.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * amplifier;
        }

        return baseSpeed;
    }

    private float getMultiplier() {
        float baseSpeed = speed.getValue().intValue();

        if (Astra.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(Astra.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
            baseSpeed = amplifier >= 2 ? 125 : speed.getValue().intValue();
        }

        return baseSpeed / 100.0f;
    }

    public static void setInstance(Module module) {
        if (instance == null) {
            instance = new Strafe(module);
        }
    }

    public static Strafe getInstance() {
        return instance;
    }
}
