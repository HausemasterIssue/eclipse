package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import xyz.aesthetical.eclipse.events.render.RenderEvent;
import xyz.aesthetical.eclipse.managers.modules.Module;

import java.util.ArrayList;

@Module.Mod(name = "LogoutSpots", description = "Logs players logout spots")
@Module.Info(category = Module.Category.COMBAT)
public class LogoutSpots extends Module {
    private final ArrayList<Spot> spots = new ArrayList<>();

    // @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && !spots.isEmpty()) {
            for (int i = 0; i < spots.size(); ++i) {
                Spot spot = spots.get(i);

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
