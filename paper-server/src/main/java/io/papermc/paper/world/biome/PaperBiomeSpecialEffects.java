package io.papermc.paper.world.biome;

import io.papermc.paper.util.RGBColor;
import io.papermc.paper.world.biome.effects.AdditionSound;
import io.papermc.paper.world.biome.effects.MoodSound;
import io.papermc.paper.world.biome.effects.MusicEntry;
import io.papermc.paper.world.biome.effects.PaperAdditionSound;
import io.papermc.paper.world.biome.effects.PaperMoodSound;
import io.papermc.paper.world.biome.effects.PaperMusicEntry;
import net.minecraft.world.level.biome.Biome;
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
    public RGBColor fogColor() {
        return RGBColor.fromRGB(effects.getFogColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RGBColor waterColor() {
        return RGBColor.fromRGB(effects.getWaterColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RGBColor waterFogColor() {
        return RGBColor.fromRGB(effects.getWaterFogColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RGBColor skyColor() {
        return RGBColor.fromRGB(effects.getSkyColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RGBColor> foliageColor() {
        return effects.getFoliageColorOverride().map(RGBColor::fromRGB);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RGBColor> grassColor() {
        return effects.getGrassColorOverride().map(RGBColor::fromRGB);
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
    public RGBColor modifyGrassColor(final RGBColor original, final double x, final double y, final double z) {
        return RGBColor.fromRGB(effects.getGrassColorModifier().modifyColor(x, z, original.asRGB()));
    }
}
