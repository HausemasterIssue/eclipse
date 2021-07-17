package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.input.MouseEvent;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.InventoryUtils;

@Module.Mod(name = "MiddleClickPearl", description = "Throws an ender pearl on a middle click")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class MiddleClickPearl extends Module {
    @SubscribeEvent
    public void onMouseClick(MouseEvent event) {
        if (event.getButton() == MouseEvent.Button.MIDDLE) {
            int oldSlot = InventoryUtils.switchInHotbar(Items.ENDER_PEARL);
            if (oldSlot != -1) {
                Eclipse.mc.playerController.processRightClick(Eclipse.mc.player, Eclipse.mc.world, EnumHand.MAIN_HAND);
                Eclipse.mc.player.inventory.currentItem = oldSlot;
            }
        }
    }
}
