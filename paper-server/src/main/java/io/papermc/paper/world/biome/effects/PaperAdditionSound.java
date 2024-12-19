package io.papermc.paper.world.biome.effects;

import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;
import org.jetbrains.annotations.Range;

/**
 * {@inheritDoc}
 */
public class PaperAdditionSound implements AdditionSound {

    private final AmbientAdditionsSettings settings;

    public PaperAdditionSound(AmbientAdditionsSettings settings) {
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
    public @Range(from = 0, to = 1) double tickChance() {
        return settings.getTickChance();
    }
}
