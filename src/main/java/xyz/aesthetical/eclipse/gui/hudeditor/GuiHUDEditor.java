package xyz.aesthetical.eclipse.gui.hudeditor;

import net.minecraft.client.gui.GuiScreen;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.gui.clickgui.ClickGuiScreen;
import xyz.aesthetical.eclipse.gui.hudeditor.components.Container;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.Arraylist;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.HudElement;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.ServerNotResponding;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.Welcomer;
import xyz.aesthetical.eclipse.util.ColorUtil;
import xyz.aesthetical.eclipse.util.RenderUtils;

import java.io.IOException;
import java.util.ArrayList;

public class GuiHUDEditor extends GuiScreen {
    public static GuiHUDEditor instance;

    private final ArrayList<HudElement> elements = new ArrayList<>();
    private HudElement dragging = null;
    private double x2, y2;

    private final Container container;

    private GuiHUDEditor() {
        elements.add(new Arraylist());
        elements.add(new ServerNotResponding());
        elements.add(new Welcomer());

        elements.forEach(HudElement::init);

        this.container = new Container(elements);

        instance = this;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (dragging != null) {
            dragging.setX(x2 + mouseX);
            dragging.setY(y2 + mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        drawDefaultBackground();

        for (HudElement element : elements) {
            if (!element.isEnabled()) {
                continue;
            }

            RenderUtils.drawRect(element.getX(), element.getY(), element.getWidth(), element.getHeight(), ColorUtil.toRGBA(184, 184, 184, 55));
            element.draw(mouseX, mouseY, partialTicks);
        }

        container.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (HudElement element : elements) {
            if (element.isMouseInBounds(mouseX, mouseY)) {
                if (mouseButton == 0) {
                    dragging = element;
                    x2 = element.getX() - mouseX;
                    y2 = element.getY() - mouseY;
                }
            }
        }

        container.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        if (state == 0 && dragging != null) {
            dragging = null;
        }

        container.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        container.keyTyped(typedChar, keyCode);
    }

    public ArrayList<HudElement> getElements() {
        return elements;
    }

    public static GuiHUDEditor getInstance() {
        if (instance == null) {
            return instance = new GuiHUDEditor();
        }

        return instance;
    }
}
