package io.papermc.paper.world.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
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
public record OptionallyFlatBedrockConditionSource(Identifier randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove, boolean isRoof) implements SurfaceRules.ConditionSource {

    public static final KeyDispatchDataCodec<OptionallyFlatBedrockConditionSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((i) -> {
        return i.group(
            Identifier.CODEC.fieldOf("random_name").forGetter(OptionallyFlatBedrockConditionSource::randomName),
            VerticalAnchor.CODEC.fieldOf("true_at_and_below").forGetter(OptionallyFlatBedrockConditionSource::trueAtAndBelow),
            VerticalAnchor.CODEC.fieldOf("false_at_and_above").forGetter(OptionallyFlatBedrockConditionSource::falseAtAndAbove),
            Codec.BOOL.fieldOf("is_roof").forGetter(OptionallyFlatBedrockConditionSource::isRoof)
        ).apply(i, OptionallyFlatBedrockConditionSource::new);
    }));

    @Override
    public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
        boolean hasFlatBedrock = ruleContext.context.level().paperConfig().environment.generateFlatBedrock;
        int tempTrueAtAndBelow = this.trueAtAndBelow().resolveY(ruleContext.context);
        int tempFalseAtAndAbove = this.falseAtAndAbove().resolveY(ruleContext.context);

        int flatYLevel = this.isRoof ? Math.max(tempFalseAtAndAbove, tempTrueAtAndBelow) - 1 : Math.min(tempFalseAtAndAbove, tempTrueAtAndBelow);
        final int trueAtAndBelow = hasFlatBedrock ? flatYLevel : tempTrueAtAndBelow;
        final int falseAtAndAbove = hasFlatBedrock ? flatYLevel : tempFalseAtAndAbove;

        final PositionalRandomFactory randomFactory = ruleContext.randomState.getOrCreateRandomFactory(this.randomName());

        class VerticalGradientCondition extends SurfaceRules.LazyYCondition {
            private VerticalGradientCondition() {
                super(ruleContext);
            }

            @Override
            protected boolean compute() {
                int blockY = this.context.blockY;
                if (blockY <= trueAtAndBelow) {
                    return true;
                } else if (blockY >= falseAtAndAbove) {
                    return false;
                } else {
                    double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0, 0.0);
                    RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ);
                    return random.nextFloat() < probability;
                }
            }
        }

        return new VerticalGradientCondition();
    }
}
