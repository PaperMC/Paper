package io.papermc.paper.registry.data;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperLevelStemRegistryEntry implements LevelStemRegistryEntry {

    protected @Nullable Holder<DimensionType> type;
    protected @Nullable ChunkGenerator generator;

    protected final Conversions conversions;

    public PaperLevelStemRegistryEntry(final Conversions conversions, final @Nullable LevelStem internal) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }
        this.type = internal.type();
        this.generator = internal.generator();
    }

    @Override
    public TypedKey<io.papermc.paper.world.worldgen.DimensionType> type() {
        return PaperRegistries.fromNms(asConfigured(this.type, "type").unwrapKey().orElseThrow());
    }

    public static final class PaperBuilder extends PaperLevelStemRegistryEntry implements LevelStemRegistryEntry.Builder, PaperRegistryBuilder<LevelStem, io.papermc.paper.world.worldgen.LevelStem> {

        private final HolderGetter<DimensionType> dimensionTypeHolderGetter;
        private final HolderGetter<WorldPreset> worldPresetHolderGetter;

        public PaperBuilder(final Conversions conversions, final @Nullable LevelStem internal) {
            super(conversions, internal);
            this.dimensionTypeHolderGetter = conversions.lookup().lookup(Registries.DIMENSION_TYPE).orElseThrow().getter();
            this.worldPresetHolderGetter = conversions.lookup().lookup(Registries.WORLD_PRESET).orElseThrow().getter();
        }

        @Override
        public Builder type(final TypedKey<io.papermc.paper.world.worldgen.DimensionType> type) {
             this.type = this.dimensionTypeHolderGetter.getOrThrow(PaperRegistries.toNms(asArgument(type, "type")));
             return this;
        }

        @Override
        public Builder copyGeneratorFrom(final TypedKey<io.papermc.paper.world.WorldPreset> preset, final TypedKey<io.papermc.paper.world.worldgen.LevelStem> levelStem) {
            final Holder<WorldPreset> presetHolder = this.worldPresetHolderGetter.getOrThrow(PaperRegistries.toNms(asArgument(preset, "preset")));
            final LevelStem targetStem = presetHolder.value().dimensions.get(PaperRegistries.toNms(asArgument(levelStem, "levelStem")));
            Preconditions.checkArgument(targetStem != null, "Level stem %s not found in preset %s", levelStem, preset);
            this.generator = this.conversions.clone(targetStem.generator(), ChunkGenerator.CODEC);
            return this;
        }

        @Override
        public LevelStem build() {
            return new LevelStem(asConfigured(this.type, "type"), asConfigured(this.generator, "generator"));
        }
    }
}
