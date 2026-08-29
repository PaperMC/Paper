package io.papermc.paper.math.provider;

import com.google.common.base.Preconditions;
import java.util.List;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.TrapezoidInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;

public class PaperIntProviderProvider implements IntProviderProvider {

    @Override
    public IntProvider.Constant constant(final int value) {
        return new PaperIntProvider.PaperConstant(ConstantInt.of(value));
    }

    @Override
    public IntProvider.Uniform uniform(final int min, final int max) {
        Preconditions.checkArgument(min <= max, "min <= max");
        return new PaperIntProvider.PaperUniform(UniformInt.of(min, max));
    }

    @Override
    public IntProvider.BiasedToBottom biasedToBottom(final int min, final int max) {
        Preconditions.checkArgument(min <= max, "min <= max");
        return new PaperIntProvider.PaperBiasedToBottom(BiasedToBottomInt.of(min, max));
    }

    @Override
    public IntProvider.Clamped clamped(final IntProvider source, final int min, final int max) {
        Preconditions.checkArgument(min <= max, "min <= max");
        return new PaperIntProvider.PaperClamped(ClampedInt.of(PaperIntProvider.toVanilla(source), min, max));
    }

    @Override
    public IntProvider.WeightedList weightedList(final List<WeightedIntProvider> distribution) {
        Preconditions.checkArgument(!distribution.isEmpty(), "distribution cannot be empty");
        final WeightedList.Builder<net.minecraft.util.valueproviders.IntProvider> builder = WeightedList.builder();
        for (final WeightedIntProvider weightedIntProvider : distribution) {
            builder.add(PaperIntProvider.toVanilla(weightedIntProvider.provider()), weightedIntProvider.weight());
        }
        return new PaperIntProvider.PaperWeightedList(new WeightedListInt(builder.build()));
    }

    @Override
    public IntProvider.ClampedNormal clampedNormal(final float mean, final float deviation, final int min, final int max) {
        Preconditions.checkArgument(min <= max, "min <= max");
        return new PaperIntProvider.PaperClampedNormal(ClampedNormalInt.of(mean, deviation, min, max));
    }

    @Override
    public IntProvider.Trapezoid trapezoid(final int plateau, final int min, final int max) {
        Preconditions.checkArgument(min <= max, "min <= max");
        Preconditions.checkArgument(plateau <= max - min, "plateau <= max - min");
        return new PaperIntProvider.PaperTrapezoid(TrapezoidInt.of(min, max, plateau));
    }
}
