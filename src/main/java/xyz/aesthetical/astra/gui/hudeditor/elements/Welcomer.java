package xyz.aesthetical.astra.gui.hudeditor.elements;

import net.minecraft.client.gui.ScaledResolution;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.gui.hudeditor.HudElement;

import java.awt.*;

public class Welcomer extends HudElement {
    public final Setting<String> text = register(new Setting<>("Text", "Hello <player> :^)").setDescription("The text to welcome you with"));
    public final Setting<Color> textColor = register(new Setting<>("Text Color", new Color(71, 71, 71)).setDescription("The color to use in the text"));

    public Welcomer() {
        super("Welcomer");
    }

    @Override
    public void init() {
        ScaledResolution resolution = new ScaledResolution(Astra.mc);

        x = resolution.getScaledWidth() / 2.0f;
        y = 10.0;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        String t = replaceText(text.getValue());

        width = Astra.textManager.getWidth(t);
        height = Astra.textManager.getHeight() + 8;

        Astra.textManager.draw(t, (float) x, (float) y, textColor.getValue().getRGB());
    }

    private String replaceText(String text) {
        return text
                .replaceAll("<player>", Astra.mc.player.getName());
    }
}
