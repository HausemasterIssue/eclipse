package xyz.aesthetical.astra.events.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ScreenChangeEvent extends Event {
    private final GuiScreen previous;
    private final GuiScreen requested;

    public ScreenChangeEvent(GuiScreen previous, GuiScreen requested) {
        this.previous = previous;
        this.requested = requested;
    }

    public GuiScreen getPrevious() {
        return previous;
    }

    public GuiScreen getRequested() {
        return requested;
    }
}
