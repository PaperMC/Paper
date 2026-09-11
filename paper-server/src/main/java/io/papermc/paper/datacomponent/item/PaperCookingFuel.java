package io.papermc.paper.datacomponent.item;

import io.papermc.paper.loot.number.PaperResolvableFloat;
import io.papermc.paper.loot.number.PaperResolvableInt;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperCookingFuel(
    net.minecraft.world.item.component.CookingFuel impl
) implements CookingFuel, Handleable<net.minecraft.world.item.component.CookingFuel> {

    @Override
    public net.minecraft.world.item.component.CookingFuel getHandle() {
        return this.impl;
    }

    @Override
    public io.papermc.paper.loot.number.ResolvableInt burnTime() {
        return PaperResolvableInt.fromVanilla(this.impl.burnTime());
    }

    @Override
    public io.papermc.paper.loot.number.ResolvableFloat speedMultiplier() {
        return PaperResolvableFloat.fromVanilla(this.impl.speedMultiplier());
    }

    static final class BuilderImpl implements CookingFuel.Builder {

        private int burnTime = 0;
        private float speedMultiplier = 1.0F;

        @Override
        public Builder burnTime(final int burnTime) {
            this.burnTime = burnTime;
            return this;
        }

        @Override
        public Builder speedMultiplier(final float speedMultiplier) {
            this.speedMultiplier = speedMultiplier;
            return this;
        }

        @Override
        public CookingFuel build() {
            return new PaperCookingFuel(
                new net.minecraft.world.item.component.CookingFuel(
                    new ResolvableInt.Constant(this.burnTime),
                    new ResolvableFloat.Constant(this.speedMultiplier)
                )
            );
        }
    }
}
