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
        return getHandle().V; // PAIL rename volume
    }

    @Override
    public float getPitch() {
        return getHandle().W; // PAIL rename pitch
    }

    @Override
    public Sound getBreakSound() {
        return CraftSound.getBukkit(getHandle().X);
    }

    @Override
    public Sound getStepSound() {
        return CraftSound.getBukkit(getHandle().d()); // PAIL rename getStepSound
    }

    @Override
    public Sound getPlaceSound() {
        return CraftSound.getBukkit(getHandle().e()); // PAIL rename getPlaceSound
    }

    @Override
    public Sound getHitSound() {
        return CraftSound.getBukkit(getHandle().aa);
    }

    @Override
    public Sound getFallSound() {
        return CraftSound.getBukkit(getHandle().g()); // PAIL rename getFallSound
    }
}
