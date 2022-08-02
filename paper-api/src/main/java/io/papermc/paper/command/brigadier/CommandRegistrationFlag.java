package io.papermc.paper.command.brigadier;

import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link CommandRegistrationFlag} is used in {@link Commands} registration for internal purposes.
 * <p>
 * A command library may use this to achieve more specific customization on how their commands are registered.
 * @apiNote Stability of these flags is not promised! This api is not intended for public use.
 */
@ApiStatus.Internal
public enum CommandRegistrationFlag {
    FLATTEN_ALIASES
}
