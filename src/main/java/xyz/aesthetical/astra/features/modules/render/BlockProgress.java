package xyz.aesthetical.astra.features.modules.render;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.render.RenderEvent;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.mixin.mixins.entity.IPlayerControllerMP;
import xyz.aesthetical.astra.util.ColorUtils;
import xyz.aesthetical.astra.util.RenderUtils;

@Module.Mod(name = "BlockProgress", description = "Shows your progress of breaking the block")
@Module.Info(category = Module.Category.RENDER)
public class BlockProgress extends Module {
    @Override
    public String getDisplay() {
        if (Module.fullNullCheck() && Astra.mc.playerController != null) {
            float progress = ((IPlayerControllerMP) Astra.mc.playerController).getCurBlockDamageMP() * 100.0f;
            return String.format("%.2f%n", progress);
        }

        return super.getDisplay();
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && Astra.mc.playerController != null) {
            IPlayerControllerMP controller = (IPlayerControllerMP) Astra.mc.playerController;

            if (controller.getCurBlockDamageMP() != 0.0f && !controller.getCurrentBlock().equals(new BlockPos(-1, -1, -1))) {
                // @todo fix this
                AxisAlignedBB box = new AxisAlignedBB(controller.getCurrentBlock())
                        .grow(-(1.0 - (double) controller.getCurBlockDamageMP()))
                        .offset(RenderUtils.getCameraPos());

                int color = controller.getCurBlockDamageMP() < 0.3f ?
                        ColorUtils.toRGBA(255, 0, 0, 80) :
                        controller.getCurBlockDamageMP() < 0.7f ?
                                ColorUtils.toRGBA(245, 240, 20, 80) :
                                ColorUtils.toRGBA(24, 245, 20, 80);

                RenderUtils.drawFilledBox(box, color);
            }
        }
    }
}
