package xyz.aesthetical.astra.features.modules.movement;

import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "IceSpeed", description = "Makes you go faster on ice")
@Module.Info(category = Module.Category.MOVEMENT)
public class IceSpeed extends Module {
    @Override
    public void onDisabled() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            Blocks.ICE.slipperiness = 0.4f;
            Blocks.PACKED_ICE.slipperiness = 0.4f;
            Blocks.FROSTED_ICE.slipperiness = 0.4f;
        }
    }
}
