package io.papermc.paper.world.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.condition.ConditionEvaluator;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

// Modelled off of net.minecraft.world.level.levelgen.material.condition.VerticalGradientCondition
// Flat bedrock generator settings
@DefaultQualifier(NonNull.class)
public record OptionallyFlatBedrockCondition(Identifier randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove, boolean isRoof) implements MaterialCondition {

    private static final ResourceKey<MapCodec<? extends MaterialCondition>> CODEC_RESOURCE_KEY = ResourceKey.create(
        Registries.MATERIAL_CONDITION_TYPE,
        Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, "optionally_flat_bedrock_material_condition")
    );
    private static final MapCodec<OptionallyFlatBedrockCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        Identifier.CODEC.fieldOf("random_name").forGetter(OptionallyFlatBedrockCondition::randomName),
        VerticalAnchor.CODEC.fieldOf("true_at_and_below").forGetter(OptionallyFlatBedrockCondition::trueAtAndBelow),
        VerticalAnchor.CODEC.fieldOf("false_at_and_above").forGetter(OptionallyFlatBedrockCondition::falseAtAndAbove),
        Codec.BOOL.fieldOf("is_roof").forGetter(OptionallyFlatBedrockCondition::isRoof)
    ).apply(i, OptionallyFlatBedrockCondition::new));

    public static void bootstrap() {
        Registry.register(BuiltInRegistries.MATERIAL_CONDITION_TYPE, CODEC_RESOURCE_KEY, CODEC);
    }

    @Override
    public MapCodec<OptionallyFlatBedrockCondition> codec() {
        return CODEC;
    }

    @Override
    public ConditionEvaluator compile(final MaterialRuleContext ruleContext) {
        boolean hasFlatBedrock = ruleContext.context.level().paperConfig().environment.generateFlatBedrock;
        final int tempTrueAtAndBelowY = ruleContext.resolveAnchorY(this.trueAtAndBelow);
        final int tempFalseAtAndAboveY = ruleContext.resolveAnchorY(this.falseAtAndAbove);

        int flatYLevel = this.isRoof ? Math.max(tempFalseAtAndAboveY, tempTrueAtAndBelowY) - 1 : Math.min(tempFalseAtAndAboveY, tempTrueAtAndBelowY);
        final int trueAtAndBelowY = hasFlatBedrock ? flatYLevel : tempTrueAtAndBelowY;
        final int falseAtAndAboveY = hasFlatBedrock ? flatYLevel : tempFalseAtAndAboveY;

        final PositionalRandomFactory randomFactory = ruleContext.getOrCreateRandomFactory(this.randomName);
        return new MaterialRuleContext.LazyYCondition(ruleContext) {
            @Override
            protected boolean compute() {
                int blockY = this.context.blockY();
                if (blockY <= trueAtAndBelowY) {
                    return true;
                }

                if (blockY >= falseAtAndAboveY) {
                    return false;
                }

                double probability = Mth.map(blockY, trueAtAndBelowY, falseAtAndAboveY, 1.0, 0.0);
                RandomSource random = randomFactory.at(this.context.blockX(), blockY, this.context.blockZ());
                return random.nextFloat() < probability;
            }
        };
    }
}
