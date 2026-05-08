package io.papermc.paper.registry.data;

import com.google.common.base.Preconditions;
import io.papermc.paper.math.provider.IntProvider;
import io.papermc.paper.math.provider.PaperIntProvider;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.timeline.Timeline;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;
import static io.papermc.paper.util.BoundChecker.requireRange;

public class PaperDimensionTypeRegistryEntry implements DimensionTypeRegistryEntry {

    protected boolean hasFixedTime = false;
    protected @Nullable Boolean hasSkylight;
    protected @Nullable Boolean hasCeiling;
    protected @Nullable Boolean hasEnderDragonFight;
    protected @Nullable Double coordinateScale;
    protected @Nullable Integer minY;
    protected @Nullable Integer height;
    protected @Nullable Integer logicalHeight;
    protected net.minecraft.tags.@Nullable TagKey<Block> infiniburn;
    protected @Nullable Float ambientLight;
    protected net.minecraft.util.valueproviders.@Nullable IntProvider monsterSpawnLightTest;
    protected @Nullable Integer monsterSpawnBlockLightLimit;
    protected DimensionType.Skybox skybox = DimensionType.Skybox.OVERWORLD;
    protected CardinalLighting.Type cardinalLightType = CardinalLighting.Type.DEFAULT;
    protected EnvironmentAttributeMap attributes = EnvironmentAttributeMap.EMPTY;
    protected HolderSet<Timeline> timelines = HolderSet.empty();
    protected Optional<Holder<WorldClock>> defaultClock = Optional.empty();

    protected final Conversions conversions;

    public PaperDimensionTypeRegistryEntry(final Conversions conversions, final @Nullable DimensionType internal) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }

        this.hasFixedTime = internal.hasFixedTime();
        this.hasSkylight = internal.hasSkyLight();
        this.hasCeiling = internal.hasCeiling();
        this.hasEnderDragonFight = internal.hasEnderDragonFight();
        this.coordinateScale = internal.coordinateScale();
        this.minY = internal.minY();
        this.height = internal.height();
        this.logicalHeight = internal.logicalHeight();
        this.infiniburn = internal.infiniburn();
        this.ambientLight = internal.ambientLight();
        this.monsterSpawnLightTest = internal.monsterSettings().monsterSpawnLightTest();
        this.monsterSpawnBlockLightLimit = internal.monsterSettings().monsterSpawnBlockLightLimit();
        this.skybox = internal.skybox();
        this.cardinalLightType = internal.cardinalLightType();
        this.attributes = internal.attributes();
        this.timelines = internal.timelines();
        this.defaultClock = internal.defaultClock();
    }

    @Override
    public boolean hasFixedTime() {
        return this.hasFixedTime;
    }

    @Override
    public boolean hasSkyLight() {
        return asConfigured(this.hasSkylight, "hasSkyLight");
    }

    @Override
    public boolean hasCeiling() {
        return asConfigured(this.hasCeiling, "hasCeiling");
    }

    @Override
    public boolean hasEnderDragonFight() {
        return asConfigured(this.hasEnderDragonFight, "hasEnderDragonFight");
    }

    @Override
    public double coordinateScale() {
        return asConfigured(this.coordinateScale, "coordinateScale");
    }

    @Override
    public @Range(from = DimensionType.MIN_Y, to = DimensionType.MAX_Y) int minY() {
        return asConfigured(this.minY, "minY");
    }

    @Override
    public @Range(from = DimensionType.MIN_HEIGHT, to = DimensionType.Y_SIZE) int height() {
        return asConfigured(this.height, "height");
    }

    @Override
    public @Range(from = 0, to = DimensionType.Y_SIZE) int logicalHeight() {
        return asConfigured(this.logicalHeight, "logicalHeight");
    }

    @Override
    public TagKey<BlockType> infiniburn() {
        return PaperRegistries.fromNms(asConfigured(this.infiniburn, "infiniburn"));
    }

    @Override
    public float ambientLight() {
        return asConfigured(this.ambientLight, "ambientLight");
    }

    @Override
    public IntProvider monsterSpawnLightTest() {
        return PaperIntProvider.fromVanilla(asConfigured(this.monsterSpawnLightTest, "monsterSpawnLightTest"));
    }

    @Override
    public @Range(from = 0, to = LightEngine.MAX_LEVEL) int monsterSpawnBlockLightLimit() {
        return asConfigured(this.monsterSpawnBlockLightLimit, "monsterSpawnBlockLightLimit");
    }

    @Override
    public Skybox skybox() {
        return Skybox.valueOf(this.skybox.name());
    }

    @Override
    public CardinalLightType cardinalLightType() {
        return CardinalLightType.valueOf(this.cardinalLightType.name());
    }

    public static final class PaperBuilder extends PaperDimensionTypeRegistryEntry implements DimensionTypeRegistryEntry.Builder, PaperRegistryBuilder<DimensionType, io.papermc.paper.world.worldgen.DimensionType> {

        public PaperBuilder(final Conversions conversions, final @Nullable DimensionType internal) {
            super(conversions, internal);
        }

        @Override
        public Builder hasFixedTime(final boolean hasFixedTime) {
            this.hasFixedTime = hasFixedTime;
            return this;
        }

        @Override
        public Builder hasSkyLight(final boolean hasSkyLight) {
            this.hasSkylight = hasSkyLight;
            return this;
        }

        @Override
        public Builder hasCeiling(final boolean hasCeiling) {
            this.hasCeiling = hasCeiling;
            return this;
        }

        @Override
        public Builder hasEnderDragonFight(final boolean hasEnderDragonFight) {
            this.hasEnderDragonFight = hasEnderDragonFight;
            return this;
        }

        @Override
        public Builder coordinateScale(final double coordinateScale) {
            this.coordinateScale = requireRange(coordinateScale, "coordinateScale", 1.0E-5F, 3.0E7);
            return this;
        }

        @Override
        public Builder minY(final @Range(from = DimensionType.MIN_Y, to = DimensionType.MAX_Y) int minY) {
            this.minY = requireRange(minY, "minY", DimensionType.MIN_Y, DimensionType.MAX_Y);
            return this;
        }

        @Override
        public Builder height(final @Range(from = DimensionType.MIN_HEIGHT, to = DimensionType.Y_SIZE) int height) {
            this.height = requireRange(height, "height", DimensionType.MIN_HEIGHT, DimensionType.Y_SIZE);
            return this;
        }

        @Override
        public Builder logicalHeight(final @Range(from = 0, to = DimensionType.Y_SIZE) int logicalHeight) {
            this.logicalHeight = requireRange(logicalHeight, "logicalHeight", 0, DimensionType.Y_SIZE);
            return this;
        }

        @Override
        public Builder infiniburn(final TagKey<BlockType> infiniburn) {
            this.infiniburn = PaperRegistries.toNms(asArgument(infiniburn, "infiniburn"));
            return this;
        }

        @Override
        public Builder ambientLight(final float ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        @Override
        public Builder monsterSpawnLightTest(final IntProvider monsterSpawnLightTest) {
            final var temp = PaperIntProvider.toVanilla(asArgument(monsterSpawnLightTest, "monsterSpawnLightTest"));
            Preconditions.checkArgument(temp.minInclusive() < 0 || temp.maxInclusive() > LightEngine.MAX_LEVEL, "monsterSpawnLightTest must be in the range [0, %s]", LightEngine.MAX_LEVEL);
            this.monsterSpawnLightTest = temp;
            return this;
        }

        @Override
        public Builder monsterSpawnBlockLightLimit(final @Range(from = 0, to = LightEngine.MAX_LEVEL) int monsterSpawnBlockLightLimit) {
            this.monsterSpawnBlockLightLimit = requireRange(monsterSpawnBlockLightLimit, "monsterSpawnBlockLightLimit", 0, LightEngine.MAX_LEVEL);
            return this;
        }

        @Override
        public Builder skybox(final Skybox skybox) {
            this.skybox = DimensionType.Skybox.valueOf(asArgument(skybox, "skybox").name());
            return this;
        }

        @Override
        public Builder cardinalLight(final CardinalLightType cardinalLightType) {
            this.cardinalLightType = CardinalLighting.Type.valueOf(asArgument(cardinalLightType, "cardinalLightType").name());
            return this;
        }

        @Override
        public DimensionType build() {
            return new DimensionType(
                this.hasFixedTime,
                this.hasSkyLight(),
                this.hasCeiling(),
                this.hasEnderDragonFight(),
                this.coordinateScale(),
                this.minY(),
                this.height(),
                this.logicalHeight(),
                asConfigured(this.infiniburn, "infiniburn"),
                this.ambientLight(),
                new DimensionType.MonsterSettings(
                    asConfigured(this.monsterSpawnLightTest, "monsterSpawnLightTest"),
                    this.monsterSpawnBlockLightLimit()
                ),
                this.skybox,
                this.cardinalLightType,
                this.attributes,
                this.timelines,
                this.defaultClock
            );
        }
    }
}
