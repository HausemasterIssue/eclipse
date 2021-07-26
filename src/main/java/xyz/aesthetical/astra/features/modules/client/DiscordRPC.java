package xyz.aesthetical.astra.features.modules.client;

import xyz.aesthetical.astra.RPC;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "DiscordRPC", description = "Shows a rich presence on discord")
@Module.Info(category = Module.Category.CLIENT)
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
