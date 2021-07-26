package xyz.aesthetical.astra.features.modules.render;

import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.managers.modules.Module;

@Module.Mod(name = "ViewModel", description = "Changes how items in your hands are rendered")
@Module.Info(category = Module.Category.RENDER)
public class ViewModel extends Module {
    public static ViewModel instance;

    public final NumberSetting leftX = register(new NumberSetting("Left X", 0.0f).setMin(-2.0f).setMax(2.0f).setDescription("The x value of your left hand"));
    public final NumberSetting leftY = register(new NumberSetting("Left Y", 0.0f).setMin(-2.0f).setMax(2.0f).setDescription("The y value of your left hand"));
    public final NumberSetting leftZ = register(new NumberSetting("Left Z", 0.0f).setMin(-2.0f).setMax(2.0f).setDescription("The z value of your left hand"));
    public final NumberSetting rightX = register(new NumberSetting("Right X", 0.0f).setMin(-2.0f).setMax(2.0f).setDescription("The x value of your right hand"));
    public final NumberSetting rightY = register(new NumberSetting("Right Y", 0.0f).setMin(-2.0f).setMax(2.0f).setDescription("The y value of your right hand"));
    public final NumberSetting rightZ = register(new NumberSetting("Right Z", 0.0f).setMin(-2.0f).setMax(2.0f).setDescription("The z value of your right hand"));

    public ViewModel() {
        instance = this;
    }

    public Vec3d getOffsets(EnumHandSide hand) {
        float x = hand == EnumHandSide.LEFT ? leftX.getValue().floatValue() : rightX.getValue().floatValue();
        float y = hand == EnumHandSide.LEFT ? leftY.getValue().floatValue() : rightY.getValue().floatValue();
        float z = hand == EnumHandSide.LEFT ? leftZ.getValue().floatValue() : rightZ.getValue().floatValue();
        return new Vec3d(x, y, z);
    }
}
