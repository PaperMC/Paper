package io.papermc.paper.world.biome.effects;

import net.minecraft.world.level.biome.AmbientMoodSettings;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

public class PaperMoodSound implements MoodSound {

    private final AmbientMoodSettings settings;

    public PaperMoodSound(AmbientMoodSettings settings) {
        this.settings = settings;
    }

    @Override
    public Sound sound() {
        return CraftSound.minecraftHolderToBukkit(settings.getSoundEvent());
    }

    @Override
    public int tickDelay() {
        return settings.getTickDelay();
    }

    @Override
    public int blockSearchExtent() {
        return settings.getBlockSearchExtent();
    }

    @Override
    public double offset() {
        return settings.getSoundPositionOffset();
    }
}
