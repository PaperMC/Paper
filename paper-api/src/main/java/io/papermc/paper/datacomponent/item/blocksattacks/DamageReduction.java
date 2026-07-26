package io.papermc.paper.datacomponent.item.blocksattacks;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.damage.DamageType;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Hold how much damage should be blocked in a given attack.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#BLOCKS_ATTACKS
 * @see io.papermc.paper.datacomponent.item.BlocksAttacks#damageReductions()
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DamageReduction {

    @Contract(value = "-> new", pure = true)
    static DamageReduction.Builder damageReduction() {
        return BlocksAttacksBridge.bridge().blocksAttacksDamageReduction();
    }

    /**
     * The damage types to block.
     *
     * @return the set of damage type
     */
    @Nullable RegistryKeySet<DamageType> type();

    /**
     * Get the maximum angle between the users facing direction and the direction of the incoming attack to be blocked.
     *
     * @return the angle
     */
    @Positive float horizontalBlockingAngle();

    /**
     * Get the constant amount of damage to be blocked.
     *
     * @return the base
     */
    float base();

    /**
     * Get the fraction of the dealt damage to be blocked.
     *
     * @return the factor
     */
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
