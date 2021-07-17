package xyz.aesthetical.eclipse.features.modules.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.render.RenderEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.ColorUtil;
import xyz.aesthetical.eclipse.util.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

@Module.Mod(name = "Breadcrumbs", description = "Draws a line wherever you go")
@Module.Info(category = Module.Category.RENDER)
public class Breadcrumbs extends Module {
    public final NumberSetting width = register(new NumberSetting("Width", 1.0f).setMin(0.1f).setMax(5.0f).setDescription("The width of the line"));
    public final Setting<Color> color = register(new Setting<>("Color", new Color(237, 47, 47)).setDescription("The color to render the line width"));

    private final ArrayList<Crumb> crumbs = new ArrayList<>();
    private double x = -1.0;
    private double y = -1.0;
    private double z = -1.0;

    @Override
    public void onDisabled() {
        crumbs.clear();
        x = y = z = -1.0;
    }

    @SubscribeEvent
    public void onUpdate(RenderEvent event) {
        if (Module.fullNullCheck()) {
            if (x == -1.0 && y == -1.0 && z == -1.0) {
                x = Eclipse.mc.player.posX;
                y = Eclipse.mc.player.posY;
                z = Eclipse.mc.player.posZ;

                return;
            }

            crumbs.add(new Crumb(x, y, z, Eclipse.mc.player.posX, Eclipse.mc.player.posY, Eclipse.mc.player.posZ));

            x = Eclipse.mc.player.posX;
            y = Eclipse.mc.player.posY;
            z = Eclipse.mc.player.posZ;
        }
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && !crumbs.isEmpty()) {
            GlStateManager.pushMatrix();

            for (int i = 0; i < crumbs.size(); ++i) {
                Crumb crumb = crumbs.get(i);
                Vec3d pos1 = new Vec3d(crumb.x, crumb.y, crumb.z).add(RenderUtils.getCameraPos());
                Vec3d pos2 = new Vec3d(crumb.x2, crumb.y2, crumb.z2).add(RenderUtils.getCameraPos());

                RenderUtils.drawLine((float) pos1.x, (float) pos1.y, (float) pos1.z, (float) pos2.x, (float) pos2.y, (float) pos2.z, width.getValue().floatValue(), true, ColorUtil.toRGBA(255, 255, 255, 255));
            }

            GlStateManager.popMatrix();
        }
    }

    private static class Crumb {
        public double x;
        public double y;
        public double z;
        public double x2;
        public double y2;
        public double z2;

        public Crumb(double x, double y, double z, double x2, double y2, double z2) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.x2 = x2;
            this.y2 = y2;
            this.z2 = z2;
        }
    }
}
