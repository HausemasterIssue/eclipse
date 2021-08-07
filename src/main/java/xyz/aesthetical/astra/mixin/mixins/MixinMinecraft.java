package xyz.aesthetical.astra.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.astra.events.client.ScreenChangeEvent;
import xyz.aesthetical.astra.events.client.ShutdownEvent;
import xyz.aesthetical.astra.features.modules.miscellaneous.MultiTask;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow
    public GuiScreen currentScreen;

    @Inject(method = "displayGuiScreen", at = @At("HEAD"), cancellable = true)
    public void onDisplayGuiScreen(GuiScreen guiScreenIn, CallbackInfo info) {
        ScreenChangeEvent event = new ScreenChangeEvent(currentScreen, guiScreenIn);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Redirect(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    public boolean hookSendClickBlockToController(EntityPlayerSP player) {
        return !MultiTask.instance.isToggled() && player.isHandActive();
    }

    @Redirect(method = "rightClickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z", ordinal = 0), require = 1)
    public boolean hookRightClickMouse(PlayerControllerMP controller) {
        return !MultiTask.instance.isToggled() && controller.getIsHittingBlock();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void onShutdown(CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new ShutdownEvent());
    }
}
