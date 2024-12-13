package io.papermc.paper.world.biome.effects;

import net.minecraft.world.level.biome.AmbientMoodSettings;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

/**
 * {@inheritDoc}
 */
public class PaperMoodSound implements MoodSound {
    private final AmbientMoodSettings settings;

    public PaperMoodSound(AmbientMoodSettings settings) {
        this.settings = settings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sound sound() {
        return CraftSound.minecraftToBukkit(settings.getSoundEvent().value());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int tickDelay() {
        return settings.getTickDelay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int blockSearchExtent() {
        return settings.getBlockSearchExtent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double offset() {
        return settings.getSoundPositionOffset();
    }
}
