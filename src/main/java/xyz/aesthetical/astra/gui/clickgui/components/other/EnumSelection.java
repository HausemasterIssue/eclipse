package xyz.aesthetical.astra.gui.clickgui.components.other;

import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.gui.clickgui.components.Component;
import xyz.aesthetical.astra.util.EnumConverter;

public class EnumSelection extends Component {
    private final Setting<Enum> setting;
    // private boolean opened = false;

    // private final EnumConverter converter;

    public EnumSelection(Setting<Enum> setting) {
        super(setting.getName() + ": " + setting.getValue(), 0.0, 12.0);

        this.setting = setting;
        // this.converter = new EnumConverter(setting.getValue().getClass());
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        Astra.textManager.draw(setting.getName() + ": " + EnumConverter.getActualName(setting.getValue()), (float) (x) + 2.3f, toCenterHeight((float) y, (float) height), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (isMouseInBounds(mouseX, mouseY) && button == 0) {
            setting.setValue(EnumConverter.increaseEnum(setting.getValue()));
        } else if (button == 1) {
            // @todo, combo box typa shit ig lol
        }
    }

//    @Override
//    public double getHeight() {
//        return setting.getValue().getClass().getEnumConstants().length * 13.5;
//    }
}
