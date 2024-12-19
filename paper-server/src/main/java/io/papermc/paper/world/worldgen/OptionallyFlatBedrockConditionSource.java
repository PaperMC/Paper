package io.papermc.paper.world.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

// Modelled off of SurfaceRules$VerticalGradientConditionSource
// Flat bedrock generator settings
@DefaultQualifier(NonNull.class)
public record OptionallyFlatBedrockConditionSource(ResourceLocation randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove, boolean isRoof) implements SurfaceRules.ConditionSource {

    private static final ResourceKey<MapCodec<? extends SurfaceRules.ConditionSource>> CODEC_RESOURCE_KEY = ResourceKey.create(
        Registries.MATERIAL_CONDITION,
        ResourceLocation.fromNamespaceAndPath(ResourceLocation.PAPER_NAMESPACE, "optionally_flat_bedrock_condition_source")
    );
    private static final KeyDispatchDataCodec<OptionallyFlatBedrockConditionSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
            ResourceLocation.CODEC.fieldOf("random_name").forGetter(OptionallyFlatBedrockConditionSource::randomName),
            VerticalAnchor.CODEC.fieldOf("true_at_and_below").forGetter(OptionallyFlatBedrockConditionSource::trueAtAndBelow),
            VerticalAnchor.CODEC.fieldOf("false_at_and_above").forGetter(OptionallyFlatBedrockConditionSource::falseAtAndAbove),
            Codec.BOOL.fieldOf("is_roof").forGetter(OptionallyFlatBedrockConditionSource::isRoof)
        ).apply(instance, OptionallyFlatBedrockConditionSource::new);
    }));

    public static void bootstrap() {
        Registry.register(BuiltInRegistries.MATERIAL_CONDITION, CODEC_RESOURCE_KEY, CODEC.codec());
    }

    @Override
    public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.Condition apply(final SurfaceRules.Context context) {
        boolean hasFlatBedrock = context.context.level().paperConfig().environment.generateFlatBedrock;
        int tempTrueAtAndBelowY = this.trueAtAndBelow().resolveY(context.context);
        int tempFalseAtAndAboveY = this.falseAtAndAbove().resolveY(context.context);

        int flatYLevel = this.isRoof ? Math.max(tempFalseAtAndAboveY, tempTrueAtAndBelowY) - 1 : Math.min(tempFalseAtAndAboveY, tempTrueAtAndBelowY);
        final int trueAtAndBelowY = hasFlatBedrock ? flatYLevel : tempTrueAtAndBelowY;
        final int falseAtAndAboveY = hasFlatBedrock ? flatYLevel : tempFalseAtAndAboveY;

        final PositionalRandomFactory positionalRandomFactory = context.randomState.getOrCreateRandomFactory(this.randomName());

        class VerticalGradientCondition extends SurfaceRules.LazyYCondition {
            VerticalGradientCondition(SurfaceRules.Context context) {
                super(context);
            }

            @Override
            protected boolean compute() {
                int blockY = this.context.blockY;
                if (blockY <= trueAtAndBelowY) {
                    return true;
                } else if (blockY >= falseAtAndAboveY) {
                    return false;
                } else {
                    double d = Mth.map(blockY, trueAtAndBelowY, falseAtAndAboveY, 1.0D, 0.0D);
                    RandomSource randomSource = positionalRandomFactory.at(this.context.blockX, blockY, this.context.blockZ);
                    return (double)randomSource.nextFloat() < d;
                }
            }
        }

        return new VerticalGradientCondition(context);
    }
}
