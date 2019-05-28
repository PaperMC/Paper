package com.destroystokyo.paper.block;

import net.minecraft.server.SoundEffectType;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

public class CraftBlockSoundGroup implements BlockSoundGroup {
    private final SoundEffectType soundEffectType;

    public CraftBlockSoundGroup(SoundEffectType soundEffectType) {
        this.soundEffectType = soundEffectType;
    }

    @Override
    public Sound getBreakSound() {
        return CraftSound.getSoundByEffect(soundEffectType.getBreakSound());
    }

    @Override
    public Sound getStepSound() {
        return CraftSound.getSoundByEffect(soundEffectType.getStepSound());
    }

    @Override
    public Sound getPlaceSound() {
        return CraftSound.getSoundByEffect(soundEffectType.getPlaceSound());
    }

    @Override
    public Sound getHitSound() {
        return CraftSound.getSoundByEffect(soundEffectType.getHitSound());
    }

    @Override
    public Sound getFallSound() {
        return CraftSound.getSoundByEffect(soundEffectType.getFallSound());
    }
}
