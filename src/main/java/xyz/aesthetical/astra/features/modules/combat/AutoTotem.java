package xyz.aesthetical.astra.features.modules.combat;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.Timer;

@Module.Mod(name = "AutoTotem", description = "Automatically replaces totems")
@Module.Info(category = Module.Category.COMBAT)
public class AutoTotem extends Module {
    public final NumberSetting delay = register(new NumberSetting("Delay", 0.0f).setMax(0.0f).setMax(10.0f).setDescription("How long to wait in S until replacing the totem"));

    private final Timer timer = new Timer();

    @Override
    public String getDisplay() {
        return String.valueOf(getTotemCount());
    }

    @Override
    public void onEnabled() {
        timer.reset();
    }

    @Override
    public void onDisabled() {
        timer.reset();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && Astra.mc.currentScreen == null) {
            // check for totems
            if (Astra.mc.player.getHeldItemOffhand().isEmpty() || Astra.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                if (timer.passedS(delay.getValue().doubleValue())) {
                    replaceTotem();
                    timer.reset();
                }
            }
        }
    }

    private void replaceTotem() {
        for (int i = 0; i < Astra.mc.player.inventory.mainInventory.size(); ++i) {
            ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);

            // if theres a totem in that inventory slot,
            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING) {
                boolean offhandEmpty = Astra.mc.player.getHeldItemOffhand().isEmpty();

                Astra.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Astra.mc.player);
                Astra.mc.playerController.windowClick(0, i < 9 ? i + 36 : i, 0, ClickType.PICKUP, Astra.mc.player);

                if (offhandEmpty) {
                    Astra.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Astra.mc.player);
                }

                return;
            }
        }
    }

    public static int getTotemCount() {
        int count = 0;

        for (ItemStack stack : Astra.mc.player.inventory.mainInventory) {
            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING) {
                ++count;
            }
        }

        if (!Astra.mc.player.getHeldItemOffhand().isEmpty() && Astra.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++count;
        }

        return count;
    }
}
