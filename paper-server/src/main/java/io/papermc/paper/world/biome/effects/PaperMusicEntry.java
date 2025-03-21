package io.papermc.paper.world.biome.effects;

import net.minecraft.sounds.Music;
import net.minecraft.util.random.WeightedEntry;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

public class PaperMusicEntry implements MusicEntry {

    private final WeightedEntry.Wrapper<Music> entry;

    public PaperMusicEntry(WeightedEntry.Wrapper<Music> entry) {
        this.entry = entry;
    }

    @Override
    public Sound sound() {
        return CraftSound.minecraftToBukkit(entry.data().getEvent().value());
    }

    @Override
    public int minDelay() {
        return entry.data().getMinDelay();
    }

    @Override
    public int maxDelay() {
        return entry.data().getMaxDelay();
    }

    @Override
    public boolean replaceCurrentMusic() {
        return entry.data().replaceCurrentMusic();
    }

    @Override
    public int weight() {
        return entry.weight().asInt();
    }
}
