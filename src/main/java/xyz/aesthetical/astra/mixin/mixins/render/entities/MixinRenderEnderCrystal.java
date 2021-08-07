package xyz.aesthetical.astra.mixin.mixins.render.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.astra.features.modules.render.Chams;

@Mixin(RenderEnderCrystal.class)
public abstract class MixinRenderEnderCrystal extends Render<EntityEnderCrystal> {
    protected MixinRenderEnderCrystal(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "doRender", at = @At("HEAD"))
    public void onPreDoRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.instance.isToggled() && Chams.instance.crystals.getValue()) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
        }
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    public void onPostDoRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.instance.isToggled() && Chams.instance.crystals.getValue()) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
}
