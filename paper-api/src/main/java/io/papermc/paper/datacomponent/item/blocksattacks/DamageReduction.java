package io.papermc.paper.datacomponent.item.blocksattacks;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.damage.DamageType;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DamageReduction {

    @Contract(value = "-> new", pure = true)
    static DamageReduction.Builder damageReduction() {
        return BlocksAttacksBridge.bridge().blocksAttacksDamageReduction();
    }

    @Nullable
    RegistryKeySet<DamageType> type();

    @Positive
    float horizontalBlockingAngle();

    float base();

    float factor();

    /**
     * Builder for {@link DamageReduction}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<DamageReduction> {

        @Contract(value = "_ -> this", mutates = "this")
        DamageReduction.Builder type(RegistryKeySet<DamageType> type);

        @Contract(value = "_ -> this", mutates = "this")
        DamageReduction.Builder horizontalBlockingAngle(@Positive float horizontalBlockingAngle);

        @Contract(value = "_ -> this", mutates = "this")
        DamageReduction.Builder base(float base);

        @Contract(value = "_ -> this", mutates = "this")
        DamageReduction.Builder factor(float factor);
    }

}
