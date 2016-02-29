package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.SoundEffect;

import org.apache.commons.lang.Validate;
import org.bukkit.Sound;

public class CraftSound {

    public static String getSound(final Sound sound) {
        Validate.notNull(sound, "Sound cannot be null");

        return sound.name().replace('_', '.').toLowerCase();
    }

    public static SoundEffect getSoundEffect(String s) {
        SoundEffect effect = SoundEffect.a.get(new MinecraftKey(s));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    private CraftSound() {}
}
