package xyz.aesthetical.eclipse.gui.hudeditor.elements;

import net.minecraft.client.gui.ScaledResolution;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.gui.hudeditor.HudElement;

public class Arraylist extends HudElement {
    public Arraylist() {
        super("Arraylist");
    }

    @Override
    public void init() {
        ScaledResolution resolution = new ScaledResolution(Eclipse.mc);

        x = resolution.getScaledWidth() - 1.0;
        y = 2.0;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {

    }
}
