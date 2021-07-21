package xyz.aesthetical.eclipse.features.modules.miscellaneous;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.modules.exploits.MountBypass;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.managers.commands.Command;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.Timer;

@Module.Mod(name = "AutoSalC1Dupe", description = "Automatically preforms the SalC1 dupe")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class AutoSalC1Dupe extends Module {
    public final NumberSetting delay = register(new NumberSetting("Delay", 1000.0f).setMin(0.0f).setMax(5000.0f).setDescription("How long to wait before doing the dupe"));
    public final NumberSetting entityRange = register(new NumberSetting("Range", 5.0f).setMin(1.0f).setMax(6.0f).setDescription("How far to look for chestable entities if not on one already"));

    private EnumHand hand = EnumHand.MAIN_HAND;
    private AbstractChestHorse ridden = null;
    private int oldSlot = -1;

    private boolean doDupe = false;
    private boolean previousMountBypassState = false;

    private final Timer timer = new Timer();

    @Override
    public void onDisabled() {
        timer.reset();
        previousMountBypassState = false;
        doDupe = false;
        hand = EnumHand.MAIN_HAND;
        ridden = null;
        oldSlot = -1;
    }

    @Override
    public void onEnabled() {
        if (Module.fullNullCheck()) {
            boolean success = doChestCheck();
            if (!success) {
                Command.send(new TextBuilder().append(ChatColor.Dark_Gray, "I'm sorry, but I could not find any chests in your hotbar/offhand! Toggling..."));
                toggle();
                return;
            }

            doDupe = true;
        } else {
            toggle();
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player && doDupe && timer.passedMs(delay.getValue().longValue())) {
            timer.reset();

            if (Eclipse.mc.player.getRidingEntity() instanceof AbstractChestHorse && Eclipse.mc.player.getRidingEntity() == Eclipse.mc.player) {
                ridden = (AbstractChestHorse) Eclipse.mc.player.getRidingEntity();
            } else {
                for (int i = 0; i < Eclipse.mc.world.loadedEntityList.size(); ++i) {
                    Entity entity = Eclipse.mc.world.loadedEntityList.get(i);
                    if (!(entity instanceof AbstractChestHorse) || Eclipse.mc.player.getDistance(entity) > entityRange.getValue().floatValue()) {
                        continue;
                    }

                    AbstractChestHorse horse = (AbstractChestHorse) entity;
                    if (horse.getControllingPassenger() == null) {
                        ridden = horse;
                        break;
                    }
                }
            }

            Eclipse.mc.player.dismountRidingEntity();
            Eclipse.mc.playerController.interactWithEntity(Eclipse.mc.player, ridden, hand);

            Module module = Eclipse.moduleManager.getModule(MountBypass.class);
            previousMountBypassState = module.isToggled();
            if (!module.isToggled()) {
                module.toggle();
            }

            // get on the entity
            Eclipse.mc.playerController.interactWithEntity(Eclipse.mc.player, ridden, EnumHand.MAIN_HAND);
            ridden.openGUI(Eclipse.mc.player);

            boolean atleastOne = false;

            for (int i = 0; i < Eclipse.mc.player.inventory.mainInventory.size(); ++i) {
                ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemShulkerBox) {
                    Eclipse.mc.playerController.windowClick(0, i < 9 ? i + 36 : i, 0, ClickType.QUICK_MOVE, Eclipse.mc.player);
                    atleastOne = true;
                }
            }

            if (!atleastOne) {
                Command.send(new TextBuilder().append(ChatColor.Dark_Gray, "Failed to move shulker boxes into inventory, none found. Toggling..."));
                toggle();
                return;
            }

            // turn off mount bypass so we can make the server throw the error
            module.toggle();

            Eclipse.mc.playerController.interactWithEntity(Eclipse.mc.player, ridden, EnumHand.MAIN_HAND);
            Eclipse.mc.player.dismountRidingEntity();

            if (!module.isToggled() && previousMountBypassState) {
                module.toggle();
            }
        }
    }

    private boolean doChestCheck() {
        ItemStack offhand = Eclipse.mc.player.getHeldItemOffhand();
        if (isChest(offhand)) {
            hand = EnumHand.OFF_HAND;
        } else {
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
                if (isChest(stack)) {
                    hand = EnumHand.MAIN_HAND;
                    oldSlot = i;
                    break;
                }
            }
        }

        return oldSlot != -1 && hand != EnumHand.OFF_HAND;
    }

    private boolean isChest(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.CHEST;
    }
}
