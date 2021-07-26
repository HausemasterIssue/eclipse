package xyz.aesthetical.astra.features.modules.render;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Module;

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

        Astra.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisabled() {
        Astra.mc.renderGlobal.loadRenderers();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && shouldReload) {
            shouldReload = false;
            Astra.mc.renderGlobal.loadRenderers();
        }
    }
}
