package xyz.aesthetical.eclipse.gui.hudeditor.elements;

import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.gui.hudeditor.HudElement;

import java.awt.*;

public class Watermark extends HudElement {
    public final Setting<String> text = register(new Setting<>("Text", "Eclipse v" + Eclipse.MOD_VERSION).setDescription("What the watermark should show up as"));
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
        Eclipse.textManager.draw(text.getValue(), (float) x, (float) y, textColor.getValue().getRGB());
    }
}
