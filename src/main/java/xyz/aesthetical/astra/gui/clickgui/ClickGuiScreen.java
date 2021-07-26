package xyz.aesthetical.astra.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.modules.client.ClickGUI;
import xyz.aesthetical.astra.gui.clickgui.components.Container;
import xyz.aesthetical.astra.gui.clickgui.components.buttons.ModuleButton;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {
    private static ClickGuiScreen instance;

    private final ArrayList<Container> containers = new ArrayList<>();

    private ClickGuiScreen() {
        double x = 4.0, y = 4.0;

        for (Pair<String, List<Module>> pair : Astra.moduleManager.getModulesSortedByCategory()) {
            containers.add(new Container(pair.getKey(), x, y) {
                @Override
                protected void init() {
                    pair.getValue().forEach(module ->  components.add(new ModuleButton(module)));
                }
            });

            x += 84.0;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int scroll = Mouse.getDWheel();
        if (scroll < 0) {
            containers.forEach(container -> container.setY(container.getY() - 10.0));
        } else if (scroll > 0) {
            containers.forEach(container -> container.setY(container.getY() + 10.0));
        }

        drawDefaultBackground();
        containers.forEach(container -> container.draw(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        containers.forEach(container -> container.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        containers.forEach(container -> container.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        containers.forEach(container -> container.keyTyped(typedChar, keyCode));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return ClickGUI.instance.pause.getValue();
    }

    public static ClickGuiScreen getInstance() {
        if (instance == null) {
            return instance = new ClickGuiScreen();
        }

        return instance;
    }
}
