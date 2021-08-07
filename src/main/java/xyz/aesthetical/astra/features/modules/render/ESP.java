package xyz.aesthetical.astra.features.modules.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.render.RenderModelEvent;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.EnumConverter;

@Module.Mod(name = "ESP", description = "wip")
@Module.Info(category = Module.Category.RENDER)
public class ESP extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.OUTLINE).setDescription("How to render the ESP"));
    public final NumberSetting width = register(new NumberSetting("Width", 1.0f).setMin(0.1f).setMax(5.0f).setDescription("The width of the line").setVisibility((e) -> mode.getValue() == Mode.OUTLINE || mode.getValue() == Mode.FILLED_OUTLINE || mode.getValue() == Mode.SHADER));

    @Override
    public String getDisplay() {
        return EnumConverter.getActualName(mode.getValue());
    }

    @SubscribeEvent
    public void onRenderModel(RenderModelEvent event) {
        if (Module.fullNullCheck() && mode.getValue() == Mode.OUTLINE) {
            checkSetupFBO();

            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(width.getValue().floatValue());
            GL11.glEnable(2848);
            GL11.glEnable(2960);
            GL11.glClear(1024);
            GL11.glClearStencil(15);
            GL11.glStencilFunc(512, 1, 15);
            GL11.glStencilOp(7681, 7681, 7681);
            GL11.glPolygonMode(1032, 6913);

            event.getModelBase().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

            GL11.glStencilFunc(512, 0, 15);
            GL11.glStencilOp(7681, 7681, 7681);
            GL11.glPolygonMode(1032, 6914);

            event.getModelBase().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

            GL11.glStencilFunc(514, 1, 15);
            GL11.glStencilOp(7680, 7680, 7680);
            GL11.glPolygonMode(1032, 6913);

            GL11.glColor3d(1.0f, 1.0f, 1.0f);

            GL11.glDepthMask(false);
            GL11.glDisable(2929);
            GL11.glEnable(10754);
            GL11.glPolygonOffset(1.0f, -2000000.0f);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);

            event.getModelBase().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

            GL11.glPolygonOffset(1.0f, 2000000.0f);
            GL11.glDisable(10754);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(2960);
            GL11.glDisable(2848);
            GL11.glHint(3154, 4352);
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }

    private void checkSetupFBO() {
        Framebuffer fbo = Astra.mc.getFramebuffer();
        if (fbo.depthBuffer > -1) {
            setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }

    private void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Astra.mc.displayWidth, Astra.mc.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
    }

    public enum Mode {
        OUTLINE,
        BOX,
        FILLED,
        FILLED_OUTLINE,
        SHADER,
        GLOW
    }
}
