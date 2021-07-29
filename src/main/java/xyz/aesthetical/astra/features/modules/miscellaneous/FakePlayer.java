package xyz.aesthetical.astra.features.modules.miscellaneous;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.Pair;

import java.util.Random;
import java.util.UUID;

@Module.Mod(name = "FakePlayer", description = "Spawns a fake player to test out configs")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class FakePlayer extends Module {
    private static final Random RNG = new Random();
    private static final Pair<String, UUID>[] ACCOUNTS = new Pair[] {
            new Pair<>("popbob", UUID.fromString("0f75a81d-70e5-43c5-b892-f33c524284f2")),
            new Pair<>("Fit", UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a")),
            new Pair<>("Nes", UUID.fromString("9d1f7b2c-d4ee-4702-9430-5e48265a0fa8")),
            new Pair<>("Aestheticall", UUID.fromString("873c0367-7ba9-4a9a-96ae-fa312ae756cb")),
            new Pair<>("Pryobite", UUID.fromString("724c7d3b-87d5-4758-8207-8aa9424360de")),
            new Pair<>("iTristan", UUID.fromString("64a5c834-514b-4024-9aa4-515719f6e7fa"))
    };

    private EntityOtherPlayerMP fake = null;

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        fake = null;
        toggle();
    }

    @Override
    public void onEnabled() {
        if (Module.fullNullCheck()) {
            Pair<String, UUID> account = ACCOUNTS[RNG.nextInt(ACCOUNTS.length - 1)];

            fake = new EntityOtherPlayerMP(Astra.mc.world, new GameProfile(account.getValue(), account.getKey()));
            fake.copyLocationAndAnglesFrom(Astra.mc.player);
            fake.inventory.copyInventory(Astra.mc.player.inventory);
            fake.setEntityId(-69420);
            fake.setGameType(GameType.SURVIVAL);

            Astra.mc.world.spawnEntity(fake);
        }
    }

    @Override
    public void onDisabled() {
        remove();
    }

    private void remove() {
        if (fake != null) {
            Astra.mc.world.removeEntityFromWorld(fake.getEntityId());
            Astra.mc.world.removeEntityDangerously(fake);
            fake = null;
        }
    }
}
