package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.util.Handleable;

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
    public float speedMultiplier() {
        return this.impl.speedMultiplier();
    }

    static final class BuilderImpl implements UseEffects.Builder {

        private boolean canSprint = net.minecraft.world.item.component.UseEffects.DEFAULT.canSprint();
        private float speedMultiplier = net.minecraft.world.item.component.UseEffects.DEFAULT.speedMultiplier();

        @Override
        public UseEffects.Builder canSprint(final boolean canSprint) {
            this.canSprint = canSprint;
            return this;
        }

        @Override
        public UseEffects.Builder speedMultiplier(final float speedMultiplier) {
            Preconditions.checkArgument(speedMultiplier >= 0.0F && speedMultiplier <= 1.0F, "speedMultiplier must be between 0.0 and 1.0 (inclusive)");
            this.speedMultiplier = speedMultiplier;
            return this;
        }

        @Override
        public UseEffects build() {
            return new PaperUseEffects(
                new net.minecraft.world.item.component.UseEffects(
                    this.canSprint,
                    this.speedMultiplier
                )
            );
        }
    }
}
