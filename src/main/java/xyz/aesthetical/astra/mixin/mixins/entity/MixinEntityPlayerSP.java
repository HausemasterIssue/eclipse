package xyz.aesthetical.astra.mixin.mixins.entity;

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
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.MoveEvent;
import xyz.aesthetical.astra.events.entity.MoveUpdateEvent;
import xyz.aesthetical.astra.mixin.accessors.IEntityPlayerSP;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer implements IEntityPlayerSP {
    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo info) {
        if (message.startsWith(Astra.commandManager.getPrefix().toLowerCase())) {
            Astra.commandManager.execute(message);
            info.cancel();
        }
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void onMove(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
        MoveEvent event = new MoveEvent(type, x, y, z);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            super.move(type, event.getX(), event.getY(), event.getZ());
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    private void preOnUpdateWalkingPlayer(CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new MoveUpdateEvent(MoveUpdateEvent.Stage.PRE));
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    private void postOnUpdateWalkingPlayer(CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new MoveUpdateEvent(MoveUpdateEvent.Stage.POST));
    }

    @Override
    public void setInPortal(boolean inPortal) {
        this.inPortal = inPortal;
    }
}
