package xyz.aesthetical.eclipse.gui.hudeditor.components;

import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.gui.clickgui.components.Component;
import xyz.aesthetical.eclipse.gui.hudeditor.components.buttons.ElementButton;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.HudElement;
import xyz.aesthetical.eclipse.util.ColorUtil;
import xyz.aesthetical.eclipse.util.RenderUtils;

import java.util.ArrayList;

public class Container extends Component {
    private final ArrayList<HudElement> elements;

    private double x2;
    private double y2;

    private boolean dragging = false;
    private boolean expanded = true;

    private final ArrayList<Component> components = new ArrayList<>();

    public Container(ArrayList<HudElement> elements) {
        super("Elements", 78.0, 15.0);

        this.elements = elements;
        this.init();
    }

    private void init() {
        for (HudElement element : elements) {
            components.add(new ElementButton(element));
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        if (dragging) {
            x = x2 + mouseX;
            y = y2 + mouseY;
        }

        RenderUtils.drawRect(x, y, width, height, ColorUtil.toRGBA(71, 71, 71, 255));
        Eclipse.textManager.draw(title, (float) (x + 2.3), toCenterHeight((float) y, (float) height), -1);

        if (expanded) {
            RenderUtils.drawRect(x, y + height, width, getTotalHeight(), 0x77000000);

            double compY = (y + height) + 1.5;
            for (Component component : components) {
                component.setX(x + 2.0);
                component.setY(compY);
                component.setWidth(width - 4.0);

                component.draw(mouseX, mouseY, tickDelta);

                compY += component.getHeight() + 1.5;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseInBounds(mouseX, mouseY)) {
            switch (button) {
                case 0: {
                    x2 = x - mouseX;
                    y2 = y - mouseY;
                    dragging = true;
                    break;
                }

                case 1: {
                    playClickSound(1.0f);
                    expanded = !expanded;
                    break;
                }
            }
        }

        if (expanded) {
            for (Component component : components) {
                component.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (button == 0) {
            dragging = false;
        }

        if (expanded) {
            for (Component component : components) {
                component.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void keyTyped(char character, int code) {
        if (expanded) {
            for (Component component : components) {
                component.keyTyped(character, code);
            }
        }
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    private double getTotalHeight() {
        double h = 0.0;

        if (expanded) {
            for (Component component : components) {
                h += component.getHeight() + 1.5;
            }
        }

        return h + 1.5;
    }
}
