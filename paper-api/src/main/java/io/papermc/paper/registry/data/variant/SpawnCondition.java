package io.papermc.paper.registry.data.variant;

import com.google.common.base.Preconditions;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import org.bukkit.block.Biome;
import org.bukkit.generator.structure.Structure;
import org.checkerframework.checker.units.qual.min;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A condition checked when an entity is trying to spawn.
 */
public sealed interface SpawnCondition permits SpawnCondition.BiomeCheck, SpawnCondition.MoonBrightnessCheck, SpawnCondition.StructureCheck {

    /**
     * A condition that checks if the position matches a biome.
     *
     * @param requiredBiomes The biomes that the position must match
     * @return The biome check condition
     */
    @SafeVarargs
    @Contract(pure = true, value = "_ -> new")
    static SpawnCondition biomeCheck(final TypedKey<Biome>... requiredBiomes) {
        return biomeCheck(RegistrySet.keySet(RegistryKey.BIOME, requiredBiomes));
    }

    /**
     * A condition that checks if the position matches a biome.
     *
     * @param requiredBiomes The biomes that the position must match
     * @return The biome check condition
     */
    @Contract(pure = true, value = "_ -> new")
    static SpawnCondition biomeCheck(final RegistryKeySet<Biome> requiredBiomes) {
        return new BiomeCheckImpl(requiredBiomes);
    }

    /**
     * A condition that checks if the moon brightness is within a certain range.
     *
     * @param range The range of the moon brightness (must be closed range)
     * @return The moon brightness check condition
     */
    @Contract(pure = true, value = "_ -> new")
    static SpawnCondition moonBrightnessCheck(final Range<Double> range) {
        Preconditions.checkArgument(!range.hasLowerBound() || range.lowerBoundType() == BoundType.CLOSED, "Range lower bound must be closed");
        Preconditions.checkArgument(!range.hasUpperBound() || range.upperBoundType() == BoundType.CLOSED, "Range upper bound must be closed");
        return new MoonBrightnessCheckImpl(range);
    }

    /**
     * A condition that checks if the position is within a certain structure.
     *
     * @param requiredStructures The structures that the position must be within
     * @return The structure check condition
     */
    @SafeVarargs
    @Contract(pure = true, value = "_ -> new")
    static SpawnCondition structureCheck(final TypedKey<Structure>... requiredStructures) {
        return structureCheck(RegistrySet.keySet(RegistryKey.STRUCTURE, requiredStructures));
    }

    /**
     * A condition that checks if the position is within a certain structure.
     *
     * @param requiredStructures The structures that the position must be within
     * @return The structure check condition
     */
    @Contract(pure = true, value = "_ -> new")
    static SpawnCondition structureCheck(final RegistryKeySet<Structure> requiredStructures) {
        return new StructureCheckImpl(requiredStructures);
    }

    /**
     * A condition that checks if the position matches a biome.
     */
    sealed interface BiomeCheck extends SpawnCondition permits BiomeCheckImpl {

        /**
         * The biomes that the position must match.
         *
         * @return The required biomes
         */
        @Contract(pure = true)
        RegistryKeySet<Biome> requiredBiomes();
    }

    /**
     * A condition that checks if the moon brightness is within a certain range.
     */
    sealed interface MoonBrightnessCheck extends SpawnCondition permits MoonBrightnessCheckImpl {

        /**
         * The range of moon brightness that the position must match.
         *
         * @return The required moon brightness range
         */
        @Contract(pure = true)
        Range<Double> range();
    }

    /**
     * A condition that checks if the position is within a certain structure.
     */
    sealed interface StructureCheck extends SpawnCondition permits StructureCheckImpl {

        /**
         * The structures that the position must be within.
         *
         * @return The required structures
         */
        @Contract(pure = true)
        RegistryKeySet<Structure> requiredStructures();
    }
}
