package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.HoleManager;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.RotationUtils;
import xyz.aesthetical.eclipse.util.WorldUtils;

@Module.Mod(name = "HoleFiller", description = "Automatically fills in safe holes for others")
@Module.Info(category = Module.Category.COMBAT)
public class HoleFiller extends Module {
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing client side"));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to rotate when placing"));
    public final NumberSetting rotationPackets = register(new NumberSetting("Rotation Packets", 2).setMin(0).setMax(10).setDescription("How many rotation packets to send"));
    public final Setting<Boolean> toggleWhenFinished = register(new Setting<>("Toggle When Finished", true).setDescription("If to toggle once finished"));

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            EnumHand hand = EnumHand.MAIN_HAND;
            int oldSlot = -1;

            ItemStack offHand = Eclipse.mc.player.getHeldItemOffhand();
            if (!offHand.isEmpty() && offHand.getItem() instanceof ItemBlock && ((ItemBlock) offHand.getItem()).getBlock() == Blocks.OBSIDIAN) {
                hand = EnumHand.OFF_HAND;
            } else {
                // @todo make this an inventory util
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.OBSIDIAN) {
                        oldSlot = Eclipse.mc.player.inventory.currentItem;
                        Eclipse.mc.player.inventory.currentItem = i;
                        break;
                    }
                }
            }

            if (oldSlot == -1 && hand != EnumHand.OFF_HAND) {
                return;
            }

            for (HoleManager.HoleInfo info : Eclipse.holeManager.getHoles()) {
                if (info.isEntityInHole()) {
                    continue;
                }

                if (rotate.getValue()) {
                    for (int i = 0; i < rotationPackets.getValue().intValue(); ++i) {
                        RotationUtils.rotate(info.getPos(), true);
                    }
                }

                WorldUtils.place(info.getPos(), EnumHand.MAIN_HAND, swing.getValue(), true);
            }

            Eclipse.mc.player.inventory.currentItem = oldSlot;

            if (toggleWhenFinished.getValue()) {
                toggle();
            }
        }
    }
}
