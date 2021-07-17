package xyz.aesthetical.eclipse.gui.clickgui.components.buttons;

import xyz.aesthetical.eclipse.gui.clickgui.components.Component;

public class Button extends Component {
    protected boolean clicked = false;

    public Button(String title) {
        super(title, 0.0, 12.0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseInBounds(mouseX, mouseY)) {
            playClickSound(1.0f);
            onClicked(button);
        }
    }

    public void onClicked(int button) { }

    public void toggle() {
        clicked = !clicked;
    }
}
