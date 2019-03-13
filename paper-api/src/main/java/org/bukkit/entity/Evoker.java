package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an Evoker "Illager".
 */
public interface Evoker extends Spellcaster {

    /**
     * Represents the current spell the Evoker is using.
     *
     * @deprecated future versions of Minecraft have additional spell casting
     * entities.
     */
    @Deprecated
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
     * Gets the {@link Spell} the Evoker is currently using.
     *
     * @return the current spell
     * @deprecated future versions of Minecraft have additional spell casting
     * entities.
     *
     */
    @Deprecated
    @NotNull
    Spell getCurrentSpell();

    /**
     * Sets the {@link Spell} the Evoker is currently using.
     *
     * @param spell the spell the evoker should be using
     * @deprecated future versions of Minecraft have additional spell casting
     * entities.
     */
    @Deprecated
    void setCurrentSpell(@Nullable Spell spell);
}
