package io.papermc.paper.registry.data;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.math.provider.IntProvider;
import io.papermc.paper.math.provider.PaperIntProvider;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Optional;
import java.util.OptionalLong;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asArgumentRange;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperDimensionTypeRegistryEntry implements DimensionTypeRegistryEntry {

    protected OptionalLong fixedTime = OptionalLong.empty();
    protected @Nullable Boolean hasSkylight;
    protected @Nullable Boolean hasCeiling;
    protected @Nullable Boolean ultraWarm;
    protected @Nullable Boolean natural;
    protected @Nullable Double coordinateScale;
    protected @Nullable Boolean bedWorks;
    protected @Nullable Boolean respawnAnchorWorks;
    protected @Nullable Integer minY;
    protected @Nullable Integer height;
    protected @Nullable Integer logicalHeight;
    protected net.minecraft.tags.@Nullable TagKey<Block> infiniburn;
    protected @Nullable ResourceLocation effectsLocation;
    protected @Nullable Float ambientLight;
    protected Optional<Integer> cloudHeight = Optional.empty();
    protected @Nullable Boolean piglinSafe;
    protected @Nullable Boolean hasRaids;
    protected net.minecraft.util.valueproviders.@Nullable IntProvider monsterSpawnLightTest;
    protected @Nullable Integer monsterSpawnBlockLightLimit;

    protected final Conversions conversions;

    public PaperDimensionTypeRegistryEntry(final Conversions conversions, final @Nullable DimensionType internal) {
        this.conversions = conversions;
        if (internal == null) return;

        this.fixedTime = internal.fixedTime();
        this.hasSkylight = internal.hasSkyLight();
        this.hasCeiling = internal.hasCeiling();
        this.ultraWarm = internal.ultraWarm();
        this.natural = internal.natural();
        this.coordinateScale = internal.coordinateScale();
        this.bedWorks = internal.bedWorks();
        this.respawnAnchorWorks = internal.respawnAnchorWorks();
        this.minY = internal.minY();
        this.height = internal.height();
        this.logicalHeight = internal.logicalHeight();
        this.infiniburn = internal.infiniburn();
        this.effectsLocation = internal.effectsLocation();
        this.ambientLight = internal.ambientLight();
        this.cloudHeight = internal.cloudHeight();
        this.piglinSafe = internal.monsterSettings().piglinSafe();
        this.hasRaids = internal.monsterSettings().hasRaids();
        this.monsterSpawnLightTest = internal.monsterSettings().monsterSpawnLightTest();
        this.monsterSpawnBlockLightLimit = internal.monsterSettings().monsterSpawnBlockLightLimit();
    }

    @Override
    public @Nullable Long fixedTime() {
        return this.fixedTime.isPresent() ? this.fixedTime.getAsLong() : null;
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
    public boolean ultraWarm() {
        return asConfigured(this.ultraWarm, "ultraWarm");
    }

    @Override
    public boolean natural() {
        return asConfigured(this.natural, "natural");
    }

    @Override
    public double coordinateScale() {
        return asConfigured(this.coordinateScale, "coordinateScale");
    }

    @Override
    public boolean bedWorks() {
        return asConfigured(this.bedWorks, "bedWorks");
    }

    @Override
    public boolean respawnAnchorWorks() {
        return asConfigured(this.respawnAnchorWorks, "respawnAnchorWorks");
    }

    @Override
    public @Range(from = -2032, to = 2031) int minY() {
        return asConfigured(this.minY, "minY");
    }

    @Override
    public @Range(from = 16, to = 4064) int height() {
        return asConfigured(this.height, "height");
    }

    @Override
    public @Range(from = 0, to = 4096) int logicalHeight() {
        return asConfigured(this.logicalHeight, "logicalHeight");
    }

    @Override
    public TagKey<BlockType> infiniburn() {
        return PaperRegistries.fromNms(asConfigured(this.infiniburn, "infiniburn"));
    }

    @Override
    public Key effectsLocation() {
        return PaperAdventure.asAdventure(asConfigured(this.effectsLocation, "effectsLocation"));
    }

    @Override
    public float ambientLight() {
        return asConfigured(this.ambientLight, "ambientLight");
    }

    @Override
    public @Nullable Integer cloudHeight() {
        return this.cloudHeight.orElse(null);
    }

    @Override
    public boolean piglinSafe() {
        return asConfigured(this.piglinSafe, "piglinSafe");
    }

    @Override
    public boolean hasRaids() {
        return asConfigured(this.hasRaids, "hasRaids");
    }

    @Override
    public IntProvider monsterSpawnLightTest() {
        return PaperIntProvider.fromMinecraft(asConfigured(this.monsterSpawnLightTest, "monsterSpawnLightTest"));
    }

    @Override
    public @Range(from = 0, to = 15) int monsterSpawnBlockLightLimit() {
        return asConfigured(this.monsterSpawnBlockLightLimit, "monsterSpawnBlockLightLimit");
    }

    public static final class PaperBuilder extends PaperDimensionTypeRegistryEntry implements DimensionTypeRegistryEntry.Builder, PaperRegistryBuilder<DimensionType, io.papermc.paper.world.worldgen.DimensionType> {

        public PaperBuilder(final Conversions conversions, final @Nullable DimensionType internal) {
            super(conversions, internal);
        }

        @Override
        public Builder fixedTime(final @Nullable Long fixedTime) {
            this.fixedTime = fixedTime == null ? OptionalLong.empty() : OptionalLong.of(fixedTime);
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
        public Builder ultraWarm(final boolean ultraWarm) {
            this.ultraWarm = ultraWarm;
            return this;
        }

        @Override
        public Builder natural(final boolean natural) {
            this.natural = natural;
            return this;
        }

        @Override
        public Builder coordinateScale(final double coordinateScale) {
            this.coordinateScale = coordinateScale;
            return this;
        }

        @Override
        public Builder bedWorks(final boolean bedWorks) {
            this.bedWorks = bedWorks;
            return this;
        }

        @Override
        public Builder respawnAnchorWorks(final boolean respawnAnchorWorks) {
            this.respawnAnchorWorks = respawnAnchorWorks;
            return this;
        }

        @Override
        public Builder minY(final @Range(from = -2032, to = 2031) int minY) {
            this.minY = asArgumentRange(minY, "minY", -2032, 2031);
            return this;
        }

        @Override
        public Builder height(final @Range(from = 16, to = 4064) int height) {
            this.height = asArgumentRange(height, "height", 16, 4064);
            return this;
        }

        @Override
        public Builder logicalHeight(final @Range(from = 0, to = 4096) int logicalHeight) {
            this.logicalHeight = asArgumentRange(logicalHeight, "logicalHeight", 0, 4096);
            return this;
        }

        @Override
        public Builder infiniburn(final TagKey<BlockType> infiniburn) {
            this.infiniburn = PaperRegistries.toNms(asArgument(infiniburn, "infiniburn"));
            return this;
        }

        @Override
        public Builder effectsLocation(final Key effectsLocation) {
            this.effectsLocation = PaperAdventure.asVanilla(asArgument(effectsLocation, "effectsLocation"));
            return this;
        }

        @Override
        public Builder ambientLight(final float ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        @Override
        public Builder cloudHeight(final @Nullable Integer cloudHeight) {
            this.cloudHeight = cloudHeight == null ? Optional.empty() : Optional.of(cloudHeight);
            return this;
        }

        @Override
        public Builder piglinSafe(final boolean piglinSafe) {
            this.piglinSafe = piglinSafe;
            return this;
        }

        @Override
        public Builder hasRaids(final boolean hasRaids) {
            this.hasRaids = hasRaids;
            return this;
        }

        @Override
        public Builder monsterSpawnLightTest(final IntProvider monsterSpawnLightTest) {
            final var temp = PaperIntProvider.toMinecraft(asArgument(monsterSpawnLightTest, "monsterSpawnLightTest"));
            Preconditions.checkArgument(temp.getMinValue() < 0 || temp.getMaxValue() > 15, "monsterSpawnLightTest must be in the range [0, 15]");
            this.monsterSpawnLightTest = temp;
            return this;
        }

        @Override
        public Builder monsterSpawnBlockLightLimit(final @Range(from = 0, to = 15) int monsterSpawnBlockLightLimit) {
            this.monsterSpawnBlockLightLimit = asArgumentRange(monsterSpawnBlockLightLimit, "monsterSpawnBlockLightLimit", 0, 15);
            return this;
        }

        @Override
        public DimensionType build() {
            return new DimensionType(
                this.fixedTime,
                this.hasSkyLight(),
                this.hasCeiling(),
                this.ultraWarm(),
                this.natural(),
                this.coordinateScale(),
                this.bedWorks(),
                this.respawnAnchorWorks(),
                this.minY(),
                this.height(),
                this.logicalHeight(),
                asConfigured(this.infiniburn, "infiniburn"),
                asConfigured(this.effectsLocation, "effectsLocation"),
                this.ambientLight(),
                this.cloudHeight,
                new DimensionType.MonsterSettings(
                    this.piglinSafe(),
                    this.hasRaids(),
                    asConfigured(this.monsterSpawnLightTest, "monsterSpawnLightTest"),
                    this.monsterSpawnBlockLightLimit()
                )
            );
        }
    }
}
