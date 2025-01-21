package io.papermc.paper.datacomponent.item.consumable;

import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Effect that occurs when consuming an item.
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ConsumeEffect {

    /**
     * Creates a consume effect that randomly teleports the entity on consumption.
     *
     * @param diameter diameter of random teleportation
     * @return the effect instance
     */
    @Contract(value = "_ -> new", pure = true)
    static TeleportRandomly teleportRandomlyEffect(final float diameter) {
        return ConsumableTypesBridge.bridge().teleportRandomlyEffect(diameter);
    }

    /**
     * Creates a consume effect that removes status effects on consumption.
     *
     * @param effects the potion effects to remove
     * @return the effect instance
     */
    @Contract(value = "_ -> new", pure = true)
    static RemoveStatusEffects removeEffects(final RegistryKeySet<PotionEffectType> effects) {
        return ConsumableTypesBridge.bridge().removeStatusEffects(effects);
    }

    /**
     * Creates a consume effect that plays a sound on consumption.
     *
     * @param key the key sound effect to play
     * @return the effect instance
     */
    @Contract(value = "_ -> new", pure = true)
    static PlaySound playSoundConsumeEffect(final Key key) {
        return ConsumableTypesBridge.bridge().playSoundEffect(key);
    }

    /**
     * Creates a consume effect that clears all status effects.
     *
     * @return the effect instance
     */
    @Contract(value = "-> new", pure = true)
    static ClearAllStatusEffects clearAllStatusEffects() {
        return ConsumableTypesBridge.bridge().clearAllStatusEffects();
    }

    /**
     * Creates a consume effect that gives potion effects on consumption.
     *
     * @param effects     the potion effects to apply
     * @param probability the probability of these effects being applied, between 0 and 1 inclusive
     * @return the effect instance
     */
    @Contract(value = "_, _ -> new", pure = true)
    static ApplyStatusEffects applyStatusEffects(final List<PotionEffect> effects, final float probability) {
        return ConsumableTypesBridge.bridge().applyStatusEffects(effects, probability);
    }

    /**
     * Represents a consumable effect that randomly teleports the entity on consumption.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface TeleportRandomly extends ConsumeEffect {

        /**
         * The max range that the entity can be teleported to.
         *
         * @return teleportation diameter
         */
        float diameter();
    }

    /**
     * Represents a consumable effect that removes status effects on consumption.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface RemoveStatusEffects extends ConsumeEffect {

        /**
         * Potion effects to remove.
         *
         * @return effects
         */
        RegistryKeySet<PotionEffectType> removeEffects();
    }

    /**
     * Represents a consumable effect that plays a sound on consumption.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface PlaySound extends ConsumeEffect {

        /**
         * Sound effect to play in the world.
         *
         * @return sound effect
         */
        Key sound();
    }

    /**
     * Represents a consumable effect that clears all effects on consumption.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface ClearAllStatusEffects extends ConsumeEffect {

    }

    /**
     * Represents a consumable effect that applies potion effects based on a probability on consumption.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface ApplyStatusEffects extends ConsumeEffect {

        /**
         * Potion effect instances to grant.
         *
         * @return potion effects
         */
        List<PotionEffect> effects();

        /**
         * Float between 0 and 1, chance for the effect to be applied.
         *
         * @return chance
         */
        float probability();
    }
}
