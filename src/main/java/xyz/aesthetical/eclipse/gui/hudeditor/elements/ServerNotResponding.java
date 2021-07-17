package xyz.aesthetical.eclipse.gui.hudeditor.elements;

import net.minecraft.client.gui.ScaledResolution;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.Setting;

import java.awt.*;

public class ServerNotResponding extends HudElement {
    public final Setting<Color> textColor = register(new Setting<>("Text Color", new Color(71, 71, 71)).setDescription("The color to use in the text"));

    public ServerNotResponding() {
        super("ServerNotResponding");
    }

    @Override
    public void init() {
        ScaledResolution resolution = new ScaledResolution(Eclipse.mc);

        x = resolution.getScaledWidth() / 2.0f;
        y = resolution.getScaledWidth() / 2.0f;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        if (Eclipse.serverManager.isServerUnresponsive()) {
            float timeNotResponding = Eclipse.serverManager.getServerRespondingTime() / 1000.0f;
            String display = "Server has not been responding for " + Eclipse.serverManager.format(timeNotResponding) + "s";

            width = Eclipse.textManager.getWidth(display);
            height = Eclipse.textManager.getHeight() + 8;

            Eclipse.textManager.draw(display, (float) x, (float) y, textColor.getValue().getRGB());
        }
    }
}
