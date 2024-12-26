package io.papermc.paper.registry.data;

import io.papermc.paper.math.provider.IntProvider;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.world.worldgen.DimensionType;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DimensionTypeRegistryEntry {

    @Nullable Long fixedTime();

    boolean hasSkyLight();

    boolean hasCeiling();

    boolean ultraWarm();

    boolean natural();

    double coordinateScale();

    boolean bedWorks();

    boolean respawnAnchorWorks();

    @Range(from = -2032, to = 2031) int minY();

    @Range(from = 16, to = 4064) int height();

    @Range(from = 0, to = 4096) int logicalHeight();

    TagKey<BlockType> infiniburn();

    Key effectsLocation();

    float ambientLight();

    @Nullable Integer cloudHeight();

    boolean piglinSafe();

    boolean hasRaids();

    IntProvider monsterSpawnLightTest();

    @Range(from = 0, to = 15) int monsterSpawnBlockLightLimit();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends RegistryBuilder<DimensionType> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder fixedTime(@Nullable Long fixedTime);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hasSkyLight(boolean hasSkyLight);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hasCeiling(boolean hasCeiling);

        @Contract(value = "_ -> this", mutates = "this")
        Builder ultraWarm(boolean ultraWarm);

        @Contract(value = "_ -> this", mutates = "this")
        Builder natural(boolean natural);

        @Contract(value = "_ -> this", mutates = "this")
        Builder coordinateScale(double coordinateScale);

        @Contract(value = "_ -> this", mutates = "this")
        Builder bedWorks(boolean bedWorks);

        @Contract(value = "_ -> this", mutates = "this")
        Builder respawnAnchorWorks(boolean respawnAnchorWorks);

        @Contract(value = "_ -> this", mutates = "this")
        Builder minY(@Range(from = -2032, to = 2031) int minY);

        @Contract(value = "_ -> this", mutates = "this")
        Builder height(@Range(from = 16, to = 4064) int height);

        @Contract(value = "_ -> this", mutates = "this")
        Builder logicalHeight(@Range(from = 0, to = 4096) int logicalHeight);

        @Contract(value = "_ -> this", mutates = "this")
        Builder infiniburn(TagKey<BlockType> infiniburn);

        @Contract(value = "_ -> this", mutates = "this")
        Builder effectsLocation(Key effectsLocation);

        @Contract(value = "_ -> this", mutates = "this")
        Builder ambientLight(float ambientLight);

        @Contract(value = "_ -> this", mutates = "this")
        Builder cloudHeight(@Nullable Integer cloudHeight);

        @Contract(value = "_ -> this", mutates = "this")
        Builder piglinSafe(boolean piglinSafe);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hasRaids(boolean hasRaids);

        @Contract(value = "_ -> this", mutates = "this")
        Builder monsterSpawnLightTest(IntProvider monsterSpawnLightTest);

        @Contract(value = "_ -> this", mutates = "this")
        Builder monsterSpawnBlockLightLimit(@Range(from = 0, to = 15) int monsterSpawnBlockLightLimit);
    }
}
