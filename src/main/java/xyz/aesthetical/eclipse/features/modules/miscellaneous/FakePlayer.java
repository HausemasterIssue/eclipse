package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.managers.modules.Module;

import java.util.UUID;

@Module.Mod(name = "FakePlayer", description = "Spawns a fake player to test out configs")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class FakePlayer extends Module {
    private EntityOtherPlayerMP fakePlayer = null;

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        fakePlayer = null;
        toggle();
    }

    @Override
    public void onEnabled() {
        if (Module.fullNullCheck()) {
            fakePlayer = new EntityOtherPlayerMP(Eclipse.mc.world, new GameProfile(UUID.fromString("873c0367-7ba9-4a9a-96ae-fa312ae756cb"), "ixxaesthetical"));
            fakePlayer.copyLocationAndAnglesFrom(Eclipse.mc.player);
            fakePlayer.inventory.copyInventory(Eclipse.mc.player.inventory);
            fakePlayer.setEntityId(-69420);
            fakePlayer.setSpawnPoint(Eclipse.mc.player.getPosition(), true);
            fakePlayer.setGameType(GameType.SURVIVAL);

            Eclipse.mc.world.spawnEntity(fakePlayer);
        }
    }

    private void remove() {
        if (fakePlayer != null) {
            Eclipse.mc.world.removeEntityDangerously(fakePlayer);
            fakePlayer = null;
        }
    }
}
