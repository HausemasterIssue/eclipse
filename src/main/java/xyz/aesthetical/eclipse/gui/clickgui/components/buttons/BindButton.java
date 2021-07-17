package xyz.aesthetical.eclipse.gui.clickgui.components.buttons;

import org.lwjgl.input.Keyboard;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.modules.Module;

public class BindButton extends Button {
    private final Module module;
    private boolean listening = false;

    public BindButton(Module module) {
        super("Bind: ??");
        this.module = module;
    }

    @Override
    public void draw(int mouseX, int mouseY, float tickDelta) {
        String text = listening ? "Listening..." : "Bind: " + getKey(module.getBind());
        Eclipse.textManager.draw(text, (float) (x + 2.3), toCenterHeight((float) y, (float) height), -1);
    }

    @Override
    public void onClicked(int button) {
        if (button == 0) {
            toggle();
        }
    }

    @Override
    public void keyTyped(char character, int code) {
        if (listening) {
            listening = false;

            if (code == Keyboard.KEY_ESCAPE) {
                return;
            } else if (code == Keyboard.KEY_DELETE || code == Keyboard.KEY_BACK) {
                module.setBind(0);
                return;
            }

            module.setBind(code);
        }
    }

    @Override
    public void toggle() {
        listening = !listening;
    }

    public static String getKey(int code) {
        String key = Keyboard.getKeyName(code);
        if (key == null || code == 0) {
            return "None";
        }

        return key;
    }
}
