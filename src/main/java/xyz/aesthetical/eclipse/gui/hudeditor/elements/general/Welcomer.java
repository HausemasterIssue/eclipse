package xyz.aesthetical.eclipse.gui.hudeditor.elements.general;

import net.minecraft.client.gui.ScaledResolution;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.Category;
import xyz.aesthetical.eclipse.gui.hudeditor.elements.HudElement;

public class Welcomer extends HudElement {
    public final Setting<String> text = register(new Setting<>("Text", "Hello <player> :^)").setDescription("The text to welcome you with"));

    public Welcomer(String name, Category category) {
        super(name, category);
    }

    @Override
    public void init() {
        ScaledResolution resolution = new ScaledResolution(Eclipse.mc);

        x = resolution.getScaledWidth() / 2.0f;
        y = 10.0;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        Eclipse.textManager.draw(replaceText(text.getValue()), (float) x, (float) y, -1);
    }

    private String replaceText(String text) {
        return text
                .replaceAll("<player>", Eclipse.mc.player.getName());
    }
}
