package xyz.aesthetical.eclipse.events;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.entity.TotemPopEvent;
import xyz.aesthetical.eclipse.events.input.MouseEvent;
import xyz.aesthetical.eclipse.events.network.PacketEvent;
import xyz.aesthetical.eclipse.events.render.RenderEvent;
import xyz.aesthetical.eclipse.managers.modules.Module;

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
                if (packet.getOpCode() == 35 && packet.getEntity(Eclipse.mc.world) instanceof EntityPlayer) {
                    MinecraftForge.EVENT_BUS.post(new TotemPopEvent((EntityPlayer) packet.getEntity(Eclipse.mc.world)));
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        MinecraftForge.EVENT_BUS.post(new RenderEvent(event.getPartialTicks()));

        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }
}
