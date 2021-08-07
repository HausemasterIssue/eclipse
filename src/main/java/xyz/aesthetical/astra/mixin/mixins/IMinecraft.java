package xyz.aesthetical.astra.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Minecraft.class)
public interface IMinecraft {
    @Accessor("timer")
    Timer getTimer();

    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimer(int rightClickDelayTimer);

    @Accessor("defaultResourcePacks")
    List<IResourcePack> getDefaultResourcePacks();
}
