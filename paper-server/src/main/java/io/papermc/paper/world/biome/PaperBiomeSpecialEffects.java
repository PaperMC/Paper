package io.papermc.paper.world.biome;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class PaperBiomeSpecialEffects implements BiomeSpecialEffects {
    private final net.minecraft.world.level.biome.BiomeSpecialEffects effects;

    public PaperBiomeSpecialEffects(final net.minecraft.world.level.biome.BiomeSpecialEffects effects) {
        this.effects = effects;
    }

    @Override
    public Color waterColor() {
        return Color.fromRGB(this.effects.waterColor() & 0xFFFFFF);
    }

    @Override
    public @Nullable Color foliageColor() {
        return this.effects.foliageColorOverride().map(c -> Color.fromRGB(c & 0xFFFFFF)).orElse(null);
    }

    @Override
    public @Nullable Color dryFoliageColor() {
        return this.effects.dryFoliageColorOverride().map(c -> Color.fromRGB(c & 0xFFFFFF)).orElse(null);
    }

    @Override
    public @Nullable Color grassColor() {
        return this.effects.grassColorOverride().map(c -> Color.fromRGB(c & 0xFFFFFF)).orElse(null);
    }

    @Override
    public Color modifyGrassColor(final Color original, final Location location) {
        Preconditions.checkArgument(original != null, "original color cannot be null");
        Preconditions.checkArgument(location != null, "location of the color cannot be null");
        return Color.fromRGB(this.effects.grassColorModifier().modifyColor(location.x(), location.z(), original.asRGB()));
    }
}
