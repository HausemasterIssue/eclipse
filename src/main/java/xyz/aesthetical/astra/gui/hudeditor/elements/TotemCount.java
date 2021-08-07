package xyz.aesthetical.astra.gui.hudeditor.elements;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.gui.hudeditor.HudElement;

public class TotemCount extends HudElement {
    private static final ItemStack DUMMY_TOTEM_STACK = new ItemStack(Items.TOTEM_OF_UNDYING);

    public TotemCount() {
        super("TotemCount");
    }

    @Override
    public void init() {
        x = 2.0;
        y = 2.0; // wtf do i make this the default of?
        width = 20;  // ?
        height = 20; // ?
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        RenderItem itemRenderer = Astra.mc.getRenderItem();

        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();

        itemRenderer.renderItemAndEffectIntoGUI(Astra.mc.player, DUMMY_TOTEM_STACK, (int) x, (int) y);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();

        Astra.textManager.draw(String.valueOf(getTotemCount()), (float) (x + width) - 12.0f, (float) (y + height) - 8.0f, -1);
    }

    private int getTotemCount() {
        int count = 0;

        if (Astra.mc.player.getHeldItemMainhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++count;
        }

        for (ItemStack stack : Astra.mc.player.inventory.mainInventory) {
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                ++count;
            }
        }

        return count;
    }
}
