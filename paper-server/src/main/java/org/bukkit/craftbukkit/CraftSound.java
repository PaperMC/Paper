package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.sounds.SoundEffect;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class CraftSound {

    public static SoundEffect getSoundEffect(String s) {
        SoundEffect effect = BuiltInRegistries.SOUND_EVENT.get(new MinecraftKey(s));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    public static SoundEffect getSoundEffect(Sound s) {
        SoundEffect effect = BuiltInRegistries.SOUND_EVENT.get(CraftNamespacedKey.toMinecraft(s.getKey()));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    public static Sound getBukkit(SoundEffect soundEffect) {
        return Registry.SOUNDS.get(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.SOUND_EVENT.getKey(soundEffect)));
    }
}
