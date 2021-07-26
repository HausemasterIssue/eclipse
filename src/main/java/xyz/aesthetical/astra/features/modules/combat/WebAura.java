package xyz.aesthetical.astra.features.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
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
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.EntityUtil;
import xyz.aesthetical.astra.util.RotationUtils;
import xyz.aesthetical.astra.util.WorldUtils;

@Module.Mod(name = "WebAura", description = "Places webs under players to slow them down")
@Module.Info(category = Module.Category.COMBAT)
public class WebAura extends Module {
    public final NumberSetting range = register(new NumberSetting("Range", 5.0f).setMin(3.0f).setMax(7.0f).setDescription("How far to look for players"));
    public final Setting<Boolean> invisible = register(new Setting<>("Invisible", true).setDescription("If to web invisible players"));
    public final Setting<Boolean> antiNaked = register(new Setting<>("Anti-Naked", true).setDescription("If to not web naked players"));
    public final Setting<Boolean> targetFriends = register(new Setting<>("Target Friends", false).setDescription("If to web friends"));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to send rotation packets"));
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing client side"));
    public final Setting<Boolean> toggleWhenFinished = register(new Setting<>("Toggle When Finished", true).setDescription("If to toggle once finished"));

    private EntityPlayer target = null;
    private EnumHand hand = EnumHand.MAIN_HAND;
    private int oldSlot = -1;

    @Override
    public void onEnabled() {
        target = null;
        oldSlot = -1;
        hand = EnumHand.MAIN_HAND;
    }

    @Override
    public void onDisabled() {
        target = null;
        oldSlot = -1;
        hand = EnumHand.MAIN_HAND;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck()) {
            if (target == null) {
                EntityPlayer possibleTarget = EntityUtil.findTarget(range.getValue().floatValue(), invisible.getValue(), antiNaked.getValue(), targetFriends.getValue(), EntityUtil.Sorting.NONE);
                if (possibleTarget == null) {
                    return;
                }

                target = possibleTarget;
            }

            boolean success = switchToWeb();
            if (!success) {
                if (toggleWhenFinished.getValue()) {
                    toggle();
                }

                return;
            }

            if (rotate.getValue()) {
                RotationUtils.rotate(target.getPosition(), true);
            }


            WorldUtils.place(new BlockPos(target.getPositionVector()), hand, swing.getValue(), true);

            Astra.mc.player.inventory.currentItem = oldSlot;
            oldSlot = -1;

            if (toggleWhenFinished.getValue()) {
                toggle();
            }

            target = null;
            oldSlot = -1;
            hand = EnumHand.MAIN_HAND;
        }
    }

    private boolean switchToWeb() {
        ItemStack offhand = Astra.mc.player.getHeldItemOffhand();
        if (!offhand.isEmpty() && offhand.getItem() instanceof ItemBlock && ((ItemBlock) offhand.getItem()).getBlock() == Blocks.WEB) {
            hand = EnumHand.OFF_HAND;
            return true;
        }

        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.WEB) {
                oldSlot = i;
                hand = EnumHand.MAIN_HAND;
                return true;
            }
        }

        return false;
    }
}
