package xyz.aesthetical.astra.gui.clickgui.components.buttons;

import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.util.ColorUtil;
import xyz.aesthetical.astra.util.RenderUtils;

public class BooleanButton extends Button {
    private final Setting<Boolean> setting;

    public BooleanButton(Setting<Boolean> setting) {
        super(setting.getName());

        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        clicked = setting.getValue();

        if (clicked) {
            RenderUtils.drawRect(x, y, width, height, ColorUtil.toRGBA(2, 112, 222, 255));
        }

        Astra.textManager.draw(setting.getName(), (float) (x) + 2.3f, toCenterHeight((float) y, (float) height), -1);
    }

    @Override
    public void onClicked(int button) {
        super.onClicked(button);

        if (button == 0) {
            toggle();
        }
    }

    @Override
    public void toggle() {
        super.toggle();
        setting.setValue(!setting.getValue());
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }
}
