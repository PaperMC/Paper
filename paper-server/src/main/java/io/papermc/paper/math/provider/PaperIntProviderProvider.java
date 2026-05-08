package io.papermc.paper.math.provider;

import com.google.common.base.Preconditions;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;

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
        return new PaperIntProvider.PaperClamped(ClampedInt.of(PaperIntProvider.toMinecraft(source), min, max));
    }

    @Override
    public IntProvider.ClampedNormal clampedNormal(final float mean, final float deviation, final int min, final int max) {
        Preconditions.checkArgument(min <= max, "min <= max");
        return new PaperIntProvider.PaperClampedNormal(ClampedNormalInt.of(mean, deviation, min, max));
    }
}
