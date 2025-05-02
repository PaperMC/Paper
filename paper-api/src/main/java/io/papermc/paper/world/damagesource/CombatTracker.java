package io.papermc.paper.world.damagesource;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Represents entity's combat tracker
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface CombatTracker {

    /**
     * Gets the entity behind this combat tracker.
     *
     * @return the entity behind this combat tracker
     */
    LivingEntity getEntity();

    /**
     * Gets the list of recorded combat entries.
     * <p>
     * The returned list is a copy, so any modifications
     * to its contents won't affect this entity's
     * combat history.
     *
     * @return the list of combat entries
     * @see #setCombatEntries(List)
     */
    List<CombatEntry> getCombatEntries();

    /**
     * Sets the entity's combat history.
     * <p>
     * Note that overriding the entity's combat history won't
     * affect the entity's current or new combat state.
     * Reset the current combat state and register new combat entries instead
     * if you want the new history to affect the combat state.
     *
     * @param combatEntries combat entries
     * @see #resetCombatState()
     * @see #addCombatEntry(CombatEntry)
     */
    void setCombatEntries(List<CombatEntry> combatEntries);

    /**
     * Calculates the most significant fall damage entry.
     *
     * @return the most significant fall damage entry
     */
    @Nullable CombatEntry computeMostSignificantFall();

    /**
     * Checks whether the entity is in combat,
     * i.e. has taken damage from an entity
     * since the combat tracking has begun.
     *
     * @return whether the entity is in combat
     */
    boolean isInCombat();

    /**
     * Checks whether the entity has started recording damage,
     * i.e. its combat tracking is active.
     *
     * @return whether the entity has started recording damage
     */
    boolean isTakingDamage();

    /**
     * Gets the last or current combat duration.
     *
     * @return the combat duration
     * @see #isInCombat()
     */
    int getCombatDuration();

    /**
     * Adds a new entry the pool of combat entries,
     * updating the entity's combat state.
     *
     * @param combatEntry combat entry
     */
    void addCombatEntry(CombatEntry combatEntry);

    /**
     * Constructs a death message based on the current combat history.
     *
     * @return a death message
     */
    Component getDeathMessage();

    /**
     * Resets entity's combat state, clearing combat history.
     */
    void resetCombatState();

    /**
     * Calculates the fall location type from the current entity's location.
     *
     * @return the fall location type
     */
    @Nullable FallLocationType calculateFallLocationType();
}
