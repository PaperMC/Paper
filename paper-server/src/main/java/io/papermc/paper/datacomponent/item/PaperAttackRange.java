package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperAttackRange(
    net.minecraft.world.item.component.AttackRange impl
) implements AttackRange, Handleable<net.minecraft.world.item.component.AttackRange> {

    @Override
    public net.minecraft.world.item.component.AttackRange getHandle() {
        return this.impl;
    }

    @Override
    public float minRange() {
        return this.impl.minRange();
    }

    @Override
    public float maxRange() {
        return this.impl.maxRange();
    }

    @Override
    public float hitboxMargin() {
        return this.impl.hitboxMargin();
    }

    @Override
    public float mobFactor() {
        return this.impl.mobFactor();
    }

    static final class BuilderImpl implements AttackRange.Builder {

        private float minRange = net.minecraft.world.item.component.AttackRange.DEFAULT.minRange();
        private float maxRange = net.minecraft.world.item.component.AttackRange.DEFAULT.maxRange();
        private float hitboxMargin = net.minecraft.world.item.component.AttackRange.DEFAULT.hitboxMargin();
        private float mobFactor = net.minecraft.world.item.component.AttackRange.DEFAULT.mobFactor();

        @Override
        public AttackRange.Builder minRange(final float minRange) {
            Preconditions.checkArgument(minRange >= 0.0F && minRange <= 64.0F, "minRange must be in range [0,64] was %s", minRange);
            this.minRange = minRange;
            return this;
        }

        @Override
        public AttackRange.Builder maxRange(final float maxRange) {
            Preconditions.checkArgument(maxRange >= 0.0F && maxRange <= 64, "maxRange must be in range [0,64] was %s", maxRange);
            this.maxRange = maxRange;
            return this;
        }

        @Override
        public AttackRange.Builder hitboxMargin(final float hitboxMargin) {
            Preconditions.checkArgument(hitboxMargin >= 0.0F && hitboxMargin <= 1.0F, "hitboxMargin must be in range [0,1] was %s", hitboxMargin);
            this.hitboxMargin = hitboxMargin;
            return this;
        }

        @Override
        public AttackRange.Builder mobFactor(final float mobFactor) {
            Preconditions.checkArgument(mobFactor >= 0.0F && mobFactor <= 2.0F, "mobFactor must be in range [0,2] was %s", mobFactor);
            this.mobFactor = mobFactor;
            return this;
        }

        @Override
        public AttackRange build() {
            return new PaperAttackRange(
                new net.minecraft.world.item.component.AttackRange(
                    this.minRange,
                    this.maxRange,
                    this.hitboxMargin,
                    this.mobFactor
                )
            );
        }
    }
}
