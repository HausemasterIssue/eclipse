package xyz.aesthetical.eclipse.features.modules.render;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "Xray", description = "Allows you to see blocks through walls")
@Module.Info(category = Module.Category.RENDER)
public class Xray extends Module {
    public static Xray instance;

    private boolean shouldReload = false;

    public Xray() {
        instance = this;
    }

    @Override
    public void onEnabled() {
        if (!Module.fullNullCheck()) {
            shouldReload = true;
            return;
        }

        Eclipse.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisabled() {
        Eclipse.mc.renderGlobal.loadRenderers();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && shouldReload) {
            shouldReload = false;
            Eclipse.mc.renderGlobal.loadRenderers();
        }
    }
}
