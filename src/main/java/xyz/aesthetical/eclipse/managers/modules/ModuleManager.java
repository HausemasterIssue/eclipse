package xyz.aesthetical.eclipse.managers.modules;

import baritone.api.BaritoneAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import xyz.aesthetical.eclipse.config.ModuleConfiguration;
import xyz.aesthetical.eclipse.events.eclipse.ModuleToggledEvent;
import xyz.aesthetical.eclipse.features.modules.client.*;
import xyz.aesthetical.eclipse.features.modules.combat.*;
import xyz.aesthetical.eclipse.features.modules.exploits.*;
import xyz.aesthetical.eclipse.features.modules.miscellaneous.*;
import xyz.aesthetical.eclipse.features.modules.movement.*;
import xyz.aesthetical.eclipse.features.modules.render.*;
import xyz.aesthetical.eclipse.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final ArrayList<Module> modules = new ArrayList<>();
    private final Logger LOGGER = LogManager.getLogger(ModuleManager.class);

    private ModuleConfiguration configuration;

    public ModuleManager() {
        // client
        modules.add(new ClickGUI());
        modules.add(new CustomFont());
        modules.add(new DiscordRPC());
        modules.add(new HUD());
        modules.add(new HudEditor());
        modules.add(new Notifications());
        modules.add(new Safety());
        // modules.add(new Test());

        // combat
        modules.add(new AutoBreaker());
        modules.add(new AutoTotem());
        modules.add(new Burrow());
        modules.add(new Criticals());
        modules.add(new CrystalAura());
        modules.add(new HoleFiller());
        modules.add(new Surround());
        modules.add(new WebAura());

        // exploits
        modules.add(new MountBypass());
        modules.add(new NewChunks());
        modules.add(new NoHandshake());
        modules.add(new Reach());
        modules.add(new ThunderHack());
        modules.add(new Timer());
        modules.add(new XCarry());

        // misc
        modules.add(new AutoTunnel());
        modules.add(new Avoid());
        modules.add(new FakePlayer());
        modules.add(new FastUse());
        modules.add(new LiquidInteract());
        modules.add(new MiddleClickPearl());
        modules.add(new NoSwing());
        modules.add(new Time());
        modules.add(new VisualRange());

        // movement
        modules.add(new AutoWalk());
        modules.add(new EntityControl());
        modules.add(new IceSpeed());
        modules.add(new Jesus());
        modules.add(new NoSlow());
        modules.add(new ReverseStep());
        modules.add(new Scaffold());
        modules.add(new Sprint());
        modules.add(new Velocity());

        // render
        modules.add(new Breadcrumbs());
        modules.add(new Brightness());
        modules.add(new CameraClip());
        modules.add(new Chams());
        modules.add(new CustomFOV());
        modules.add(new ESP());
        modules.add(new HoleESP());
        modules.add(new NoRender());
        modules.add(new ViewModel());
        modules.add(new Yaw());

        LOGGER.info("Loaded {} modules, registered ModuleManager to the forge EventBus", modules.size());
    }

    public ModuleManager init() {
        configuration = new ModuleConfiguration();
        return this;
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onInput(InputEvent.KeyInputEvent event) {
        int key = Keyboard.getEventKey();
        if (!Keyboard.getEventKeyState() && key != Keyboard.KEY_NONE) {
            for (Module module : modules) {
                if (module.getBind() == key) {
                    module.toggle();
                    LOGGER.info("{} module {}", module.isToggled() ? "Enabled" : "Disabled", module.getName());

                    MinecraftForge.EVENT_BUS.post(new ModuleToggledEvent(module));
                }
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Module.fullNullCheck()) {
            BaritoneAPI.getSettings().allowSprint.value = getModule(Sprint.class).isToggled();
            BaritoneAPI.getSettings().assumeWalkOnWater.value = getModule(Jesus.class).isToggled() && this.<Jesus>getModule(Jesus.class).water.getValue();
            BaritoneAPI.getSettings().assumeWalkOnLava.value = getModule(Jesus.class).isToggled() && this.<Jesus>getModule(Jesus.class).lava.getValue();
        }
    }

    public <T extends Module> T getModule(Class<? extends Module> clazz) {
        for (Module module : modules) {
            if (clazz.isInstance(module)) {
                return (T) module;
            }
        }

        return null;
    }

    public <T extends Module> T getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return (T) module;
            }
        }

        return null;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public List<Module> getEnabledModules() {
        return modules.stream().filter(Module::isToggled).collect(Collectors.toList());
    }

    public ArrayList<Pair<String, List<Module>>> getModulesSortedByCategory() {
        ArrayList<Pair<String, List<Module>>> list = new ArrayList<>();

        for (String category : Arrays.stream(Module.Category.values()).map(Module.Category::getDisplay).sorted().collect(Collectors.toList())) {
            ArrayList<Module> mods = new ArrayList<>();
            for (Module module : modules) {
                if (module.getCategory().getDisplay().equalsIgnoreCase(category)) {
                    mods.add(module);
                }
            }

            if (mods.isEmpty()) {
                continue;
            }

            list.add(new Pair<>(category, mods));
        }

        return list;
    }
}
