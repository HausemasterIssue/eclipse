package xyz.aesthetical.astra.events.render;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderEvent extends Event {
    private final float tickDelta;

    public RenderEvent(float tickDelta) {
        this.tickDelta = tickDelta; // aka partialTicks
    }

    public float getTickDelta() {
        return tickDelta;
    }
}
