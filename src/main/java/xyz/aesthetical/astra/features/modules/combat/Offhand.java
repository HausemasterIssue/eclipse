package xyz.aesthetical.astra.features.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.EntityUtil;

import java.util.List;

// @todo i have a feeling that this is too little code for what it's supposed to do, but it works surprisingly well except for crystal switching
@Module.Mod(name = "Offhand", description = "Manages your offhand")
@Module.Info(category = Module.Category.COMBAT)
public class Offhand extends Module {
    // totem settings
    public final Setting<Boolean> totems = register(new Setting<>("Totems", true).setDescription("If to automatically place totems in your offhand"));
    public final NumberSetting totemHealth = register(new NumberSetting("Totem Health", 10.0f).setMin(1.0f).setMax(36.0f).setDescription("At what health totems should be placed in your offhand if not already"));

    // gapple settings
    public final Setting<Boolean> gapples = register(new Setting<>("Gapples", true).setDescription("If to switch to a gapple when holding down right click"));
    public final NumberSetting gappleHealth = register(new NumberSetting("Gapple Health", 10.0f).setMin(1.0f).setMax(36.0f).setDescription("At what health where gapples should be considered safe to switch to"));

    // crystal settings
    public final Setting<Boolean> crystals = register(new Setting<>("Crystals", true).setDescription("If to switch to a crystal in your offhand"));
    public final NumberSetting crystalRange = register(new NumberSetting("Crystal Range", 10.0f).setMin(2.0f).setMax(50.0f).setDescription("How far to look for enemies to place crystals in your offhand"));
    // public final Setting<Boolean> crystalCheckFriends = register(new Setting<>("Crystal Check Friends", false).setDescription("If to check if the player is a friend"));
    public final NumberSetting crystalHealth = register(new NumberSetting("Crystal Health", 16.0f).setMin(1.0f).setMax(36.0f).setDescription("At what health where crystals should be considered safe to switch to"));

    private Item item;

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && Astra.mc.currentScreen == null) {
            boolean fuck = false; // this is if we detect "oh shit ur abouta die" and we force a totem

            if (totems.getValue()) {
                if (EntityUtil.getTotalHealth(Astra.mc.player) <= totemHealth.getValue().floatValue()) {
                    fuck = true;
                }

                item = Items.TOTEM_OF_UNDYING;
            }

            if (Mouse.isButtonDown(1) && canGappleSwitch() && !fuck) {
                item = Items.GOLDEN_APPLE;
            }

            if (canCrystalSwitch() && !fuck) {
                item = Items.END_CRYSTAL;
            }

            if (item != null) {
                if (Astra.mc.player.getHeldItemOffhand().getItem() == item || Astra.mc.player.getHeldItemMainhand().getItem() == item) {
                    return;
                }

                int itemSlot = getItemSlotHotbar(item);
                if (itemSlot != -1) {
                    setInHand(itemSlot);
                }
            }
        }
    }

    private void setInHand(int slot) {
        // if theres an item in the offhand slot, we'll click on the offhand slot
        Astra.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Astra.mc.player);
        // click on the item slot, as above this will just put the old offhand item into the slot
        Astra.mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, Astra.mc.player);
        // put the item into the offhand slot
        Astra.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Astra.mc.player);
    }

    private boolean canGappleSwitch() {
        return gapples.getValue() && gappleHealth.getValue().floatValue() <= EntityUtil.getTotalHealth(Astra.mc.player);
    }

    private boolean canCrystalSwitch() {
        List<EntityPlayer> players = Astra.mc.world.getEntities(EntityPlayer.class, (player) -> Astra.mc.player.getDistance(player) >= crystalRange.getValue().floatValue());
        return crystals.getValue() && !players.isEmpty() && crystalHealth.getValue().floatValue() <= EntityUtil.getTotalHealth(Astra.mc.player);
    }

    private int getItemSlotHotbar(Item item) {
        for (int i = 0; i < Astra.mc.player.inventory.mainInventory.size(); ++i) {
            ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return i;
            }
        }

        return -1;
    }
}
