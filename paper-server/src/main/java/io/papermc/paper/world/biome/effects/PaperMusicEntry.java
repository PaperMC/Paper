package io.papermc.paper.world.biome.effects;

import net.minecraft.sounds.Music;
import net.minecraft.util.random.WeightedEntry;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

/**
 * {@inheritDoc}
 */
public class PaperMusicEntry implements MusicEntry {
    private final WeightedEntry.Wrapper<Music> entry;

    public PaperMusicEntry(WeightedEntry.Wrapper<Music> entry) {
        this.entry = entry;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Sound sound() {
        return CraftSound.minecraftToBukkit(entry.data().getEvent().value());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int minDelay() {
        return entry.data().getMinDelay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int maxDelay() {
        return entry.data().getMaxDelay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean replaceCurrentMusic() {
        return entry.data().replaceCurrentMusic();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int weight() {
        return entry.weight().asInt();
    }
}
