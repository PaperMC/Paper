package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import io.papermc.paper.datacomponent.item.consumable.PaperConsumableEffect;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.bukkit.craftbukkit.util.Handleable;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.Unmodifiable;

public record PaperConsumable(
    net.minecraft.world.item.component.Consumable impl
) implements Consumable, Handleable<net.minecraft.world.item.component.Consumable> {

    private static final ItemUseAnimation[] VALUES = ItemUseAnimation.values();

    @Override
    public net.minecraft.world.item.component.Consumable getHandle() {
        return this.impl;
    }

    @Override
    public @NonNegative float consumeSeconds() {
        return this.impl.consumeSeconds();
    }

    @Override
    public ItemUseAnimation animation() {
        return VALUES[this.impl.animation().ordinal()];
    }

    @Override
    public Key sound() {
        return PaperAdventure.asAdventure(this.impl.sound().value().location());
    }

    @Override
    public boolean hasConsumeParticles() {
        return this.impl.hasConsumeParticles();
    }

    @Override
    public @Unmodifiable List<ConsumeEffect> consumeEffects() {
        return MCUtil.transformUnmodifiable(this.impl.onConsumeEffects(), PaperConsumableEffect::fromVanilla);
    }

    @Override
    public Consumable.Builder toBuilder() {
        return new BuilderImpl()
            .consumeSeconds(this.consumeSeconds())
            .animation(this.animation())
            .sound(this.sound())
            .addEffects(this.consumeEffects());
    }

    static final class BuilderImpl implements Builder {

        private static final net.minecraft.world.item.ItemUseAnimation[] VALUES = net.minecraft.world.item.ItemUseAnimation.values();

        private float consumeSeconds = net.minecraft.world.item.component.Consumable.DEFAULT_CONSUME_SECONDS;
        private net.minecraft.world.item.ItemUseAnimation consumeAnimation = net.minecraft.world.item.ItemUseAnimation.EAT;
        private Holder<SoundEvent> eatSound = SoundEvents.GENERIC_EAT;
        private boolean hasConsumeParticles = true;
        private final List<net.minecraft.world.item.consume_effects.ConsumeEffect> effects = new ObjectArrayList<>();

        @Override
        public Builder consumeSeconds(final @NonNegative float consumeSeconds) {
            Preconditions.checkArgument(consumeSeconds >= 0, "consumeSeconds must be non-negative, was %s", consumeSeconds);
            this.consumeSeconds = consumeSeconds;
            return this;
        }

        @Override
        public Builder animation(final ItemUseAnimation animation) {
            this.consumeAnimation = VALUES[animation.ordinal()];
            return this;
        }

        @Override
        public Builder sound(final Key sound) {
            this.eatSound = PaperAdventure.resolveSound(sound);
            return this;
        }

        @Override
        public Builder hasConsumeParticles(final boolean hasConsumeParticles) {
            this.hasConsumeParticles = hasConsumeParticles;
            return this;
        }

        @Override
        public Builder effects(final List<ConsumeEffect> effects) {
            this.effects.clear();
            return this.addEffects(effects);
        }

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
        public Consumable build() {
            return new PaperConsumable(
                new net.minecraft.world.item.component.Consumable(
                    this.consumeSeconds,
                    this.consumeAnimation,
                    this.eatSound,
                    this.hasConsumeParticles,
                    new ObjectArrayList<>(this.effects)
                )
            );
        }
    }
}
