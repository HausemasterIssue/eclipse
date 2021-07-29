package xyz.aesthetical.astra.features.modules.combat;

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
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.entity.TotemPopEvent;
import xyz.aesthetical.astra.events.render.RenderEvent;
import xyz.aesthetical.astra.features.settings.NumberSetting;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.friends.Friend;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.util.*;
import xyz.aesthetical.astra.util.Timer;

import java.awt.*;
import java.util.*;
import java.util.List;
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

    // place settings
    public final Setting<Boolean> placeCrystals = register(new Setting<>("Place", true).setDescription("If to automatically place crystals").setVisibility((m) -> menu.getValue() == Menu.PLACE));
    public final NumberSetting placeRange = register(new NumberSetting("Place Range", 5.0f).setMin(2.0f).setMax(7.0f).setDescription("The range of which crystals will be placed").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final NumberSetting placeDelay = register(new NumberSetting("Place Delay", 175.0f).setMin(0.0f).setMax(2500.0f).setDescription("The rate crystals will be placed at").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final Setting<Boolean> multiplace = register(new Setting<>("Multiplace", false).setDescription("If to allow mulitple placement of crystals at once").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final NumberSetting maxWasteAmount = register(new NumberSetting("Max Waste Amount", 0).setMin(0).setMax(10).setDescription("The maximum amount of crystals allowed to be placed at once").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue() && multiplace.getValue()));
    public final Setting<Boolean> oneDotThirteen = register(new Setting<>("1.13", false).setDescription("If 1.13 placement of crystals is allowed").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final Setting<Boolean> placeSwing = register(new Setting<>("Place Swing", true).setDescription("If to swing client side").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    public final Setting<Rotation> placeRotation = register(new Setting<>("Place Rotation", Rotation.HEAD).setDescription("How to rotate yourself to place the crystal").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
    // @todo public final Setting<Boolean> raytrace = register(new Setting<>("Raytracing", false).setDescription("WIP - not done").setVisibility((m) -> menu.getValue() == Menu.PLACE && placeCrystals.getValue()));
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
    private EnumHand crystalHand = EnumHand.MAIN_HAND;

    // multi pop
    private boolean canMultiPop = false;
    private final Map<EntityPlayer, Timer> pops = new ConcurrentHashMap<>();

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
                RenderUtils.drawFilledBox(new AxisAlignedBB(currentPos).offset(RenderUtils.getCameraPos()), ColorUtil.toRGBA(c.getRed(), c.getGreen(), c.getBlue(), 80));
            }

            if (renderDamage.getValue()) {
                // do a text render shitter thing
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
        if (Module.fullNullCheck() && event.getEntityLiving() == Astra.mc.player) {
            // check for a target
            if (target == null || !isValidCurrentTarget()) {
                EntityPlayer possibleNewTarget = findTarget();
                // if none, return
                if (possibleNewTarget == null) {
                    return;
                }

                target = possibleNewTarget;
            }

            if (!Astra.mc.player.getHeldItemOffhand().isEmpty() && Astra.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
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
                    if (Astra.mc.player.isPotionActive(MobEffects.WEAKNESS) && antiWeakness.getValue()) {
                        doAntiWeakness();
                    }

                    if (!breakTimer.passedMs(breakDelay.getValue().longValue())) {
                        return;
                    }

                    breakTimer.reset();
                    hitCrystals();

                    if (slotBeforeTool != -1 && antiWeakness.getValue()) {
                        Astra.mc.player.inventory.currentItem = slotBeforeTool;
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

                    float selfDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Astra.mc.player);
                    if (antiSuicide.getValue()) {
                        if (selfDamage - 0.5f >= maxSelfDamage.getValue().floatValue() || DamageUtils.willPopTotem(Astra.mc.player, selfDamage - 0.5f)) {
                            continue;
                        }
                    }

                    float targetDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, target);
                    if (selfDamage >= targetDamage || targetDamage < minDamage.getValue().floatValue()) {
                        continue;
                    }

                    if (antiFriendPop.getValue()) {
                        for (Friend friend : Astra.friendManager.getFriends()) {
                            EntityPlayer friendPlayer = Astra.serverManager.getPlayer(friend.getUuid());
                            if (friendPlayer == null) {
                                continue;
                            }

                            float friendDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Astra.mc.player);
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
                    Astra.mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                } else {
                    Astra.mc.playerController.attackEntity(Astra.mc.player, crystal);
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
        List<BlockPos> crystalPlacePositions = WorldUtils.getCrystalPlacePositions(Astra.mc.player.getPositionVector(), placeRange.getValue().intValue(), oneDotThirteen.getValue());

        BlockPos suggestedMultiPop = null;
        float multiPopDamage = -1.0f;

        canMultiPop = false;

        // if double pop positions were found
        // @todo

        if (!crystalPlacePositions.isEmpty()) {
            loop: for (BlockPos pos : crystalPlacePositions) {
                // check if we place a crystal there if it will pop us
                float selfDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Astra.mc.player);
                // if the self damage is greater than the max damage allowed to be taken or we'll pop a totem, don't consider it
                if (antiSuicide.getValue()) {
                    if (selfDamage - 0.5f >= maxSelfDamage.getValue().floatValue() || DamageUtils.willPopTotem(Astra.mc.player, selfDamage - 0.5f)) {
                        continue;
                    }
                }

                // check for target damage
                float targetDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, target);
                if (selfDamage >= targetDamage || targetDamage < minDamage.getValue().floatValue()) {
                    continue;
                }

                if (multiPop.getValue() && canMultiPop(targetDamage)) {
                    currentDamage = targetDamage;
                    suggestedMultiPop = pos;
                    multiPopDamage = targetDamage;
                    canMultiPop = true;

                    continue;
                }

                if (antiFriendPop.getValue()) {
                    for (Friend friend : Astra.friendManager.getFriends()) {
                        EntityPlayer friendPlayer = Astra.serverManager.getPlayer(friend.getUuid());
                        if (friendPlayer == null) {
                            continue;
                        }

                        float friendDamage = DamageUtils.calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Astra.mc.player);
                        if (friendDamage >= targetDamage || friendDamage >= EntityUtil.getTotalHealth(friendPlayer) || DamageUtils.willPopTotem(friendPlayer, friendDamage)) {
                            continue loop;
                        }
                    }
                }

                if ((suggestedMultiPop != null && multiPopDamage != -1.0f) && (targetDamage > multiPopDamage || selfDamage >= multiPopDamage)) {
                    continue;
                }

                positions.add(new Pair<>(pos, targetDamage));
            }
        }

        if (suggestedMultiPop != null && multiPopDamage != -1.0f) {
            currentPos = suggestedMultiPop;
            currentDamage = multiPopDamage;

            return;
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
        return Astra.mc.world.getEntities(EntityEnderCrystal.class, (crystal) -> Astra.mc.player.getDistance(crystal) <= breakRange.getValue().floatValue());
    }

    private EntityPlayer findTarget() {
        EntityUtil.Sorting sorting = priority.getValue() == Priority.CLOSEST ? EntityUtil.Sorting.NONE : EntityUtil.Sorting.LOW_HEALTH;
        return EntityUtil.findTarget(targetRange.getValue().floatValue(), invisible.getValue(), antiNaked.getValue(), false, sorting);
    }

    private boolean isValidCurrentTarget() {
        return target != null && !target.isDead && Astra.mc.player.getDistance(target) < targetRange.getValue().floatValue();
    }

    // we can assume the target is well, the target set as a field here
    private boolean canMultiPop(float damage) {
        damage -= 0.5f;
        // the extraCalc thing doesn't work as intended, check this later @todo
        if (damage > multiPopDamage.getValue().floatValue() && (extraCalculate.getValue() && damage > EntityUtil.getTotalHealth(target))) {
            Timer timer = pops.get(target);
            return timer == null || timer.passedMs(multiPopDelay.getValue().longValue());
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
        crystalHand = EnumHand.MAIN_HAND;

        canMultiPop = false;
        pops.clear();

        placeTimer.reset();
        breakTimer.reset();

        slotBeforeTool = -1;
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
