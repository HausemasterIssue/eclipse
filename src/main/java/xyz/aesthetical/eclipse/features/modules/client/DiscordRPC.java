package xyz.aesthetical.eclipse.features.modules.client;

import xyz.aesthetical.eclipse.RPC;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "DiscordRPC", description = "Shows a rich presence on discord")
@Module.Info(category = Module.Category.CLIENT, preToggled = true)
public class DiscordRPC extends Module {
    @Override
    public void onEnabled() {
        RPC.run();
    }

    @Override
    public void onDisabled() {
        RPC.stop();
    }
}
