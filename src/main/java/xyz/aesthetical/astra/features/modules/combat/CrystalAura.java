package xyz.aesthetical.astra.features.modules.combat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.TotemPopEvent;
import xyz.aesthetical.astra.events.render.RenderEvent;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.commands.text.ChatColor;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.*;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Module.Mod(name = "CrystalAura", description = "Automatically places and detonates end crystals")
@Module.Info(category = Module.Category.COMBAT)
public class CrystalAura extends Module {
    public final Setting<Menu> menu = register(new Setting<>("Menu", Menu.TARGET).setDescription("What setting menu of CrystalAura to manage"));

    // target settings
    public final Setting<Priority> priority = register(new Setting<>("Priority", Priority.CLOSEST).setDescription("How to figure out your target").setVisibility((m) -> menu.getValue() == Menu.TARGET));
    public final NumberSetting targetRange = register(new NumberSetting("Target Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("The range to find a target").setVisibility((m) -> menu.getValue() == Menu.TARGET));
    public final Setting<Boolean> antiNaked = register(new Setting<>("AntiNaked", true).setDescription("If to not target naked players to save crystals").setVisibility((m) -> menu.getValue() == Menu.TARGET));
    public final Setting<Boolean> invisible = register(new Setting<>("Invisible", true).setDescription("If to target invisible players").setVisibility((m) -> menu.getValue() == Menu.TARGET));
    public final Setting<Boolean> targetFriends = register(new Setting<>("Target Friends", false).setDescription("If to target friends"));

    // place settings
    public final Setting<Boolean> placeCrystals = register(new Setting<>("Place", true).setDescription("If to automatically place crystals").setVisibility((m) -> menu.getValue() == Menu.PLACE));
    public final NumberSetting placeRange = register(new NumberSetting("Place Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("The range of which crystals will be placed").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final NumberSetting placeDelay = register(new NumberSetting("Place Delay", 175.0f).setMin(0.0f).setMax(2500.0f).setDescription("The rate crystals will be placed at").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final Setting<Boolean> multiplace = register(new Setting<>("Multiplace", false).setDescription("If to allow mulitple placement of crystals at once").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final NumberSetting maxWasteAmount = register(new NumberSetting("Max Waste Amount", 0).setMin(0).setMax(10).setDescription("The maximum amount of crystals allowed to be placed at once").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue() && multiplace.getValue()));
    public final Setting<Boolean> oneDotThirteen = register(new Setting<>("1.13", false).setDescription("If 1.13 placement of crystals is allowed").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final Setting<Boolean> placeSwing = register(new Setting<>("Place Swing", true).setDescription("If to swing client side").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final Setting<Rotation> placeRotation = register(new Setting<>("Place Rotation", Rotation.HEAD).setDescription("How to rotate yourself to place the crystal").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final Setting<Boolean> multiPop = register(new Setting<>("Multi Pop", false).setDescription("If to find chain pops").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final NumberSetting multiPopDelay = register(new NumberSetting("Multi Pop Delay", 115.0f).setMin(0.0f).setMax(2000.0f).setDescription("How long to wait in MS before attempting to multi pop").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue() && multiPop.getValue()));
    public final NumberSetting multiPopDamage = register(new NumberSetting("Multi Pop Damage", 5.0f).setMin(1.0f).setMax(36.0f).setDescription("The minimum amount of damage the multi pop has to do").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue() && multiPop.getValue()));

    // break settings
    public final Setting<Boolean> breakCrystals = register(new Setting<>("Break", true).setDescription("If to automatically break crystals").setVisibility((m) -> menu.getValue() == Menu.BREAK));
    public final NumberSetting breakRange = register(new NumberSetting("Break Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("The range of which crystals will be detonated").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final NumberSetting breakDelay = register(new NumberSetting("Break Delay", 175.0f).setMin(0.0f).setMax(2500.0f).setDescription("The rate crystals will be broken at").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final Setting<Boolean> breakSwing = register(new Setting<>("Break Swing", true).setDescription("If to swing client side").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final Setting<Rotation> breakRotation = register(new Setting<>("Place Rotation", Rotation.HEAD).setDescription("How to rotate yourself to break the crystal").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final NumberSetting minDamage = register(new NumberSetting("Minimum Damage", 6.0f).setMin(0.0f).setMax(36.0f).setDescription("The minimum amount of damage the broken crystal has to do to the target").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final NumberSetting maxSelfDamage = register(new NumberSetting("Max Self Damage", 36.0f).setMin(2.0f).setMax(36.0f).setDescription("The minimum amount of damage the broken crystal has to do to the target").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final Setting<Boolean> antiFriendPop = register(new Setting<>("Anti Friend Pop", false).setDescription("The minimum amount of damage the broken crystal has to do to the target").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final Setting<Boolean> extraCalculate = register(new Setting<>("Extra Calc", false).setDescription("If to calculate the damage taken from the crystal as a double check").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final Setting<Boolean> antiSuicide = register(new Setting<>("Anti-Suicide", true).setDescription("If to calculate the damage taken from the crystal as a double check").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final Setting<Boolean> antiWeakness = register(new Setting<>("Anti-Weakness", false).setDescription("If to switch to a sword/axe under the \"Weakness\" status effect to break crystals").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));
    public final Setting<Boolean> sync = register(new Setting<>("Sync", false).setDescription("If to sync crystal breaking using packets").setVisibility((m) -> menu.getValue() == Menu.BREAK && breakCrystals.getValue()));

    // render options
    public final Setting<Boolean> renderPlacePos = register(new Setting<>("Place Pos", true).setDescription("If to render the place position of the crystal").setVisibility((m) -> menu.getValue() == Menu.RENDER));
    public final Setting<Color> renderPlacePosColor = register(new Setting<>("Place Pos Color", new Color(255, 255, 255)).setDescription("The color to render the place pos").setVisibility((m) -> menu.getValue() == Menu.RENDER && renderPlacePos.getValue()));
    public final Setting<Boolean> renderMultiPop = register(new Setting<>("Multi Pop", true).setDescription("If to render a different color for the multi pop position").setVisibility((m) -> menu.getValue() == Menu.RENDER && renderPlacePos.getValue()));
    public final Setting<Color> renderMultiPopColor = register(new Setting<>("Place Pos Color", new Color(0, 255, 0)).setDescription("The color to render the multi pop pos").setVisibility((m) -> menu.getValue() == Menu.RENDER && renderPlacePos.getValue() && renderMultiPop.getValue()));
    public final Setting<Boolean> renderDamage = register(new Setting<>("Render Damage", true).setDescription("If to render the current damage").setVisibility((m) -> menu.getValue() == Menu.RENDER));

    // keep track of shit
    private EntityPlayer target = null;
    private BlockPos currentPos = null;
    private float currentDamage = 0.0f;
    private EnumHand hand = EnumHand.MAIN_HAND;

    // queues and whatever, i'll get to this whenever

    // multi pop
    private boolean canMultiPop = false;
    private final Map<EntityPlayer, Timer> pops = new ConcurrentHashMap<>();

    private final Timer placeTimer = new Timer();
    private final Timer breakTimer = new Timer();

    @Override
    public String getDisplay() {
        if (target != null) {
            return target.getName();
        }

        return super.getDisplay();
    }

    @Override
    public void onEnabled() {
        reset();
    }

    @Override
    public void onDisabled() {
        reset();
    }

    @SubscribeEvent
    public void onRender(RenderEvent event) {
        if (Module.fullNullCheck() && currentPos != null) {
            if (renderPlacePos.getValue()) {
                Color c = renderMultiPop.getValue() && canMultiPop ? renderMultiPopColor.getValue() : renderPlacePosColor.getValue();
                RenderUtils.drawFilledBox(new AxisAlignedBB(currentPos).offset(RenderUtils.getCameraPos()), ColorUtils.toRGBA(c.getRed(), c.getGreen(), c.getBlue(), 80));
            }

            if (renderDamage.getValue()) {
                GlStateManager.pushMatrix();
                Vec3d interpolated = RenderUtils.interpolateVec(new Vec3d(currentPos.getX(), currentPos.getY(), currentPos.getZ())).add(RenderUtils.getCameraPos());
                RenderUtils.drawTag(new BlockPos(interpolated.x, interpolated.y, interpolated.z), String.format("%.3f%n", currentDamage), 0.0, 0.0f);
                GlStateManager.popMatrix();
            }
        }
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (Module.fullNullCheck() && event.getPlayer() != Astra.mc.player && event.getPlayer() == target) {
            pops.put(event.getPlayer(), new Timer().reset());
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player && !shouldPause()) {
            // find a target
            if (target == null || isValidCurrentTarget()) {
                target = findTarget();
                if (target == null) {
                    return;
                }
            }

            // if no crystals were found
            if (!crystalCheck()) {
                return;
            }

            doCrystalAura();
        }
    }

    private void doCrystalAura() {
        // @todo multiplace, we'll deal with single place right now
        if (placeCrystals.getValue()) {
            calculate(); // do calculations
            if (currentPos != null && currentDamage != 0.0f) {
                if (!placeTimer.passedMs(placeDelay.getValue().longValue())) {
                    // @todo queue the pos
                    return;
                }

                placeTimer.reset();

                doPlace(currentPos);
            }
        }

        if (breakCrystals.getValue()) {
            if (!breakTimer.passedMs(breakDelay.getValue().longValue())) {
                // @todo queue the crystal
                return;
            }

            breakTimer.reset();

            // @todo we should have a setting if they should "Always" break or Calc break. kinda how phobos does
            // for now we'll check if the current pos can be used to break that single crystal

            for (EntityEnderCrystal crystal : getSurroundingCrystals()) {
                if (extraCalculate.getValue()) {
                    // @todo
                }

                // @todo shouldn't do the below
                doBreak(crystal);
            }
        }
    }

    private void calculate() {
        // get all possible place positions
        final List<BlockPos> placePositions = WorldUtils.getCrystalPlacePositions(Astra.mc.player.getPositionVector(), placeRange.getValue().intValue(), oneDotThirteen.getValue());

        // if placePositions is not empty, we'll look through it until we find a decent place position
        if (!placePositions.isEmpty()) {
            // have a few vars for multipop positions
            BlockPos popPos = null;
            float popMaxDmg = -1.0f;

            // normal place positions
            BlockPos placePos = null;
            float placeMaxDmg = -1.0f;

            // we need a label here as we need another loop in here, line
            positions:
            for (BlockPos pos : placePositions) {
                // the location we need to calculate place position damage
                double x = pos.getX() + 0.5, y = pos.getY() + 1.0, z = pos.getZ() + 0.5;

                // get the damage for us, and if antiSucide is on, make sure we wont commit sodoku
                float selfDmg = DamageUtils.calculateDamage(x, y, z, Astra.mc.player) + 0.5f;
                // if our damage is greater than the set maximum self damage we can take from that crystal, or the damage is greater than or equal to our health, tell it to go fuck itself
                if (antiSuicide.getValue() && (selfDmg >= maxSelfDamage.getValue().floatValue() || selfDmg >= EntityUtil.getTotalHealth(Astra.mc.player))) {
                    continue;
                }

                float targetDmg = DamageUtils.calculateDamage(x, y, z, target);
                // if the targets damage is less than the minimum damage, or our damage is greater than our targets damage, dont consider as a place pos
                if (targetDmg < minDamage.getValue().floatValue() || selfDmg > targetDmg) {
                    continue;
                }

                // if anti friend popping is on, let's make sure we wont shit on our friends
                // @todo a "just do it" setting like phobos has if the place pos is REALLY good but something is blocking it, use it anyway?
                if (antiFriendPop.getValue() && !Astra.friendManager.getFriends().isEmpty()) {
                    for (EntityPlayer friend : Astra.mc.world.getPlayers(EntityPlayer.class, (player) -> !Astra.friendManager.isFriend(player.getUniqueID()))) {
                        if (friend == null || friend == Astra.mc.player) {
                            continue;
                        }

                        float friendDmg = DamageUtils.calculateDamage(x, y, z, friend);
                        if (friendDmg > targetDmg || friendDmg <= EntityUtil.getTotalHealth(friend)) {
                            continue positions;
                        }
                    }
                }

                if (multiPop.getValue() && canMultiPop(targetDmg) && targetDmg > popMaxDmg) {
                    popPos = pos;
                    popMaxDmg = targetDmg;
                }

                if (targetDmg > placeMaxDmg) {
                    placePos = pos;
                    placeMaxDmg = targetDmg;
                }
            }

            if (popMaxDmg != -1.0f && popMaxDmg > placeMaxDmg) {
                currentPos = popPos;
                currentDamage = popMaxDmg;
                canMultiPop = true;
                return;
            }

            if (placeMaxDmg != -1.0f) {
                currentPos = placePos;
                currentDamage = placeMaxDmg;
                canMultiPop = false;
                return;
            }
        }

        currentPos = null;
        currentDamage = 0.0f;
    }

    private boolean crystalCheck() {
        if (Astra.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            hand = EnumHand.OFF_HAND;
            return true;
        } else {
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = Astra.mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.END_CRYSTAL) {
                    Astra.mc.player.inventory.currentItem = i;
                    hand = EnumHand.MAIN_HAND;
                    return true;
                }
            }
        }

        return false;
    }

    private void doPlace(BlockPos pos) {
        if (placeRotation.getValue() != Rotation.NONE) {
            RotationUtils.rotate(pos, true);
        }

        WorldUtils.place(pos, hand, placeSwing.getValue(), false, sync.getValue());
    }

    private void doBreak(EntityEnderCrystal crystal) {
        if (breakRotation.getValue() != Rotation.NONE) {
            RotationUtils.rotate(crystal.getPosition(), true);
        }

        if (sync.getValue()) {
            Astra.mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
        } else {
            Astra.mc.playerController.attackEntity(Astra.mc.player, crystal);
        }

        if (breakSwing.getValue()) {
            WorldUtils.swingArm(EnumHand.MAIN_HAND);
        }
    }

    private List<EntityEnderCrystal> getSurroundingCrystals() {
        return Astra.mc.world.getEntities(EntityEnderCrystal.class, (crystal) -> Astra.mc.player.getDistance(crystal) <= breakRange.getValue().floatValue());
    }

    private EntityPlayer findTarget() {
        EntityUtil.Sorting sorting = priority.getValue() == Priority.CLOSEST ? EntityUtil.Sorting.NONE : EntityUtil.Sorting.LOW_HEALTH;
        return EntityUtil.findTarget(targetRange.getValue().floatValue(), invisible.getValue(), antiNaked.getValue(), false, sorting);
    }

    private boolean isValidCurrentTarget() {
        return target != null && !target.isDead && Astra.mc.player.getDistance(target) < targetRange.getValue().floatValue();
    }

    private boolean canMultiPop(float damage) {
        if (damage > multiPopDamage.getValue().floatValue()) {
            Timer timer = pops.get(target);
            return timer != null && timer.passedMs(multiPopDelay.getValue().longValue());
        }

        return false;
    }

    public boolean shouldPause() {
        return (Astra.moduleManager.getModule(AutoBreaker.class).isToggled() && AutoBreaker.isBreaking);
    }

    private void reset() {
        target = null;
        currentPos = null;
        currentDamage = 0.0f;
        hand = EnumHand.MAIN_HAND;

        canMultiPop = false;
        pops.clear();

        placeTimer.reset();
        breakTimer.reset();
    }

    public enum Menu {
        TARGET,
        PLACE,
        BREAK,
        RENDER
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
