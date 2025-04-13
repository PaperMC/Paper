package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.tag.TagKey;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.Nullable;

public record PaperBlocksAttacks(
    net.minecraft.world.item.component.BlocksAttacks impl
) implements BlocksAttacks, Handleable<net.minecraft.world.item.component.BlocksAttacks> {

    @Override
    public net.minecraft.world.item.component.BlocksAttacks getHandle() {
        return this.impl;
    }

    @Override
    public float blockDelaySeconds() {
        return this.impl.blockDelaySeconds();
    }

    @Override
    public float disableCooldownScale() {
        return this.impl.disableCooldownScale();
    }

    @Override
    public @Nullable TagKey<DamageType> bypassedBy() {
        final Optional<TagKey<DamageType>> tagKey = this.impl.bypassedBy().map(PaperRegistries::fromNms);
        return tagKey.orElse(null);
    }

    @Override
    public @Nullable Key blockSound() {
        return this.impl.blockSound().map(holder -> PaperAdventure.asAdventure(holder.value().location())).orElse(null);
    }

    @Override
    public @Nullable Key disableSound() {
        return this.impl.disableSound().map(holder -> PaperAdventure.asAdventure(holder.value().location())).orElse(null);
    }

    static final class BuilderImpl implements Builder {

        private float blockDelaySeconds;
        private float disableCooldownScale = 1.0F;
        //private List<DamageReduction> damageReductions = List.of();
        //private ItemDamageFunction itemDamage = ItemDamageFunction.DEFAULT;
        private @Nullable TagKey<DamageType> bypassedBy;
        private @Nullable Key blockSound;
        private @Nullable Key disableSound;

        @Override
        public Builder blockDelaySeconds(final float delay) {
            Preconditions.checkArgument(delay >= 0, "delay must be non-negative, was %s", delay);
            this.blockDelaySeconds = delay;
            return this;
        }

        @Override
        public Builder disableCooldownScale(final float scale) {
            Preconditions.checkArgument(scale >= 0, "scale must be non-negative, was %s", scale);
            this.disableCooldownScale = scale;
            return this;
        }

        //@Override
        //public Builder addDamageReduction(final DamageReduction reduction) {
        //    return null;
        //}

        //@Override
        //public Builder itemDamage(final ItemDamageFunction function) {
        //    return null;
        //}

        @Override
        public Builder bypassedBy(@Nullable final TagKey<DamageType> bypassedBy) {
            this.bypassedBy = bypassedBy;
            return this;
        }

        @Override
        public Builder blockSound(@Nullable final Key sound) {
            this.blockSound = sound;
            return this;
        }

        @Override
        public Builder disableSound(@Nullable final Key sound) {
            this.disableSound = sound;
            return this;
        }

        //@Override
        //public Builder damageReductions(final List<DamageReduction> reductions) {
        //    return null;
        //}

        @Override
        public BlocksAttacks build() {
            return new PaperBlocksAttacks(new net.minecraft.world.item.component.BlocksAttacks(
                this.blockDelaySeconds,
                this.disableCooldownScale,
                List.of(), // TODO
                net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction.DEFAULT, // TODO
                Optional.ofNullable(this.bypassedBy).map(PaperRegistries::toNms),
                Optional.ofNullable(this.blockSound).map(PaperAdventure::resolveSound),
                Optional.ofNullable(this.disableSound).map(PaperAdventure::resolveSound)
            ));
        }
    }
}
