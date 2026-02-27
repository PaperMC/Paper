package io.papermc.paper.world.biome.effects;

import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;
import org.jetbrains.annotations.Range;

public class PaperAdditionSound implements AdditionSound {

    private final AmbientAdditionsSettings settings;

    public PaperAdditionSound(AmbientAdditionsSettings settings) {
        this.settings = settings;
    }

    @Override
    public Sound sound() {
        return CraftSound.minecraftHolderToBukkit(settings.getSoundEvent());
    }

    @Override
    public @Range(from = 0, to = 1) double tickChance() {
        return settings.getTickChance();
    }
}
