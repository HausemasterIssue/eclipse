package xyz.aesthetical.astra.gui.clickgui.components.other;

import org.lwjgl.input.Mouse;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.gui.clickgui.components.Component;
import xyz.aesthetical.astra.util.ColorUtil;
import xyz.aesthetical.astra.util.RenderUtils;

public class Slider extends Component {
    private final NumberSetting setting;
    private final float difference;

    public Slider(NumberSetting setting) {
        super(setting.getName() + ": " + setting.getValue(), 0.0, 12.0);

        this.setting = setting;
        this.difference = setting.getMax().floatValue() - setting.getMin().floatValue();
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        if (canSetValue(mouseX, mouseY)) {
            setValue(mouseX);
        }

        RenderUtils.drawRect(x, y, setting.getValue().floatValue() <= setting.getMin().floatValue() ? 0.0f : width * partialMultiplier(), height, ColorUtil.toRGBA(2, 112, 222, 255));
        Astra.textManager.draw(setting.getName() + ": " + setting.getValue(), (float) (x) + 2.3f, toCenterHeight((float) y, (float) height), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (canSetValue(mouseX, mouseY)) {
            setValue(mouseX);
        }
    }

    private void setValue(double mouseX) {
        float percent = (float) (((float) mouseX - x) / width);

        if (setting.getValue() instanceof Float) {
            float result = setting.getMin().floatValue() + difference * percent;
            setting.setValue(Math.round(10.0f * result) / 10.0f);
        } else if (setting.getValue() instanceof Double) {
            // @todo
        } else {
            setting.setValue(Math.round(setting.getMin().intValue() + difference * percent));
        }
    }

    private boolean canSetValue(int mouseX, int mouseY) {
        return Mouse.isButtonDown(0) && isMouseInBounds(mouseX, mouseY);
    }

    private float part() {
        return setting.getValue().floatValue() - setting.getMin().floatValue();
    }

    private float partialMultiplier() {
        return part() / difference;
    }
}
