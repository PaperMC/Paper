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
    @Deprecated(since = "1.11.2")
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
    @Deprecated(since = "1.11.2")
    @NotNull
    Spell getCurrentSpell();

    /**
     * Sets the {@link Spell} the Evoker is currently using.
     *
     * @param spell the spell the evoker should be using
     * @deprecated future versions of Minecraft have additional spell casting
     * entities.
     */
    @Deprecated(since = "1.11.2")
    void setCurrentSpell(@Nullable Spell spell);

    /**
     * @return the sheep being targeted by the {@link Spell#WOLOLO wololo spell}, or {@code null} if none
     */
    @Nullable
    Sheep getWololoTarget();

    /**
     * Set the sheep to be the target of the {@link Spell#WOLOLO wololo spell}, or {@code null} to clear.
     *
     * @param sheep new wololo target
     */
    void setWololoTarget(@Nullable Sheep sheep);
}
