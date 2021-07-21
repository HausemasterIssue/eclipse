package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.mixin.accessors.IEntityPlayerSP;

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
        if (Module.fullNullCheck() && gui.getValue() && event.getEntityLiving() == Eclipse.mc.player) {
            ((IEntityPlayerSP) Eclipse.mc.player).setInPortal(false);
        }
    }
}
