package xyz.aesthetical.astra.events.input;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MouseEvent extends Event {
    private Button button;

    public MouseEvent(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public enum Button {
        UNKNOWN(-1),
        RIGHT(0),
        LEFT(1),
        MIDDLE(2);

        public int id;
        Button(int id) {
            this.id = id;
        }

        public static Button getById(int id) {
            for (Button button : Button.class.getEnumConstants()) {
                if (button.id == id) {
                    return button;
                }
            }

            return Button.UNKNOWN;
        }
    }
}
