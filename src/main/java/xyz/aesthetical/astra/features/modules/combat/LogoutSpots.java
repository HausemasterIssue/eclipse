package xyz.aesthetical.astra.features.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.ConnectionEvent;
import xyz.aesthetical.astra.events.render.RenderEvent;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.ColorUtils;
import xyz.aesthetical.astra.util.RenderUtils;

import java.util.ArrayList;

@Module.Mod(name = "LogoutSpots", description = "Logs players logout spots")
@Module.Info(category = Module.Category.COMBAT)
public class LogoutSpots extends Module {
    private final ArrayList<Spot> spots = new ArrayList<>();

    @SubscribeEvent
    public void onConnection(ConnectionEvent event) {
        if (Module.fullNullCheck() && event.getPlayer() != null && event.getPlayer() != Astra.mc.player && event.getAction() == ConnectionEvent.Action.DISCONNECT) {
            EntityPlayer player = event.getPlayer();
            Vec3d screen = RenderUtils.interpolateEntity(player).subtract(RenderUtils.getCameraPos());

            spots.add(new Spot(
                    new AxisAlignedBB(
                            player.getEntityBoundingBox().minX - 0.05 - player.posX + screen.x,
                            player.getEntityBoundingBox().minY - 0.0 - player.posY + screen.y,
                            player.getEntityBoundingBox().minZ - 0.05 - player.posZ + screen.z,
                            player.getEntityBoundingBox().maxX + 0.05  - player.posX + screen.x,
                            player.getEntityBoundingBox().maxY + 0.1  - player.posY + screen.y,
                            player.getEntityBoundingBox().minZ - 0.05  - player.posZ + screen.z
                    ),
                    player.getPosition(),
                    event.getUsername()
            ));
        }
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && !spots.isEmpty()) {
            for (int i = 0; i < spots.size(); ++i) {
                Spot spot = spots.get(i);

                // @todo yes
                RenderUtils.drawFilledBox(spot.getBox(), ColorUtils.toRGBA(255, 255, 255, 80));
            }
        }
    }

    private static class Spot {
        private final AxisAlignedBB box;
        private final BlockPos pos;
        private final String username;

        public Spot(AxisAlignedBB box, BlockPos pos, String username) {
            this.box = box;
            this.pos = pos;
            this.username = username;
        }

        public AxisAlignedBB getBox() {
            return box;
        }

        public BlockPos getPos() {
            return pos;
        }

        public String getUsername() {
            return username;
        }
    }
}
