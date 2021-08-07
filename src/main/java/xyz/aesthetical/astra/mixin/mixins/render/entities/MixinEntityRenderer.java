package xyz.aesthetical.astra.mixin.mixins.render.entities;

import com.google.common.base.Predicate;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.astra.features.modules.exploits.GhostHand;
import xyz.aesthetical.astra.features.modules.render.NoRender;
import xyz.aesthetical.astra.features.modules.render.ViewClip;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements IResourceManagerReloadListener {
    @Shadow
    private ItemStack itemActivationItem;

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void onHurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (NoRender.instance.isToggled() && NoRender.instance.hurtCamera.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderItemActivation", at = @At("HEAD"), cancellable = true)
    private void renderItemActivation(int p_190563_1_, int p_190563_2_, float p_190563_3_, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.totemUse.getValue() && itemActivationItem != null && !itemActivationItem.isEmpty() && itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private void setupFog(int startCoords, float partialTicks, CallbackInfo info) {
        if (NoRender.instance.isToggled() && NoRender.instance.fog.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "addRainParticles", at = @At("HEAD"), cancellable = true)
    public void onAddRainParticles(CallbackInfo ci) {
        if (NoRender.instance.isToggled() && NoRender.instance.weather.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderRainSnow", at = @At("HEAD"), cancellable = true)
    public void onRenderRainSnow(CallbackInfo ci) {
        if (NoRender.instance.isToggled() && NoRender.instance.weather.getValue()) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), ordinal = 3, require = 1)
    private double preOrientCamera(double range) {
        return ViewClip.instance.isToggled() ? ViewClip.instance.range.getValue().floatValue() : range;
    }

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), ordinal = 7, require = 1)
    private double postOrientCamera(double range) {
        return ViewClip.instance.isToggled() ? ViewClip.instance.range.getValue().floatValue() : range;
    }

    // https://github.com/Elementars/Xulu-v1.5.2/blob/d2d1e1c5679f0815fc921377be6bba4699c4b566/xuluv1.5.2/com/elementars/eclient/mixin/mixins/MixinEntityRenderer.java#L49#L55
    @Redirect(method = "getMouseOver", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List hookMouseOver(WorldClient client, Entity entity, AxisAlignedBB box, Predicate predicate) {
        if (GhostHand.instance.isToggled() && GhostHand.instance.shouldBlock()) {
            return new ArrayList<>();
        }

        return client.getEntitiesInAABBexcluding(entity, box, predicate);
    }
}
