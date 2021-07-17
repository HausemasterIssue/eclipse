package xyz.aesthetical.eclipse.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.aesthetical.eclipse.Eclipse;

public class InventoryUtils {
    public static int switchInHotbar(Item item) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                int oldSlot = Eclipse.mc.player.inventory.currentItem;
                Eclipse.mc.player.inventory.currentItem = i;

                return oldSlot;
            }
        }

        return -1;
    }

    public static int switchInHotbar(Class<? extends Item> clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && clazz.isInstance(stack.getItem())) {
                int oldSlot = Eclipse.mc.player.inventory.currentItem;
                Eclipse.mc.player.inventory.currentItem = i;

                return oldSlot;
            }
        }

        return -1;
    }
}
