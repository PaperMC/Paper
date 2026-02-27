package org.bukkit.craftbukkit.block;

import java.util.Objects;
import io.papermc.paper.world.biome.BiomeClimate;
import io.papermc.paper.world.biome.BiomeMobSpawning;
import io.papermc.paper.world.biome.BiomeSpecialEffects;
import io.papermc.paper.world.biome.PaperBiomeClimate;
import io.papermc.paper.world.biome.PaperBiomeMobSpawning;
import io.papermc.paper.world.biome.PaperBiomeSpecialEffects;
import io.papermc.paper.util.OldEnumHolderable;
import java.util.Objects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftBiome extends OldEnumHolderable<Biome, net.minecraft.world.level.biome.Biome> implements Biome {

    private static int count = 0;

    public static Biome minecraftHolderToBukkit(Holder<net.minecraft.world.level.biome.Biome> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.BIOME);
    }

    public static @Nullable Holder<net.minecraft.world.level.biome.Biome> bukkitToMinecraftHolder(Biome bukkit) {
        if (bukkit == Biome.CUSTOM) {
            return null;
        }
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public CraftBiome(final Holder<net.minecraft.world.level.biome.Biome> holder) {
        super(holder, count++);
    }

    @Override
    public BiomeClimate climate() {
        return new PaperBiomeClimate(this.getHolder().value().climateSettings, this.getHolder().value());
    }

    @Override
    public BiomeMobSpawning mobSpawning() {
        return new PaperBiomeMobSpawning(this.getHolder().value().getMobSettings());
    }

    @Override
    public BiomeSpecialEffects specialEffects() {
        return new PaperBiomeSpecialEffects(this.getHolder().value().getSpecialEffects());
    }

    /**
     * Implementation for the deprecated, API only, CUSTOM biome.
     * As per {@link #bukkitToMinecraftHolder(Biome)} it cannot be
     * converted into an internal biome and only serves backwards compatibility reasons.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    @ApiStatus.ScheduledForRemoval(inVersion = "1.22")
    public static class LegacyCustomBiomeImpl implements Biome {

        private static final NamespacedKey LEGACY_CUSTOM_KEY = new NamespacedKey("minecraft", "custom");
        private final int ordinal;

        public LegacyCustomBiomeImpl() {
            this.ordinal = count++;
        }

        @Override
        public NamespacedKey getKey() {
            return LEGACY_CUSTOM_KEY;
        }

        @Override
        public BiomeClimate climate() {
            throw new UnsupportedOperationException("Legacy CUSTOM biome does not support climate");
        }

        @Override
        public BiomeMobSpawning mobSpawning() {
            throw new UnsupportedOperationException("Legacy CUSTOM biome does not support mobSpawning");
        }

        @Override
        public BiomeSpecialEffects specialEffects() {
            throw new UnsupportedOperationException("Legacy CUSTOM biome does not support specialEffects");
        }

        @Override
        public int compareTo(final Biome other) {
            return this.ordinal - other.ordinal();
        }

        @Override
        public String name() {
            return "CUSTOM";
        }

        @Override
        public int ordinal() {
            return this.ordinal;
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            final LegacyCustomBiomeImpl that = (LegacyCustomBiomeImpl) object;
            return ordinal == that.ordinal;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(ordinal);
        }

        @Override
        public String toString() {
            return "CUSTOM";
        }
    }
}
