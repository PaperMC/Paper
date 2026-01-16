package io.papermc.paper.datacomponent.item.blocksattacks;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.component.BlocksAttacks;
import org.checkerframework.checker.index.qual.NonNegative;

public record PaperItemDamageFunction(
    net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction internal
) implements ItemDamageFunction {

    @Override
    public @NonNegative float threshold() {
        return this.internal.threshold();
    }

    @Override
    public float base() {
        return this.internal.base();
    }

    @Override
    public float factor() {
        return this.internal.factor();
    }

    @Override
    public int damageToApply(final float damage) {
        return this.internal.apply(damage);
    }

    static final class BuilderImpl implements Builder {

        private float threshold = BlocksAttacks.ItemDamageFunction.DEFAULT.threshold();
        private float base = BlocksAttacks.ItemDamageFunction.DEFAULT.base();
        private float factor = BlocksAttacks.ItemDamageFunction.DEFAULT.factor();

        @Override
        public Builder threshold(final @NonNegative float threshold) {
            Preconditions.checkArgument(threshold >= 0, "threshold must be non-negative, was %s", threshold);
            this.threshold = threshold;
            return this;
        }

        @Override
        public Builder base(final float base) {
            this.base = base;
            return this;
        }

        @Override
        public Builder factor(final float factor) {
            this.factor = factor;
            return this;
        }

        @Override
        public ItemDamageFunction build() {
            return new PaperItemDamageFunction(new net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction(
                this.threshold,
                this.base,
                this.factor
            ));
        }
    }
}
