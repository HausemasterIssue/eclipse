package xyz.aesthetical.eclipse.features.modules.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.render.RenderEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.ColorUtil;
import xyz.aesthetical.eclipse.util.EntityUtil;
import xyz.aesthetical.eclipse.util.RenderUtils;

import java.awt.*;

@Module.Mod(name = "Tracers", description = "Draws lines to entities")
@Module.Info(category = Module.Category.RENDER)
public class Tracers extends Module {
    public final NumberSetting width = register(new NumberSetting("Width", 1.5f).setMin(0.1f).setMax(5.0f).setDescription("The width of the line"));
    public final Setting<Boolean> smooth = register(new Setting<>("Smooth", true).setDescription("If the line should be smoothed out"));
    public final Setting<Boolean> stem = register(new Setting<>("Stem", true).setDescription("If to draw a stem on the entity"));

    // entities to draw tracers to
    public final Setting<Boolean> players = register(new Setting<>("Players", true).setDescription("If to draw a tracer line to players").setGroup("entities"));
    public final Setting<Boolean> friends = register(new Setting<>("Friends", true).setDescription("If to draw a tracer line to friends").setGroup("entities"));
    // @todo public final Setting<Boolean> enemies = register(new Setting<>("Enemies", true).setDescription("If to draw a tracer line to enemies").setGroup("entities"));
    public final Setting<Boolean> passive = register(new Setting<>("Passive", true).setDescription("If to draw a tracer line to passive mobs").setGroup("entities"));
    public final Setting<Boolean> hostile = register(new Setting<>("Hostile", true).setDescription("If to draw a tracer line to hostile mobs").setGroup("entities"));
    public final Setting<Boolean> invisible = register(new Setting<>("Invisible", true).setDescription("If to draw a tracer line to invisible entities").setGroup("entities"));

    // colors for the entities
    public final Setting<Color> playerColor = register(new Setting<>("Player Color", new Color(247, 30, 63)).setDescription("What color to render the player tracer").setGroup("colors"));
    public final Setting<Color> friendColor = register(new Setting<>("Friend Color", new Color(30, 91, 247)).setDescription("What color to render the friend tracer").setGroup("colors"));
    // @todo public final Setting<Color> enemyColor = register(new Setting<>("Enemies Color", new Color(247, 30, 30)).setDescription("What color to render the enemy tracer").setGroup("colors"));
    public final Setting<Color> passiveColor = register(new Setting<>("Enemies Color", new Color(30, 247, 99)).setDescription("What color to render the passive tracer").setGroup("colors"));
    public final Setting<Color> hostileColor = register(new Setting<>("Enemies Color", new Color(255, 0, 0)).setDescription("What color to render the hostile tracer").setGroup("colors"));
    public final Setting<Color> invisibleColor = register(new Setting<>("Invisible Color", new Color(176, 176, 176)).setDescription("What color to render the invisible tracer").setGroup("colors"));

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck()) {
            GlStateManager.pushMatrix();

            for (int i = 0; i < Eclipse.mc.world.loadedEntityList.size(); ++i) {
                Entity entity = Eclipse.mc.world.loadedEntityList.get(i);
                if (!(entity instanceof EntityLivingBase) || entity == Eclipse.mc.player) {
                    continue;
                }

                int color = getColor(entity);
                if (color == -2) {
                    continue;
                }

                RenderUtils.drawTracer(entity, width.getValue().floatValue(), smooth.getValue(), stem.getValue(), color);
            }

            GlStateManager.popMatrix();
        }
    }

    private int getColor(Entity entity) {
        if (invisible.getValue() && entity.isInvisibleToPlayer(Eclipse.mc.player)) {
            Color color = invisibleColor.getValue();
            return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255);
        }

        if (entity instanceof EntityPlayer) {
            if (friends.getValue() && Eclipse.friendManager.isFriend(entity.getUniqueID())) {
                Color color = friendColor.getValue();
                return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255);
            }

            if (players.getValue()) {
                Color color = playerColor.getValue();
                return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255);
            }
        }

        if (hostile.getValue() && EntityUtil.isHostile(entity)) {
            Color color = hostileColor.getValue();
            return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255);
        }

        if (passive.getValue() && EntityUtil.isPassive(entity)) {
            Color color = passiveColor.getValue();
            return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), 255);
        }

        return -2;
    }
}
