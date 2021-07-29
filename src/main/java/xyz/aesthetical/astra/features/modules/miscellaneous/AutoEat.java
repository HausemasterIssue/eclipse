package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "AutoEat", description = "Automatically eats food")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class AutoEat extends Module {
    private int slot = -1;
    private EnumHand hand = EnumHand.MAIN_HAND;
    private boolean eating = false;

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (eating && !Astra.mc.player.isHandActive()) {
                holdUse(eating = false);

                if (slot != -1 && hand == EnumHand.OFF_HAND) {
                    Astra.mc.player.inventory.currentItem = slot;
                    slot = -1;
                }
            }

            if (isValidFood(Astra.mc.player.getHeldItemOffhand())) {
                Astra.mc.player.setActiveHand(hand = EnumHand.OFF_HAND);
                holdUse(eating = true);
            } else {
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);
                    if (isValidFood(stack)) {
                        Astra.mc.player.setActiveHand(hand = EnumHand.MAIN_HAND);
                        slot = Astra.mc.player.inventory.currentItem;
                        Astra.mc.player.inventory.currentItem = i;

                        holdUse(eating = true);
                        break;
                    }
                }
            }
        }
    }

    private void holdUse(boolean pressed) {
        KeyBinding.setKeyBindState(Astra.mc.gameSettings.keyBindUseItem.getKeyCode(), pressed);
    }

    private boolean isValidFood(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemFood)) {
            return false;
        }

        return 20 - Astra.mc.player.getFoodStats().getFoodLevel() >= ((ItemFood) stack.getItem()).getHealAmount(stack);
    }
}
