package xyz.aesthetical.eclipse.events.entity;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MoveUpdateEvent extends Event {
    private final Stage stage;

    public MoveUpdateEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public enum Stage {
        PRE,
        POST
    }
}
