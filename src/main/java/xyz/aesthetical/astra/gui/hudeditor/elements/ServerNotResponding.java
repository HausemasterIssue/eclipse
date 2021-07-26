package xyz.aesthetical.astra.gui.hudeditor.elements;

import net.minecraft.client.gui.ScaledResolution;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.gui.hudeditor.HudElement;

import java.awt.*;

public class ServerNotResponding extends HudElement {
    public final Setting<Color> textColor = register(new Setting<>("Text Color", new Color(71, 71, 71)).setDescription("The color to use in the text"));

    public ServerNotResponding() {
        super("ServerNotResponding");
    }

    @Override
    public void init() {
        ScaledResolution resolution = new ScaledResolution(Astra.mc);

        x = resolution.getScaledWidth() / 2.0f;
        y = resolution.getScaledWidth() / 2.0f;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        if (Astra.serverManager.isServerUnresponsive()) {
            float timeNotResponding = Astra.serverManager.getServerRespondingTime() / 1000.0f;
            String display = "Server has not been responding for " + Astra.serverManager.format(timeNotResponding) + "s";

            width = Astra.textManager.getWidth(display);
            height = Astra.textManager.getHeight() + 8;

            Astra.textManager.draw(display, (float) x, (float) y, textColor.getValue().getRGB());
        }
    }
}
