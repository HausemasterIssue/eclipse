package xyz.aesthetical.astra.mixin.mixins.input;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.astra.events.input.KeyEvent;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding implements Comparable<KeyBinding> {
    @Shadow
    private boolean pressed;

    @Inject(method = "isKeyDown", at = @At("RETURN"), cancellable = true)
    public void hookIsKeyDown(CallbackInfoReturnable<Boolean> info) {
        KeyEvent event = new KeyEvent(info.getReturnValue(), pressed);
        MinecraftForge.EVENT_BUS.post(event);
        info.setReturnValue(event.isInfo());
    }
}
