package xyz.aesthetical.astra.managers.macros;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Module;

public class Macro {
    private String name;
    private String command;
    private int bind;

    public Macro(String name, String command, int bind) {
        this.name = name;
        this.command = command;
        this.bind = bind;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int key = Keyboard.getEventKey();
        if (Module.fullNullCheck() && !Keyboard.getEventKeyState() && key == bind) {
            Astra.mc.player.sendChatMessage(getCommand());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public int getBind() {
        return bind;
    }
}
