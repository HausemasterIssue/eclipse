package xyz.aesthetical.astra.features.modules.miscellaneous;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Module;

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
            fakePlayer = new EntityOtherPlayerMP(Astra.mc.world, new GameProfile(UUID.fromString("873c0367-7ba9-4a9a-96ae-fa312ae756cb"), "ixxaesthetical"));
            fakePlayer.copyLocationAndAnglesFrom(Astra.mc.player);
            fakePlayer.inventory.copyInventory(Astra.mc.player.inventory);
            fakePlayer.setEntityId(-69420);
            fakePlayer.setSpawnPoint(Astra.mc.player.getPosition(), true);
            fakePlayer.setGameType(GameType.SURVIVAL);

            Astra.mc.world.spawnEntity(fakePlayer);
        }
    }

    private void remove() {
        if (fakePlayer != null) {
            Astra.mc.world.removeEntityDangerously(fakePlayer);
            fakePlayer = null;
        }
    }
}
