package io.papermc.paper.datacomponent.item.blocksattacks;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import org.bukkit.damage.DamageType;
import org.checkerframework.checker.index.qual.Positive;
import org.jspecify.annotations.Nullable;

public record PaperDamageReduction(
    net.minecraft.world.item.component.BlocksAttacks.DamageReduction internal
) implements DamageReduction {

    @Override
    public @Nullable RegistryKeySet<DamageType> type() {
        return this.internal.type().map((set) -> PaperRegistrySets.convertToApi(RegistryKey.DAMAGE_TYPE, set)).orElse(null);
    }

    @Override
    public @Positive float horizontalBlockingAngle() {
        return this.internal.horizontalBlockingAngle();
    }

    @Override
    public float base() {
        return this.internal.base();
    }

    @Override
    public float factor() {
        return this.internal.factor();
    }

    static final class BuilderImpl implements Builder {

        private Optional<HolderSet<net.minecraft.world.damagesource.DamageType>> type = Optional.empty();
        private float horizontalBlockingAngle = 90.0F;
        private float base = 0.0F;
        private float factor = 1.0F;

        @Override
        public Builder type(final @Nullable RegistryKeySet<DamageType> type) {
            this.type = Optional.ofNullable(type)
                .map((set) -> PaperRegistrySets.convertToNms(Registries.DAMAGE_TYPE, Conversions.global().lookup(), set));
            return this;
        }

        @Override
        public Builder horizontalBlockingAngle(final @Positive float horizontalBlockingAngle) {
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
