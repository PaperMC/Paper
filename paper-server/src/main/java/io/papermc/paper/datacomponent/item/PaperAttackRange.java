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
    public float minReach() {
        return this.impl.minRange();
    }

    @Override
    public float maxReach() {
        return this.impl.maxRange();
    }

    @Override
    public float minCreativeReach() {
        return this.impl.minCreativeRange();
    }

    @Override
    public float maxCreativeReach() {
        return this.impl.maxCreativeRange();
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

        private float minReach = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.minRange();
        private float maxReach = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.maxRange();
        private float minCreativeReach = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.minCreativeRange();
        private float maxCreativeReach = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.maxCreativeRange();
        private float hitboxMargin = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.hitboxMargin();
        private float mobFactor = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.mobFactor();

        @Override
        public AttackRange.Builder minReach(final float minReach) {
            Preconditions.checkArgument(minReach >= 0.0F && minReach <= 64.0F, "minReach must be in range [0,64] was %s", minReach);
            this.minReach = minReach;
            return this;
        }

        @Override
        public AttackRange.Builder maxReach(final float maxReach) {
            Preconditions.checkArgument(maxReach >= 0.0F && maxReach <= 64.0F, "maxReach must be in range [0,64] was %s", maxReach);
            this.maxReach = maxReach;
            return this;
        }

        @Override
        public AttackRange.Builder minCreativeReach(final float minCreativeReach) {
            Preconditions.checkArgument(minCreativeReach >= 0.0F && minCreativeReach <= 64.0F, "minCreativeReach must be in range [0,64] was %s", minCreativeReach);
            this.minCreativeReach = minCreativeReach;
            return this;
        }

        @Override
        public AttackRange.Builder maxCreativeReach(final float maxCreativeReach) {
            Preconditions.checkArgument(maxCreativeReach >= 0.0F && maxCreativeReach <= 64.0F, "maxCreativeReach must be in range [0,64] was %s", maxCreativeReach);
            this.maxCreativeReach = maxCreativeReach;
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
                    this.minReach,
                    this.maxReach,
                    this.minCreativeReach,
                    this.maxCreativeReach,
                    this.hitboxMargin,
                    this.mobFactor
                )
            );
        }
    }
}
