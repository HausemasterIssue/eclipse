package xyz.aesthetical.astra.gui.hudeditor.components.buttons;

import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.gui.clickgui.components.Component;
import xyz.aesthetical.astra.gui.clickgui.components.buttons.Button;
import xyz.aesthetical.astra.gui.hudeditor.HudElement;
import xyz.aesthetical.astra.util.ColorUtils;
import xyz.aesthetical.astra.util.RenderUtils;

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
            // 237, 47, 47
            RenderUtils.drawRect(x, y, width, height, ColorUtils.toRGBA(2, 112, 222, 255));
        }

        Astra.textManager.draw(element.getName(), (float) (x + 2.3), toCenterHeight((float) y, (float) height), -1);

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
