package xyz.aesthetical.astra.events;

import joptsimple.internal.Strings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.ConnectionEvent;
import xyz.aesthetical.astra.events.entity.TotemPopEvent;
import xyz.aesthetical.astra.events.input.MouseEvent;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.events.render.RenderEvent;
import xyz.aesthetical.astra.managers.modules.Module;

public class ForgeEventProcessor {
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!Mouse.getEventButtonState()) {
            MinecraftForge.EVENT_BUS.post(new MouseEvent(MouseEvent.Button.getById(Mouse.getEventButton())));
        }
    }

    @SubscribeEvent
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (Module.fullNullCheck()) {
            if (event.getPacket() instanceof SPacketEntityStatus) {
                SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
                if (packet.getOpCode() == 35 && packet.getEntity(Astra.mc.world) instanceof EntityPlayer) {
                    MinecraftForge.EVENT_BUS.post(new TotemPopEvent((EntityPlayer) packet.getEntity(Astra.mc.world)));
                }
            } else if (event.getPacket() instanceof SPacketPlayerListItem) {
                SPacketPlayerListItem packet = (SPacketPlayerListItem) event.getPacket();
                if (packet.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER || packet.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                    for (SPacketPlayerListItem.AddPlayerData data : packet.getEntries()) {
                        if (Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() == null) {
                            continue;
                        }

                        MinecraftForge.EVENT_BUS.post(new ConnectionEvent(
                            packet.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER ? ConnectionEvent.Action.CONNECT : ConnectionEvent.Action.DISCONNECT,
                            Astra.mc.world.getPlayerEntityByUUID(data.getProfile().getId()),
                            data.getProfile().getId(),
                            data.getProfile().getName()
                        ));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        RenderEvent evt = new RenderEvent(event.getPartialTicks());
        MinecraftForge.EVENT_BUS.post(evt);

        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }
}
