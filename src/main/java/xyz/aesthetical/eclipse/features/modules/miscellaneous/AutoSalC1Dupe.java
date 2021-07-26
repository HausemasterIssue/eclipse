package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.Timer;

// @todo
@Module.Mod(name = "AutoSalC1Dupe", description = "Automatically preforms the SalC1 dupe")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class AutoSalC1Dupe extends Module {
    public final NumberSetting delay = register(new NumberSetting("Delay", 860.0f).setMin(0.0f).setMax(5000.0f).setDescription("How long to wait before doing the dupe"));
    public final NumberSetting entityRange = register(new NumberSetting("Range", 5.0f).setMin(1.0f).setMax(6.0f).setDescription("How far to look for chestable entities if not on one already"));

    private final Timer timer = new Timer();
    private Stage stage = Stage.IDLE;

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        toggle();
    }

    private boolean isChest(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.CHEST;
    }

    private enum Stage {
        IDLE,
        CHECKS,
        CHESTING,
    }
}
