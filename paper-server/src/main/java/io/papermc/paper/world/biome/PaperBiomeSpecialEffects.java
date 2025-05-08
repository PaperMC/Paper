package io.papermc.paper.world.biome;

import io.papermc.paper.util.MCUtil;
import io.papermc.paper.world.biome.effects.AdditionSound;
import io.papermc.paper.world.biome.effects.MoodSound;
import io.papermc.paper.world.biome.effects.MusicEntry;
import io.papermc.paper.world.biome.effects.PaperAdditionSound;
import io.papermc.paper.world.biome.effects.PaperMoodSound;
import io.papermc.paper.world.biome.effects.PaperMusicEntry;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PaperBiomeSpecialEffects implements BiomeSpecialEffects {
    private final Holder<Biome> biome;

    public PaperBiomeSpecialEffects(Holder<Biome> biome) {
        this.biome = biome;
    }

    @Override
    public Color fogColor() {
        return Color.fromRGB(this.biome.value().getSpecialEffects().getFogColor());
    }

    @Override
    public Color waterColor() {
        return Color.fromRGB(this.biome.value().getSpecialEffects().getWaterColor());
    }

    @Override
    public Color waterFogColor() {
        return Color.fromRGB(this.biome.value().getSpecialEffects().getWaterFogColor());
    }

    @Override
    public Color skyColor() {
        return Color.fromRGB(this.biome.value().getSpecialEffects().getSkyColor());
    }

    @Override
    public Optional<Color> foliageColor() {
        return this.biome.value().getSpecialEffects().getFoliageColorOverride().map(Color::fromRGB);
    }

    @Override
    public Optional<Color> grassColor() {
        return this.biome.value().getSpecialEffects().getGrassColorOverride().map(Color::fromRGB);
    }

    @Override
    public Optional<Sound> ambientSound() {
        return this.biome.value().getSpecialEffects().getAmbientLoopSoundEvent().map(holder -> CraftSound.minecraftToBukkit(holder.value()));
    }

    @Override
    public Optional<MoodSound> moodSound() {
        return this.biome.value().getSpecialEffects().getAmbientMoodSettings().map(PaperMoodSound::new);
    }

    @Override
    public Optional<AdditionSound> additionSound() {
        return this.biome.value().getSpecialEffects().getAmbientAdditionsSettings().map(PaperAdditionSound::new);
    }

    @Override
    public List<MusicEntry> music() {
        Optional<WeightedList<Music>> music = this.biome.value().getSpecialEffects().getBackgroundMusic();
        if (music.isEmpty()) {
            return Collections.emptyList();
        }
        return MCUtil.transformUnmodifiable(music.orElseThrow().unwrap(), PaperMusicEntry::new);
    }

    @Override
    public float musicVolume() {
        return this.biome.value().getSpecialEffects().getBackgroundMusicVolume();
    }

    @Override
    public Color modifyGrassColor(final Color original, final Location location) {
        return Color.fromRGB(this.biome.value().getSpecialEffects().getGrassColorModifier().modifyColor(location.x(), location.z(), original.asRGB()));
    }
}
