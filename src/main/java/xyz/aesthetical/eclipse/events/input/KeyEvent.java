package xyz.aesthetical.eclipse.events.input;

import net.minecraftforge.fml.common.eventhandler.Event;

public class KeyEvent extends Event {
    private boolean info;
    private boolean isPressed;

    public KeyEvent(boolean info, boolean pressed) {
        this.info = info;
        this.isPressed = pressed;
    }

    public boolean isInfo() {
        return info;
    }

    public void setInfo(boolean info) {
        this.info = info;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }
}
