package xyz.aesthetical.eclipse.util;

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
}
