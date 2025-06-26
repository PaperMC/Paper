package io.papermc.paper.world.biome.effects;

import net.minecraft.sounds.Music;
import net.minecraft.util.random.Weighted;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

public class PaperMusicEntry implements MusicEntry {

    private final Weighted<Music> entry;

    public PaperMusicEntry(Weighted<Music> entry) {
        this.entry = entry;
    }

    @Override
    public Sound sound() {
        return CraftSound.minecraftHolderToBukkit(entry.value().event());
    }

    @Override
    public int minDelay() {
        return entry.value().minDelay();
    }

    @Override
    public int maxDelay() {
        return entry.value().maxDelay();
    }

    @Override
    public boolean replaceCurrentMusic() {
        return entry.value().replaceCurrentMusic();
    }

    @Override
    public int weight() {
        return entry.weight();
    }
}
