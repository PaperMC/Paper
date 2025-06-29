package io.papermc.paper.math.provider;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import org.jetbrains.annotations.Unmodifiable;

public interface PaperIntProvider {

    static IntProvider fromMinecraft(final net.minecraft.util.valueproviders.IntProvider nms) {
        return switch (nms) {
            case final ConstantInt constantInt -> new PaperConstant(constantInt);
            case final UniformInt uniformInt -> new PaperUniform(uniformInt);
            case final BiasedToBottomInt biasedToBottomInt -> new PaperBiasedToBottom(biasedToBottomInt);
            case final ClampedInt clampedInt -> new PaperClamped(clampedInt);
            case final WeightedListInt weightedListInt -> new PaperWeightedList(weightedListInt);
            case final ClampedNormalInt clampedNormalInt -> new PaperClampedNormal(clampedNormalInt);
            default -> throw new IllegalArgumentException("Unknown int provider type: " + nms);
        };
    }

    static net.minecraft.util.valueproviders.IntProvider toMinecraft(final IntProvider provider) {
        return ((PaperIntProvider) provider).handle();
    }

    net.minecraft.util.valueproviders.IntProvider handle();

    record PaperConstant(ConstantInt handle) implements IntProvider.Constant, PaperIntProvider {
        @Override
        public int value() {
            return this.handle.getValue();
        }
    }

    record PaperUniform(UniformInt handle) implements IntProvider.Uniform, PaperIntProvider {
        @Override
        public int minInclusive() {
            return this.handle.getMinValue();
        }

        @Override
        public int maxInclusive() {
            return this.handle.getMaxValue();
        }
    }

    record PaperBiasedToBottom(BiasedToBottomInt handle) implements IntProvider.BiasedToBottom, PaperIntProvider {
        @Override
        public int minInclusive() {
            return this.handle.getMinValue();
        }

        @Override
        public int maxInclusive() {
            return this.handle.getMaxValue();
        }
    }

    record PaperClamped(ClampedInt handle) implements IntProvider.Clamped, PaperIntProvider {
        @Override
        public IntProvider source() {
            return PaperIntProvider.fromMinecraft(this.handle.source);
        }

        @Override
        public int minInclusive() {
            return this.handle.getMinValue();
        }

        @Override
        public int maxInclusive() {
            return this.handle.getMaxValue();
        }
    }

    record PaperWeightedList(WeightedListInt handle) implements IntProvider.WeightedList, PaperIntProvider {

        @Override
        public @Unmodifiable List<WeightedIntProvider> distribution() {
            final ImmutableList.Builder<WeightedIntProvider> builder = ImmutableList.builder();
            for (final Weighted<net.minecraft.util.valueproviders.IntProvider> weighted : this.handle.distribution.unwrap()) {
                builder.add(WeightedIntProvider.create(weighted.weight(), PaperIntProvider.fromMinecraft(weighted.value())));
            }
            return builder.build();
        }
    }

    record PaperClampedNormal(ClampedNormalInt handle) implements IntProvider.ClampedNormal, PaperIntProvider {
        @Override
        public float mean() {
            return this.handle.mean;
        }

        @Override
        public float deviation() {
            return this.handle.deviation;
        }

        @Override
        public int minInclusive() {
            return this.handle.getMinValue();
        }

        @Override
        public int maxInclusive() {
            return this.handle.getMaxValue();
        }
    }
}
