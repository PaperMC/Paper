package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.damage.DamageType;
import org.checkerframework.common.value.qual.IntRange;
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
    public List<DamageReduction> damageReductions() {
        return List.of();
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
        private List<DamageReduction> damageReductions = List.of();
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

        @Override
        public Builder addDamageReduction(final DamageReduction reduction) {
            Preconditions.checkArgument(reduction.horizontalBlockingAngle() >= 0, "horizontalBlockingAngle must be non-negative, was %s", reduction.horizontalBlockingAngle());
            this.damageReductions.add(reduction);
            return this;
        }

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

        @Override
        public Builder damageReductions(final List<DamageReduction> reductions) {
            this.damageReductions = List.copyOf(reductions);
            return this;
        }

        @Override
        public BlocksAttacks build() {
            return new PaperBlocksAttacks(new net.minecraft.world.item.component.BlocksAttacks(
                this.blockDelaySeconds,
                this.disableCooldownScale,
                this.damageReductions, // TODO, doc: how convert this?
                net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction.DEFAULT, // TODO
                Optional.ofNullable(this.bypassedBy).map(PaperRegistries::toNms),
                Optional.ofNullable(this.blockSound).map(PaperAdventure::resolveSound),
                Optional.ofNullable(this.disableSound).map(PaperAdventure::resolveSound)
            ));
        }
    }

    public record PaperDamageReduction(
        net.minecraft.world.item.component.BlocksAttacks.DamageReduction impl
    ) implements DamageReduction, Handleable<net.minecraft.world.item.component.BlocksAttacks.DamageReduction> {

        @Override
        public net.minecraft.world.item.component.BlocksAttacks.DamageReduction getHandle() {
            return this.impl;
        }

        @Override
        public @Nullable RegistryKeySet<DamageType> type() {
            return this.impl.type().map((set) -> PaperRegistrySets.convertToApi(RegistryKey.DAMAGE_TYPE, set)).orElse(null);
        }

        @Override
        public @IntRange(from = 0) float horizontalBlockingAngle() {
            return this.impl.horizontalBlockingAngle();
        }

        @Override
        public float base() {
            return this.impl.base();
        }

        @Override
        public float factor() {
            return this.impl.factor();
        }

        static final class BuilderImpl implements BlocksAttacks.DamageReduction.Builder {

            private Optional<HolderSet<net.minecraft.world.damagesource.DamageType>> type;
            private float horizontalBlockingAngle;
            private float base;
            private float factor;

            @Override
            public BlocksAttacks.DamageReduction.Builder type(final @Nullable RegistryKeySet<DamageType> type) {
                this.type = Optional.ofNullable(type)
                    .map((set) -> PaperRegistrySets.convertToNms(Registries.DAMAGE_TYPE, BuiltInRegistries.BUILT_IN_CONVERSIONS.lookup(), set));
                return this;
            }

            @Override
            public BlocksAttacks.DamageReduction.Builder horizontalBlockingAngle(@IntRange(from = 0) final float horizontalBlockingAngle) {
                this.horizontalBlockingAngle = horizontalBlockingAngle;
                return this;
            }

            @Override
            public BlocksAttacks.DamageReduction.Builder base(final float base) {
                this.base = base;
                return this;
            }

            @Override
            public BlocksAttacks.DamageReduction.Builder factor(final float factor) {
                this.factor = factor;
                return this;
            }

            @Override
            public DamageReduction build() {
                return new PaperDamageReduction(new net.minecraft.world.item.component.BlocksAttacks.DamageReduction(
                    this.horizontalBlockingAngle,
                    this.type,
                    this.base,
                    this.factor
                ));
            }
        }
    }
}
