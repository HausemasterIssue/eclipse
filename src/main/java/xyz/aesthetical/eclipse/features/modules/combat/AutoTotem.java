package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.Timer;

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
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player && Eclipse.mc.currentScreen == null) {
            // check for totems
            if (Eclipse.mc.player.getHeldItemOffhand().isEmpty() || Eclipse.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                if (timer.passedS(delay.getValue().doubleValue())) {
                    replaceTotem();
                    timer.reset();
                }
            }
        }
    }

    private void replaceTotem() {
        for (int i = 0; i < Eclipse.mc.player.inventory.mainInventory.size(); ++i) {
            ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);

            // if theres a totem in that inventory slot,
            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING) {
                boolean offhandEmpty = Eclipse.mc.player.getHeldItemOffhand().isEmpty();

                Eclipse.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Eclipse.mc.player);
                Eclipse.mc.playerController.windowClick(0, i < 9 ? i + 36 : i, 0, ClickType.PICKUP, Eclipse.mc.player);

                if (offhandEmpty) {
                    Eclipse.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Eclipse.mc.player);
                }

                return;
            }
        }
    }

    public static int getTotemCount() {
        int count = 0;

        for (ItemStack stack : Eclipse.mc.player.inventory.mainInventory) {
            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING) {
                ++count;
            }
        }

        if (!Eclipse.mc.player.getHeldItemOffhand().isEmpty() && Eclipse.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++count;
        }

        return count;
    }
}
