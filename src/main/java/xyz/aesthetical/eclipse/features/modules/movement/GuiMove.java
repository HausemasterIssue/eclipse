package xyz.aesthetical.eclipse.features.modules.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.input.KeyEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "GuiMove", description = "Allows you to move around in GUIs")
@Module.Info(category = Module.Category.MOVEMENT)
public class GuiMove extends Module {
    public final Setting<Boolean> sprint = register(new Setting<>("Sprint", true).setDescription("If to be able to sprint in a gui"));
    public final Setting<Boolean> jump = register(new Setting<>("Jump", true).setDescription("If to be able to jump in a gui"));
    public final Setting<Boolean> arrowLook = register(new Setting<>("Arrow Look", true).setDescription("If to be able to look around with the arrow keys"));
    public final NumberSetting sensitivity = register(new NumberSetting("Arrow Sensitivity", 0.3f).setMin(0.1f).setMax(2.0f).setDescription("How sensitive the arrow keys will be").setVisibility((m) -> arrowLook.getValue()));

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player && isValidGUI()) {
            GameSettings settings = Eclipse.mc.gameSettings;
            KeyBinding[] keys = new KeyBinding[] {
                    settings.keyBindForward,
                    settings.keyBindBack,
                    settings.keyBindLeft,
                    settings.keyBindRight
            };

            for (KeyBinding key : keys) {
                KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown(key.getKeyCode()));
            }

            if (sprint.getValue()) {
                KeyBinding.setKeyBindState(settings.keyBindSprint.getKeyCode(), Keyboard.isKeyDown(settings.keyBindSprint.getKeyCode()));
            }

            if (jump.getValue()) {
                KeyBinding.setKeyBindState(settings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(settings.keyBindJump.getKeyCode()));
            }

            if (arrowLook.getValue()) {
                float offset = sensitivity.getValue().floatValue() * 10.0f;

                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    Eclipse.mc.player.rotationPitch = Math.max(Eclipse.mc.player.rotationPitch - offset, -90.0f);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    Eclipse.mc.player.rotationPitch = Math.min(Eclipse.mc.player.rotationPitch + offset, 90.0f);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    Eclipse.mc.player.rotationYaw += offset;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    Eclipse.mc.player.rotationYaw -= offset;
                }

                Eclipse.mc.playerController.updateController();
            }
        }
    }

    @SubscribeEvent
    public void onKey(KeyEvent event) {
        if (Module.fullNullCheck() && isValidGUI()) {
            event.setInfo(event.isPressed());
        }
    }

    public boolean isValidGUI() {
        return Eclipse.mc.currentScreen != null && !(Eclipse.mc.currentScreen instanceof GuiChat);
    }
}
