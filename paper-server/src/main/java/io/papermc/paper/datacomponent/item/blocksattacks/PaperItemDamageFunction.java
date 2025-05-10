package io.papermc.paper.datacomponent.item.blocksattacks;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.util.Handleable;
import org.checkerframework.checker.index.qual.NonNegative;

public record PaperItemDamageFunction(
    net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction impl
) implements ItemDamageFunction, Handleable<net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction> {

    @Override
    public net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction getHandle() {
        return this.impl;
    }

    @Override
    public @NonNegative float threshold() {
        return this.impl.threshold();
    }

    @Override
    public float base() {
        return this.impl.base();
    }

    @Override
    public float factor() {
        return this.impl.factor();
    }

    @Override
    public int damageToApply(final float damage) {
        return this.impl.apply(damage);
    }

    static final class BuilderImpl implements Builder {

        private float threshold;
        private float base;
        private float factor;

        @Override
        public Builder threshold(@NonNegative final float threshold) {
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
