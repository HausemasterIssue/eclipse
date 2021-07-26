package xyz.aesthetical.eclipse.features.modules.combat;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.events.render.RenderEvent;
import xyz.aesthetical.eclipse.features.settings.NumberSetting;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.friends.Friend;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Module.Mod(name = "CrystalAura", description = "Automatically places and detonates end crystals")
@Module.Info(category = Module.Category.COMBAT)
public class CrystalAura extends Module {
    // target settings
    public final Setting<Priority> priority = register(new Setting<>("Priority", Priority.CLOSEST).setDescription("How to figure out your target"));
    public final NumberSetting targetRange = register(new NumberSetting("Target Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("The range to find a target"));
    public final Setting<Boolean> antiNaked = register(new Setting<>("AntiNaked", true).setDescription("If to not target naked players to save crystals"));
    public final Setting<Boolean> invisible = register(new Setting<>("Invisible", true).setDescription("If to target invisible players"));

    // place settings
    public final Setting<Boolean> placeCrystals = register(new Setting<>("Place", true).setDescription("If to automatically place crystals"));
    public final NumberSetting placeRange = register(new NumberSetting("Place Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("The range of which crystals will be placed"));
    public final NumberSetting placeDelay = register(new NumberSetting("Place Delay", 175.0f).setMin(0.0f).setMax(2500.0f).setDescription("The rate crystals will be placed at"));
    public final Setting<Boolean> multiplace = register(new Setting<>("Multiplace", false).setDescription("If to allow mulitple placement of crystals at once"));
    public final NumberSetting maxWasteAmount = register(new NumberSetting("Max Waste Amount", 0).setMin(0).setMax(10).setDescription("The maximum amount of crystals allowed to be placed at once"));
    public final Setting<Boolean> oneDotThirteen = register(new Setting<>("1.13", false).setDescription("If 1.13 placement of crystals is allowed"));
    public final Setting<Boolean> placeSwing = register(new Setting<>("Place Swing", true).setDescription("If to swing client side"));
    public final Setting<Rotation> placeRotation = register(new Setting<>("Place Rotation", Rotation.HEAD).setDescription("How to rotate yourself to place the crystal"));
    public final Setting<Boolean> raytrace = register(new Setting<>("Raytracing", false).setDescription("WIP - not done"));

    // break settings
    public final Setting<Boolean> breakCrystals = register(new Setting<>("Break", true).setDescription("If to automatically break crystals"));
    public final NumberSetting breakRange = register(new NumberSetting("Break Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("The range of which crystals will be detonated"));
    public final NumberSetting breakDelay = register(new NumberSetting("Break Delay", 175.0f).setMin(0.0f).setMax(2500.0f).setDescription("The rate crystals will be broken at"));
    public final Setting<Boolean> breakSwing = register(new Setting<>("Break Swing", true).setDescription("If to swing client side"));
    public final Setting<Rotation> breakRotation = register(new Setting<>("Place Rotation", Rotation.HEAD).setDescription("How to rotate yourself to break the crystal"));
    public final NumberSetting minDamage = register(new NumberSetting("Minimum Damage", 6.0f).setMin(0.0f).setMax(36.0f).setDescription("The minimum amount of damage the broken crystal has to do to the target"));
    public final NumberSetting maxSelfDamage = register(new NumberSetting("Max Self Damage", 36.0f).setMin(2.0f).setMax(36.0f).setDescription("The minimum amount of damage the broken crystal has to do to the target"));
    public final Setting<Boolean> antiFriendPop = register(new Setting<>("Anti Friend Pop", false).setDescription("The minimum amount of damage the broken crystal has to do to the target"));
    public final Setting<Boolean> extraCalculate = register(new Setting<>("Extra Calc", false).setDescription("If to calculate the damage taken from the crystal as a double check"));
    public final Setting<Boolean> antiSuicide = register(new Setting<>("Anti-Suicide", true).setDescription("If to calculate the damage taken from the crystal as a double check"));
    public final Setting<Boolean> doublePop = register(new Setting<>("Double Pop", true).setDescription("If to calculate the damage taken from the crystal as a double check"));
    public final Setting<Boolean> antiWeakness = register(new Setting<>("Anti-Weakness", false).setDescription("If to switch to a sword/axe under the \"Weakness\" status effect to break crystals"));
    public final Setting<Boolean> sync = register(new Setting<>("Sync", false).setDescription("If to sync crystal breaking using packets"));

    // render options
    // @todo

    // keep track of shit
    private EntityPlayer target = null;
    private BlockPos currentPos = null;
    private float currentDamage = 0.0f;
    private EnumHand crystalHand = EnumHand.MAIN_HAND;

    // double popping basically
    private final ArrayList<BlockPos> queuedPlace = new ArrayList<>();
    private final ArrayList<EntityEnderCrystal> queuedBreakCrystals = new ArrayList<>();

    private final Timer placeTimer = new Timer();
    private final Timer breakTimer = new Timer();

    // antiweakness
    private int slotBeforeTool = -1;

    @Override
    public String getDisplay() {
        if (target != null) {
            return target.getName();
        }

        return super.getDisplay();
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && currentPos != null) {
            RenderUtils.drawFilledBox(new AxisAlignedBB(currentPos).offset(RenderUtils.getCameraPos()), ColorUtil.toRGBA(255, 255, 255, 80));
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Eclipse.mc.player) {
            // check for a target
            if (target == null || !isValidCurrentTarget()) {
                EntityPlayer possibleNewTarget = findTarget();
                // if none, return
                if (possibleNewTarget == null) {
                    return;
                }

                target = possibleNewTarget;
            }

            if (!Eclipse.mc.player.getHeldItemOffhand().isEmpty() && Eclipse.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                crystalHand = EnumHand.OFF_HAND;
            } else {
                int slot = InventoryUtils.switchInHotbar(Items.END_CRYSTAL);
                if (slot == -1) {
                    return;
                }

                crystalHand = EnumHand.MAIN_HAND;
            }

            if (multiplace.getValue()) {
                doMultiPlace();
            } else {
                if (placeCrystals.getValue() && currentPos == null) {
                    calculateSinglePlace();

                    if (!placeTimer.passedMs(placeDelay.getValue().longValue())) {
                        return;
                    }

                    if (currentPos == null) {
                        placeTimer.reset();
                        currentDamage = 0.0f;
                        return;
                    }

                    placeTimer.reset();

                    if (placeRotation.getValue() != Rotation.NONE && currentPos != null) {
                        RotationUtils.rotate(currentPos, true);
                    }

                    placeCrystal(currentPos);
                }

                if (breakCrystals.getValue()) {
                    if (Eclipse.mc.player.isPotionActive(MobEffects.WEAKNESS) && antiWeakness.getValue()) {
                        doAntiWeakness();
                    }

                    if (!breakTimer.passedMs(breakDelay.getValue().longValue())) {
                        return;
                    }

                    breakTimer.reset();
                    hitCrystals();

                    if (slotBeforeTool != -1 && antiWeakness.getValue()) {
                        Eclipse.mc.player.inventory.currentItem = slotBeforeTool;
                        slotBeforeTool = -1;
                    }
                }

                currentPos = null;
                currentDamage = 0.0f;
            }
        }
    }

    private void placeCrystal(BlockPos pos) {
        if (placeSwing.getValue()) {
            WorldUtils.swingArm(crystalHand);
        }

        WorldUtils.place(pos, crystalHand, placeSwing.getValue(), false);
    }

    private void hitCrystals() {
        List<EntityEnderCrystal> enderCrystals = mapOutCrystals();
        if (!enderCrystals.isEmpty()) {
            // time to go
            loop: for (EntityEnderCrystal crystal : enderCrystals) {
                if (extraCalculate.getValue()) {
                    BlockPos pos = crystal.getPosition();

                    float selfDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Eclipse.mc.player);
                    if (antiSuicide.getValue()) {
                        if (selfDamage - 0.5f >= maxSelfDamage.getValue().floatValue() || DamageUtils.willPopTotem(Eclipse.mc.player, selfDamage - 0.5f)) {
                            continue;
                        }
                    }

                    float targetDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, target);
                    if (selfDamage >= targetDamage || targetDamage < minDamage.getValue().floatValue()) {
                        continue;
                    }

                    if (antiFriendPop.getValue()) {
                        for (Friend friend : Eclipse.friendManager.getFriends()) {
                            EntityPlayer friendPlayer = Eclipse.serverManager.getPlayer(friend.getUuid());
                            if (friendPlayer == null) {
                                continue;
                            }

                            float friendDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Eclipse.mc.player);
                            if (friendDamage >= targetDamage || friendDamage >= EntityUtil.getTotalHealth(friendPlayer) || DamageUtils.willPopTotem(friendPlayer, friendDamage)) {
                                continue loop;
                            }
                        }
                    }
                }

                if (breakRotation.getValue() != Rotation.NONE) {
                    RotationUtils.rotate(crystal.getPosition().add(0, 0.5, 0), true);
                }

                if (sync.getValue()) {
                    Eclipse.mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                } else {
                    Eclipse.mc.playerController.attackEntity(Eclipse.mc.player, crystal);
                }

                if (breakSwing.getValue()) {
                    WorldUtils.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    private void doMultiPlace() {
        // @todo
    }

    private void calculateSinglePlace() {
        ArrayList<Pair<BlockPos, Float>> positions = new ArrayList<>();
        List<BlockPos> crystalPlacePositions = WorldUtils.getCrystalPlacePositions(Eclipse.mc.player.getPositionVector(), placeRange.getValue().intValue(), oneDotThirteen.getValue());

        // if double pop positions were found
        // @todo

        if (!crystalPlacePositions.isEmpty()) {
            loop: for (BlockPos pos : crystalPlacePositions) {
                // check if we place a crystal there if it will pop us
                float selfDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Eclipse.mc.player);
                // if the self damage is greater than the max damage allowed to be taken or we'll pop a totem, don't consider it
                if (antiSuicide.getValue()) {
                    if (selfDamage - 0.5f >= maxSelfDamage.getValue().floatValue() || DamageUtils.willPopTotem(Eclipse.mc.player, selfDamage - 0.5f)) {
                        continue;
                    }
                }

                // check for target damage
                float targetDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, target);
                if (selfDamage >= targetDamage || targetDamage < minDamage.getValue().floatValue()) {
                    continue;
                }

                // @todo check for double pops

                if (antiFriendPop.getValue()) {
                    for (Friend friend : Eclipse.friendManager.getFriends()) {
                        EntityPlayer friendPlayer = Eclipse.serverManager.getPlayer(friend.getUuid());
                        if (friendPlayer == null) {
                            continue;
                        }

                        float friendDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Eclipse.mc.player);
                        if (friendDamage >= targetDamage || friendDamage >= EntityUtil.getTotalHealth(friendPlayer) || DamageUtils.willPopTotem(friendPlayer, friendDamage)) {
                            continue loop;
                        }
                    }
                }

                positions.add(new Pair<>(pos, targetDamage));
            }
        }

        // @todo check for double pops found in the loop
        if (!positions.isEmpty()) {
            // sort by greatest damage
            positions.sort(Comparator.comparingDouble(Pair::getValue));
            // reverse cause java hates us
            Collections.reverse(positions);

            // get the first pos, and use it
            Pair<BlockPos, Float> placePos = positions.get(0);

            // set values;
            currentPos = placePos.getKey();
            currentDamage = placePos.getValue();
        } else {
            currentPos = null;
            currentDamage = 0.0f;
        }
    }

    private void doAntiWeakness() {
        int old = InventoryUtils.switchInHotbar(ItemSword.class);
        if (old == -1) {
            // attempt to a axe
            old = InventoryUtils.switchInHotbar(ItemAxe.class);
            if (old == -1) {
                return;
            }
        }

        slotBeforeTool = old;
    }

    private List<EntityEnderCrystal> mapOutCrystals() {
        return Eclipse.mc.world.getEntities(EntityEnderCrystal.class, (crystal) -> Eclipse.mc.player.getDistance(crystal) <= breakRange.getValue().floatValue());
    }

    private EntityPlayer findTarget() {
        EntityUtil.Sorting sorting = priority.getValue() == Priority.CLOSEST ? EntityUtil.Sorting.NONE : EntityUtil.Sorting.LOW_HEALTH;
        return EntityUtil.findTarget(targetRange.getValue().floatValue(), invisible.getValue(), antiNaked.getValue(), false, sorting);
    }

    private boolean isValidCurrentTarget() {
        return target != null && !target.isDead && Eclipse.mc.player.getDistance(target) < targetRange.getValue().floatValue();
    }

    public boolean shouldPause() {
        return (Eclipse.moduleManager.<Module>getModule(AutoBreaker.class).isToggled() && AutoBreaker.isBreaking);
    }

    public enum Priority {
        CLOSEST,
        HEALTH
    }

    public enum Rotation {
        BODY,
        HEAD,
        NONE
    }
}
