package com.destroystokyo.paper.block;

import net.minecraft.world.level.block.SoundType;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

@Deprecated(forRemoval = true)
public class CraftBlockSoundGroup implements BlockSoundGroup {
    private final SoundType soundEffectType;

    public CraftBlockSoundGroup(SoundType soundEffectType) {
        this.soundEffectType = soundEffectType;
    }

    @Override
    public Sound getBreakSound() {
        return CraftSound.minecraftToBukkit(soundEffectType.getBreakSound());
    }

    @Override
    public Sound getStepSound() {
        return CraftSound.minecraftToBukkit(soundEffectType.getStepSound());
    }

    @Override
    public Sound getPlaceSound() {
        return CraftSound.minecraftToBukkit(soundEffectType.getPlaceSound());
    }

    @Override
    public Sound getHitSound() {
        return CraftSound.minecraftToBukkit(soundEffectType.getHitSound());
    }

    @Override
    public Sound getFallSound() {
        return CraftSound.minecraftToBukkit(soundEffectType.getFallSound());
    }
}
