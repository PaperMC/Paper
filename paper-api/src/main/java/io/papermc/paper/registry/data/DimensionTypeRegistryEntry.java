package io.papermc.paper.registry.data;

import io.papermc.paper.math.provider.IntProvider;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.world.worldgen.DimensionType;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DimensionTypeRegistryEntry {

    enum Skybox {
        // Start generate - DimensionTypeSkybox
        NONE,
        OVERWORLD,
        END;
        // End generate - DimensionTypeSkybox
    }

    enum CardinalLightType {
        // Start generate - DimensionTypeCardinalLightType
        DEFAULT,
        NETHER;
        // End generate - DimensionTypeCardinalLightType
    }

    boolean hasFixedTime();

    boolean hasSkyLight();

    boolean hasCeiling();

    boolean hasEnderDragonFight();

    double coordinateScale();

    @Range(from = -2032, to = 2031) int minY();

    @Range(from = 16, to = 4064) int height();

    @Range(from = 0, to = 4096) int logicalHeight();

    TagKey<BlockType> infiniburn();

    float ambientLight();

    IntProvider monsterSpawnLightTest();

    @Range(from = 0, to = 15) int monsterSpawnBlockLightLimit();

    Skybox skybox();

    CardinalLightType cardinalLightType();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends RegistryBuilder<DimensionType> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder hasFixedTime(boolean hasFixedTime);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hasSkyLight(boolean hasSkyLight);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hasCeiling(boolean hasCeiling);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hasEnderDragonFight(boolean hasEnderDragonFight);

        @Contract(value = "_ -> this", mutates = "this")
        Builder coordinateScale(double coordinateScale);

        @Contract(value = "_ -> this", mutates = "this")
        Builder minY(@Range(from = -2032, to = 2031) int minY);

        @Contract(value = "_ -> this", mutates = "this")
        Builder height(@Range(from = 16, to = 4064) int height);

        @Contract(value = "_ -> this", mutates = "this")
        Builder logicalHeight(@Range(from = 0, to = 4096) int logicalHeight);

        @Contract(value = "_ -> this", mutates = "this")
        Builder infiniburn(TagKey<BlockType> infiniburn);

        @Contract(value = "_ -> this", mutates = "this")
        Builder ambientLight(float ambientLight);

        @Contract(value = "_ -> this", mutates = "this")
        Builder monsterSpawnLightTest(IntProvider monsterSpawnLightTest);

        @Contract(value = "_ -> this", mutates = "this")
        Builder monsterSpawnBlockLightLimit(@Range(from = 0, to = 15) int monsterSpawnBlockLightLimit);

        @Contract(value = "_ -> this", mutates = "this")
        Builder skybox(Skybox skybox);

        @Contract(value = "_ -> this", mutates = "this")
        Builder cardinalLight(CardinalLightType cardinalLightType);
    }
}
