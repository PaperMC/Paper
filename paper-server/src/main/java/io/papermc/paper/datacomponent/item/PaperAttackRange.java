package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.util.Handleable;

import static io.papermc.paper.util.BoundChecker.requireRange;

public record PaperAttackRange(
    net.minecraft.world.item.component.AttackRange impl
) implements AttackRange, Handleable<net.minecraft.world.item.component.AttackRange> {

    @Override
    public net.minecraft.world.item.component.AttackRange getHandle() {
        return this.impl;
    }

    @Override
    public float minReach() {
        return this.impl.minReach();
    }

    @Override
    public float maxReach() {
        return this.impl.maxReach();
    }

    @Override
    public float minCreativeReach() {
        return this.impl.minCreativeReach();
    }

    @Override
    public float maxCreativeReach() {
        return this.impl.maxCreativeReach();
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

        private float minReach = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.minReach();
        private float maxReach = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.maxReach();
        private float minCreativeReach = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.minCreativeReach();
        private float maxCreativeReach = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.maxCreativeReach();
        private float hitboxMargin = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.hitboxMargin();
        private float mobFactor = net.minecraft.world.item.component.AttackRange.CODEC_DEFAULT.mobFactor();

        @Override
        public AttackRange.Builder minReach(final float minReach) {
            this.minReach = requireRange(minReach, "minReach", 0.0F, 64.0F);
            return this;
        }

        @Override
        public AttackRange.Builder maxReach(final float maxReach) {
            this.maxReach = requireRange(maxReach, "maxReach", 0.0F, 64.0F);
            return this;
        }

        @Override
        public AttackRange.Builder minCreativeReach(final float minCreativeReach) {
            this.minCreativeReach = requireRange(minCreativeReach, "minCreativeReach", 0.0F, 64.0F);
            return this;
        }

        @Override
        public AttackRange.Builder maxCreativeReach(final float maxCreativeReach) {
            this.maxCreativeReach = requireRange(maxCreativeReach, "maxCreativeReach", 0.0F, 64.0F);
            return this;
        }

        @Override
        public AttackRange.Builder hitboxMargin(final float hitboxMargin) {
            this.hitboxMargin = requireRange(hitboxMargin, "hitboxMargin", 0.0F, 1.0F);
            return this;
        }

        @Override
        public AttackRange.Builder mobFactor(final float mobFactor) {
            this.mobFactor = requireRange(mobFactor, "mobFactor", 0.0F, 2.0F);
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
