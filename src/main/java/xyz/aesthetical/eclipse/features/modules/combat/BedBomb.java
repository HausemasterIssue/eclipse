package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.render.RenderEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.commands.Command;
import xyz.aesthetical.eclipse.managers.commands.text.ChatColor;
import xyz.aesthetical.eclipse.managers.commands.text.TextBuilder;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// @todo fix this bullshit i hate it
@Module.Mod(name = "BedBomb", description = "haha ez")
@Module.Info(category = Module.Category.COMBAT)
public class BedBomb extends Module {
    public final Setting<Priority> priority = register(new Setting<>("Priority", Priority.CLOSEST).setDescription("How to figure out your target"));
    public final NumberSetting targetRange = register(new NumberSetting("Target Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("How far to look for targets"));
    public final Setting<Boolean> invisible = register(new Setting<>("Invisible", false).setDescription("If to attack invisible entities"));
    public final Setting<Boolean> antiNaked = register(new Setting<>("Anti-Naked", false).setDescription("If to waste beds on naked players"));
    public final NumberSetting placeDelay = register(new NumberSetting("Place Delay", 250.0f).setMin(0.0f).setMax(2000.0f).setDescription("The delay in MS before placing another bed"));
    public final NumberSetting placeRange = register(new NumberSetting("Place Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("How far to be able to place beds"));
    public final NumberSetting maxSelfDamage = register(new NumberSetting("Max Self Damage", 6.0f).setMin(2.0f).setMax(36.0f).setDescription("The maximum amount of damage a bed explosion can do to you"));
    public final NumberSetting minDamage = register(new NumberSetting("Min Damage", 16.0f).setMin(2.0f).setMax(36.0f).setDescription("The minimum amount of damage the bed explosion has to do"));
    public final Setting<Boolean> suicide = register(new Setting<>("Suicide", false).setDescription("If to attempt not to kill you when placing and sleeping in the bed"));
    public final NumberSetting sleepDelay = register(new NumberSetting("Sleep Delay", 250.0f).setMin(0.0f).setMax(2000.0f).setDescription("The delay in MS before sleeping in another bed"));
    public final Setting<Boolean> sync = register(new Setting<>("Sync", false).setDescription("If to sync sleeping using packets"));

    private EntityPlayer target = null;
    private EnumHand hand = EnumHand.MAIN_HAND;
    private BlockPos placePos = null;
    private EnumFacing direction = null;

    private final Timer placeTimer = new Timer();
    private final Timer sleepTimer = new Timer();

    @Override
    public void onDisabled() {
        target = null;
        hand = EnumHand.MAIN_HAND;
        placePos = null;
        direction = null;
        placeTimer.reset();
        sleepTimer.reset();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            Biome biome = Eclipse.mc.world.getBiome(Eclipse.mc.player.getPosition());
            // @todo check for the end dimension
            if (biome != Biomes.HELL) {
                Command.send(new TextBuilder().append(ChatColor.Dark_Gray, "You are not in the nether or end. Toggling..."));
                toggle();
                return;
            }

            if (target == null || !isValidCurrentTarget()) {
                EntityPlayer possibleNewTarget = findTarget();
                if (possibleNewTarget == null) {
                    return;
                }

                target = possibleNewTarget;
            }

            ItemStack offhand = Eclipse.mc.player.getHeldItemOffhand();
            if (isBed(offhand)) {
                hand = EnumHand.OFF_HAND;
            } else {
                boolean success = false;
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
                    if (isBed(stack)) {
                        hand = EnumHand.MAIN_HAND;
                        success = true;
                        break;
                    }
                }

                if (!success) {
                    return;
                }
            }

            doBedBomb();
        }
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && placePos != null && direction != null) {
            RenderUtils.drawFilledBox(new AxisAlignedBB(placePos).offset(RenderUtils.getCameraPos()), ColorUtil.toRGBA(255, 255, 255, 80));
            RenderUtils.drawFilledBox(new AxisAlignedBB(placePos.offset(direction)).offset(RenderUtils.getCameraPos()), ColorUtil.toRGBA(255, 255, 255, 80));
        }
    }

    private void doBedBomb() {
        if (placePos == null || direction == null) {
            findPlacePositions();
        } else {
            if (!(Eclipse.mc.world.getBlockState(placePos).getBlock() instanceof BlockBed)) {
                return;
            }

            if (placeTimer.passedMs(placeDelay.getValue().longValue())) {
                placeTimer.reset();

                // @todo setting - swing
                WorldUtils.place(placePos, hand, true, true);
            }

            if (sleepTimer.passedMs(sleepDelay.getValue().longValue())) {
                sleepTimer.reset();

                boolean yes = false;
                EnumHand h = EnumHand.MAIN_HAND;
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = Eclipse.mc.player.inventory.getStackInSlot(i);
                    if (!isBed(stack)) {
                        Eclipse.mc.player.inventory.currentItem = i;
                        yes = true;
                        break;
                    }
                }

                if (!yes) {
                    ItemStack offhand = Eclipse.mc.player.getHeldItemOffhand();
                    if (!isBed(offhand)) {
                        h = EnumHand.OFF_HAND;
                        yes = true;
                    }
                }

                if (yes) {
                    sleepInBed(h);
                } else { // @todo not a good failsafe??
                    placePos = null;
                    direction = null;
                }
            }
        }
    }

    private void findPlacePositions() {
        List<BlockPos> placePositions = WorldUtils.getSphere(new BlockPos(Eclipse.mc.player.getPositionVector()), placeRange.getValue().intValue(), placeRange.getValue().intValue(), false, true, 0);
        if (!placePositions.isEmpty()) {
            ArrayList<Pair<BlockPos, Pair<EnumFacing, Float>>> pairs = new ArrayList<>();

            for (BlockPos pos : placePositions) {
                if (!Eclipse.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
                    continue;
                }

                // check if you can even place a bed
                EnumFacing dir = null;
                for (EnumFacing direction : EnumFacing.values()) {
                    if (direction == EnumFacing.DOWN || direction == EnumFacing.UP) {
                        continue;
                    }

                    if (!Eclipse.mc.world.getBlockState(pos.offset(direction)).getMaterial().isReplaceable()) {
                        dir = direction;
                        break;
                    }
                }

                if (dir == null) {
                    continue;
                }

                float selfDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5.0f, true, Eclipse.mc.player);
                if (!suicide.getValue()) {
                    if (selfDamage - 0.5 >= maxSelfDamage.getValue().floatValue() || selfDamage - 0.5 >= EntityUtil.getTotalHealth(Eclipse.mc.player)) {
                        continue;
                    }
                }

                float damage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5.0f, true, target);
                if (selfDamage > damage || damage < minDamage.getValue().floatValue()) {
                    continue;
                }

                pairs.add(new Pair<>(pos, new Pair<>(dir, damage)));
            }

            if (!pairs.isEmpty()) {
                pairs.sort(Comparator.comparingDouble(a -> a.getValue().getValue()));
                Collections.reverse(pairs);

                Pair<BlockPos, Pair<EnumFacing, Float>> data = pairs.get(0);

                placePos = data.getKey();
                direction = data.getValue().getKey();
            }
        }
    }

    private void sleepInBed(EnumHand h) {
        if (Eclipse.mc.world.getBlockState(placePos).getBlock() instanceof BlockBed) {
            EnumFacing facing = WorldUtils.getFacing(placePos);
            Vec3d hitVector = new Vec3d(placePos.add(facing.getDirectionVec())).scale(0.5);

            // @todo setting
            WorldUtils.swingArm(h);
            if (sync.getValue()) {
                float x = (float) (hitVector.x - placePos.getX());
                float y = (float) (hitVector.y - placePos.getY());
                float z = (float) (hitVector.z - placePos.getZ());

                Eclipse.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, facing, h, x, y, z));
            } else {
                Eclipse.mc.playerController.processRightClickBlock(Eclipse.mc.player, Eclipse.mc.world, placePos, facing, hitVector, h);
            }
        }
    }

    private EntityPlayer findTarget() {
        EntityUtil.Sorting sorting = priority.getValue() == Priority.CLOSEST ? EntityUtil.Sorting.NONE : EntityUtil.Sorting.LOW_HEALTH;
        return EntityUtil.findTarget(targetRange.getValue().floatValue(), invisible.getValue(), antiNaked.getValue(), false, sorting);
    }

    private boolean isValidCurrentTarget() {
        return target != null && !target.isDead && Eclipse.mc.player.getDistance(target) < targetRange.getValue().floatValue();
    }

    private boolean isBed(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemBed;
    }

    public enum Priority {
        CLOSEST,
        HEALTH
    }
}
