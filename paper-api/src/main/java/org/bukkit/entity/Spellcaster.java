package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a spell casting "Illager".
 */
public interface Spellcaster extends Illager {

    /**
     * Represents the current spell the entity is using.
     */
    public enum Spell {

        /**
         * No spell is being used..
         */
        NONE,
        /**
         * The spell that summons Vexes.
         */
        SUMMON_VEX,
        /**
         * The spell that summons Fangs.
         */
        FANGS,
        /**
         * The "wololo" spell.
         */
        WOLOLO,
        /**
         * The spell that makes the casting entity invisible.
         */
        DISAPPEAR,
        /**
         * The spell that makes the target blind.
         */
        BLINDNESS;
    }

    /**
     * Gets the {@link Spell} the entity is currently using.
     *
     * @return the current spell
     */
    @NotNull
    Spell getSpell();

    /**
     * Sets the {@link Spell} the entity is currently using.
     *
     * @param spell the spell the entity should be using
     */
    void setSpell(@NotNull Spell spell);
}
