package xyz.aesthetical.astra;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.gui.GuiMainMenu;

public class RPC {
    public static DiscordRichPresence presence = new DiscordRichPresence();
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;
    private static Thread thread;

    public static void run() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize("865433028200497203", handlers, true, "");

        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.details = getLocation();
        presence.largeImageKey = "1";
        presence.largeImageText = "hi";

        if (Astra.MOD_VERSION.startsWith("b") || Astra.MOD_VERSION.startsWith("d")) {
            presence.state = Astra.MOD_NAME + " Client - In Development Environment";
        } else {
            presence.state = Astra.MOD_NAME + " Client " + Astra.MOD_VERSION;
        }

        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();

                presence.details = getLocation();
                presence.largeImageKey = "1";
                presence.largeImageText = "hi";

                if (Astra.MOD_VERSION.startsWith("b") || Astra.MOD_VERSION.startsWith("d")) {
                    presence.state = Astra.MOD_NAME + " Client - In Development Environment";
                } else {
                    presence.state = Astra.MOD_NAME + " Client " + Astra.MOD_VERSION;
                }

                rpc.Discord_UpdatePresence(presence);

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException ignored) { }
            }
        }, "DiscordRPC-Handler");
        thread.start();
    }

    public static void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }

        rpc.Discord_Shutdown();
    }

    public static String getLocation() {
        if (Astra.mc.currentScreen instanceof GuiMainMenu) {
            return "Browsing the main menu";
        }

        if (Astra.mc.getCurrentServerData() != null) {
            return "Playing on " + Astra.mc.getCurrentServerData().serverIP + ".";
        } else if (Astra.mc.isSingleplayer()) {
            return "Playing on singleplayer.";
        }

        return "Doing god knows what";
    }
}