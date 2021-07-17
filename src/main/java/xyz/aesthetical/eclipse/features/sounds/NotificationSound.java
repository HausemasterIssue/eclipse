package xyz.aesthetical.eclipse.features.sounds;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import xyz.aesthetical.eclipse.Eclipse;

import javax.annotation.Nullable;

public class NotificationSound {
    public static final ISound sound;
    public static final String name = "notification";
    private static final ResourceLocation resource = new ResourceLocation("sounds/notification.ogg");

    public static ISound getSound() {
        return sound;
    }

    static {
        sound = new ISound() {
            @Override
            public ResourceLocation getSoundLocation() {
                return resource;
            }

            @Nullable
            @Override
            public SoundEventAccessor createAccessor(SoundHandler handler) {
                return new SoundEventAccessor(resource, "Eclipse Notification");
            }

            @Override
            public Sound getSound() {
                return new Sound(name, 0.7f, 1.0f, 1, Sound.Type.SOUND_EVENT, false);
            }

            @Override
            public SoundCategory getCategory() {
                return SoundCategory.NEUTRAL;
            }

            @Override
            public boolean canRepeat() {
                return false;
            }

            @Override
            public int getRepeatDelay() {
                return 0;
            }

            @Override
            public float getVolume() {
                return 0.7f;
            }

            @Override
            public float getPitch() {
                return 1.0f;
            }

            @Override
            public float getXPosF() {
                return Eclipse.mc.player.getPosition().getX();
            }

            @Override
            public float getYPosF() {
                return Eclipse.mc.player.getPosition().getY();
            }

            @Override
            public float getZPosF() {
                return Eclipse.mc.player.getPosition().getZ();
            }

            @Override
            public AttenuationType getAttenuationType() {
                return AttenuationType.LINEAR;
            }
        };
    }
}
