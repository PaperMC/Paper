package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.datacomponent.item.consumable.PaperConsumableEffect;
import io.papermc.paper.util.MCUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Unmodifiable;

public record PaperDeathProtection(
    net.minecraft.world.item.component.DeathProtection impl
) implements DeathProtection, Handleable<net.minecraft.world.item.component.DeathProtection> {

    @Override
    public net.minecraft.world.item.component.DeathProtection getHandle() {
        return this.impl;
    }

    @Override
    public @Unmodifiable List<ConsumeEffect> deathEffects() {
        return MCUtil.transformUnmodifiable(this.impl.deathEffects(), PaperConsumableEffect::fromVanilla);
    }

    static final class BuilderImpl implements Builder {

        private final List<net.minecraft.world.item.consume_effects.ConsumeEffect> effects = new ArrayList<>();

        @Override
        public Builder addEffect(final ConsumeEffect effect) {
            this.effects.add(PaperConsumableEffect.toVanilla(effect));
            return this;
        }

        @Override
        public Builder addEffects(final List<ConsumeEffect> effects) {
            for (final ConsumeEffect effect : effects) {
                this.effects.add(PaperConsumableEffect.toVanilla(effect));
            }
            return this;
        }

        @Override
        public DeathProtection build() {
            return new PaperDeathProtection(
                new net.minecraft.world.item.component.DeathProtection(this.effects)
            );
        }
    }
}
