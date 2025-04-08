package io.papermc.paper.world.biome;

import io.papermc.paper.util.MCUtil;
import io.papermc.paper.world.biome.effects.AdditionSound;
import io.papermc.paper.world.biome.effects.MoodSound;
import io.papermc.paper.world.biome.effects.MusicEntry;
import io.papermc.paper.world.biome.effects.PaperAdditionSound;
import io.papermc.paper.world.biome.effects.PaperMoodSound;
import io.papermc.paper.world.biome.effects.PaperMusicEntry;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PaperBiomeSpecialEffects implements BiomeSpecialEffects {

    private final net.minecraft.world.level.biome.BiomeSpecialEffects effects;

    public PaperBiomeSpecialEffects(Biome biome) {
        this.effects = biome.getSpecialEffects();
    }

    @Override
    public Color fogColor() {
        return Color.fromRGB(effects.getFogColor());
    }

    @Override
    public Color waterColor() {
        return Color.fromRGB(effects.getWaterColor());
    }

    @Override
    public Color waterFogColor() {
        return Color.fromRGB(effects.getWaterFogColor());
    }

    @Override
    public Color skyColor() {
        return Color.fromRGB(effects.getSkyColor());
    }

    @Override
    public Optional<Color> foliageColor() {
        return effects.getFoliageColorOverride().map(Color::fromRGB);
    }

    @Override
    public Optional<Color> grassColor() {
        return effects.getGrassColorOverride().map(Color::fromRGB);
    }

    @Override
    public Optional<Sound> ambientSound() {
        return effects.getAmbientLoopSoundEvent().map(holder -> CraftSound.minecraftToBukkit(holder.value()));
    }

    @Override
    public Optional<MoodSound> moodSound() {
        return effects.getAmbientMoodSettings().map(PaperMoodSound::new);
    }

    @Override
    public Optional<AdditionSound> additionSound() {
        return effects.getAmbientAdditionsSettings().map(PaperAdditionSound::new);
    }

    @Override
    public List<MusicEntry> music() {
        if (this.effects.getBackgroundMusic().isEmpty()) {
            return Collections.emptyList();
        }
        return MCUtil.transformUnmodifiable(this.effects.getBackgroundMusic().orElseThrow().unwrap(), PaperMusicEntry::new);
    }

    @Override
    public float musicVolume() {
        return effects.getBackgroundMusicVolume();
    }

    @Override
    public Color modifyGrassColor(final Color original, final Location location) {
        return Color.fromRGB(effects.getGrassColorModifier().modifyColor(location.x(), location.z(), original.asRGB()));
    }
}
