package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.item.blocksattacks.DamageReduction;
import io.papermc.paper.datacomponent.item.blocksattacks.ItemDamageFunction;
import io.papermc.paper.registry.tag.TagKey;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.damage.DamageType;
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
@ApiStatus.Experimental
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
    float blockDelaySeconds();

    /**
     * Gets the multiplier applied to the cooldown time for the item when attacked by a disabling attack (the multiplier for {@link Weapon#disableBlockingForSeconds()}).
     * <br>
     * If set to 0, this item can never be disabled by attacks.
     *
     * @return the multiplier for the cooldown time
     */
    float disableCooldownScale();

    /**
     * Gets a list of {@link DamageReduction} of how much damage should be blocked in a given attack.
     *
     * @return a list of damage reductions
     */
    List<DamageReduction> damageReductions();

    /**
     * Gets how much damage should be applied to the item from a given attack.
     *
     * @return the damage function
     */
    ItemDamageFunction itemDamage();

    /**
     * Gets the DamageType that can bypass the blocking.
     *
     * @return a damage type tag key, or null if there is no such tag key
     */
    @Nullable TagKey<DamageType> bypassedBy();

    /**
     * Gets the key sound to play when an attack is successfully blocked.
     *
     * @return a key of the sound
     */
    @Nullable Key blockSound();

    /**
     * Gets the key sound to play when the item goes on its disabled cooldown due to an attack.
     *
     * @return a key of the sound
     */
    @Nullable Key disableSound();

    /**
     * Builder for {@link BlocksAttacks}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BlocksAttacks> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder blockDelaySeconds(float delay);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disableCooldownScale(float scale);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addDamageReduction(DamageReduction reduction);

        @Contract(value = "_ -> this", mutates = "this")
        Builder damageReductions(List<DamageReduction> reductions);

        @Contract(value = "_ -> this", mutates = "this")
        Builder itemDamage(ItemDamageFunction function);

        @Contract(value = "_ -> this", mutates = "this")
        Builder bypassedBy(@Nullable TagKey<DamageType> bypassedBy);

        @Contract(value = "_ -> this", mutates = "this")
        Builder blockSound(@Nullable Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disableSound(@Nullable Key sound);
    }
}
