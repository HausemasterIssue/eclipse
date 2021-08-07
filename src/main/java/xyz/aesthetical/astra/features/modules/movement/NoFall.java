package xyz.aesthetical.astra.features.modules.movement;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.EnumConverter;

@Module.Mod(name = "NoFall", description = "Stops you from taking fall damage")
@Module.Info(category = Module.Category.MOVEMENT)
public class NoFall extends Module {
    public final Setting<Mode> mode = register(new Setting<>("Mode", Mode.PACKET).setDescription("How to negate fall distance"));
    public final NumberSetting distance = register(new NumberSetting("Fall Distance", 3.0f).setMin(0.0f).setMax(255.0f).setDescription("How far you can fall before doing anything"));

    @Override
    public String getDisplay() {
        return EnumConverter.getActualName(mode.getValue());
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            if (Astra.mc.player.fallDistance >= distance.getValue().floatValue()) {
                switch (mode.getValue()) {
                    case PACKET: {
                        Astra.mc.player.connection.sendPacket(new CPacketPlayer(true));
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
            ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);
            if (mode.getValue() == Mode.WATER_BUCKET) {
                if (stack.getItem() == Items.WATER_BUCKET) {
                    oldSlot = i;
                    Astra.mc.player.inventory.currentItem = i;
                    break;
                }
            } else if (mode.getValue() == Mode.WEB) {
                if (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.WEB) {
                    oldSlot = i;
                    Astra.mc.player.inventory.currentItem = i;
                    break;
                }
            }
        }

        if (oldSlot == -1) {
            return;
        }

        Astra.mc.player.rotationPitch = 90.0f;
        Astra.mc.playerController.processRightClick(Astra.mc.player, Astra.mc.world, EnumHand.MAIN_HAND);
    }

    public enum Mode {
        PACKET,
        WATER_BUCKET,
        WEB
    }
}
