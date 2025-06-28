package org.bukkit.damage;

/**
 * Represents a type of death message used by a {@link DamageSource}.
 */
public enum DeathMessageType {

    /**
     * No special death message logic is applied.
     */
    DEFAULT,
    /**
     * Shows a variant of fall damage death instead of a regular death message.
     * <br>
     * <b>Example:</b> death.fell.assist.item
     */
    FALL_VARIANTS,
    /**
     * Shows the intentional game design death message instead of a regular
     * death message.
     */
    INTENTIONAL_GAME_DESIGN;
}
