package xyz.aesthetical.astra.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.astra.events.client.ShutdownEvent;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow
    public GuiScreen currentScreen;

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void onShutdown(CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new ShutdownEvent());
    }
}
