package io.papermc.paper.datacomponent.item;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.blocksattacks.DamageReduction;
import io.papermc.paper.datacomponent.item.blocksattacks.ItemDamageFunction;
import io.papermc.paper.datacomponent.item.blocksattacks.PaperDamageReduction;
import io.papermc.paper.datacomponent.item.blocksattacks.PaperItemDamageFunction;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.tag.TagKey;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.damage.DamageType;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.requireArgumentNonNegative;

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
        return this.impl.damageReductions().stream().map(PaperDamageReduction::new).map(paperDamageReduction -> ((DamageReduction) paperDamageReduction)).toList();
    }

    @Override
    public ItemDamageFunction itemDamage() {
        return new PaperItemDamageFunction(this.impl.itemDamage());
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
        private List<DamageReduction> damageReductions = new ObjectArrayList<>();
        private ItemDamageFunction itemDamage = new PaperItemDamageFunction(net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction.DEFAULT);
        private @Nullable TagKey<DamageType> bypassedBy;
        private @Nullable Key blockSound;
        private @Nullable Key disableSound;

        @Override
        public Builder blockDelaySeconds(final float delay) {
            this.blockDelaySeconds = requireArgumentNonNegative(delay, "delay");
            return this;
        }

        @Override
        public Builder disableCooldownScale(final float scale) {
            this.disableCooldownScale = requireArgumentNonNegative(scale, "scale");
            return this;
        }

        @Override
        public Builder addDamageReduction(final DamageReduction reduction) {
            this.damageReductions.add(reduction);
            return this;
        }

        @Override
        public Builder damageReductions(final List<DamageReduction> reductions) {
            this.damageReductions = new ObjectArrayList<>(reductions);
            return this;
        }

        @Override
        public Builder itemDamage(final ItemDamageFunction function) {
            this.itemDamage = function;
            return this;
        }

        @Override
        public Builder bypassedBy(@Nullable final TagKey<DamageType> bypassedBy) {
            this.bypassedBy = bypassedBy;
            return this;
        }

        @Override
        public Builder blockSound(final @Nullable Key sound) {
            this.blockSound = sound;
            return this;
        }

        @Override
        public Builder disableSound(final @Nullable Key sound) {
            this.disableSound = sound;
            return this;
        }

        @Override
        public BlocksAttacks build() {
            return new PaperBlocksAttacks(new net.minecraft.world.item.component.BlocksAttacks(
                this.blockDelaySeconds,
                this.disableCooldownScale,
                this.damageReductions.stream().map(damageReduction -> ((PaperDamageReduction) damageReduction).internal()).toList(),
                ((PaperItemDamageFunction) this.itemDamage).internal(),
                Optional.ofNullable(this.bypassedBy).map(PaperRegistries::toNms),
                Optional.ofNullable(this.blockSound).map(PaperAdventure::resolveSound),
                Optional.ofNullable(this.disableSound).map(PaperAdventure::resolveSound)
            ));
        }
    }
}
