package io.papermc.paper.datacomponent.item.blocksattacks;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.damage.DamageType;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

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
    public @Positive float horizontalBlockingAngle() {
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

    static final class BuilderImpl implements Builder {

        private Optional<HolderSet<net.minecraft.world.damagesource.DamageType>> type = Optional.empty();
        private float horizontalBlockingAngle = 90f;
        private float base = 0;
        private float factor = 0;

        @Override
        public Builder type(final @Nullable RegistryKeySet<DamageType> type) {
            this.type = Optional.ofNullable(type)
                .map((set) -> PaperRegistrySets.convertToNms(Registries.DAMAGE_TYPE, net.minecraft.server.MinecraftServer.getServer().registryAccess().createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE).lookupProvider, set));
            return this;
        }

        @Override
        public Builder horizontalBlockingAngle(@Positive final float horizontalBlockingAngle) {
            Preconditions.checkArgument(horizontalBlockingAngle > 0, "horizontalBlockingAngle must be positive and not zero, was %s", horizontalBlockingAngle);
            this.horizontalBlockingAngle = horizontalBlockingAngle;
            return this;
        }

        @Override
        public Builder base(final float base) {
            this.base = base;
            return this;
        }

        @Override
        public Builder factor(final float factor) {
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
