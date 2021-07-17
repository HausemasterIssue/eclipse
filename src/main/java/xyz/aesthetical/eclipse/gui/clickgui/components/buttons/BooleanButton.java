package xyz.aesthetical.eclipse.gui.clickgui.components.buttons;

import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.util.ColorUtil;
import xyz.aesthetical.eclipse.util.RenderUtils;

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
            RenderUtils.drawRect(x, y, width, height, ColorUtil.toRGBA(237, 47, 47, 255));
        }

        Eclipse.textManager.draw(setting.getName(), (float) (x) + 2.3f, toCenterHeight((float) y, (float) height), -1);
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
}
