package io.papermc.paper.datacomponent.item;

import io.papermc.paper.loot.number.PaperResolvableFloat;
import io.papermc.paper.loot.number.PaperResolvableInt;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperBrewingFuel(
    net.minecraft.world.item.component.BrewingFuel impl
) implements BrewingFuel, Handleable<net.minecraft.world.item.component.BrewingFuel> {

    @Override
    public net.minecraft.world.item.component.BrewingFuel getHandle() {
        return this.impl;
    }

    @Override
    public io.papermc.paper.loot.number.ResolvableInt uses() {
        return PaperResolvableInt.fromVanilla(this.impl.uses());
    }

    @Override
    public io.papermc.paper.loot.number.ResolvableFloat speedMultiplier() {
        return PaperResolvableFloat.fromVanilla(this.impl.speedMultiplier());
    }

    static final class BuilderImpl implements BrewingFuel.Builder {

        private int uses = 0;
        private float speedMultiplier = 1.0F;

        @Override
        public Builder uses(int uses) {
            this.uses = uses;
            return this;
        }

        @Override
        public Builder speedMultiplier(float speedMultiplier) {
            this.speedMultiplier = speedMultiplier;
            return this;
        }

        @Override
        public BrewingFuel build() {
            return new PaperBrewingFuel(
                new net.minecraft.world.item.component.BrewingFuel(
                    new ResolvableInt.Constant(this.uses),
                    new ResolvableFloat.Constant(this.speedMultiplier)
                )
            );
        }
    }
}
