package xyz.aesthetical.astra.features.modules.miscellaneous;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.world.EntitySpawnEvent;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.managers.notifications.Notification;
import xyz.aesthetical.astra.mixin.mixins.entity.IEntityEnderPearl;

@Module.Mod(name = "PearlNotifier", description = "Notifies you if someone throws an ender pearl")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class PearlNotifier extends Module {
    @SubscribeEvent
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (Module.fullNullCheck() && event.getEntity() instanceof EntityEnderPearl) {
            EntityEnderPearl pearl = (EntityEnderPearl) event.getEntity();
            EntityLivingBase entity;

            // @todo im not 100% sure that the pearl will give us the data, but from looking at the decompiled code the one without
            // it setting in the constructor is not client side, so i guess that means if it doesnt have a thrower it's us that threw it?
            if ((entity = ((IEntityEnderPearl) pearl).getPerlThrower()) != null) {
                if (entity instanceof EntityPlayer) {
                    Astra.notificationManager.createNotification(new Notification(
                            "Pearl Thrown",
                            Lists.newArrayList(entity.getName() + " threw an ender pearl."),
                            Notification.Type.WARNING
                    ));
                }
            }
        }
    }
}
