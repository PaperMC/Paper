package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Material#FIREWORK_ROCKET} and its effects.
 *
 * @since 1.4.6
 */
public interface FireworkMeta extends ItemMeta {

    /**
     * Add another effect to this firework.
     *
     * @param effect The firework effect to add
     * @throws IllegalArgumentException If effect is null
     */
    void addEffect(@NotNull FireworkEffect effect) throws IllegalArgumentException;

    /**
     * Add several effects to this firework.
     *
     * @param effects The firework effects to add
     * @throws IllegalArgumentException If effects is null
     * @throws IllegalArgumentException If any effect is null (may be thrown
     *     after changes have occurred)
     */
    void addEffects(@NotNull FireworkEffect... effects) throws IllegalArgumentException;

    /**
     * Add several firework effects to this firework.
     *
     * @param effects An iterable object whose iterator yields the desired
     *     firework effects
     * @throws IllegalArgumentException If effects is null
     * @throws IllegalArgumentException If any effect is null (may be thrown
     *     after changes have occurred)
     */
    void addEffects(@NotNull Iterable<FireworkEffect> effects) throws IllegalArgumentException;

    /**
     * Get the effects in this firework.
     *
     * @return An immutable list of the firework effects
     */
    @NotNull
    List<FireworkEffect> getEffects();

    /**
     * Get the number of effects in this firework.
     *
     * @return The number of effects
     */
    int getEffectsSize();

    /**
     * Remove an effect from this firework.
     *
     * @param index The index of the effect to remove
     * @throws IndexOutOfBoundsException If index {@literal < 0 or index >} {@link
     *     #getEffectsSize()}
     */
    void removeEffect(int index) throws IndexOutOfBoundsException;

    /**
     * Remove all effects from this firework.
     */
    void clearEffects();

    /**
     * Get whether this firework has any effects.
     *
     * @return true if it has effects, false if there are no effects
     */
    boolean hasEffects();

    /**
     * Get whether this firework has power set by component.
     *
     * @return true if it has power set, false if there are no power set
     * @since 1.21.1
     */
    boolean hasPower();

    /**
     * Gets the approximate height the firework will fly.
     * <br>
     * Plugins should check that hasPower() returns <code>true</code>
     * before calling this method.
     *
     * @return approximate flight height of the firework.
     * @see #hasPower()
     */
    int getPower();

    /**
     * Sets the approximate power of the firework. Each level of power is half
     * a second of flight time.
     *
     * @param power the power of the firework, from 0-255
     * @throws IllegalArgumentException if {@literal power<0 or power>255}
     */
    void setPower(int power) throws IllegalArgumentException;

    @Override
    @NotNull
    FireworkMeta clone();
}
