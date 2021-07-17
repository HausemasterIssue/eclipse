package xyz.aesthetical.eclipse.gui.hudeditor.components.buttons;

import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.gui.clickgui.components.Component;
import xyz.aesthetical.eclipse.gui.clickgui.components.buttons.Button;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.HudElement;
import xyz.aesthetical.eclipse.util.ColorUtil;
import xyz.aesthetical.eclipse.util.RenderUtils;

import java.util.ArrayList;

public class ElementButton extends Button {
    private final HudElement element;

    private final ArrayList<Component> components = new ArrayList<>();
    private boolean expanded = false;

    public ElementButton(HudElement element) {
        super(element.getName());
        this.element = element;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        clicked = element.isEnabled();

        if (clicked) {
            RenderUtils.drawRect(x, y, width, height, ColorUtil.toRGBA(237, 47, 47, 255));
        }

        Eclipse.textManager.draw(element.getName(), (float) (x + 2.3), toCenterHeight((float) y, (float) height), -1);

        if (expanded) {
            double h = 1.0;
            for (Component component : components) {
                component.setX(x + 1);
                component.setY(y + (h += component.getHeight() + 1));
                component.setWidth(width - 4.0);
                component.setHeight(11.0);

                component.draw(mouseX, mouseY, tickDelta);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (expanded) {
            components.forEach(component -> component.mouseClicked(mouseX, mouseY, button));
        }
    }

    @Override
    public void onClicked(int button) {
        if (button == 0) {
            element.toggle();
        } else if (button == 1) {
            expanded = !expanded;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (expanded) {
            components.forEach(component -> component.mouseReleased(mouseX, mouseY, button));
        }
    }

    @Override
    public void keyTyped(char character, int code) {
        super.keyTyped(character, code);
        if (expanded) {
            components.forEach(component -> component.keyTyped(character, code));
        }
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    @Override
    public double getHeight() {
        double h = 12.0;

        if (expanded) {
            for (Component item : components) {
                h += item.getHeight() + 1.0;
            }
        }

        return h + 1.5;
    }
}
