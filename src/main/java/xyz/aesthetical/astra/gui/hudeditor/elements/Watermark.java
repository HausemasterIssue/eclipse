package xyz.aesthetical.astra.gui.hudeditor.elements;

import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.gui.hudeditor.HudElement;

import java.awt.*;

public class Watermark extends HudElement {
    public final Setting<String> text = register(new Setting<>("Text", "Eclipse v" + Astra.MOD_VERSION).setDescription("What the watermark should show up as"));
    public final Setting<Color> textColor = register(new Setting<>("Text Color", new Color(255, 255, 255)).setDescription("The color to use in the text"));

    public Watermark() {
        super("Watermark");
    }

    @Override
    public void init() {
        x = 2.0;
        y = 2.0;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        Astra.textManager.draw(text.getValue(), (float) x, (float) y, textColor.getValue().getRGB());
    }
}
