package org.bukkit.damage;

/// Represents a type of death message used by a [DamageSource].
public enum DeathMessageType {

    /// No special death message logic is applied.
    DEFAULT,
    /// Shows a variant of fall damage death instead of a regular death message.
    ///
    /// **Example:** death.fell.assist.item
    FALL_VARIANTS,
    /// Shows the intentional game design death message instead of a regular
    /// death message.
    INTENTIONAL_GAME_DESIGN;
}
