package org.bukkit.craftbukkit;

import io.papermc.paper.util.OldEnumHolderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.Registry;
import org.bukkit.Sound;

public class CraftSound extends OldEnumHolderable<Sound, SoundEvent> implements Sound {

    private static int count = 0;

    public static Sound minecraftToBukkit(SoundEvent minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.SOUND_EVENT, Registry.SOUNDS);
    }

    public static Sound minecraftHolderToBukkit(Holder<SoundEvent> minecraft) {
        return minecraftToBukkit(minecraft.value());
    }

    public static SoundEvent bukkitToMinecraft(Sound bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<SoundEvent> bukkitToMinecraftHolder(Sound bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.SOUND_EVENT);
    }

    public CraftSound(Holder<SoundEvent> soundEffect) {
        super(soundEffect, count++);
    }

    public static String getSound(Sound sound) {
        return sound.getKey().getKey();
    }
}
