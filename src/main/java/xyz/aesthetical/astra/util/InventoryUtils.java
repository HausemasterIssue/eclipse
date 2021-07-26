package xyz.aesthetical.astra.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.aesthetical.astra.Astra;

public class InventoryUtils {
    public static int switchInHotbar(Item item) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                int oldSlot = Astra.mc.player.inventory.currentItem;
                Astra.mc.player.inventory.currentItem = i;

                return oldSlot;
            }
        }

        return -1;
    }

    public static int switchInHotbar(Class<? extends Item> clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && clazz.isInstance(stack.getItem())) {
                int oldSlot = Astra.mc.player.inventory.currentItem;
                Astra.mc.player.inventory.currentItem = i;

                return oldSlot;
            }
        }

        return -1;
    }
}
