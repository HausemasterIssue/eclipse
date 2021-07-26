package xyz.aesthetical.astra.features.modules.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "NoSlow", description = "Stops actions from slowing you down")
@Module.Info(category = Module.Category.MOVEMENT)
public class NoSlow extends Module {
    public static NoSlow instance;

    public final Setting<Boolean> cobwebs = register(new Setting<>("Cobwebs", false).setDescription("If to go normal speed in cobwebs"));
    public final Setting<Boolean> soulsand = register(new Setting<>("Soulsand", false).setDescription("If to go normal speed in soulsand"));
    public final Setting<Boolean> slime = register(new Setting<>("Slime Blocks", false).setDescription("If to go normal speed on slime blocks"));

    public NoSlow() {
        instance = this;
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        // mojang multiplies the orginal values by 0.2, so we revert that
        if (Module.fullNullCheck() && Astra.mc.player.isHandActive()) {
            event.getMovementInput().moveStrafe *= 5.0f;
            event.getMovementInput().moveForward *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof CPacketPlayer && Astra.mc.player.isHandActive()) {
            Astra.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
}
