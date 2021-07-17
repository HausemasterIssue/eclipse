package xyz.aesthetical.eclipse.mixin.mixins.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.entity.MoveEvent;
import xyz.aesthetical.eclipse.events.entity.MoveUpdateEvent;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo info) {
        if (message.startsWith(Eclipse.commandManager.getPrefix().toLowerCase())) {
            Eclipse.commandManager.execute(message);
            info.cancel();
        }
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void onMove(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
        MoveEvent event = new MoveEvent(type, x, y, z);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            super.move(type, x, y, z);
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    private void preOnUpdateWalkingPlayer(CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new MoveUpdateEvent(MoveUpdateEvent.Stage.PRE));
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    private void postOnUpdateWalkingPlayer(CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new MoveUpdateEvent(MoveUpdateEvent.Stage.PRE));
    }
}
