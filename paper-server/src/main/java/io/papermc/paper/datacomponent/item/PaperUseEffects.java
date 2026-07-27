package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.util.Handleable;

import static io.papermc.paper.util.BoundChecker.requireRange;

public record PaperUseEffects(
    net.minecraft.world.item.component.UseEffects impl
) implements UseEffects, Handleable<net.minecraft.world.item.component.UseEffects> {

    @Override
    public net.minecraft.world.item.component.UseEffects getHandle() {
        return this.impl;
    }

    @Override
    public boolean canSprint() {
        return this.impl.canSprint();
    }

    @Override
    public boolean interactVibrations() {
        return this.impl.interactVibrations();
    }

    @Override
    public float speedMultiplier() {
        return this.impl.speedMultiplier();
    }

    static final class BuilderImpl implements UseEffects.Builder {

        private boolean canSprint = net.minecraft.world.item.component.UseEffects.DEFAULT.canSprint();
        private boolean interactVibrations = net.minecraft.world.item.component.UseEffects.DEFAULT.interactVibrations();
        private float speedMultiplier = net.minecraft.world.item.component.UseEffects.DEFAULT.speedMultiplier();

        @Override
        public UseEffects.Builder canSprint(final boolean canSprint) {
            this.canSprint = canSprint;
            return this;
        }

        @Override
        public UseEffects.Builder interactVibrations(final boolean interactVibrations) {
            this.interactVibrations = interactVibrations;
            return this;
        }

        @Override
        public UseEffects.Builder speedMultiplier(final float speedMultiplier) {
            this.speedMultiplier = requireRange(speedMultiplier, "speedMultiplier", 0.0F, 1.0F);
            return this;
        }

        @Override
        public UseEffects build() {
            return new PaperUseEffects(
                new net.minecraft.world.item.component.UseEffects(
                    this.canSprint,
                    this.interactVibrations,
                    this.speedMultiplier
                )
            );
        }
    }
}
