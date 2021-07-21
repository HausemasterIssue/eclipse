package xyz.aesthetical.eclipse.features.modules.movement;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;

@Module.Mod(name = "NoFall", description = "Stops you from taking fall damage")
@Module.Info(category = Module.Category.MOVEMENT)
public class NoFall extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.PACKET).setDescription("How to negate fall distance"));
    public final NumberSetting distance = register(new NumberSetting("Fall Distance", 3.0f).setMin(0.0f).setMax(255.0f).setDescription("How far you can fall before doing anything"));

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            if (Eclipse.mc.player.fallDistance >= distance.getValue().floatValue()) {
                switch (mode.getValue()) {
                    case PACKET: {
                        Eclipse.mc.player.connection.sendPacket(new CPacketPlayer(true));
                        break;
                    }

                    case WATER_BUCKET:
                    case WEB: {
                        doNoFall();
                        break;
                    }
                }
            }
        }
    }

    private void doNoFall() {
        int oldSlot = -1;

        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
            if (mode.getValue() == Mode.WATER_BUCKET) {
                if (stack.getItem() == Items.WATER_BUCKET) {
                    oldSlot = i;
                    Eclipse.mc.player.inventory.currentItem = i;
                    break;
                }
            } else if (mode.getValue() == Mode.WEB) {
                if (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.WEB) {
                    oldSlot = i;
                    Eclipse.mc.player.inventory.currentItem = i;
                    break;
                }
            }
        }

        if (oldSlot == -1) {
            return;
        }

        Eclipse.mc.player.rotationPitch = 90.0f;
        Eclipse.mc.playerController.processRightClick(Eclipse.mc.player, Eclipse.mc.world, EnumHand.MAIN_HAND);
    }

    public enum Mode {
        PACKET,
        WATER_BUCKET,
        WEB
    }
}
