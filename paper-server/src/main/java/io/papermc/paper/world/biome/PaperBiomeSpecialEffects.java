package io.papermc.paper.world.biome;

import io.papermc.paper.math.Position;
import io.papermc.paper.world.biome.effects.AdditionSound;
import io.papermc.paper.world.biome.effects.MoodSound;
import io.papermc.paper.world.biome.effects.MusicEntry;
import io.papermc.paper.world.biome.effects.PaperAdditionSound;
import io.papermc.paper.world.biome.effects.PaperMoodSound;
import io.papermc.paper.world.biome.effects.PaperMusicEntry;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
@NullMarked
public class PaperBiomeSpecialEffects implements BiomeSpecialEffects {
    private final net.minecraft.world.level.biome.BiomeSpecialEffects effects;

    public PaperBiomeSpecialEffects(Biome biome) {
        this.effects = biome.getSpecialEffects();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Color fogColor() {
        return Color.fromRGB(effects.getFogColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color waterColor() {
        return Color.fromRGB(effects.getWaterColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color waterFogColor() {
        return Color.fromRGB(effects.getWaterFogColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color skyColor() {
        return Color.fromRGB(effects.getSkyColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Color> foliageColor() {
        return effects.getFoliageColorOverride().map(Color::fromRGB);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Color> grassColor() {
        return effects.getGrassColorOverride().map(Color::fromRGB);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Sound> ambientSound() {
        return effects.getAmbientLoopSoundEvent().map(holder -> CraftSound.minecraftToBukkit(holder.value()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MoodSound> moodSound() {
        return effects.getAmbientMoodSettings().map(PaperMoodSound::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AdditionSound> additionSound() {
        return effects.getAmbientAdditionsSettings().map(PaperAdditionSound::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MusicEntry> music() {
        return effects.getBackgroundMusic()
            .stream()
            .flatMap(list -> list.unwrap().stream())
            .<MusicEntry>map(PaperMusicEntry::new)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float musicVolume() {
        return effects.getBackgroundMusicVolume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color modifyGrassColor(final Color original, final Position position) {
        return Color.fromRGB(effects.getGrassColorModifier().modifyColor(position.x(), position.z(), original.asRGB()));
    }
}
