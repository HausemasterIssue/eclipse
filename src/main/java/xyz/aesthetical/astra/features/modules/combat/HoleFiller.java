package xyz.aesthetical.astra.features.modules.combat;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.HoleManager;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.RotationUtils;
import xyz.aesthetical.astra.util.Timer;
import xyz.aesthetical.astra.util.WorldUtils;

import java.util.ArrayList;

@Module.Mod(name = "HoleFiller", description = "Automatically fills in safe holes for others")
@Module.Info(category = Module.Category.COMBAT)
public class HoleFiller extends Module {
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing client side"));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to rotate when placing"));
    public final NumberSetting rotationPackets = register(new NumberSetting("Rotation Packets", 2).setMin(0).setMax(10).setDescription("How many rotation packets to send"));
    public final Setting<Boolean> sync = register(new Setting<>("Sync", false).setDescription("If to sync with the server by sending packets"));
    public final NumberSetting delay = register(new NumberSetting("Delay", 0.3f).setMin(0.0f).setMax(2.5f).setDescription("How long to wait in S between placing blocks in safe holes"));
    public final Setting<Boolean> toggleWhenFinished = register(new Setting<>("Toggle When Finished", true).setDescription("If to toggle once finished"));

    private ArrayList<BlockPos> positions = new ArrayList<>();
    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            EnumHand hand = EnumHand.MAIN_HAND;
            int oldSlot = -1;

            ItemStack offHand = Astra.mc.player.getHeldItemOffhand();
            if (!offHand.isEmpty() && offHand.getItem() instanceof ItemBlock && ((ItemBlock) offHand.getItem()).getBlock() == Blocks.OBSIDIAN) {
                hand = EnumHand.OFF_HAND;
            } else {
                // @todo make this an inventory util
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.OBSIDIAN) {
                        oldSlot = Astra.mc.player.inventory.currentItem;
                        Astra.mc.player.inventory.currentItem = i;
                        break;
                    }
                }
            }

            if (oldSlot == -1 && hand != EnumHand.OFF_HAND) {
                return;
            }

            for (HoleManager.HoleInfo info : Astra.holeManager.getHoles()) {
                if (info.isEntityInHole()) {
                    continue;
                }

                if (rotate.getValue()) {
                    for (int i = 0; i < rotationPackets.getValue().intValue(); ++i) {
                        RotationUtils.rotate(info.getPos(), true);
                    }
                }

                WorldUtils.place(info.getPos(), EnumHand.MAIN_HAND, swing.getValue(), true, sync.getValue());
            }

            Astra.mc.player.inventory.currentItem = oldSlot;

            if (toggleWhenFinished.getValue()) {
                toggle();
            }
        }
    }
}
