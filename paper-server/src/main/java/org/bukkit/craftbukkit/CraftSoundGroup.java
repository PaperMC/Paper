package org.bukkit.craftbukkit;

import java.util.Objects;
import net.minecraft.world.level.block.sounds.BlockSoundSet;
import org.bukkit.Sound;
import org.bukkit.SoundGroup;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record CraftSoundGroup(BlockSoundSet handle) implements SoundGroup {

    @Override
    public float getVolume() {
        return this.handle().volume();
    }

    @Override
    public float getPitch() {
        return this.handle().pitch();
    }

    @Override
    public Sound getBreakSound() {
        return Objects.requireNonNullElse(this.breakSound(), Sound.BLOCK_STONE_BREAK);
    }

    @Override
    public Sound getStepSound() {
        return Objects.requireNonNullElse(this.stepSound(), Sound.BLOCK_STONE_STEP);
    }

    @Override
    public Sound getPlaceSound() {
        return Objects.requireNonNullElse(this.placeSound(), Sound.BLOCK_STONE_PLACE);
    }

    @Override
    public Sound getHitSound() {
        return Objects.requireNonNullElse(this.hitSound(), Sound.BLOCK_STONE_HIT);
    }

    @Override
    public Sound getFallSound() {
        return Objects.requireNonNullElse(this.fallSound(), Sound.BLOCK_STONE_FALL);
    }

    @Override
    public @Nullable Sound breakSound() {
        return this.handle().breakSound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
    }

    @Override
    public @Nullable Sound stepSound() {
        return this.handle().stepSound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
    }

    @Override
    public @Nullable Sound placeSound() {
        return this.handle().placeSound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
    }

    @Override
    public @Nullable Sound hitSound() {
        return this.handle().hitSound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
    }

    @Override
    public @Nullable Sound fallSound() {
        return this.handle().fallSound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
    }
}
