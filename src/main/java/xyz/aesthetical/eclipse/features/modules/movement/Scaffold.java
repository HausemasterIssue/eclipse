package xyz.aesthetical.eclipse.features.modules.movement;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.entity.MoveEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.*;

import java.util.HashMap;
import java.util.Map;

@Module.Mod(name = "Scaffold", description = "Places blocks under you")
@Module.Info(category = Module.Category.MOVEMENT)
public class Scaffold extends Module {
    private static final BlockPos[] DIRECTION_OFFSETS = new BlockPos[] {
            new BlockPos(0, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, -1),
            new BlockPos(0, 0, 1)
    };

    public final Setting<Boolean> tower = register(new Setting<>("Tower", false).setDescription("If to change your horizontal velocity to make towering easier"));
    public final NumberSetting delay = register(new NumberSetting("Delay", 0.0f).setMin(0.0f).setMax(5.0f).setDescription("How long to wait in S before placing another block"));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true).setDescription("If to send a rotation packet"));
    public final Setting<Boolean> swing = register(new Setting<>("Swing", true).setDescription("If to swing when placing blocks"));
    public final Setting<Boolean> noGravityBlocks = register(new Setting<>("No Gravity Blocks", true).setDescription("If to avoid placing gravity blocks"));

    private final Map<BlockPos, EnumFacing> positions = new HashMap<>();
    private BlockPos currentPos = null;
    private final Timer timer = new Timer();
    private EnumHand hand = EnumHand.MAIN_HAND;

    @Override
    public void onDisabled() {
        positions.clear();
        currentPos = null;
        timer.reset();
        hand = EnumHand.MAIN_HAND;
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (Module.fullNullCheck()) {
            if (tower.getValue() && Eclipse.mc.gameSettings.keyBindJump.isKeyDown()) {
                Eclipse.mc.player.motionY = 0.4;
            }

            BlockPos pos = new BlockPos(Eclipse.mc.player.getPositionVector()).add(0, -1, 0);
            if (WorldUtils.getBlockFromPos(pos) == Blocks.AIR) {
                int slot = InventoryUtils.switchInHotbar(ItemBlock.class);
                if (slot == -1) {
                    return;
                }

                if (timer.passedS(delay.getValue().doubleValue())) {
                    timer.reset();

                    Pair<BlockPos, EnumFacing> place = getPlacePos(pos);
                    if (place == null) {
                        return;
                    }

                    if (!positions.isEmpty()) {
                        synchronized (positions.entrySet()) {
                            for (Map.Entry<BlockPos, EnumFacing> entry : positions.entrySet()) {
                                currentPos = pos;
                                WorldUtils.place(entry.getKey(), hand, swing.getValue(), true);

                                if (rotate.getValue()) {
                                    RotationUtils.rotate(pos, true);
                                }

                                positions.remove(entry.getKey());
                            }
                        }
                    }
                }
            }
        }
    }

    private Pair<BlockPos, EnumFacing> getPlacePos(BlockPos origin) {
        if (WorldUtils.getBlockFromPos(origin) != Blocks.AIR) {
            return null;
        }

        for (BlockPos offset : DIRECTION_OFFSETS) {
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing == EnumFacing.DOWN) {
                    continue;
                }

                BlockPos pos = origin.add(offset);
                if (facing == EnumFacing.UP) {
                    return add(new Pair<>(pos, EnumFacing.UP));
                } else {
                    return add(new Pair<>(pos.add(facing.getDirectionVec()), facing));
                }
            }
        }

        return null;
    }

    public Pair<BlockPos, EnumFacing> add(Pair<BlockPos, EnumFacing> pair) {
        positions.put(pair.getKey(), pair.getValue());
        return pair;
    }
}
