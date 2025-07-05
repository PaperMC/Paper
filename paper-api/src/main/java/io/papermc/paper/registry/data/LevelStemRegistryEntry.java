package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.world.WorldPreset;
import io.papermc.paper.world.worldgen.DimensionType;
import io.papermc.paper.world.worldgen.LevelStem;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface LevelStemRegistryEntry {

    // codec says it supports inlining, but the protocol does NOT (respawn packet only sends network id)
    TypedKey<DimensionType> type();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends RegistryBuilder<LevelStem> {

        Builder type(TypedKey<DimensionType> type);

        Builder copyGeneratorFrom(TypedKey<WorldPreset> preset, TypedKey<LevelStem> levelStem);
    }
}
