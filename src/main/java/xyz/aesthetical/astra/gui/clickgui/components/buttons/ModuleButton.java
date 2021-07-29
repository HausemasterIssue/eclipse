package xyz.aesthetical.astra.gui.clickgui.components.buttons;

import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.gui.clickgui.components.Component;
import xyz.aesthetical.astra.gui.clickgui.components.other.EnumSelection;
import xyz.aesthetical.astra.gui.clickgui.components.other.Slider;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.ColorUtil;
import xyz.aesthetical.astra.util.RenderUtils;

import java.util.ArrayList;

public class ModuleButton extends Button {
    private final Module module;
    private final ArrayList<Component> components = new ArrayList<>();
    private boolean expanded = false;

    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;

        this.init();
    }

    private void init() {
        components.add(new BindButton(module));

        // add settings
        for (Setting setting : module.getSettings()) {
            if (setting.getValue() instanceof Boolean) {
                components.add(new BooleanButton(setting));
            } else if (setting.getValue() instanceof Number) {
                components.add(new Slider((NumberSetting) setting));
            } else if (setting.getValue() instanceof Enum) {
                components.add(new EnumSelection(setting));
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        clicked = module.isToggled();

        if (clicked) {
            RenderUtils.drawRect(x, y, width, height, ColorUtil.toRGBA(2, 112, 222, 255));
        }

        Astra.textManager.draw(module.getName(), (float) (x + 2.3), toCenterHeight((float) y, (float) height), -1);

        if (expanded) {
            double h = 1.0;
            for (Component component : components) {
                if (!component.isVisible()) {
                    continue;
                }

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
            components.stream().filter(Component::isVisible).forEach(component -> component.mouseClicked(mouseX, mouseY, button));
        }
    }

    @Override
    public void onClicked(int button) {
        if (button == 0) {
            module.toggle();
        } else if (button == 1) {
            expanded = !expanded;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (expanded) {
            components.stream().filter(Component::isVisible).forEach(component -> component.mouseReleased(mouseX, mouseY, button));
        }
    }

    @Override
    public void keyTyped(char character, int code) {
        super.keyTyped(character, code);
        if (expanded) {
            components.stream().filter(Component::isVisible).forEach(component -> component.keyTyped(character, code));
        }
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public double getHeight() {
        double h = 12.0;

        if (expanded) {
            for (Component item : components) {
                if (!item.isVisible()) {
                    continue;
                }

                h += item.getHeight() + 1.0;
            }
        }

        return h + 1.5;
    }
}
