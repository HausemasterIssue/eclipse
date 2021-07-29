package xyz.aesthetical.astra.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import xyz.aesthetical.astra.Astra;

public class RotationUtils {
    public static void rotate(BlockPos pos, boolean packet) {
        float prevPitch = Astra.mc.player.rotationPitch;
        float prevYaw = Astra.mc.player.rotationYaw;

        EntityPlayer player = Astra.mc.player;

        Vec3d vec = new Vec3d(pos);
        Vec3d position = player.getPositionVector();

        double[] diff = new double[] { vec.x - position.x, vec.y - position.y, vec.z - position.z };
        double distance = MathHelper.sqrt(diff[0] * diff[0] + diff[2] * diff[2]);

        float yaw = (float) (Math.toDegrees(MathHelper.atan2(diff[2], diff[0])) - 90.0f);
        float pitch = (float) -Math.toDegrees(MathHelper.atan2(diff[1], distance));

        if (packet) {
            Astra.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, Astra.mc.player.onGround));
        }

        Astra.mc.player.rotationYaw = yaw;
        Astra.mc.player.rotationPitch = pitch;
        Astra.mc.player.rotationYaw = prevYaw;
        Astra.mc.player.rotationPitch = prevPitch;
    }

    public static void rotate(Entity entity, boolean packet) {
        float prevPitch = Astra.mc.player.rotationPitch;
        float prevYaw = Astra.mc.player.rotationYaw;

        EntityPlayer player = Astra.mc.player;

        Vec3d vec = entity.getPositionVector();
        Vec3d position = player.getPositionVector();

        double[] diff = new double[] { vec.x - position.x, vec.y - position.y, vec.z - position.z };
        double distance = MathHelper.sqrt(diff[0] * diff[0] + diff[2] * diff[2]);

        float yaw = (float) (Math.toDegrees(MathHelper.atan2(diff[2], diff[0])) - 90.0f);
        float pitch = (float) -Math.toDegrees(MathHelper.atan2(diff[1], distance));

        if (packet) {
            Astra.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, Astra.mc.player.onGround));
        }

        Astra.mc.player.rotationYaw = yaw;
        Astra.mc.player.rotationPitch = pitch;
        Astra.mc.player.rotationYaw = prevYaw;
        Astra.mc.player.rotationPitch = prevPitch;
    }

    public static Pair<Double, Double> getDirectionalSpeed(double speed) {
        float forward = Astra.mc.player.movementInput.moveForward,
                strafe = Astra.mc.player.movementInput.moveStrafe,
                yaw = (float) RenderUtils.interpolate(Astra.mc.player.rotationYaw, Astra.mc.player.prevRotationYaw);

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
