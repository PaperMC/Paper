package org.bukkit.entity;

/**
 * Represents an Evoker.
 */
public interface Evoker extends Monster {

    /**
     * Represents the current spell the Evoker is using.
     */
    public enum Spell {

        /**
         * No spell is being evoked.
         */
        NONE,
        /**
         * The spell that summons Vexes.
         */
        SUMMON,
        /**
         * The spell that summons Fangs.
         */
        FANGS,
        /**
         * The "wololo" spell.
         */
        WOLOLO;
    }

    /**
     * Gets the {@link Spell} the Evoker is currently using.
     *
     * @return the current spell
     */
    Spell getCurrentSpell();

    /**
     * Sets the {@link Spell} the Evoker is currently using.
     *
     * @param spell the spell the evoker should be using
     */
    void setCurrentSpell(Spell spell);
}
