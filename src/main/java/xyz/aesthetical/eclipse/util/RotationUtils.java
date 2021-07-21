package xyz.aesthetical.eclipse.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import xyz.aesthetical.eclipse.Eclipse;

public class RotationUtils {
    public static void rotate(BlockPos pos, boolean packet) {
        float prevPitch = Eclipse.mc.player.rotationPitch,
                prevYaw = Eclipse.mc.player.rotationYaw,
                prevCamPitch = Eclipse.mc.player.cameraPitch,
                prevCamYaw = Eclipse.mc.player.cameraYaw;

        EntityPlayer player = Eclipse.mc.player;

        Vec3d vec = new Vec3d(pos);
        Vec3d position = player.getPositionVector();

        double[] diff = new double[] { vec.x - position.x, vec.y - position.y, vec.z - position.z };
        double distance = MathHelper.sqrt(diff[0] * diff[0] + diff[2] * diff[2]);

        float yaw = (float) (Math.toDegrees(MathHelper.atan2(diff[2], diff[0])) - 90.0f);
        float pitch = (float) -Math.toDegrees(MathHelper.atan2(diff[1], distance));

        if (packet) {
            Eclipse.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, Eclipse.mc.player.onGround));
        } else {
            Eclipse.mc.player.prevRotationPitch = yaw;
            Eclipse.mc.player.prevRotationYaw = pitch;
            Eclipse.mc.player.rotationYawHead = yaw;

            Eclipse.mc.player.rotationYaw = prevYaw;
            Eclipse.mc.player.rotationPitch = prevPitch;
            Eclipse.mc.player.cameraPitch = prevCamPitch;
            Eclipse.mc.player.cameraYaw = prevCamYaw;
        }
    }

    public static void rotate(Entity entity, boolean packet) {
        float prevPitch = Eclipse.mc.player.rotationPitch,
                prevYaw = Eclipse.mc.player.rotationYaw,
                prevCamPitch = Eclipse.mc.player.cameraPitch,
                prevCamYaw = Eclipse.mc.player.cameraYaw;

        EntityPlayer player = Eclipse.mc.player;

        Vec3d vec = entity.getPositionVector();
        Vec3d position = player.getPositionVector();

        double[] diff = new double[] { vec.x - position.x, vec.y - position.y, vec.z - position.z };
        double distance = MathHelper.sqrt(diff[0] * diff[0] + diff[2] * diff[2]);

        float yaw = (float) (Math.toDegrees(MathHelper.atan2(diff[2], diff[0])) - 90.0f);
        float pitch = (float) -Math.toDegrees(MathHelper.atan2(diff[1], distance));

        if (packet) {
            Eclipse.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, Eclipse.mc.player.onGround));
        } else {
            Eclipse.mc.player.prevRotationPitch = yaw;
            Eclipse.mc.player.prevRotationYaw = pitch;
            Eclipse.mc.player.rotationYawHead = yaw;

            Eclipse.mc.player.rotationYaw = prevYaw;
            Eclipse.mc.player.rotationPitch = prevPitch;
            Eclipse.mc.player.cameraPitch = prevCamPitch;
            Eclipse.mc.player.cameraYaw = prevCamYaw;
        }
    }

    public static Pair<Double, Double> getDirectionalSpeed(double speed) {
        float forward = Eclipse.mc.player.movementInput.moveForward,
                strafe = Eclipse.mc.player.movementInput.moveStrafe,
                yaw = (float) RenderUtils.interpolate(Eclipse.mc.player.rotationYaw, Eclipse.mc.player.prevRotationYaw);

        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (strafe < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }

            strafe = 0.0f;

            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }

        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));

        return new Pair<>(forward * speed * cos + strafe * speed * sin, forward * speed * sin - strafe * speed * cos);
    }
}
