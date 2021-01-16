package org.bukkit.craftbukkit;

import java.util.HashMap;
import net.minecraft.server.SoundEffectType;
import org.bukkit.Sound;
import org.bukkit.SoundGroup;

public class CraftSoundGroup implements SoundGroup {

    private final net.minecraft.server.SoundEffectType handle;
    private static final HashMap<SoundEffectType, CraftSoundGroup> SOUND_GROUPS = new HashMap<>();

    public static SoundGroup getSoundGroup(SoundEffectType soundEffectType) {
        return SOUND_GROUPS.computeIfAbsent(soundEffectType, CraftSoundGroup::new);
    }

    private CraftSoundGroup(net.minecraft.server.SoundEffectType soundEffectType) {
        this.handle = soundEffectType;
    }

    public net.minecraft.server.SoundEffectType getHandle() {
        return handle;
    }

    @Override
    public float getVolume() {
        return getHandle().getVolume();
    }

    @Override
    public float getPitch() {
        return getHandle().getPitch();
    }

    @Override
    public Sound getBreakSound() {
        return CraftSound.getBukkit(getHandle().breakSound);
    }

    @Override
    public Sound getStepSound() {
        return CraftSound.getBukkit(getHandle().getStepSound());
    }

    @Override
    public Sound getPlaceSound() {
        return CraftSound.getBukkit(getHandle().getPlaceSound());
    }

    @Override
    public Sound getHitSound() {
        return CraftSound.getBukkit(getHandle().hitSound);
    }

    @Override
    public Sound getFallSound() {
        return CraftSound.getBukkit(getHandle().getFallSound());
    }
}
