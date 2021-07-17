package xyz.aesthetical.eclipse.features.modules.movement;

import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.events.world.blocks.LiquidCollisionEvent;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.mixin.mixins.network.packets.outbound.ICPacketPlayer;

@Module.Mod(name = "Jesus", description = "Becomes Jesus Christ and walk on liquids")
@Module.Info(category = Module.Category.MOVEMENT)
public class Jesus extends Module {
    private static final AxisAlignedBB JESUS_SOLID_BOX = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.99, 1.0);

    // @todo dolphin and trampoline
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.SOLID).setDescription("How to walk on liquids"));
    public final Setting<Boolean> water = register(new Setting<>("Water", true).setDescription("If to walk on water"));
    public final Setting<Boolean> lava = register(new Setting<>("Lava", true).setDescription("If to walk on lava"));
    public final Setting<Boolean> flowing = register(new Setting<>("Flowing Liquids", true).setDescription("If to use jesus in flowing liquids"));
    public final Setting<Boolean> noVehicle = register(new Setting<>("No Vehicle", true).setDescription("If to cancel vehicle move packets"));
    // @todo sneakSink
    public final Setting<Boolean> sneakSink = register(new Setting<>("Sneak Sink", true).setDescription("If to sink upon holding sneak"));
    public final Setting<Boolean> antiKick = register(new Setting<>("Anti-Kick", true).setDescription("If to attempt to stop getting kicked"));

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() && mode.getValue() == Mode.SOLID) {
            if (event.getPacket() instanceof CPacketPlayer && !isInLiquid()) {
                if (antiKick.getValue() && Eclipse.mc.player.ticksExisted % 2 == 0) {
                    ICPacketPlayer packet = ((ICPacketPlayer) event.getPacket());
                    packet.setY(packet.getY() + 0.02);
                }
            } else if (noVehicle.getValue() && event.getPacket() instanceof CPacketVehicleMove) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onLiquidCollision(LiquidCollisionEvent event) {
        if (Module.fullNullCheck() && mode.getValue() == Mode.SOLID && event.getEntity() == Eclipse.mc.player) {
            if ((event.getBlock() == Blocks.WATER || event.getBlock() == Blocks.FLOWING_WATER) && !water.getValue()) {
                return;
            }

            if ((event.getBlock() == Blocks.LAVA || event.getBlock() == Blocks.FLOWING_LAVA) && !lava.getValue()) {
                return;
            }

            if ((event.getBlock() == Blocks.FLOWING_WATER || event.getBlock() == Blocks.FLOWING_LAVA) && !flowing.getValue()) {
                return;
            }

            AxisAlignedBB box = JESUS_SOLID_BOX.offset(event.getPos());
            if (event.getBox().intersects(box)) {
                event.addBox(box);
            }

            event.setCanceled(true);
        }
    }

    private boolean isInLiquid() {
        for (double x = 0.0; x < Math.floor(Eclipse.mc.player.posX); ++x) {
            for (double z = 0; z < Math.floor(Eclipse.mc.player.posZ); ++z) {
                if (Eclipse.mc.world.getBlockState(new BlockPos(x, Eclipse.mc.player.posY, z)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }

        return false;
    }


    public enum Mode {
        SOLID,
        DOLPHIN,
        TRAMPOLINE
    }
}
