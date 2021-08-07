package xyz.aesthetical.astra.util.blur;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.client.ScreenChangeEvent;
import xyz.aesthetical.astra.features.modules.render.GuiModifier;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.mixin.mixins.render.IShaderGroup;

import java.util.List;

public class BlurHandler {
    private static final ResourceLocation BLUR_RESOURCE = new ResourceLocation("assets/minecraft/shaders/post/fade_in_blur.json");
    private static long start;

    private float getProgress() {
        return Math.min((System.currentTimeMillis() - start) / GuiModifier.instance.fadeTime.getValue().intValue(), 1);
    }

    @SubscribeEvent
    public void onGuiChange(ScreenChangeEvent event) {
        if (Module.fullNullCheck() && !(event.getRequested() instanceof GuiDownloadTerrain) && GuiModifier.instance.isToggled() && GuiModifier.instance.background.getValue() == GuiModifier.Background.BLUR) {
            EntityRenderer renderer = Astra.mc.entityRenderer;
            if (!renderer.isShaderActive()) {
                renderer.loadShader(BLUR_RESOURCE);
                start = System.currentTimeMillis();
            } else if (renderer.isShaderActive()) {
                renderer.stopUseShader();
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Astra.mc.currentScreen != null && GuiModifier.instance.isToggled() && GuiModifier.instance.background.getValue() == GuiModifier.Background.BLUR && Astra.mc.entityRenderer.isShaderActive()) {
            try {
                List<Shader> shaders = ((IShaderGroup) Astra.mc.entityRenderer).getListShaders();
                if (!shaders.isEmpty()) {
                    for (Shader shader : shaders) {
                        ShaderUniform uniform = shader.getShaderManager().getShaderUniform("Progress");
                        if (uniform != null) {
                            uniform.set(getProgress());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
