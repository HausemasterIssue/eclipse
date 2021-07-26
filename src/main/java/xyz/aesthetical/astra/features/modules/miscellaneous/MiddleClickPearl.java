package xyz.aesthetical.astra.features.modules.miscellaneous;

import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.input.MouseEvent;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.InventoryUtils;

@Module.Mod(name = "MiddleClickPearl", description = "Throws an ender pearl on a middle click")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class MiddleClickPearl extends Module {
    @SubscribeEvent
    public void onMouseClick(MouseEvent event) {
        if (event.getButton() == MouseEvent.Button.MIDDLE) {
            int oldSlot = InventoryUtils.switchInHotbar(Items.ENDER_PEARL);
            if (oldSlot != -1) {
                Astra.mc.playerController.processRightClick(Astra.mc.player, Astra.mc.world, EnumHand.MAIN_HAND);
                Astra.mc.player.inventory.currentItem = oldSlot;
            }
        }
    }
}
