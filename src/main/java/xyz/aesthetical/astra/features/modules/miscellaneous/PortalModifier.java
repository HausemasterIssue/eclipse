package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.mixin.accessors.IEntityPlayerSP;

@Module.Mod(name = "PortalModifier", description = "Modifies actions inside of nether portals")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class PortalModifier extends Module {
    public final Setting<Boolean> gui = register(new Setting<>("GUI", false).setDescription("If to allow you to open GUIs in portals"));
    public final Setting<Boolean> godMode = register(new Setting<>("God Mode", false).setDescription("If to make you fucking god in portals"));

    @SubscribeEvent
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (Module.fullNullCheck() && godMode.getValue() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && gui.getValue() && event.getEntityLiving() == Astra.mc.player) {
            ((IEntityPlayerSP) Astra.mc.player).setInPortal(false);
        }
    }
}
