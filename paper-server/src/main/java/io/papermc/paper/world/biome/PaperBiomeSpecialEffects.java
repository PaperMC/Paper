package io.papermc.paper.world.biome;

import com.google.common.base.Preconditions;
import io.papermc.paper.util.MCUtil;
import io.papermc.paper.world.biome.effects.AdditionSound;
import io.papermc.paper.world.biome.effects.MoodSound;
import io.papermc.paper.world.biome.effects.MusicEntry;
import io.papermc.paper.world.biome.effects.PaperAdditionSound;
import io.papermc.paper.world.biome.effects.PaperMoodSound;
import io.papermc.paper.world.biome.effects.PaperMusicEntry;
import net.minecraft.sounds.Music;
import net.minecraft.util.random.WeightedList;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PaperBiomeSpecialEffects implements BiomeSpecialEffects {
    private final net.minecraft.world.level.biome.BiomeSpecialEffects effects;

    public PaperBiomeSpecialEffects(net.minecraft.world.level.biome.BiomeSpecialEffects effects) {
        this.effects = effects;
    }

    @Override
    public Color fogColor() {
        return Color.fromRGB(this.effects.getFogColor() & 0xFFFFFF);
    }

    @Override
    public Color waterColor() {
        return Color.fromRGB(this.effects.getWaterColor() & 0xFFFFFF);
    }

    @Override
    public Color waterFogColor() {
        return Color.fromRGB(this.effects.getWaterFogColor() & 0xFFFFFF);
    }

    @Override
    public Color skyColor() {
        return Color.fromRGB(this.effects.getSkyColor() & 0xFFFFFF);
    }

    @Override
    public Optional<Color> foliageColor() {
        return this.effects.getFoliageColorOverride().map(c -> Color.fromRGB(c & 0xFFFFFF));
    }

    @Override
    public Optional<Color> dryFoliageColor() {
        return this.effects.getDryFoliageColorOverride().map(c -> Color.fromRGB(c & 0xFFFFFF));
    }

    @Override
    public Optional<Color> grassColor() {
        return this.effects.getGrassColorOverride().map(c -> Color.fromRGB(c & 0xFFFFFF));
    }

    @Override
    public Optional<Sound> ambientSound() {
        return this.effects.getAmbientLoopSoundEvent().map(CraftSound::minecraftHolderToBukkit);
    }

    @Override
    public Optional<MoodSound> moodSound() {
        return this.effects.getAmbientMoodSettings().map(PaperMoodSound::new);
    }

    @Override
    public Optional<AdditionSound> additionSound() {
        return this.effects.getAmbientAdditionsSettings().map(PaperAdditionSound::new);
    }

    @Override
    public List<MusicEntry> music() {
        Optional<WeightedList<Music>> music = this.effects.getBackgroundMusic();
        if (music.isEmpty()) {
            return Collections.emptyList();
        }
        return MCUtil.transformUnmodifiable(music.orElseThrow().unwrap(), PaperMusicEntry::new);
    }

    @Override
    public float musicVolume() {
        return this.effects.getBackgroundMusicVolume();
    }

    @Override
    public Color modifyGrassColor(final Color original, final Location location) {
        Preconditions.checkArgument(original != null, "original color cannot be null");
        Preconditions.checkArgument(location != null, "location of the color cannot be null");
        return Color.fromRGB(this.effects.getGrassColorModifier().modifyColor(location.x(), location.z(), original.asRGB()));
    }
}
