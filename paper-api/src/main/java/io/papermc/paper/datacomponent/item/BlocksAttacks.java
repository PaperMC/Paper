package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.item.blocksattacks.DamageReduction;
import io.papermc.paper.datacomponent.item.blocksattacks.ItemDamageFunction;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.bukkit.damage.DamageType;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds block attacks to the holding player like Shield.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#BLOCKS_ATTACKS
 */
@NullMarked
@ApiStatus.NonExtendable
public interface BlocksAttacks {

    @Contract(value = "-> new", pure = true)
    static Builder blocksAttacks() {
        return ItemComponentTypesBridge.bridge().blocksAttacks();
    }

    /**
     * Gets the amount of time (in seconds) that use must be held before successfully blocking attacks.
     *
     * @return the delay in seconds
     */
    @Contract(pure = true)
    @NonNegative float blockDelaySeconds();

    /**
     * Gets the multiplier applied to the cooldown time for the item when attacked by a disabling attack (the multiplier for {@link Weapon#disableBlockingForSeconds()}).
     * <br>
     * If set to 0, this item can never be disabled by attacks.
     *
     * @return the multiplier for the cooldown time
     */
    @Contract(pure = true)
    @NonNegative float disableCooldownScale();

    /**
     * Gets a list of {@link DamageReduction} of how much damage should be blocked in a given attack.
     *
     * @return a list of damage reductions
     */
    @Contract(pure = true)
    List<DamageReduction> damageReductions();

    /**
     * Gets how much damage should be applied to the item from a given attack.
     *
     * @return the damage function
     */
    @Contract(pure = true)
    ItemDamageFunction itemDamage();

    /**
     * Gets the DamageType that can bypass the blocking.
     *
     * @return a damage type tag key, or null if there is no such tag key
     */
    @Contract(pure = true)
    @Nullable RegistryKeySet<DamageType> bypassedBy();

    /**
     * Gets the sound to play when an attack is successfully blocked.
     *
     * @return the sound
     */
    @Contract(pure = true)
    @Nullable Sound blockSound();

    /**
     * Gets the sound to play when the item goes on its disabled cooldown due to an attack.
     *
     * @return the sound
     */
    @Contract(pure = true)
    @Nullable Sound disableSound();

    /**
     * Builder for {@link BlocksAttacks}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BlocksAttacks> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder blockDelaySeconds(@NonNegative float delay);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disableCooldownScale(@NonNegative float scale);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addDamageReduction(DamageReduction reduction);

        @Contract(value = "_ -> this", mutates = "this")
        Builder damageReductions(List<DamageReduction> reductions);

        @Contract(value = "_ -> this", mutates = "this")
        Builder itemDamage(ItemDamageFunction function);

        @Contract(value = "_ -> this", mutates = "this")
        Builder bypassedBy(@Nullable RegistryKeySet<DamageType> bypassedBy);

        /**
         * Sets the sound to play when an attack is successfully blocked.
         *
         * @param sound the sound, or null to play no sound
         * @return the builder for chaining
         * @see Sound#create(net.kyori.adventure.key.Key, Float)
         * @see Sound#create(java.util.function.Consumer)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder blockSound(@Nullable Sound sound);

        /**
         * Sets the sound to play when the item goes on its disabled cooldown due to an attack.
         *
         * @param sound the sound, or null to play no sound
         * @return the builder for chaining
         * @see Sound#create(net.kyori.adventure.key.Key, Float)
         * @see Sound#create(java.util.function.Consumer)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder disableSound(@Nullable Sound sound);
    }
}
