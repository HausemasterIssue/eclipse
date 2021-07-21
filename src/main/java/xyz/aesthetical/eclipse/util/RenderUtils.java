package xyz.aesthetical.eclipse.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.mixin.mixins.render.IEntityRenderer;

public class RenderUtils {
    public static void drawFilledBox(AxisAlignedBB box, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        float alpha = (color >> 24 & 0xff) / 255f;
        float red = (color >> 16 & 0xff) / 255f;
        float green = (color >> 8 & 0xff) / 255f;
        float blue = (color & 0xff) / 255f;

        RenderGlobal.renderFilledBox(box, red, green, blue, alpha);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void renderNametag(String text, Vec3d pos, double yOffset, float transparency) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(pos.x, pos.y + yOffset, pos.z);
        GlStateManager.glNormal3f(0f, 1f, 0f);
        GlStateManager.rotate(-Eclipse.mc.player.rotationYaw, 0f, 1f, 0f);
        GlStateManager.rotate(Eclipse.mc.player.rotationPitch, 1f, 0f, 0f);
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();

        float i = Eclipse.textManager.getWidth(text) / 2f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-i - 1, -1, 0).color(0.0f, 0.0f, 0.0f, transparency).endVertex();
        buffer.pos(-i - 1, 8, 0).color(0.0f, 0.0f, 0.0f, transparency).endVertex();
        buffer.pos(i + 1, 8, 0).color(0.0f, 0.0f, 0.0f, transparency).endVertex();
        buffer.pos(i + 1, -1, 0).color(0.0f, 0.0f, 0.0f, transparency).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);

        Eclipse.textManager.draw(text, -i, 0, -1);

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void drawLine(double x, double y, double z, double x2, double y2, double z2, float width, boolean smooth, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        GL11.glLineWidth(width);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        GlStateManager.disableDepth();

        GL11.glEnable(GL32.GL_DEPTH_CLAMP);

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();

        float alpha = (color >> 24 & 0xff) / 255f;
        float red = (color >> 16 & 0xff) / 255f;
        float green = (color >> 8 & 0xff) / 255f;
        float blue = (color & 0xff) / 255f;

        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, z).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawTracer(Entity entity, float width, boolean smooth, boolean stem, int color) {
        Vec3d eyes = Eclipse.mc.player.getLookVec();
        double x = interpolate(entity.posX, entity.lastTickPosX) - Eclipse.mc.getRenderManager().viewerPosX;
        double y = interpolate(entity.posY, entity.lastTickPosY) - Eclipse.mc.getRenderManager().viewerPosY;
        double z = interpolate(entity.posZ, entity.lastTickPosZ) - Eclipse.mc.getRenderManager().viewerPosZ;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GL11.glLineWidth(width);

        if (smooth) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        }

        GlStateManager.disableDepth();
        GL11.glEnable(GL32.GL_DEPTH_CLAMP);

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();

        float alpha = (color >> 24 & 0xff) / 255f;
        float red = (color >> 16 & 0xff) / 255f;
        float green = (color >> 8 & 0xff) / 255f;
        float blue = (color & 0xff) / 255f;

        GL11.glLoadIdentity();
        ((IEntityRenderer) Eclipse.mc.entityRenderer).doOrientCamera(Eclipse.mc.getRenderPartialTicks());

        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(eyes.x, eyes.y + Eclipse.mc.player.getEyeHeight(), eyes.z).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x, y, z).color(red, green, blue, alpha).endVertex();

        if (stem) {
            bufferbuilder.pos(x, y, z).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x, y + entity.height, z).color(red, green, blue, alpha).endVertex();
        }

        tessellator.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRect(double x, double y, double width, double height, int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        float alpha = (color >> 24 & 0xff) / 255f;
        float red = (color >> 16 & 0xff) / 255f;
        float green = (color >> 8 & 0xff) / 255f;
        float blue = (color & 0xff) / 255f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x, y + height, 0.0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + width, y + height, 0.0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + width, y, 0.0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void drawBox(AxisAlignedBB box, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.glLineWidth(1f);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        float alpha = (color >> 24 & 0xff) / 255f;
        float red = (color >> 16 & 0xff) / 255f;
        float green = (color >> 8 & 0xff) / 255f;
        float blue = (color & 0xff) / 255f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        builder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }

    public static Vec3d getCameraPos() {
        RenderManager manager = Eclipse.mc.getRenderManager();
        return new Vec3d(-manager.viewerPosX, -manager.viewerPosY, -manager.viewerPosZ);
    }

    public static double interpolate(double start, double end) {
        return end + (start - end) * Eclipse.mc.getRenderPartialTicks();
    }

    public static Vec3d interpolateVec(Vec3d input) {
        return interpolateVec(input, input);
    }

    public static Vec3d interpolateVec(Vec3d start, Vec3d end) {
        return new Vec3d(interpolate(start.x, end.x), interpolate(start.y, end.y), interpolate(start.z, end.z));
    }

    public static Vec3d interpolateEntity(Entity entity) {
        return interpolateVec(new Vec3d(entity.posX, entity.posY, entity.posZ), new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ));
    }

    public static Vec3d transformScreen(Vec3d input) {
        return input.subtract(getCameraPos());
    }
}
