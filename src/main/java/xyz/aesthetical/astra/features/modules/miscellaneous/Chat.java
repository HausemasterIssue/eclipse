package xyz.aesthetical.astra.features.modules.miscellaneous;

import baritone.api.BaritoneAPI;
import com.google.common.collect.Lists;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.events.network.PacketEvent;
import xyz.aesthetical.astra.features.settings.Setting;
import xyz.aesthetical.astra.managers.modules.Module;
import xyz.aesthetical.astra.mixin.mixins.network.packets.outbound.ICPacketChatMessage;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Module.Mod(name = "Chat", description = "Modifies chat behavior")
@Module.Info(category = Module.Category.MISCELLANEOUS)
public class Chat extends Module {
    private static final String DISABLED_UNICODE = "\u267F";
    private static final Random RNG = new Random();
    private static final List<String> BL_FANCY_CHARS = Lists.newArrayList("(", ")", "{", "}", "[", "]", "|");;

    // message shit
    public final Setting<Modifier> modifier = register(new Setting<>("Modifier", Modifier.NONE).setDescription("How to modify your chat text"));
    public final Setting<Prefix> prefix = register(new Setting<>("Prefix", Prefix.NONE).setDescription("What to prefix your message with"));
    public final Setting<Suffix> suffix = register(new Setting<>("Suffix", Suffix.ASTRA).setDescription("What to suffix your message with"));
    public final Setting<String> customSuffix = register(new Setting<>("Custom Suffix", "Aesthetical").setDescription("The custom suffix").setVisibility((m) -> suffix.getValue() == Suffix.CUSTOM));

    @SubscribeEvent
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (Module.fullNullCheck() && event.getPacket() instanceof CPacketChatMessage && !event.isCanceled()) {
            CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            String message = packet.getMessage();

            if (message.startsWith(BaritoneAPI.getSettings().prefix.value) || message.startsWith(Astra.commandManager.getPrefix()) || message.startsWith("/")) {
                return;
            }

            // start by modding the message
            if (modifier.getValue() != Modifier.NONE) {
                message = doModify(message);
            }

            if (prefix.getValue() != Prefix.NONE) {
                message = prefix.getValue().getPrefix() + message;
            }

            if (suffix.getValue() != Suffix.NONE) {
                String suf = suffix.getValue() == Suffix.CUSTOM ?
                        customSuffix.getValue() :
                        suffix.getValue().getSuffix();

                message += " | " + doUnicode(suf);
            }

            if (!packet.getMessage().equals(message)) {
                ((ICPacketChatMessage) packet).setMessage(message);
            }
        }
    }

    private String doModify(String text) {
        switch (modifier.getValue()) {
            case LOWERCASE: return text.toLowerCase();
            case MOCK: return doMock(text);
            case FANCY: return doFancyChat(text);
        }

        return text;
    }

    private String doFancyChat(String text) {
        StringBuilder modified = new StringBuilder();

        for (char c : text.trim().toCharArray()) {
            if (c < 0x21 || c > 0x80) {
                modified.append(c);
                continue;
            }

            if (BL_FANCY_CHARS.contains(Character.toString(c))) {
                modified.append(c);
                continue;
            }

            modified.append(Character.toChars(c + 0xfee0));
        }

        return modified.toString();
    }

    private String doMock(String text) {
        return Arrays.stream(text.split(""))
                .map(c -> (RNG.nextBoolean() ? c.toUpperCase() : c.toLowerCase()))
                .collect(Collectors.joining(""));
    }

    // 100% not copied from https://github.com/ChompChompDead/Teddyware-Client/blob/master/src/main/java/com/chompchompdead/teddyware/client/module/client/ChatSuffix.java#L30#L55
    private String doUnicode(String text) {
        return text
                .toLowerCase()
                .replace("a", "\u1d00")
                .replace("b", "\u0299")
                .replace("c", "\u1d04")
                .replace("d", "\u1d05")
                .replace("e", "\u1d07")
                .replace("f", "\ua730")
                .replace("g", "\u0262")
                .replace("h", "\u029c")
                .replace("i", "\u026a")
                .replace("j", "\u1d0a")
                .replace("k", "\u1d0b")
                .replace("l", "\u029f")
                .replace("m", "\u1d0d")
                .replace("n", "\u0274")
                .replace("o", "\u1d0f")
                .replace("p", "\u1d18")
                .replace("q", "\u01eb")
                .replace("r", "\u0280")
                .replace("s", "\ua731")
                .replace("t", "\u1d1b")
                .replace("u", "\u1d1c")
                .replace("v", "\u1d20")
                .replace("w", "\u1d21")
                .replace("x", "\u02e3")
                .replace("y", "\u028f")
                .replace("z", "\u1d22");
    }

    public enum Prefix {
        NONE(null),
        WHEELCHAIR(DISABLED_UNICODE),
        ARROW(">");

        private String prefix;
        Prefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public enum Suffix {
        NONE(null),
        ASTRA("Astra"),
        AESTHETICAL("Aesthetical"),
        CUSTOM(null);

        private String suffix;
        Suffix(String suffix) {
            this.suffix = suffix;
        }

        public String getSuffix() {
            return suffix;
        }
    }

    public enum Modifier {
        NONE,
        LOWERCASE,
        MOCK,
        FANCY
    }
}
