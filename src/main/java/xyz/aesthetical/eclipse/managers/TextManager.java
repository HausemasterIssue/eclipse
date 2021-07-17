package xyz.aesthetical.eclipse.managers;

import net.minecraft.client.gui.FontRenderer;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.modules.client.CustomFont;
import xyz.aesthetical.eclipse.gui.font.CustomFontRenderer;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TextManager {
    public static CustomFontRenderer customFontRenderer = new CustomFontRenderer(new Font("Arial", Font.PLAIN, 18), true, false);
    public static FontRenderer defaultFontRenderer = Eclipse.mc.fontRenderer;

    public static void setCustomFontRenderer(Font newFont) {
        customFontRenderer = new CustomFontRenderer(newFont, true, false);
    }

    public void draw(String text, float x, float y, int color) {
        if (CustomFont.instance.isToggled()) {
            customFontRenderer.drawString(text, x, y, color);
        } else {
            defaultFontRenderer.drawString(text, (int) x, (int) y, color);
        }
    }

    public void drawCentered(String text, float x, float y, int color) {
        if (CustomFont.instance.isToggled()) {
            customFontRenderer.drawCenteredString(text, x, y, color);
        } else {
            defaultFontRenderer.drawString(text, (int) (x) - defaultFontRenderer.getStringWidth(text) / 2, (int) y, color);
        }
    }

    public void drawWithShadow(String text, float x, float y, int color) {
        if (CustomFont.instance.isToggled()) {
            customFontRenderer.drawStringWithShadow(text, x, y, color);
        } else {
            defaultFontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }

    public int getWidth(String text) {
        if (CustomFont.instance.isToggled()) {
            return customFontRenderer.getStringWidth(text);
        } else {
            defaultFontRenderer.getStringWidth(text);
        }

        return -1;
    }

    public int getHeight() {
        return CustomFont.instance.isToggled() ?
                customFontRenderer.getHeight() :
                defaultFontRenderer.FONT_HEIGHT;
    }

    public List<String> getAvailableFonts() {
        return Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
    }
}
