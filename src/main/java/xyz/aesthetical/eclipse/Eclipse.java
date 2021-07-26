package xyz.aesthetical.eclipse;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import xyz.aesthetical.eclipse.events.ForgeEventProcessor;
import xyz.aesthetical.eclipse.events.client.ShutdownEvent;
import xyz.aesthetical.eclipse.features.modules.render.Brightness;
import xyz.aesthetical.eclipse.features.modules.render.CustomFOV;
import xyz.aesthetical.eclipse.managers.*;
import xyz.aesthetical.eclipse.managers.commands.CommandManager;
import xyz.aesthetical.eclipse.managers.friends.FriendManager;
import xyz.aesthetical.eclipse.managers.macros.MacroManager;
import xyz.aesthetical.eclipse.managers.modules.ModuleManager;
import xyz.aesthetical.eclipse.managers.notifications.NotificationManager;

@Mod(modid = Eclipse.MOD_ID, name = Eclipse.MOD_NAME, version = Eclipse.MOD_VERSION)
public class Eclipse {
    public static final String MOD_ID = "eclipse";
    public static final String MOD_NAME = "Eclipse";
    public static final String MOD_VERSION = "b0.1-1.12.2";

    @Mod.Instance
    public static Eclipse instance;

    public static final Logger LOGGER = LogManager.getLogger(Eclipse.class);
    public static Minecraft mc;

    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static TextManager textManager;
    public static ServerManager serverManager;
    public static SafetyManager safetyManager;
    public static HoleManager holeManager;
    public static NotificationManager notificationManager;
    public static FriendManager friendManager;
    public static MacroManager macroManager;
    public static XrayManager xrayManager;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        LOGGER.info("Loading AesthetiHack (eclipse) v{}", MOD_VERSION);

        FileManager fileManager = FileManager.getInstance();
        if (!fileManager.exists(fileManager.getClientFolder())) {
            fileManager.mkDir(fileManager.getClientFolder(), false);
            LOGGER.info("Created new client config folder at " + fileManager.getClientFolder());
        }

        Display.setTitle("AesthetiHack v" + MOD_VERSION);

        mc = Minecraft.getMinecraft();

        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        textManager = new TextManager();
        serverManager = new ServerManager();
        safetyManager = new SafetyManager();
        holeManager = new HoleManager();
        notificationManager = new NotificationManager();
        friendManager = new FriendManager();
        macroManager = new MacroManager();
        xrayManager = new XrayManager();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ForgeEventProcessor());
        MinecraftForge.EVENT_BUS.register(moduleManager);
        MinecraftForge.EVENT_BUS.register(commandManager);
        MinecraftForge.EVENT_BUS.register(serverManager);
        MinecraftForge.EVENT_BUS.register(holeManager);
        MinecraftForge.EVENT_BUS.register(notificationManager);

        LOGGER.info("Loaded and registered all listeners! Welcome to AesthetiHack :^)");
    }

    @SubscribeEvent
    public void onShutdown(ShutdownEvent event) {
        LOGGER.info("Shutting down AesthtiHack v{}", MOD_VERSION);

        Eclipse.mc.gameSettings.gammaSetting = Brightness.oldGamma;
        Eclipse.mc.gameSettings.fovSetting = CustomFOV.oldFov;

        LOGGER.info("Saving manager states...");

        moduleManager.getConfiguration().shutdown();
    }
}